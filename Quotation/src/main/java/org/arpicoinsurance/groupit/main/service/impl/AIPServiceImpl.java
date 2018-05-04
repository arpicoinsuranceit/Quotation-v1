package org.arpicoinsurance.groupit.main.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.common.DateConverter;
import org.arpicoinsurance.groupit.main.dao.BenefitsDao;
import org.arpicoinsurance.groupit.main.dao.CustomerDao;
import org.arpicoinsurance.groupit.main.dao.CustomerDetailsDao;
import org.arpicoinsurance.groupit.main.dao.OccupationDao;
import org.arpicoinsurance.groupit.main.dao.ProductDao;
import org.arpicoinsurance.groupit.main.dao.Quo_Benef_DetailsDao;
import org.arpicoinsurance.groupit.main.dao.QuotationDao;
import org.arpicoinsurance.groupit.main.dao.QuotationDetailsDao;
import org.arpicoinsurance.groupit.main.dao.RateCardAIPDao;
import org.arpicoinsurance.groupit.main.dao.UsersDao;
import org.arpicoinsurance.groupit.main.helper.AIPCalResp;
import org.arpicoinsurance.groupit.main.helper.AipCalShedule;
import org.arpicoinsurance.groupit.main.helper.InvpSavePersonalInfo;
import org.arpicoinsurance.groupit.main.model.Customer;
import org.arpicoinsurance.groupit.main.model.CustomerDetails;
import org.arpicoinsurance.groupit.main.model.Occupation;
import org.arpicoinsurance.groupit.main.model.Products;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Details;
import org.arpicoinsurance.groupit.main.model.Quotation;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;
import org.arpicoinsurance.groupit.main.model.RateCardAIP;
import org.arpicoinsurance.groupit.main.model.Users;
import org.arpicoinsurance.groupit.main.service.AIPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AIPServiceImpl implements AIPService {

	@Autowired
	private BenefitsDao benefitsDao;

	@Autowired
	private Quo_Benef_DetailsDao quoBenifDetailDao;

	@Autowired
	private RateCardAIPDao rateCardAIPDao;

	@Autowired
	private UsersDao userdao;

	@Autowired
	private CustomerDao customerDao;

	@Autowired
	private CustomerDetailsDao customerDetailsDao;

	@Autowired
	private QuotationDao quotationDao;

	@Autowired
	private QuotationDetailsDao quotationDetailsDao;

	@Autowired
	private OccupationDao occupationDao;

	@Autowired
	private ProductDao productDao;

	public AIPServiceImpl() {
	}

	public AIPCalResp calculateAIPMaturaty(Integer term, Double adbrat, Double fundmarat, Double intrat,
			Double contribution, Date chedat, String paymod, boolean schedule, boolean isAddOccuLoading) throws Exception {

		AIPCalResp aipCalResp = null;
		ArrayList<AipCalShedule> aipCalShedules = null;
		try {
			aipCalResp = new AIPCalResp();

			CalculationUtils calculationUtils = new CalculationUtils();
			BigDecimal maturity = new BigDecimal(0);
			BigDecimal open_fund = new BigDecimal("0");
			BigDecimal fund_amount = new BigDecimal("0");
			BigDecimal balance_bfi = new BigDecimal("0");
			BigDecimal interest_annum = new BigDecimal("0");
			BigDecimal balance_bmf = new BigDecimal("0");
			BigDecimal mgt_fees = new BigDecimal("0");
			BigDecimal close_bal = new BigDecimal("0");
			BigDecimal premium = new BigDecimal(contribution.doubleValue());
			BigDecimal total_amount = new BigDecimal("0");
			BigDecimal cum_premium = new BigDecimal("0");
			BigDecimal com_premium = new BigDecimal("0");
			BigDecimal management_fee = new BigDecimal(fundmarat.doubleValue());
			BigDecimal interest_rate = new BigDecimal(intrat.doubleValue());
			BigDecimal adb_rate = new BigDecimal(adbrat.doubleValue());
			System.out.println(" term : " + term + " adbrat : " + adbrat + " fundmarat : " + fundmarat + " intrat : "
					+ intrat + " paymod : " + paymod);
			aipCalShedules = new ArrayList<>();

			RateCardAIP rateCardAIP = null;
			int polyear = 0;
			for (int i = 0; i < (term.intValue() * 12); i++) {
				
				if (i % 12 == 0) {
					polyear = (polyear+1);
					if (polyear > 3) {
						polyear = 4;
					}
				}

				if (paymod.equalsIgnoreCase("S") && i == 0) {
					rateCardAIP = rateCardAIPDao
							.findByTermtoOrTermtoLessThanAndTermfromOrTermfromGreaterThanAndPaymodAndStrdatLessThanOrStrdat(
									term.intValue(), term.intValue(), term.intValue(), term.intValue(), paymod, chedat,
									chedat);
				} else if (!paymod.equalsIgnoreCase("S") && i % 12 == 0) {
					rateCardAIP = rateCardAIPDao
							.findByTermtoOrTermtoLessThanAndTermfromOrTermfromGreaterThanAndPaymodAndPolyearAndStrdatLessThanOrStrdat(
									term.intValue(), term.intValue(), term.intValue(), term.intValue(), paymod, polyear,
									chedat, chedat);
				}

				System.out.println(" term : " + term + " polyear : " + polyear + " Rate : " + rateCardAIP.getRate());
				BigDecimal fund_rate = new BigDecimal(rateCardAIP.getRate().doubleValue());

				// calculationUtils.getPayterm(paymod)

				// for (int j = 1; j <= 12; j++) {
				AipCalShedule aipCalShedule = new AipCalShedule();
				if (schedule) {
					System.out.println("polyer : " + ((i / 12) + 1) + " polmth : " + ((i % 12) + 1) + " opnfun : "
							+ open_fund.toString());
					aipCalShedule.setPolicyYear(((i / 12) + 1));
					aipCalShedule.setPolicyMonth(((i % 12) + 1));
					aipCalShedule.setOpeningFee(open_fund.setScale(0, BigDecimal.ROUND_UP).doubleValue());
				}

				if ((paymod.equalsIgnoreCase("S"))) {
					if (i == 0) {
						fund_amount = premium.multiply(fund_rate.divide(new BigDecimal("100"))).setScale(6, 4);
						cum_premium = cum_premium.add(premium);
						com_premium = premium;
					} else {
						fund_amount = new BigDecimal("0");
						com_premium = new BigDecimal("0");
					}
				} else if (paymod.equalsIgnoreCase("M")) {
					fund_amount = premium.multiply(fund_rate.divide(new BigDecimal("100"))).setScale(6, 4);
					cum_premium = cum_premium.add(premium);
					com_premium = premium;
				} else if (paymod.equalsIgnoreCase("Q")) {
					if (i % 3 == 0) {
						fund_amount = premium.multiply(fund_rate.divide(new BigDecimal("100"))).setScale(6, 4);
						cum_premium = cum_premium.add(premium);
						com_premium = premium;
					} else {
						fund_amount = new BigDecimal("0");
						com_premium = new BigDecimal("0");
					}
				} else if (paymod.equalsIgnoreCase("H")) {
					if (i % 6 == 0) {
						fund_amount = premium.multiply(fund_rate.divide(new BigDecimal("100"))).setScale(6, 4);
						cum_premium = cum_premium.add(premium);
						com_premium = premium;
					} else {
						fund_amount = new BigDecimal("0");
						com_premium = new BigDecimal("0");
					}
				} else if (paymod.equalsIgnoreCase("Y")) {
					if (i % 12 == 0) {
						fund_amount = premium.multiply(fund_rate.divide(new BigDecimal("100"))).setScale(6, 4);
						cum_premium = cum_premium.add(premium);
						com_premium = premium;
					} else {
						fund_amount = new BigDecimal("0");
						com_premium = new BigDecimal("0");
					}
				}

				balance_bfi = fund_amount.add(open_fund);

				double interest = Math.pow(1.0D + interest_rate.divide(new BigDecimal("100")).doubleValue(), 1 / 12.0D)
						- 1.0D;

				interest_annum = balance_bfi.multiply(new BigDecimal(interest)).setScale(6, 4);

				balance_bmf = balance_bfi.add(interest_annum).setScale(6, 4);

				mgt_fees = balance_bmf.multiply(management_fee.divide(new BigDecimal("100"))).setScale(6, 4);
				close_bal = balance_bmf.subtract(mgt_fees).setScale(6, 4);

				open_fund = new BigDecimal(close_bal.toPlainString()).setScale(6, 4);

				if (schedule) {

					System.out.println("cumcon : " + com_premium.toString() + " fndamt : " + fund_amount.toString()
							+ " fndbfi : " + balance_bfi.toString() + " intanm : " + interest_annum.toString()
							+ " fndbmf : " + balance_bmf.toString() + " mgtfee : " + mgt_fees.toString() + " fndclo : "
							+ close_bal.toString() + " fndclo : " + close_bal.toString());

					aipCalShedule.setComCon(com_premium.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
					aipCalShedule.setFundAmt(fund_amount.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
					aipCalShedule.setFndBfi(balance_bfi.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
					aipCalShedule.setFndBmf(balance_bmf.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
					aipCalShedule.setMgtFee(mgt_fees.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
					aipCalShedule.setFndClo(close_bal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
					aipCalShedule.setIntAmt(interest_annum.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

					if (close_bal.compareTo(cum_premium) == -1) {
						System.out.println("adbcov : " + cum_premium.multiply(adb_rate).setScale(2, 4).toPlainString());

						aipCalShedule.setAdbCov(cum_premium.multiply(adb_rate).setScale(2, 4).doubleValue());
					} else {
						System.out.println("adbcov : " + close_bal.multiply(adb_rate).setScale(2, 4).toPlainString());

						aipCalShedule.setAdbCov(close_bal.multiply(adb_rate).setScale(2, 4).doubleValue());
					}
					aipCalShedules.add(aipCalShedule);
				}

				total_amount = new BigDecimal(close_bal.toPlainString()).setScale(2, 4);

				// }

			}

			System.out.println("maturity " + intrat + " : " + total_amount.toString());

			maturity = total_amount;

			Double adminFee = calculationUtils.getAdminFee(paymod);

			Double tax = calculationUtils.getTaxAmount(adminFee + contribution);
			System.out.println(tax);
			aipCalResp.setMaturaty(maturity.setScale(0, BigDecimal.ROUND_UP).doubleValue());
			aipCalResp.setAipCalShedules(aipCalShedules);
			aipCalResp.setExtraOe(adminFee + tax);
			return aipCalResp;
		} finally

		{
			if (aipCalResp != null) {
				aipCalResp = null;
			}
		}

	}

	@Override
	public HashMap<String, Object> saveQuotation(InvpSavePersonalInfo _invpSaveQuotation, Integer id) throws Exception {
		CalculationUtils calculationUtils = null;
		Products products = null;
		Customer customer = null;
		Users user = null;
		Occupation occupation = null;
		CustomerDetails customerDetails = null;
		Quotation quotation = null;
		QuotationDetails quotationDetails = null;
		
		Quotation quo = null;
		HashMap<String, Object> responseMap = new HashMap<>();
		
		try {
			calculationUtils = new CalculationUtils();
			products = productDao.findByProductCode("AIP");
			Double contribution = _invpSaveQuotation.get_plan().get_bsa();

			AIPCalResp aip = calculateAIPMaturaty(_invpSaveQuotation.get_plan().get_term(), 2.0, 0.2, 9.0, contribution,
					new Date(), _invpSaveQuotation.get_plan().get_frequance(), false, true);

			AIPCalResp aip2 = calculateAIPMaturaty(_invpSaveQuotation.get_plan().get_term(), 2.0, 0.2, 10.0,
					contribution, new Date(), _invpSaveQuotation.get_plan().get_frequance(), false, false);

			AIPCalResp aip3 = calculateAIPMaturaty(_invpSaveQuotation.get_plan().get_term(), 2.0, 0.2, 11.0,
					contribution, new Date(), _invpSaveQuotation.get_plan().get_frequance(), false, false);

			occupation = occupationDao
					.findByOcupationid(Integer.parseInt(_invpSaveQuotation.get_mainlife().get_mOccupation()));

			Double adminFee = calculationUtils.getAdminFee(_invpSaveQuotation.get_plan().get_frequance());
			Double tax = calculationUtils.getTaxAmount(contribution + adminFee);
			customer = new Customer();
			user = userdao.findOne(id);

			customer.setCustModifyBy(user.getUserCode());
			customer.setCustModifyDate(new Date());
			customer.setCustName(_invpSaveQuotation.get_mainlife().get_mName());

			customerDetails = getCustomerDetail(occupation, _invpSaveQuotation, user);
			customerDetails.setCustomer(customer);
			quotation = new Quotation();
			quotation.setProducts(products);
			quotation.setStatus("active");
			quotation.setUser(user);

			quotationDetails = new QuotationDetails();
			quotationDetails.setQuotation(quotation);
			quotationDetails.setAdminFee(adminFee);
			quotationDetails.setQuotationModifyBy(user.getUserCode());
			quotationDetails.setQuotationModifyDate(new Date());
			quotationDetails.setBaseSum(0.0);
			quotationDetails.setInterestRate(10.0);
			quotationDetails.setTaxAmount(tax);
			String frequance = _invpSaveQuotation.get_plan().get_frequance();
			quotationDetails.setPayMode(frequance);
			quotationDetails.setPolTerm(_invpSaveQuotation.get_plan().get_term());
			quotationDetails.setPolicyFee(calculationUtils.getPolicyFee());
			quotationDetails.setQuotationCreateBy(user.getUserCode());
			quotationDetails.setQuotationquotationCreateDate(new Date());
			quotationDetails.setCustomerDetails(customerDetails);
			switch (frequance) {
			case "M":
				quotationDetails.setPremiumMonth(_invpSaveQuotation.get_plan().get_bsa());
				quotationDetails.setPremiumMonthT(_invpSaveQuotation.get_plan().get_bsa() + adminFee + tax);

				break;
			case "Q":
				quotationDetails.setPremiumQuater(_invpSaveQuotation.get_plan().get_bsa());
				quotationDetails.setPremiumQuaterT(_invpSaveQuotation.get_plan().get_bsa() + adminFee + tax);

				break;
			case "H":
				quotationDetails.setPremiumHalf(_invpSaveQuotation.get_plan().get_bsa());
				quotationDetails.setPremiumHalfT(_invpSaveQuotation.get_plan().get_bsa() + adminFee + tax);

				break;
			case "Y":
				quotationDetails.setPremiumYear(_invpSaveQuotation.get_plan().get_bsa());
				quotationDetails.setPremiumYearT(_invpSaveQuotation.get_plan().get_bsa() + adminFee + tax);

				break;
			case "S":
				quotationDetails.setPremiumSingle(_invpSaveQuotation.get_plan().get_bsa());
				quotationDetails.setPremiumSingleT(_invpSaveQuotation.get_plan().get_bsa() + adminFee + tax);

				break;

			default:
				break;
			}

			quotationDetails.setQuotationCreateBy(user.getUserCode());
			quotationDetails.setQuotationquotationCreateDate(new Date());

			ArrayList<Quo_Benef_Details> benefictList = new ArrayList<>();

			if (customerDao.save(customer) != null) {
				if (customerDetailsDao.save(customerDetails) != null) {
					quo = quotationDao.save(quotation);
					if (quo != null) {
						QuotationDetails quoDetails = quotationDetailsDao.save(quotationDetails);

						/////////// Add Maturity///////////////////////

						Quo_Benef_Details mat1 = new Quo_Benef_Details();
						mat1.setRiderPremium(_invpSaveQuotation.get_plan().get_bsa());
						mat1.setRiderTerm(2);
						mat1.setRiderSum(aip.getMaturaty());
						mat1.setQuotationDetails(quoDetails);
						mat1.setRierCode("L6");
						mat1.setBenefit(benefitsDao.findByRiderCode("L6"));
						benefictList.add(mat1);

						Quo_Benef_Details mat2 = new Quo_Benef_Details();
						mat2.setRiderPremium(_invpSaveQuotation.get_plan().get_bsa());
						mat2.setRiderTerm(2);
						mat2.setRiderSum(aip2.getMaturaty());
						mat2.setQuotationDetails(quoDetails);
						mat2.setRierCode("L8");
						mat2.setBenefit(benefitsDao.findByRiderCode("L8"));
						benefictList.add(mat2);

						Quo_Benef_Details mat3 = new Quo_Benef_Details();
						mat3.setRiderPremium(_invpSaveQuotation.get_plan().get_bsa());
						mat3.setRiderTerm(2);
						mat3.setRiderSum(aip3.getMaturaty());
						mat3.setQuotationDetails(quoDetails);
						mat3.setRierCode("L9");
						mat3.setBenefit(benefitsDao.findByRiderCode("L9"));
						benefictList.add(mat3);

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

		} finally {
			if (calculationUtils != null) {
				calculationUtils = null;
			}
			if (products != null) {
				products = null;
			}
			if (customer != null) {
				customer = null;
			}
			if (user != null) {
				user = null;
			}
			if (occupation != null) {
				occupation = null;
			}
			if (customerDetails != null) {
				customerDetails = null;
			}
			if (quotation != null) {
				quotation = null;
			}
			if (quotationDetails != null) {
				quotationDetails = null;
			}
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
		
		System.out.println(_invpSaveQuotation.get_plan().get_frequance());
		System.out.println(_invpSaveQuotation.get_plan().get_bsa());
		System.out.println(_invpSaveQuotation.get_plan().get_term());
		
		CalculationUtils calculationUtils = null;
		Products products = null;
		Customer customer = null;
		Users user = null;
		Occupation occupation = null;
		CustomerDetails customerDetails = null;
		Quotation quotation = null;
		Quotation quo = null;
		HashMap<String, Object> responseMap = new HashMap<>();
		QuotationDetails quotationDetails = null;
		try {
			calculationUtils = new CalculationUtils();
			products = productDao.findByProductCode("AIP");
			Double contribution = _invpSaveQuotation.get_plan().get_bsa();
			AIPCalResp aip = calculateAIPMaturaty(_invpSaveQuotation.get_plan().get_term(), 2.0, 0.2, 9.0, contribution,
					new Date(), _invpSaveQuotation.get_plan().get_frequance(), false, true);

			AIPCalResp aip2 = calculateAIPMaturaty(_invpSaveQuotation.get_plan().get_term(), 2.0, 0.2, 10.0,
					contribution, new Date(), _invpSaveQuotation.get_plan().get_frequance(), false, false);

			AIPCalResp aip3 = calculateAIPMaturaty(_invpSaveQuotation.get_plan().get_term(), 2.0, 0.2, 11.0,
					contribution, new Date(), _invpSaveQuotation.get_plan().get_frequance(), false, false);

			occupation = occupationDao
					.findByOcupationid(Integer.parseInt(_invpSaveQuotation.get_mainlife().get_mOccupation()));

			Double adminFee = calculationUtils.getAdminFee(_invpSaveQuotation.get_plan().get_frequance());
			

			QuotationDetails details = quotationDetailsDao.findByQdId(qdId);

			customer = details.getCustomerDetails().getCustomer();
			user = userdao.findOne(userId);

			customer.setCustCreateBy(user.getUserCode());
			customer.setCustCreateDate(new Date());
			customer.setCustName(_invpSaveQuotation.get_mainlife().get_mName());

			customerDetails = getCustomerDetail(occupation, _invpSaveQuotation, user);
			customerDetails.setCustomer(customer);
			quotation = details.getQuotation();
			quotation.setProducts(products);
			quotation.setStatus("active");
			quotation.setUser(user);

			quotationDetails = new QuotationDetails();
			quotationDetails.setQuotation(quotation);
			quotationDetails.setAdminFee(adminFee);
			quotationDetails.setBaseSum(0.0);
			quotationDetails.setInterestRate(10.0);
			
			String frequance = _invpSaveQuotation.get_plan().get_frequance();
			quotationDetails.setPayMode(frequance);
			quotationDetails.setPolTerm(_invpSaveQuotation.get_plan().get_term());
			quotationDetails.setPolicyFee(calculationUtils.getPolicyFee());
			quotationDetails.setQuotationCreateBy(user.getUserCode());
			quotationDetails.setQuotationquotationCreateDate(new Date());
			quotationDetails.setCustomerDetails(customerDetails);
			Double tax = calculationUtils.getTaxAmount(contribution + adminFee);
			quotationDetails.setTaxAmount(tax);
			switch (frequance) {
			case "M":
				
				System.out.println(_invpSaveQuotation.get_plan().get_bsa());
				System.out.println(_invpSaveQuotation.get_plan().get_bsa()+ adminFee + tax);
				System.out.println(adminFee);
				System.out.println(tax);
				
				quotationDetails.setPremiumMonth(_invpSaveQuotation.get_plan().get_bsa());
				
				quotationDetails.setPremiumMonthT(_invpSaveQuotation.get_plan().get_bsa() + adminFee + tax);

				break;
			case "Q":
				System.out.println(_invpSaveQuotation.get_plan().get_bsa());
				System.out.println(_invpSaveQuotation.get_plan().get_bsa()+ adminFee + tax);
				System.out.println(adminFee);
				System.out.println(tax);
				quotationDetails.setPremiumQuater(_invpSaveQuotation.get_plan().get_bsa());
				quotationDetails.setPremiumQuaterT(_invpSaveQuotation.get_plan().get_bsa() + adminFee + tax);

				break;
			case "H":
				System.out.println(_invpSaveQuotation.get_plan().get_bsa());
				System.out.println(_invpSaveQuotation.get_plan().get_bsa()+ adminFee + tax);
				System.out.println(adminFee);
				System.out.println(tax);
				quotationDetails.setPremiumHalf(_invpSaveQuotation.get_plan().get_bsa());
				quotationDetails.setPremiumHalfT(_invpSaveQuotation.get_plan().get_bsa() + adminFee + tax);

				break;
			case "Y":
				System.out.println(_invpSaveQuotation.get_plan().get_bsa());
				System.out.println(_invpSaveQuotation.get_plan().get_bsa()+ adminFee + tax);
				System.out.println(adminFee);
				System.out.println(tax);
				quotationDetails.setPremiumYear(_invpSaveQuotation.get_plan().get_bsa());
				quotationDetails.setPremiumYearT(_invpSaveQuotation.get_plan().get_bsa() + adminFee + tax);

				break;
			case "S":
				System.out.println(_invpSaveQuotation.get_plan().get_bsa());
				System.out.println(_invpSaveQuotation.get_plan().get_bsa()+ adminFee + tax);
				System.out.println(adminFee);
				System.out.println(tax);
				quotationDetails.setPremiumSingle(_invpSaveQuotation.get_plan().get_bsa());
				quotationDetails.setPremiumSingleT(_invpSaveQuotation.get_plan().get_bsa() + adminFee + tax);

				break;

			default:
				break;
			}

			quotationDetails.setQuotationCreateBy(user.getUserCode());
			quotationDetails.setQuotationquotationCreateDate(new Date());

			ArrayList<Quo_Benef_Details> benefictList = new ArrayList<>();

			if (customerDao.save(customer) != null) {
				if (customerDetailsDao.save(customerDetails) != null) {
					quo = quotationDao.save(quotation);
					if (quo != null) {
						QuotationDetails quoDetails = quotationDetailsDao.save(quotationDetails);

						/////////// Add Maturity///////////////////////

						Quo_Benef_Details mat1 = new Quo_Benef_Details();
						mat1.setRiderPremium(_invpSaveQuotation.get_plan().get_bsa());
						mat1.setRiderTerm(2);
						mat1.setRiderSum(aip.getMaturaty());
						mat1.setQuotationDetails(quoDetails);
						mat1.setRierCode("L6");
						mat1.setBenefit(benefitsDao.findByRiderCode("L6"));
						benefictList.add(mat1);

						Quo_Benef_Details mat2 = new Quo_Benef_Details();
						mat2.setRiderPremium(_invpSaveQuotation.get_plan().get_bsa());
						mat2.setRiderTerm(2);
						mat2.setRiderSum(aip2.getMaturaty());
						mat2.setQuotationDetails(quoDetails);
						mat2.setRierCode("L8");
						mat2.setBenefit(benefitsDao.findByRiderCode("L8"));
						benefictList.add(mat2);

						Quo_Benef_Details mat3 = new Quo_Benef_Details();
						mat3.setRiderPremium(_invpSaveQuotation.get_plan().get_bsa());
						mat3.setRiderTerm(2);
						mat3.setRiderSum(aip3.getMaturaty());
						mat3.setQuotationDetails(quoDetails);
						mat3.setRierCode("L9");
						mat3.setBenefit(benefitsDao.findByRiderCode("L9"));
						benefictList.add(mat3);

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

		} finally {
			
			if (calculationUtils != null) {
				calculationUtils = null;
			}
			if (products != null) {
				products = null;
			}
			if (customer != null) {
				customer = null;
			}
			if (user != null) {
				user = null;
			}
			if (occupation != null) {
				occupation = null;
			}
			if (customerDetails != null) {
				customerDetails = null;
			}
			if (quotation != null) {
				quotation = null;
			}
			if (quotationDetails != null) {
				quotationDetails = null;
			}
		}

	}
}