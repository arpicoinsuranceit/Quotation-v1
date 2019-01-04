package org.arpicoinsurance.groupit.main.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.common.WebClient;
import org.arpicoinsurance.groupit.main.dao.BenefitsDao;
import org.arpicoinsurance.groupit.main.dao.CustomerDao;
import org.arpicoinsurance.groupit.main.dao.CustomerDetailsDao;
import org.arpicoinsurance.groupit.main.dao.OccupationDao;
import org.arpicoinsurance.groupit.main.dao.ProductDao;
import org.arpicoinsurance.groupit.main.dao.Quo_Benef_DetailsDao;
import org.arpicoinsurance.groupit.main.dao.QuotationDao;
import org.arpicoinsurance.groupit.main.dao.QuotationDetailsDao;
import org.arpicoinsurance.groupit.main.dao.RateCardAtpMcDao;
import org.arpicoinsurance.groupit.main.dao.RateCardAtpNdcDao;
import org.arpicoinsurance.groupit.main.dao.UsersDao;
import org.arpicoinsurance.groupit.main.helper.BenefictHistory;
import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.model.Child;
import org.arpicoinsurance.groupit.main.model.CustChildDetails;
import org.arpicoinsurance.groupit.main.model.Customer;
import org.arpicoinsurance.groupit.main.model.CustomerDetails;
import org.arpicoinsurance.groupit.main.model.MedicalDetails;
import org.arpicoinsurance.groupit.main.model.Occupation;
import org.arpicoinsurance.groupit.main.model.Products;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Child_Details;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Details;
import org.arpicoinsurance.groupit.main.model.Quotation;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;
import org.arpicoinsurance.groupit.main.model.RateCardAtpMc;
import org.arpicoinsurance.groupit.main.model.RateCardAtpNdc;
import org.arpicoinsurance.groupit.main.model.Users;
import org.arpicoinsurance.groupit.main.service.ATPService;
import org.arpicoinsurance.groupit.main.service.HealthRequirmentsService;
import org.arpicoinsurance.groupit.main.service.QuotationDetailsService;
import org.arpicoinsurance.groupit.main.service.custom.CalculateRiders;
import org.arpicoinsurance.groupit.main.service.custom.QuotationSaveUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ATPServiceImpl implements ATPService {

	@Autowired
	private QuotationDetailsService quotationDetailsService;

	@Autowired
	private QuotationDao quotationDao;

	@Autowired
	private CustomerDetailsDao customerDetailsDao;

	@Autowired
	private QuotationSaveUtilService quotationSaveUtilService;

	@Autowired
	private CalculateRiders calculateriders;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private UsersDao userDao;

	@Autowired
	private OccupationDao occupationDao;

	@Autowired
	private Quo_Benef_DetailsDao quoBenifDetailDao;

	@Autowired
	private CustomerDao customerDao;

	@Autowired
	private QuotationDetailsDao quotationDetailDao;

	@Autowired
	private HealthRequirmentsService healthRequirmentsService;

	@Autowired
	private RateCardAtpMcDao rateCardAtpMcDao;

	@Autowired
	private RateCardAtpNdcDao rateCardAtpNdcDao;

	@Override
	public BigDecimal calculateMaturity(int term, double bassum) throws Exception {
		
		System.out.println(term);
		System.out.println(bassum);

		Double rate = rateCardAtpMcDao.findByTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(term, new Date(),
				new Date(), new Date(), new Date()).getRate();

		BigDecimal maturity = new BigDecimal(bassum).multiply(new BigDecimal(rate)).setScale(0, RoundingMode.HALF_UP);

		return maturity;
	}

	@Override
	public BigDecimal calculateNaturalDeath(double maturity, int age, double bassum) throws Exception {
		Double rate = rateCardAtpNdcDao
				.findByAgetoOrAgetoLessThanAndAgefromOrAgefromGreaterThanAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(
						age, age, age, age, new Date(), new Date(), new Date(), new Date())
				.getRate();

		BigDecimal ndc = (new BigDecimal(bassum).multiply(new BigDecimal(rate))).add(new BigDecimal(maturity))
				.setScale(0, RoundingMode.HALF_UP);

		return ndc;
	}

	@Override
	public QuotationQuickCalResponse getCalcutatedAtp(QuotationCalculation calculation) throws Exception {
		CalculationUtils calculationUtils = null;
		try {

			QuotationQuickCalResponse calResp = new QuotationQuickCalResponse();
			calculationUtils = new CalculationUtils();

			calResp.setBasicSumAssured(calculation.get_personalInfo().getBsa());
			/// Calculate Rebate Premium ///

			/// Calculate BSA Premium ///
//			BigDecimal bsaMonthly = calculateL2(quotationCalculation.get_personalInfo().getMocu(),
//					quotationCalculation.get_personalInfo().getMage(),
//					quotationCalculation.get_personalInfo().getTerm(), calculationUtils.getRebate("M"), new Date(),
//					quotationCalculation.get_personalInfo().getBsa(), calculationUtils.getPayterm("M"), calResp, false);
//
//			BigDecimal bsaYearly = bsaMonthly.multiply(new BigDecimal(12)).setScale(2);
//
//			BigDecimal bsaPremium = calculateL2(quotationCalculation.get_personalInfo().getMocu(),
//					quotationCalculation.get_personalInfo().getMage(),
//					quotationCalculation.get_personalInfo().getTerm(), rebate, new Date(),
//					quotationCalculation.get_personalInfo().getBsa(),
//					calculationUtils.getPayterm(quotationCalculation.get_personalInfo().getFrequance()), calResp, true);

//			calResp.setBasicSumAssured(bsaPremium.doubleValue());
//			calResp.setBsaYearlyPremium(bsaYearly.doubleValue());
			calResp.setAtp(true);

			calResp = calculateriders.getRiders(calculation, calResp);
			calResp.setAt6(
					calculateMaturity(calculation.get_personalInfo().getTerm(), calculation.get_personalInfo().getBsa())
							.doubleValue());
			calResp.setAt8(calculateNaturalDeath(calResp.getAt6(), calculation.get_personalInfo().getMage(),
					calculation.get_personalInfo().getBsa()).doubleValue());

			calResp.setMainLifeHealthReq(healthRequirmentsService.getSumAtRiskDetailsMainLife(calculation));

//			if (calculation.get_personalInfo().getSage() != null
//					&& calculation.get_personalInfo().getSgenger() != null) {
//				calResp.setSpouseHealthReq(healthRequirmentsService.getSumAtRiskDetailsSpouse(quotationCalculation));
//			}
//			calResp.setAt6(calculateMaturity(quotationCalculation.get_personalInfo().getTerm(),
//					quotationCalculation.get_personalInfo().getBsa()).doubleValue());
//			calResp.setGuaranteed(calculateMaturity(quotationCalculation.get_personalInfo().getTerm(),
//					quotationCalculation.get_personalInfo().getBsa()).doubleValue());

			Double tot = calResp.getBasicSumAssured() + calResp.getAddBenif();
			Double adminFee = calculationUtils.getAdminFee(calculation.get_personalInfo().getFrequance());
			Double tax = calculationUtils.getTaxAmount(tot + adminFee);
			Double extraOE = adminFee + tax;
			calResp.setExtraOE(extraOE);
			calResp.setTotPremium(tot + extraOE);

			return calResp;

		} finally {
			if (calculationUtils != null) {
				calculationUtils = null;
			}
		}
	}

	@Override
	public HashMap<String, Object> saveQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation,
			Integer id) throws Exception {
		Quotation quo = null;
		HashMap<String, Object> responseMap = new HashMap<>();

		System.out.println("/////////////////////////// called save");

		if (productDao.findByProductCode("ATP").getActive() == 0) {
			responseMap.put("status", "This Function is Currently Unavailable Due to Maintenance");
			return responseMap;
		}

		System.out.println("/////////////////////////// Maintains");

		QuotationQuickCalResponse calResp = getCalcutatedAtp(calculation);

		if (calResp.isErrorExist()) {
			responseMap.put("status", "Error at calculation");
			return responseMap;
		}

		System.out.println("/////////////////////////// called calculation");

//		String valPrm = validationPremium.validateEnd(calculation.get_personalInfo().getFrequance(),
//				calResp.getTotPremium());
//
//		if (!valPrm.equalsIgnoreCase("ok")) {
//			responseMap.put("status", valPrm);
//			return responseMap;
//		}
//
//		System.out.println("/////////////////////////// called validate Premium");
//		
//		System.out.println("_invpSaveQuotation.get_personalInfo().get_mainlife().get_mNic() : " +  _invpSaveQuotation.get_personalInfo().get_mainlife().get_mNic());
//		
//		if(_invpSaveQuotation.get_personalInfo().get_mainlife().get_mNic() != null && !_invpSaveQuotation.get_personalInfo().get_mainlife().get_mNic().isEmpty()) {
//			
//			System.out.println("/////////////////////////// Not");
//			
//			List<BenefictHistory> benefictHistories = benefictHistoryWebClient.getHistory(_invpSaveQuotation.get_personalInfo().get_mainlife().get_mNic());
//			
//			String resp = healthValidation.validateHealthEndArpAtrmAtrmAsfp(benefictHistories, calResp, _invpSaveQuotation);
//			
//			if (!resp.equalsIgnoreCase("ok")) {
//				responseMap.put("status", resp);
//				return responseMap;
//			}
//			
//		} else {
//			
//			System.out.println("/////////////////////////// Empty");
//			
//			String resp = healthValidation.validateHealthEndArpAtrmAtrmAsfp(calResp, _invpSaveQuotation);
//			
//			System.out.println(resp);
//			
//			if (!resp.equalsIgnoreCase("ok")) {
//				responseMap.put("status", resp);
//				return responseMap;
//			}
//		}
//		
//		
//		System.out.println("/////////////////////////// called validateHealth");

		Products products = productDao.findByProductCode("ATP");
		Users user = userDao.findOne(id);
		Occupation occupationMainlife = occupationDao.findByOcupationid(calculation.get_personalInfo().getMocu());
		// Occupation occupationSpouse =
		// occupationDao.findByOcupationid(calculation.get_personalInfo().getSocu());

		CustomerDetails mainLifeDetail = quotationSaveUtilService.getCustomerDetail(occupationMainlife,
				_invpSaveQuotation.get_personalInfo(), user);

//		CustomerDetails spouseDetail = quotationSaveUtilService.getSpouseDetail(occupationSpouse,
//				_invpSaveQuotation.get_personalInfo(), user);

		Customer mainlife = new Customer();
		mainlife.setCustName(_invpSaveQuotation.get_personalInfo().get_mainlife().get_mName());
		mainlife.setCustCreateDate(new Date());
		mainlife.setCustCreateBy(user.getUser_Name());
		mainlife.setCustCode(new WebClient().getCustCode(_invpSaveQuotation.get_personalInfo()));
		mainLifeDetail.setCustomer(mainlife);

//		Customer spouse = null;
//		if (_invpSaveQuotation.get_personalInfo().get_spouse() != null
//				&& _invpSaveQuotation.get_personalInfo().get_spouse().is_sActive()) {
//			spouse = new Customer();
//			spouse.setCustName(spouseDetail.getCustName());
//			spouseDetail.setCustomer(spouse);
//		}

//		ArrayList<Child> childList = quotationSaveUtilService
//				.getChilds(_invpSaveQuotation.get_personalInfo().get_childrenList());
//
//		ArrayList<CustChildDetails> custChildDetailsList = new ArrayList<>();
//		if (childList != null && !childList.isEmpty())
//			for (Child child : childList) {
//				CustChildDetails custChildDetails = new CustChildDetails();
//				custChildDetails.setChild(child);
//				custChildDetails.setCustomer(mainLifeDetail);
//				custChildDetailsList.add(custChildDetails);
//			}

		Quotation quotation = new Quotation();
		quotation.setStatus("active");
		quotation.setUser(user);
		quotation.setProducts(products);

		QuotationDetails quotationDetails = quotationSaveUtilService.getQuotationDetail(calResp, calculation, 0.0);

		quotationDetails.setCustomerDetails(mainLifeDetail);
//		if (spouseDetail != null) {
//			quotationDetails.setSpouseDetails(spouseDetail);
//		}

		quotationDetails.setQuotation(quotation);
		quotationDetails.setQuotationCreateBy(user.getUserCode());

//		ArrayList<MedicalDetails> medicalDetailList = new ArrayList<>();
//
//		if (calResp.getMainLifeHealthReq() != null && calResp.getMainLifeHealthReq().get("reqListMain") != null) {
//			for (String testCodes : (ArrayList<String>) calResp.getMainLifeHealthReq().get("reqListMain")) {
//				MedicalDetails medicalDetail = new MedicalDetails();
//				medicalDetail.setCustStatus("main");
//				medicalDetail.setMedDetailsCreateBy(user.getUserCode());
//				medicalDetail.setMedDetailsCreatedate(new Date());
//				medicalDetail.setMedicalReq(medicalReqDao.findOneByMedCode(testCodes));
//				medicalDetail.setStatus("Required");
//				medicalDetailList.add(medicalDetail);
//			}
//		}
//
//		if (calResp.getSpouseHealthReq() != null && calResp.getSpouseHealthReq().get("reqListMain") != null) {
//			for (String testCodes : (ArrayList<String>) calResp.getSpouseHealthReq().get("reqListMain")) {
//				MedicalDetails medicalDetail = new MedicalDetails();
//				medicalDetail.setCustStatus("spouse");
//				medicalDetail.setMedDetailsCreateBy(user.getUserCode());
//				medicalDetail.setMedDetailsCreatedate(new Date());
//				medicalDetail.setMedicalReq(medicalReqDao.findOneByMedCode(testCodes));
//				medicalDetail.setStatus("Required");
//				medicalDetailList.add(medicalDetail);
//			}
//		}

		ArrayList<Quo_Benef_Details> benef_DetailsList = quotationSaveUtilService.getBenifDetails(
				_invpSaveQuotation.get_riderDetails(), calResp, quotationDetails,
				_invpSaveQuotation.get_personalInfo().get_childrenList(),
				_invpSaveQuotation.get_personalInfo().get_plan().get_term());

//		Quo_Benef_Details l17 = new Quo_Benef_Details();
//
//		l17.setBenefit(benefitsDao.findOne(66));
//		l17.setRierCode("L17");
//		l17.setQuo_Benef_CreateBy(user.getUserCode());
//		l17.setQuo_Benef_CreateDate(new Date());
//		l17.setQuotationDetails(quotationDetails);
//		switch (quotationDetails.getPayMode()) {
//		case "M":
//			l17.setRiderPremium(0.0);
//			break;
//		case "Q":
//			l17.setRiderPremium(0.0);
//			break;
//		case "H":
//			l17.setRiderPremium(0.0);
//			break;
//		case "Y":
//			l17.setRiderPremium(0.0);
//			break;
//		case "S":
//			l17.setRiderPremium(0.0);
//			break;
//
//		default:
//			break;
//		}
//		l17.setRiderSum(calResp.getAt6());
//		l17.setRiderTerm(quotationDetails.getPolTerm());
//
//		benef_DetailsList.add(l17);
//		
//		Quo_Benef_Details l18 = new Quo_Benef_Details();
//
//		l18.setBenefit(benefitsDao.findOne(67));
//		l18.setRierCode("L18");
//		l18.setQuo_Benef_CreateBy(user.getUserCode());
//		l18.setQuo_Benef_CreateDate(new Date());
//		l18.setQuotationDetails(quotationDetails);
//		switch (quotationDetails.getPayMode()) {
//		case "M":
//			l18.setRiderPremium(0.0);
//			break;
//		case "Q":
//			l18.setRiderPremium(0.0);
//			break;
//		case "H":
//			l18.setRiderPremium(0.0);
//			break;
//		case "Y":
//			l18.setRiderPremium(0.0);
//			break;
//		case "S":
//			l18.setRiderPremium(0.0);
//			break;
//
//		default:
//			break;
//		}
//		l18.setRiderSum(calResp.getAt8());
//		l18.setRiderTerm(quotationDetails.getPolTerm());
//
//		benef_DetailsList.add(l18);

		//
		// for (Quo_Benef_Details quo_Benef_Details : benef_DetailsList) {

		// }

		//////////////////////////// save//////////////////////////////////
		Customer life = (Customer) customerDao.save(mainlife);

		CustomerDetails mainLifeDetails = customerDetailsDao.save(mainLifeDetail);

//		ArrayList<CustChildDetails> custChildDList = null;
		if (life != null && mainLifeDetails != null) {

//			if (spouse != null) {
//				Customer sp = customerDao.save(spouse);
//
//				CustomerDetails spDetsils = customerDetailsDao.save(spouseDetail);
//
//				if (sp == null && spDetsils != null) {
//					responseMap.put("status", "Error at Spouse Saving");
//					return responseMap;
//				}
//			}

//			ArrayList<Child> cList = (ArrayList<Child>) childDao.save(childList);
//
//			custChildDList = (ArrayList<CustChildDetails>) custChildDetailsDao.save(custChildDetailsList);
//
//			if (childList != null && childList.size() > 0) {
//				if (cList == null && custChildDList == null) {
//					responseMap.put("status", "Error at Child Saving");
//					return responseMap;
//				}
//			}

			quo = quotationDao.save(quotation);

			QuotationDetails quoDetails = quotationDetailDao.save(quotationDetails);

			///////////////////// Add Maturity //////////////////

			benef_DetailsList = quotationSaveUtilService.addMaturity("ATP", benef_DetailsList, calResp,
					_invpSaveQuotation.get_personalInfo().get_plan().get_term(), quoDetails);

			///////////////////// Done Add Maturity //////////////////
			///////////////////// Medical Re1q //////////////////////

//			for (MedicalDetails medicalDetails : medicalDetailList) {
//
//				medicalDetails.setQuotationDetails(quoDetails);
//			}
//
//			medicalDetailsDao.save(medicalDetailList);

			///////////////////// Done Save Medical req ////////////////

			if (quo != null && quoDetails != null) {
				// for (Quo_Benef_Details benef_Details2 : benef_DetailsList) {

				// }
				ArrayList<Quo_Benef_Details> bnfdList = (ArrayList<Quo_Benef_Details>) quoBenifDetailDao
						.save(benef_DetailsList);

				if (bnfdList == null) {

					responseMap.put("status", "Error at Benifict Saving");
					return responseMap;
				}
			} else {
				responseMap.put("status", "Error at Quotation Saving");
				return responseMap;
			}

		} else {
			responseMap.put("status", "Error at MainLife Saving");
			return responseMap;
		}

		responseMap.put("status", "Success");
		responseMap.put("code", quo.getId().toString());

		return responseMap;
	}

	@Override
	public HashMap<String, Object> editQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation,
			Integer userId, Integer qdId, Integer type) throws Exception {
		Quotation quo = null;

		HashMap<String, Object> responseMap = new HashMap<>();

		if (productDao.findByProductCode("ATP").getActive() == 0) {
			responseMap.put("status", "This Function is Currently Unavailable Due to Maintenance");
			return responseMap;
		}

		QuotationQuickCalResponse calResp = getCalcutatedAtp(calculation);

		if (calResp.isErrorExist()) {
			responseMap.put("status", "Error at calculation");
			return responseMap;
		}

//		System.out.println("check cal errors pass");
//
//		String valPrm = validationPremium.validateAtp(calculation.get_personalInfo().getFrequance(),
//				calResp.getTotPremium());
//
//		if (!valPrm.equalsIgnoreCase("ok")) {
//			responseMap.put("status", valPrm);
//			return responseMap;
//		}
//		
//		System.out.println("check health validation");
//		
//		if(_invpSaveQuotation.get_personalInfo().get_mainlife().get_mNic() != null && !_invpSaveQuotation.get_personalInfo().get_mainlife().get_mNic().isEmpty()) {
//			List<BenefictHistory> benefictHistories = benefictHistoryWebClient.getHistory(_invpSaveQuotation.get_personalInfo().get_mainlife().get_mNic());
//			
//			String resp = healthValidation.validateHealthEndArpAtrmAtrmAsfp(benefictHistories, calResp, _invpSaveQuotation);
//			
//			if (!resp.equalsIgnoreCase("ok")) {
//				responseMap.put("status", resp);
//				return responseMap;
//			}
//			
//		} else {
//			String resp = healthValidation.validateHealthEndArpAtrmAtrmAsfp(calResp, _invpSaveQuotation);
//			if (!resp.equalsIgnoreCase("ok")) {
//				responseMap.put("status", resp);
//				return responseMap;
//			}
//		}
//		
//		System.out.println("check health validation pass");

		Users user = userDao.findOne(userId);

		Occupation occupationMainlife = occupationDao.findByOcupationid(calculation.get_personalInfo().getMocu());
//		Occupation occupationSpouse = occupationDao.findByOcupationid(calculation.get_personalInfo().getSocu());

		CustomerDetails mainLifeDetail = quotationSaveUtilService.getCustomerDetail(occupationMainlife,
				_invpSaveQuotation.get_personalInfo(), user);
//		CustomerDetails spouseDetail = quotationSaveUtilService.getSpouseDetail(occupationSpouse,
//				_invpSaveQuotation.get_personalInfo(), user);

		QuotationDetails quotationDetails = quotationDetailsService.findQuotationDetails(qdId);

		Customer mainlife = quotationDetails.getCustomerDetails().getCustomer();
		Customer spouse = null;
//		if (spouseDetail != null) {
//			try {
//				spouse = quotationDetails.getSpouseDetails().getCustomer();
//			} catch (NullPointerException ex) {
//				spouse = null;
//			}
//
//			if (spouse != null) {
//				spouseDetail.setCustomer(spouse);
//			} else {
//				spouse = new Customer();
//				spouse.setCustName(spouseDetail.getCustName());
//				spouse.setCustCreateDate(new Date());
//				spouse.setCustCreateBy(user.getUser_Name());
//				spouseDetail.setCustomer(spouse);
//			}
//
//		} else {
//
//		}

		mainLifeDetail.setCustomer(mainlife);

//		ArrayList<Child> childList = quotationSaveUtilService
//				.getChilds(_invpSaveQuotation.get_personalInfo().get_childrenList());
//
//		ArrayList<CustChildDetails> custChildDetailsList = new ArrayList<>();
//		if (childList != null && !childList.isEmpty()) {
//			for (Child child : childList) {
//				CustChildDetails custChildDetails = new CustChildDetails();
//				custChildDetails.setChild(child);
//				custChildDetails.setCustomer(mainLifeDetail);
//				custChildDetailsList.add(custChildDetails);
//			}
//		}

		Quotation quotation = quotationDetails.getQuotation();

		Integer count = quotationDetailDao.countByQuotation(quotation);

		if (type == 1) {
			quotation.setStatus("active");
		}
		QuotationDetails quotationDetails1 = quotationSaveUtilService.getQuotationDetail(calResp, calculation, 0.0);
		quotationDetails1.setSeqnum(count + 1);
		quotationDetails1.setCustomerDetails(mainLifeDetail);
//		if (spouseDetail != null) {
//			quotationDetails1.setSpouseDetails(spouseDetail);
//		} else {
		quotationDetails1.setSpouseDetails(null);
//		}

		quotationDetails1.setQuotation(quotation);
		quotationDetails1.setQuotationCreateBy(user.getUserCode());
		quotationDetails1.setQuotationCreateDate(new Date());

//		ArrayList<MedicalDetails> medicalDetailList = new ArrayList<>();
//
//		if (calResp.getMainLifeHealthReq() != null && calResp.getMainLifeHealthReq().get("reqListMain") != null) {
//			for (String testCodes : (ArrayList<String>) calResp.getMainLifeHealthReq().get("reqListMain")) {
//				MedicalDetails medicalDetail = new MedicalDetails();
//				medicalDetail.setCustStatus("main");
//				medicalDetail.setMedDetailsCreateBy(user.getUserCode());
//				medicalDetail.setMedDetailsCreatedate(new Date());
//				medicalDetail.setMedicalReq(medicalReqDao.findOneByMedCode(testCodes));
//				medicalDetail.setStatus("Required");
//				medicalDetailList.add(medicalDetail);
//			}
//		}
//
//		if (calResp.getSpouseHealthReq() != null && calResp.getSpouseHealthReq().get("reqListMain") != null) {
//			for (String testCodes : (ArrayList<String>) calResp.getSpouseHealthReq().get("reqListMain")) {
//				MedicalDetails medicalDetail = new MedicalDetails();
//				medicalDetail.setCustStatus("spouse");
//				medicalDetail.setMedDetailsCreateBy(user.getUserCode());
//				medicalDetail.setMedDetailsCreatedate(new Date());
//				medicalDetail.setMedicalReq(medicalReqDao.findOneByMedCode(testCodes));
//				medicalDetail.setStatus("Required");
//				medicalDetailList.add(medicalDetail);
//			}
//		}

		ArrayList<Quo_Benef_Details> benef_DetailsList = quotationSaveUtilService.getBenifDetails(
				_invpSaveQuotation.get_riderDetails(), calResp, quotationDetails1,
				_invpSaveQuotation.get_personalInfo().get_childrenList(),
				_invpSaveQuotation.get_personalInfo().get_plan().get_term());

//		Quo_Benef_Details benef_Details = new Quo_Benef_Details();
//		benef_Details.setBenefit(benefitsDao.findOne(21));
//		benef_Details.setRierCode("L2");
//		benef_Details.setQuo_Benef_CreateBy(user.getUserCode());
//		benef_Details.setQuo_Benef_CreateDate(new Date());
//		benef_Details.setQuotationDetails(quotationDetails1);
//		switch (quotationDetails1.getPayMode()) {
//		case "M":
//			benef_Details.setRiderPremium(quotationDetails1.getPremiumMonth());
//			break;
//		case "Q":
//			benef_Details.setRiderPremium(quotationDetails1.getPremiumQuater());
//			break;
//		case "H":
//			benef_Details.setRiderPremium(quotationDetails1.getPremiumHalf());
//			break;
//		case "Y":
//			benef_Details.setRiderPremium(quotationDetails1.getPremiumYear());
//			break;
//		case "S":
//			benef_Details.setRiderPremium(quotationDetails1.getPremiumSingle());
//			break;
//
//		default:
//			break;
//		}
//		benef_Details.setRiderSum(quotationDetails1.getBaseSum());
//		benef_Details.setRiderTerm(quotationDetails1.getPolTerm());
//
//		benef_DetailsList.add(benef_Details);
		//////////////////////////// save edit//////////////////////////////////

		Customer life = (Customer) customerDao.save(mainlife);
		CustomerDetails mainLifeDetails = customerDetailsDao.save(mainLifeDetail);
//		ArrayList<CustChildDetails> custChildDList = null;
		if (life != null && mainLifeDetails != null) {

//			if (spouseDetail != null) {
//				Customer sp = customerDao.save(spouse);
//				CustomerDetails spDetsils = customerDetailsDao.save(spouseDetail);
//				if (sp == null && spDetsils != null) {
//					responseMap.put("status", "Error at Spouse Updating");
//					return responseMap;
//				}
//			}
//
//			ArrayList<Child> cList = (ArrayList<Child>) childDao.save(childList);
//			custChildDList = (ArrayList<CustChildDetails>) custChildDetailsDao.save(custChildDetailsList);
//			if (childList != null && childList.size() > 0) {
//				if (cList == null && custChildDList == null) {
//					responseMap.put("status", "Error at Child Updating");
//					return responseMap;
//				}
//			}

			quo = quotationDao.save(quotation);
			QuotationDetails quoDetails = quotationDetailDao.save(quotationDetails1);

			///////////////////// Add Maturity //////////////////

			benef_DetailsList = quotationSaveUtilService.addMaturity("ATP", benef_DetailsList, calResp,
					_invpSaveQuotation.get_personalInfo().get_plan().get_term(), quoDetails);

			///////////////////// Done Add Maturity //////////////////

			///////////////////// Medical Re1q //////////////////////

//			for (MedicalDetails medicalDetails : medicalDetailList) {
//
//				medicalDetails.setQuotationDetails(quoDetails);
//			}
//
//			medicalDetailsDao.save(medicalDetailList);

			///////////////////// Done Save Medical req ////////////////

			if (quo != null && quoDetails != null) {
				ArrayList<Quo_Benef_Details> bnfdList = (ArrayList<Quo_Benef_Details>) quoBenifDetailDao
						.save(benef_DetailsList);
				if (bnfdList != null) {

//					ArrayList<Quo_Benef_Child_Details> childBenifList = quotationSaveUtilService.getChildBenif(bnfdList,
//							custChildDList, childList, _invpSaveQuotation.get_personalInfo().get_childrenList(),
//							_invpSaveQuotation.get_personalInfo().get_plan().get_term(),
//							calculation.get_personalInfo().getFrequance(), calculation.get_riderDetails().get_cRiders(),
//							calResp);
//
//					if (quoBenifChildDetailsDao.save(childBenifList) == null) {
//						responseMap.put("status", "Error at Child Benifict Updating");
//						return responseMap;
//					}

				} else {
					responseMap.put("status", "Error at Benifict Updating");
					return responseMap;
				}
			} else {
				responseMap.put("status", "Error at Quotation Updating");
				return responseMap;
			}

		} else {
			responseMap.put("status", "Error at MainLife Updating");
			return responseMap;
		}

		responseMap.put("status", "Success");
		responseMap.put("code", quo.getId().toString());
		return responseMap;
	}

}