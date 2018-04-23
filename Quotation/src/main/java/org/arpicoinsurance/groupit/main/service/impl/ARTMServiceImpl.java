package org.arpicoinsurance.groupit.main.service.impl;

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
import org.arpicoinsurance.groupit.main.helper.InvpSavePersonalInfo;
import org.arpicoinsurance.groupit.main.helper.Plan;
import org.arpicoinsurance.groupit.main.model.Customer;
import org.arpicoinsurance.groupit.main.model.CustomerDetails;
import org.arpicoinsurance.groupit.main.model.Occupation;
import org.arpicoinsurance.groupit.main.model.Products;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Details;
import org.arpicoinsurance.groupit.main.model.Quotation;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;
import org.arpicoinsurance.groupit.main.model.Users;
import org.arpicoinsurance.groupit.main.service.ARTMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ARTMServiceImpl implements ARTMService{

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

	
	@Override
	public AIPCalResp calculateARTMMaturaty(Plan plan, Double intrat, boolean shedule, boolean isAddOccuLoading) throws Exception {
		AIPCalResp aipCalResp = new AIPCalResp();
		aipCalResp.setMaturaty(100.00);/// set maturity 1
		aipCalResp.setMaturaty10(110.00);/// set maturity 2
		aipCalResp.setMaturaty12(120.00);/// set maturity 3
		aipCalResp.setExtraOe(50.00);
		aipCalResp.setAipCalShedules(null);/// set cal schedule if 'schedule' is true..
		return aipCalResp;
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
			products = productDao.findByProductCode("ARTM");
			
			Double contribution = _invpSaveQuotation.get_plan().get_bsa();
			
			AIPCalResp calValues = calculateARTMMaturaty(_invpSaveQuotation.get_plan(), 0.0, false, true);
			
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
			quotationDetails.setRetirmentAge(_invpSaveQuotation.get_plan().getRetAge());
			quotationDetails.setPensionTerm(_invpSaveQuotation.get_plan().getPensionPaingTerm());
			quotationDetails.setPolTerm(_invpSaveQuotation.get_plan().get_term());
			quotationDetails.setPolicyFee(calculationUtils.getPolicyFee());
			quotationDetails.setPaingTerm(_invpSaveQuotation.get_plan().get_payingterm());
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
						mat1.setRiderPremium(0.0);
						mat1.setRiderTerm(_invpSaveQuotation.get_plan().get_term());
						mat1.setRiderSum(calValues.getMaturaty());
						mat1.setQuotationDetails(quoDetails);
						mat1.setRierCode("L14");
						mat1.setBenefit(benefitsDao.findByRiderCode("L14"));
						benefictList.add(mat1);

						Quo_Benef_Details mat2 = new Quo_Benef_Details();
						mat2.setRiderPremium(0.0);
						mat2.setRiderTerm(_invpSaveQuotation.get_plan().get_term());
						mat2.setRiderSum(calValues.getMaturaty10());
						mat2.setQuotationDetails(quoDetails);
						mat2.setRierCode("L15");
						mat2.setBenefit(benefitsDao.findByRiderCode("L15"));
						benefictList.add(mat2);

						Quo_Benef_Details mat3 = new Quo_Benef_Details();
						mat3.setRiderPremium(0.0);
						mat3.setRiderTerm(_invpSaveQuotation.get_plan().get_term());
						mat3.setRiderSum(calValues.getMaturaty12());
						mat3.setQuotationDetails(quoDetails);
						mat3.setRierCode("L16");
						mat3.setBenefit(benefitsDao.findByRiderCode("L16"));
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

	@Override
	public HashMap<String, Object> editQuotation(InvpSavePersonalInfo _invpSaveQuotation, Integer userId, Integer qdId)
			throws Exception {
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
			products = productDao.findByProductCode("ARTM");
			
			Double contribution = _invpSaveQuotation.get_plan().get_bsa();
			
			AIPCalResp calValues = calculateARTMMaturaty(_invpSaveQuotation.get_plan(), 0.0, false, true);
			
			occupation = occupationDao
					.findByOcupationid(Integer.parseInt(_invpSaveQuotation.get_mainlife().get_mOccupation()));
			
			Double adminFee = calculationUtils.getAdminFee(_invpSaveQuotation.get_plan().get_frequance());
			
			Double tax = calculationUtils.getTaxAmount(contribution + adminFee);
			
			QuotationDetails details = quotationDetailsDao.findByQdId(qdId);
			
			customer = details.getCustomerDetails().getCustomer();
			
			user = userdao.findOne(userId);

			customer.setCustModifyBy(user.getUserCode());
			customer.setCustModifyDate(new Date());
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
			quotationDetails.setQuotationModifyBy(user.getUserCode());
			quotationDetails.setQuotationModifyDate(new Date());
			quotationDetails.setBaseSum(0.0);
			quotationDetails.setInterestRate(10.0);
			quotationDetails.setTaxAmount(tax);
			String frequance = _invpSaveQuotation.get_plan().get_frequance();
			quotationDetails.setPayMode(frequance);
			quotationDetails.setRetirmentAge(_invpSaveQuotation.get_plan().getRetAge());
			quotationDetails.setPensionTerm(_invpSaveQuotation.get_plan().getPensionPaingTerm());
			quotationDetails.setPolTerm(_invpSaveQuotation.get_plan().get_term());
			quotationDetails.setPolicyFee(calculationUtils.getPolicyFee());
			quotationDetails.setPaingTerm(_invpSaveQuotation.get_plan().get_payingterm());
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
						mat1.setRiderPremium(0.0);
						mat1.setRiderTerm(_invpSaveQuotation.get_plan().get_term());
						mat1.setRiderSum(calValues.getMaturaty());
						mat1.setQuotationDetails(quoDetails);
						mat1.setRierCode("L14");
						mat1.setBenefit(benefitsDao.findByRiderCode("L14"));
						benefictList.add(mat1);

						Quo_Benef_Details mat2 = new Quo_Benef_Details();
						mat2.setRiderPremium(0.0);
						mat2.setRiderTerm(_invpSaveQuotation.get_plan().get_term());
						mat2.setRiderSum(calValues.getMaturaty10());
						mat2.setQuotationDetails(quoDetails);
						mat2.setRierCode("L15");
						mat2.setBenefit(benefitsDao.findByRiderCode("L15"));
						benefictList.add(mat2);

						Quo_Benef_Details mat3 = new Quo_Benef_Details();
						mat3.setRiderPremium(0.0);
						mat3.setRiderTerm(_invpSaveQuotation.get_plan().get_term());
						mat3.setRiderSum(calValues.getMaturaty12());
						mat3.setQuotationDetails(quoDetails);
						mat3.setRierCode("L16");
						mat3.setBenefit(benefitsDao.findByRiderCode("L16"));
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

}
