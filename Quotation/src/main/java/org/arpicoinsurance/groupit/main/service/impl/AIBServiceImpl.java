package org.arpicoinsurance.groupit.main.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.common.DateConverter;
import org.arpicoinsurance.groupit.main.common.WebClient;
import org.arpicoinsurance.groupit.main.dao.BenefitsDao;
import org.arpicoinsurance.groupit.main.dao.CustomerDao;
import org.arpicoinsurance.groupit.main.dao.CustomerDetailsDao;
import org.arpicoinsurance.groupit.main.dao.OccupationDao;
import org.arpicoinsurance.groupit.main.dao.ProductDao;
import org.arpicoinsurance.groupit.main.dao.Quo_Benef_DetailsDao;
import org.arpicoinsurance.groupit.main.dao.QuotationDao;
import org.arpicoinsurance.groupit.main.dao.QuotationDetailsDao;
import org.arpicoinsurance.groupit.main.dao.RateCardAIBDao;
import org.arpicoinsurance.groupit.main.dao.UsersDao;
import org.arpicoinsurance.groupit.main.helper.InvpSavePersonalInfo;
import org.arpicoinsurance.groupit.main.model.Customer;
import org.arpicoinsurance.groupit.main.model.CustomerDetails;
import org.arpicoinsurance.groupit.main.model.Occupation;
import org.arpicoinsurance.groupit.main.model.Products;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Details;
import org.arpicoinsurance.groupit.main.model.Quotation;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;
import org.arpicoinsurance.groupit.main.model.RateCardAIB;
import org.arpicoinsurance.groupit.main.model.Users;
import org.arpicoinsurance.groupit.main.service.AIBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AIBServiceImpl implements AIBService {

	@Autowired
	private BenefitsDao benefitsDao;

	@Autowired
	private Quo_Benef_DetailsDao quoBenifDetailDao;

	@Autowired
	private RateCardAIBDao rateCardAIBDao;

	@Autowired
	private CustomerDao customerDao;

	@Autowired
	private UsersDao userDao;

	@Autowired
	private OccupationDao occupationDao;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private QuotationDao quotationDao;

	@Autowired
	private QuotationDetailsDao quotationDetailsDao;

	@Autowired
	private CustomerDetailsDao CustomerDetailsDao;

	@Override
	public BigDecimal calculateAIBMaturaty(Integer term, Double adbrat, Double fundmarat, Double fundrat,
			Double contribution, Date chedat, String paymod) throws Exception {
		// System.out.println(contribution);
		CalculationUtils calculationUtils = new CalculationUtils();
		BigDecimal maturity = new BigDecimal(0);
		BigDecimal open_fund = new BigDecimal("0");
		BigDecimal fund_amount = new BigDecimal("0");
		BigDecimal balance_bfi = new BigDecimal("0");
		BigDecimal interest_annum = new BigDecimal("0");
		BigDecimal balance_bmf = new BigDecimal("0");
		BigDecimal mgt_fees = new BigDecimal("0");
		BigDecimal close_bal = new BigDecimal("0");
		BigDecimal premium = new BigDecimal(contribution);
		BigDecimal total_amount = new BigDecimal("0");
		BigDecimal cum_premium = new BigDecimal("0");
		BigDecimal management_fee = new BigDecimal(fundmarat);
		BigDecimal fund_rate = new BigDecimal(fundrat);
		RateCardAIB rateCardAIB = rateCardAIBDao.findByTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(term,
				chedat, chedat, chedat, chedat);

		BigDecimal interest_rate = null;

		try {
			interest_rate = new BigDecimal(rateCardAIB.getRate());
		} catch (Exception e) {
			throw new NullPointerException("AIB Rate not found at Term : " + term);
		}

		// System.out.println("term : " + term + " adbrat : " + adbrat + " fundmarat : "
		// + fundmarat + " fundrat : "
		// + fundrat + " intrat : " + interest_rate + " paymod : " + paymod);
		for (int i = 1; i <= term; i++) {
			for (int j = 1; j <= calculationUtils.getPayterm(paymod); j++) {

				if (paymod.equalsIgnoreCase("S") && i > 1) {
					fund_amount = new BigDecimal("0");
					cum_premium = new BigDecimal("0");
				} else {
					fund_amount = premium.multiply(fund_rate.divide(new BigDecimal("100"))).setScale(6,
							BigDecimal.ROUND_HALF_UP);
					cum_premium = cum_premium.add(premium);
				}

				balance_bfi = fund_amount.add(open_fund);

				double interest = Math.pow((1 + interest_rate.divide(new BigDecimal("100")).doubleValue()),
						((12.00 / calculationUtils.getPayterm(paymod)) / 12.00)) - 1;

				interest_annum = balance_bfi.multiply(new BigDecimal(interest)).setScale(6, BigDecimal.ROUND_HALF_UP);

				balance_bmf = balance_bfi.add(interest_annum).setScale(6, BigDecimal.ROUND_HALF_UP);

				mgt_fees = balance_bmf.multiply(management_fee.divide(new BigDecimal("100"))).setScale(6,
						BigDecimal.ROUND_HALF_UP);
				close_bal = balance_bmf.subtract(mgt_fees).setScale(6, BigDecimal.ROUND_HALF_UP);

				open_fund = new BigDecimal(close_bal.toPlainString()).setScale(6, BigDecimal.ROUND_HALF_UP);

				total_amount = new BigDecimal(close_bal.toPlainString()).setScale(2, BigDecimal.ROUND_HALF_UP);

			}

		}

		// System.out.println("maturity : " + total_amount.toString());
		maturity = total_amount.setScale(0, BigDecimal.ROUND_UP);
		return maturity;
	}

	@Override
	public HashMap<String, Object> saveQuotation(InvpSavePersonalInfo _invpSaveQuotation, Integer id) throws Exception {
		CalculationUtils calculationUtils = new CalculationUtils();
		Users user = null;
		try {
			user = userDao.findOne(id);
		} catch (Exception e) {
			throw new NullPointerException("User Not Found");
		}
		Quotation quo = null;
		HashMap<String, Object> responseMap = new HashMap<>();

		Products products = productDao.findByProductCode("AIB");
		// System.out.println(_invpSaveQuotation.get_plan().get_bsa() +
		// "*********************");
		Double contribution = _invpSaveQuotation.get_plan().get_bsa();
		/*
		 * BigDecimal bsa = calculateAIBMaturaty(2, 0.0, 0.0, 100.0,contribution, new
		 * Date(), "S");
		 */
		Double adminFee = calculationUtils.getAdminFee("S");
		Double tax = calculationUtils.getTaxAmount(contribution + adminFee);

		Customer customer = new Customer();
		try {
			customer.setCustCreateBy(user.getUserCode());
		} catch (Exception e) {
			throw new NullPointerException("User Not Found");
		}
		customer.setCustCreateDate(new Date());

		customer.setCustCode(new WebClient().getCustCode(_invpSaveQuotation));

		customer.setCustName(_invpSaveQuotation.get_mainlife().get_mName());

		Occupation occupation = occupationDao
				.findByOcupationid(Integer.parseInt(_invpSaveQuotation.get_mainlife().get_mOccupation()));
		CustomerDetails customerDetails = null;
		try {
			customerDetails = getCustomerDetail(occupation, _invpSaveQuotation, user);
		} catch (Exception e) {
			throw new NullPointerException("Occupation Not Found");
		}
		customerDetails.setCustomer(customer);

		Quotation quotation = new Quotation();
		quotation.setProducts(products);
		quotation.setStatus("active");
		quotation.setUser(user);

		QuotationDetails quotationDetails = new QuotationDetails();
		quotationDetails.setQuotation(quotation);
		quotationDetails.setPolTerm(_invpSaveQuotation.get_plan().get_term());
		quotationDetails.setAdminFee(adminFee);
		quotationDetails.setBaseSum(0.0);
		quotationDetails.setInterestRate(10.0);
		quotationDetails.setTaxAmount(tax);
		quotationDetails.setPayMode("S");
		quotationDetails.setPolicyFee(calculationUtils.getPolicyFee());
		quotationDetails.setPremiumSingle(contribution);
		quotationDetails.setPremiumSingleT(contribution + adminFee + tax);
		quotationDetails.setQuotationCreateBy(user.getUserCode());
		quotationDetails.setQuotationquotationCreateDate(new Date());
		quotationDetails.setCustomerDetails(customerDetails);

		ArrayList<Quo_Benef_Details> benefictList = new ArrayList<>();

		if (customerDao.save(customer) != null) {

			CustomerDetails c = CustomerDetailsDao.save(customerDetails);
			if (c != null) {
				quo = quotationDao.save(quotation);
				if (quo != null) {

					QuotationDetails quoDetails = quotationDetailsDao.save(quotationDetails);

					/////////// Add Maturity///////////////////////

					Quo_Benef_Details mat1 = new Quo_Benef_Details();
					mat1.setRiderPremium(0.0);
					mat1.setRiderTerm(2);
					mat1.setRiderSum(
							calculateAIBMaturaty(2, 0.0, 0.0, 100.0, contribution, new Date(), "S").doubleValue());
					mat1.setQuotationDetails(quoDetails);
					mat1.setRierCode("IAIB");
					try {
						mat1.setBenefit(benefitsDao.findByRiderCode("IAIB"));
					} catch (Exception e) {
						throw new NullPointerException("Benefict IAIB not found");
					}

					benefictList.add(mat1);

					///////////////////////////// END ADD MATURITY////////////////////////

					if (quoDetails != null) {
						if (quoBenifDetailDao.save(benefictList) != null) {
							responseMap.put("status", "Success");
							responseMap.put("code", quo.getId().toString());

							return responseMap;
						} else {
							responseMap.put("status", "Error at saving Maturity");
							return responseMap;
						}

					} else {
						responseMap.put("status", "Error at Quotation Detail Saving");
						return responseMap;
					}

				} else {
					responseMap.put("status", "Error at Quotation Saving");
					return responseMap;
				}
			} else {
				responseMap.put("status", "Error at Customer Details Saving");
				return responseMap;
			}
		} else {
			responseMap.put("status", "Error at Customer Saving");
			return responseMap;
		}

	}

	private CustomerDetails getCustomerDetail(Occupation occupation, InvpSavePersonalInfo get_personalInfo,
			Users user) {
		CustomerDetails mainLifeDetail = null;
		try {
			mainLifeDetail = new CustomerDetails();
			mainLifeDetail.setCustName(get_personalInfo.get_mainlife().get_mName());
			mainLifeDetail.setCustCivilStatus(get_personalInfo.get_mainlife().get_mCivilStatus());
			mainLifeDetail.setCustCreateBy(user.getUser_Name());
			mainLifeDetail.setCustCreateDate(new Date());
			mainLifeDetail.setCustDob(new DateConverter().stringToDate(get_personalInfo.get_mainlife().get_mDob()));
			mainLifeDetail.setCustEmail(get_personalInfo.get_mainlife().get_mEmail());
			mainLifeDetail.setCustGender(get_personalInfo.get_mainlife().get_mGender());
			mainLifeDetail.setCustNic(get_personalInfo.get_mainlife().get_mNic());
			mainLifeDetail.setCustTel(get_personalInfo.get_mainlife().get_mMobile());
			mainLifeDetail.setCustTitle(get_personalInfo.get_mainlife().get_mTitle());
			mainLifeDetail.setOccupation(occupation);

			return mainLifeDetail;
		} finally {
			if (mainLifeDetail != null) {
				mainLifeDetail = null;
			}
		}
	}

	@Override
	public HashMap<String, Object> editQuotation(InvpSavePersonalInfo _invpSaveQuotation, Integer userId, Integer qdId)
			throws Exception {
		CalculationUtils calculationUtils = new CalculationUtils();
		Users user = userDao.findOne(userId);

		Quotation quo = null;
		HashMap<String, Object> responseMap = new HashMap<>();

		QuotationDetails details = quotationDetailsDao.findByQdId(qdId);

		Products products = productDao.findByProductCode("AIB");
		// System.out.println(_invpSaveQuotation.get_plan().get_bsa() +
		// "*********************");
		Double contribution = _invpSaveQuotation.get_plan().get_bsa();
		/*
		 * BigDecimal bsa = calculateAIBMaturaty(2, 0.0, 0.0, 100.0, contribution, new
		 * Date(), "S");
		 */
		Double adminFee = calculationUtils.getAdminFee("S");
		Double tax = calculationUtils.getTaxAmount(contribution + adminFee);

		Customer customer = details.getCustomerDetails().getCustomer();
		try {
			customer.setCustModifyBy(user.getUserCode());
		} catch (Exception e) {
			throw new NullPointerException("User Not Found");
		}
		customer.setCustModifyDate(new Date());
		customer.setCustName(_invpSaveQuotation.get_mainlife().get_mName());

		Occupation occupation = occupationDao
				.findByOcupationid(Integer.parseInt(_invpSaveQuotation.get_mainlife().get_mOccupation()));

		CustomerDetails customerDetails = null;
		try {
			customerDetails = getCustomerDetail(occupation, _invpSaveQuotation, user);
		} catch (Exception e) {
			throw new NullPointerException("Occupation not found");
		}
		customerDetails.setCustomer(customer);

		Quotation quotation = details.getQuotation();
		quotation.setProducts(products);
		quotation.setStatus("active");
		quotation.setUser(user);

		QuotationDetails quotationDetails = new QuotationDetails();
		quotationDetails.setQuotation(quotation);
		quotationDetails.setPolTerm(_invpSaveQuotation.get_plan().get_term());
		quotationDetails.setAdminFee(adminFee);
		quotationDetails.setQuotationModifyBy(user.getUserCode());
		quotationDetails.setQuotationModifyDate(new Date());
		quotationDetails.setBaseSum(0.0);
		quotationDetails.setInterestRate(10.0);
		quotationDetails.setTaxAmount(tax);
		quotationDetails.setPayMode("S");
		quotationDetails.setPolicyFee(calculationUtils.getPolicyFee());
		quotationDetails.setPremiumSingle(contribution);
		quotationDetails.setPremiumSingleT(contribution + adminFee + tax);
		quotationDetails.setQuotationCreateBy(user.getUserCode());
		quotationDetails.setQuotationquotationCreateDate(new Date());
		quotationDetails.setCustomerDetails(customerDetails);
		ArrayList<Quo_Benef_Details> benefictList = new ArrayList<>();

		if (customerDao.save(customer) != null) {
			if (CustomerDetailsDao.save(customerDetails) != null) {
				quo = quotationDao.save(quotation);
				if (quo != null) {
					QuotationDetails quoDetails = quotationDetailsDao.save(quotationDetails);

					/////////// Add Maturity///////////////////////

					Quo_Benef_Details mat1 = new Quo_Benef_Details();
					mat1.setRiderPremium(0.0);
					mat1.setRiderTerm(2);
					mat1.setRiderSum(
							calculateAIBMaturaty(2, 0.0, 0.0, 100.0, contribution, new Date(), "S").doubleValue());
					mat1.setQuotationDetails(quoDetails);
					mat1.setRierCode("IAIB");
					mat1.setBenefit(benefitsDao.findByRiderCode("IAIB"));
					benefictList.add(mat1);

					///////////////////////////// END ADD MATURITY////////////////////////

					if (quoDetails != null) {
						if (quoBenifDetailDao.save(benefictList) != null) {
							responseMap.put("status", "Success");
							responseMap.put("code", quo.getId().toString());

							return responseMap;
						} else {
							responseMap.put("status", "Error at saving Maturity");
							return responseMap;
						}

					} else {
						responseMap.put("status", "Error at Quotation Detail Saving");
						return responseMap;
					}

				} else {
					responseMap.put("status", "Error at Quotation Saving");
					return responseMap;
				}
			} else {
				responseMap.put("status", "Error at Customer Details Saving");
				return responseMap;
			}
		} else {
			responseMap.put("status", "Error at Customer Saving");
			return responseMap;
		}

	}

}
