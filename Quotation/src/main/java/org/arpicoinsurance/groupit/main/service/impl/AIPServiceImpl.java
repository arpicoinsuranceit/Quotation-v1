package org.arpicoinsurance.groupit.main.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.common.DateConverter;
import org.arpicoinsurance.groupit.main.dao.CustomerDao;
import org.arpicoinsurance.groupit.main.dao.CustomerDetailsDao;
import org.arpicoinsurance.groupit.main.dao.OccupationDao;
import org.arpicoinsurance.groupit.main.dao.ProductDao;
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
			Double contribution, Date chedat, String paymod, boolean schedule) throws Exception {

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
			BigDecimal management_fee = new BigDecimal(fundmarat.doubleValue());
			BigDecimal interest_rate = new BigDecimal(intrat.doubleValue());
			BigDecimal adb_rate = new BigDecimal(adbrat.doubleValue());
			System.out.println(" term : " + term + " adbrat : " + adbrat + " fundmarat : " + fundmarat + " intrat : "
					+ intrat + " paymod : " + paymod);
			aipCalShedules = new ArrayList<>();
			for (int i = 1; i <= term.intValue(); i++) {
				int polyear;
				if (i > 3) {
					polyear = 4;
				} else {
					polyear = i;
				}

				RateCardAIP rateCardAIP = null;

				if (paymod.equalsIgnoreCase("S")) {
					rateCardAIP = rateCardAIPDao
							.findByTermtoOrTermtoLessThanAndTermfromOrTermfromGreaterThanAndPaymodAndStrdatLessThanOrStrdat(
									term.intValue(), term.intValue(), term.intValue(), term.intValue(), paymod, chedat,
									chedat);
				} else {
					rateCardAIP = rateCardAIPDao
							.findByTermtoOrTermtoLessThanAndTermfromOrTermfromGreaterThanAndPaymodAndPolyearAndStrdatLessThanOrStrdat(
									term.intValue(), term.intValue(), term.intValue(), term.intValue(), paymod, polyear,
									chedat, chedat);
				}

				System.out.println(" term : " + term + " polyear : " + polyear + " Rate : " + rateCardAIP.getRate());
				BigDecimal fund_rate = new BigDecimal(rateCardAIP.getRate().doubleValue());

				for (int j = 1; j <= calculationUtils.getPayterm(paymod); j++) {
					AipCalShedule aipCalShedule = new AipCalShedule();
					if (schedule) {
						System.out.println("polyer : " + i + " polmth : " + j + " opnfun : " + open_fund.toString());
						aipCalShedule.setPolicyYear(i);
						aipCalShedule.setPolicyMonth(j);
						aipCalShedule.setOpeningFee(open_fund.setScale(0,BigDecimal.ROUND_UP).doubleValue());
					}

					if ((paymod.equalsIgnoreCase("S")) && (i > 1)) {
						fund_amount = new BigDecimal("0");
						cum_premium = new BigDecimal("0");
					} else {
						fund_amount = premium.multiply(fund_rate.divide(new BigDecimal("100"))).setScale(6, 4);
						cum_premium = cum_premium.add(premium);
					}

					balance_bfi = fund_amount.add(open_fund);

					double interest = Math.pow(1.0D + interest_rate.divide(new BigDecimal("100")).doubleValue(),
							12.0D / calculationUtils.getPayterm(paymod) / 12.0D) - 1.0D;

					interest_annum = balance_bfi.multiply(new BigDecimal(interest)).setScale(6, 4);

					balance_bmf = balance_bfi.add(interest_annum).setScale(6, 4);

					mgt_fees = balance_bmf.multiply(management_fee.divide(new BigDecimal("100"))).setScale(6, 4);
					close_bal = balance_bmf.subtract(mgt_fees).setScale(6, 4);

					open_fund = new BigDecimal(close_bal.toPlainString()).setScale(6, 4);

					if (schedule) {

						System.out.println("cumcon : " + cum_premium.toString() + " fndamt : " + fund_amount.toString()
								+ " fndbfi : " + balance_bfi.toString() + " intanm : " + interest_annum.toString()
								+ " fndbmf : " + balance_bmf.toString() + " mgtfee : " + mgt_fees.toString()
								+ " fndclo : " + close_bal.toString() + " fndclo : " + close_bal.toString());
						
						
						aipCalShedule.setComCon(cum_premium.setScale(0,BigDecimal.ROUND_UP).doubleValue());
						aipCalShedule.setFundAmt(fund_amount.setScale(0,BigDecimal.ROUND_UP).doubleValue());
						aipCalShedule.setFndBfi(balance_bfi.setScale(0,BigDecimal.ROUND_UP).doubleValue());
						aipCalShedule.setIntAmt(interest_annum.setScale(0,BigDecimal.ROUND_UP).doubleValue());
						aipCalShedule.setFndBmf(balance_bmf.setScale(0,BigDecimal.ROUND_UP).doubleValue());
						aipCalShedule.setMgtFee(mgt_fees.setScale(0,BigDecimal.ROUND_UP).doubleValue());
						aipCalShedule.setFndClo(close_bal.setScale(0,BigDecimal.ROUND_UP).doubleValue());

						if (close_bal.compareTo(cum_premium) == -1) {
							System.out.println(
									"adbcov : " + cum_premium.multiply(adb_rate).setScale(0, 4).toPlainString());

							aipCalShedule.setAdbCov(cum_premium.multiply(adb_rate).setScale(0, 4).doubleValue());
						} else {
							System.out
									.println("adbcov : " + close_bal.multiply(adb_rate).setScale(0, 4).toPlainString());

							aipCalShedule.setAdbCov(close_bal.multiply(adb_rate).setScale(0, 4).doubleValue());
						}
						aipCalShedules.add(aipCalShedule);
					}

					total_amount = new BigDecimal(close_bal.toPlainString()).setScale(2, 4);

				}

			}

			System.out.println("maturity " + intrat + " : " + total_amount.toString());

			maturity = total_amount;

			Double adminFee = calculationUtils.getAdminFee(paymod);

			Double tax = calculationUtils.getTaxAmount(adminFee + contribution);
			System.out.println(tax);
			aipCalResp.setMaturaty(maturity.setScale(0,BigDecimal.ROUND_UP)   .doubleValue());
			aipCalResp.setAipCalShedules(aipCalShedules);
			aipCalResp.setExtraOe(adminFee + tax);
			return aipCalResp;
		} finally {
			if (aipCalResp != null) {
				aipCalResp = null;
			}
		}

	}

	@Override
	public String saveQuotation(InvpSavePersonalInfo _invpSaveQuotation, Integer id) throws Exception {
		CalculationUtils calculationUtils = null;
		Products products = null;
		Customer customer = null;
		Users user = null;
		Occupation occupation = null;
		CustomerDetails customerDetails = null;
		Quotation quotation = null;
		QuotationDetails quotationDetails = null;
		try {
			calculationUtils = new CalculationUtils();
			products = productDao.findByProductCode("AIP");
			Double contribution = _invpSaveQuotation.get_plan().get_bsa();
			AIPCalResp aip = calculateAIPMaturaty(_invpSaveQuotation.get_plan().get_term(), 2.0, 0.02, 9.5,
					contribution, new Date(), _invpSaveQuotation.get_plan().get_frequance(), true);
			occupation = occupationDao
					.findByOcupationid(Integer.parseInt(_invpSaveQuotation.get_mainlife().get_mOccupation()));

			Double adminFee = calculationUtils.getAdminFee(_invpSaveQuotation.get_plan().get_frequance());
			Double tax = calculationUtils.getTaxAmount(aip.getMaturaty() + adminFee);
			customer = new Customer();
			user = userdao.findOne(id);

			customer.setCustModifyBy(user.getUser_Code());
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
			quotationDetails.setQuotationModifyBy(user.getUser_Code());
			quotationDetails.setQuotationModifyDate(new Date());
			quotationDetails.setBaseSum(aip.getMaturaty());
			quotationDetails.setInterestRate(10.0);
			quotationDetails.setTaxAmount(tax);
			String frequance = _invpSaveQuotation.get_plan().get_frequance();
			quotationDetails.setPayMode(frequance);
			quotationDetails.setPolTerm(_invpSaveQuotation.get_plan().get_term());
			quotationDetails.setPolicyFee(calculationUtils.getPolicyFee());
			quotationDetails.setQuotationCreateBy(user.getUser_Code());
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

			quotationDetails.setQuotationCreateBy(user.getUser_Code());
			quotationDetails.setQuotationquotationCreateDate(new Date());

			if (customerDao.save(customer) != null) {
				if (customerDetailsDao.save(customerDetails) != null) {
					if (quotationDao.save(quotation) != null) {
						if (quotationDetailsDao.save(quotationDetails) != null) {
							return "Success";
						} else {
							return "Error at Quotation Details Saving";
						}
					} else {
						return "Error at Quotation Saving";
					}
				} else {
					return "Error at Customer Details Saving";
				}
			} else {
				return "Error at Customer Saving";
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
			mainLifeDetail.setCustCivilStatus("");
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
	public String editQuotation(InvpSavePersonalInfo _invpSaveQuotation, Integer userId, Integer qdId) throws Exception {
		CalculationUtils calculationUtils = null;
		Products products = null;
		Customer customer = null;
		Users user = null;
		Occupation occupation = null;
		CustomerDetails customerDetails = null;
		Quotation quotation = null;
		QuotationDetails quotationDetails = null;
		try {
			calculationUtils = new CalculationUtils();
			products = productDao.findByProductCode("AIP");
			Double contribution = _invpSaveQuotation.get_plan().get_bsa();
			AIPCalResp aip = calculateAIPMaturaty(_invpSaveQuotation.get_plan().get_term(), 2.0, 0.02, 9.5,
					contribution, new Date(), _invpSaveQuotation.get_plan().get_frequance(), true);
			occupation = occupationDao
					.findByOcupationid(Integer.parseInt(_invpSaveQuotation.get_mainlife().get_mOccupation()));

			Double adminFee = calculationUtils.getAdminFee(_invpSaveQuotation.get_plan().get_frequance());
			Double tax = calculationUtils.getTaxAmount(aip.getMaturaty() + adminFee);
			
			QuotationDetails details = quotationDetailsDao.findByQdId(qdId);
			
			customer = details.getCustomerDetails().getCustomer();
			user = userdao.findOne(userId);
			

			customer.setCustCreateBy(user.getUser_Code());
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
			quotationDetails.setBaseSum(aip.getMaturaty());
			quotationDetails.setInterestRate(10.0);
			quotationDetails.setTaxAmount(tax);
			String frequance = _invpSaveQuotation.get_plan().get_frequance();
			quotationDetails.setPayMode(frequance);
			quotationDetails.setPolTerm(_invpSaveQuotation.get_plan().get_term());
			quotationDetails.setPolicyFee(calculationUtils.getPolicyFee());
			quotationDetails.setQuotationCreateBy(user.getUser_Code());
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

			quotationDetails.setQuotationCreateBy(user.getUser_Code());
			quotationDetails.setQuotationquotationCreateDate(new Date());

			if (customerDao.save(customer) != null) {
				if (customerDetailsDao.save(customerDetails) != null) {
					if (quotationDao.save(quotation) != null) {
						if (quotationDetailsDao.save(quotationDetails) != null) {
							return "Success";
						} else {
							return "Error at Quotation Details Saving";
						}
					} else {
						return "Error at Quotation Saving";
					}
				} else {
					return "Error at Customer Details Saving";
				}
			} else {
				return "Error at Customer Saving";
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