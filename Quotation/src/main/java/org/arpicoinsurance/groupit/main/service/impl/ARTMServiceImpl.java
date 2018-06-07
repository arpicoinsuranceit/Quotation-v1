package org.arpicoinsurance.groupit.main.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.common.WebClient;
import org.arpicoinsurance.groupit.main.dao.BenefitsDao;
import org.arpicoinsurance.groupit.main.dao.ChildDao;
import org.arpicoinsurance.groupit.main.dao.CustChildDetailsDao;
import org.arpicoinsurance.groupit.main.dao.CustomerDao;
import org.arpicoinsurance.groupit.main.dao.CustomerDetailsDao;
import org.arpicoinsurance.groupit.main.dao.MedicalDetailsDao;
import org.arpicoinsurance.groupit.main.dao.MedicalReqDao;
import org.arpicoinsurance.groupit.main.dao.OccupationDao;
import org.arpicoinsurance.groupit.main.dao.ProductDao;
import org.arpicoinsurance.groupit.main.dao.Quo_Benef_Child_DetailsDao;
import org.arpicoinsurance.groupit.main.dao.Quo_Benef_DetailsDao;
import org.arpicoinsurance.groupit.main.dao.QuotationDao;
import org.arpicoinsurance.groupit.main.dao.QuotationDetailsDao;
import org.arpicoinsurance.groupit.main.dao.UsersDao;
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
import org.arpicoinsurance.groupit.main.model.Users;
import org.arpicoinsurance.groupit.main.service.ARTMService;
import org.arpicoinsurance.groupit.main.service.HealthRequirmentsService;
import org.arpicoinsurance.groupit.main.service.QuotationDetailsService;
import org.arpicoinsurance.groupit.main.service.custom.CalculateRiders;
import org.arpicoinsurance.groupit.main.service.custom.QuotationSaveUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ARTMServiceImpl implements ARTMService{

	@Autowired
	private MedicalDetailsDao medicalDetailsDao;
	
	@Autowired
	private BenefitsDao benefitsDao;

	@Autowired
	private Quo_Benef_DetailsDao quoBenifDetailDao;

	@Autowired
	private UsersDao userDao;

	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private CustChildDetailsDao custChildDetailsDao;

	@Autowired
	private CustomerDetailsDao customerDetailsDao;

	@Autowired
	private QuotationDao quotationDao;
	
	@Autowired
	private QuotationDetailsService quotationDetailsService;

	@Autowired
	private Quo_Benef_Child_DetailsDao quoBenifChildDetailsDao;
	
	@Autowired
	private MedicalReqDao medicalReqDao;
	
	@Autowired
	private QuotationDetailsDao quotationDetailDao;
	
	@Autowired
	private ChildDao childDao;

	@Autowired
	private QuotationSaveUtilService quotationSaveUtilService;

	@Autowired
	private OccupationDao occupationDao;

	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private CalculateRiders calculateriders;
	
	@Autowired
	private HealthRequirmentsService healthRequirmentsService;

	@Override
	public BigDecimal calculateMaturity() throws Exception {
		return new BigDecimal(1000000);
	}

	@Override
	public QuotationQuickCalResponse getCalcutatedARTM(QuotationCalculation calculation) throws Exception {
		
		CalculationUtils calculationUtils = null;
		try {

			QuotationQuickCalResponse calResp = new QuotationQuickCalResponse();
			calculationUtils = new CalculationUtils();
			BigDecimal bsaPremium = new BigDecimal(calculation.get_personalInfo().getBsa());
			calResp.setBasicSumAssured(bsaPremium.doubleValue());
			calResp = calculateriders.getRiders(calculation, calResp);
			calResp.setMainLifeHealthReq(healthRequirmentsService.getSumAtRiskDetailsMainLife(calculation));

			if(calculation.get_personalInfo().getSage()!=null &&
					calculation.get_personalInfo().getSgenger()!=null){
				calResp.setSpouseHealthReq(healthRequirmentsService.getSumAtRiskDetailsSpouse(calculation));
			}
			
			calResp.setAt6(calculateMaturity().doubleValue());
			calResp.setAt8(calculateMaturity().doubleValue());
			calResp.setAt10(calculateMaturity().doubleValue());

			calResp.setPensionPremium1(pensionPremium().doubleValue());
			calResp.setPensionPremium2(pensionPremium().doubleValue());
			calResp.setPensionPremium3(pensionPremium().doubleValue());
			
			
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

		QuotationQuickCalResponse calResp = getCalcutatedARTM(calculation);

		if (calResp.isErrorExist()) {
			responseMap.put("status", "Error at calculation");
			return responseMap;
		}
		Products products = productDao.findByProductCode("ARTM");
		Users user = userDao.findOne(id);
		Occupation occupationMainlife = occupationDao.findByOcupationid(calculation.get_personalInfo().getMocu());
		Occupation occupationSpouse = occupationDao.findByOcupationid(calculation.get_personalInfo().getSocu());

		CustomerDetails mainLifeDetail = quotationSaveUtilService.getCustomerDetail(occupationMainlife,
				_invpSaveQuotation.get_personalInfo(), user);

		CustomerDetails spouseDetail = quotationSaveUtilService.getSpouseDetail(occupationSpouse,
				_invpSaveQuotation.get_personalInfo(), user);

		Customer mainlife = new Customer();
		mainlife.setCustName(_invpSaveQuotation.get_personalInfo().get_mainlife().get_mName());
		mainlife.setCustCreateDate(new Date());
		mainlife.setCustCreateBy(user.getUser_Name());
		mainlife.setCustCode(new WebClient().getCustCode(_invpSaveQuotation.get_personalInfo()));
		mainLifeDetail.setCustomer(mainlife);

		Customer spouse = null;
		if (_invpSaveQuotation.get_personalInfo().get_spouse() != null
				&& _invpSaveQuotation.get_personalInfo().get_spouse().is_sActive()) {
			spouse = new Customer();
			spouse.setCustName(spouseDetail.getCustName());
			spouseDetail.setCustomer(spouse);
		}

		ArrayList<Child> childList = quotationSaveUtilService
				.getChilds(_invpSaveQuotation.get_personalInfo().get_childrenList());

		ArrayList<CustChildDetails> custChildDetailsList = new ArrayList<>();
		if (childList != null && !childList.isEmpty())
			for (Child child : childList) {
				CustChildDetails custChildDetails = new CustChildDetails();
				custChildDetails.setChild(child);
				custChildDetails.setCustomer(mainLifeDetail);
				custChildDetailsList.add(custChildDetails);
			}

		Quotation quotation = new Quotation();
		quotation.setStatus("active");
		quotation.setUser(user);
		quotation.setProducts(products);

		QuotationDetails quotationDetails = quotationSaveUtilService.getQuotationDetail(calResp, calculation, 0.0);

		quotationDetails.setCustomerDetails(mainLifeDetail);
		if (spouseDetail != null) {
			quotationDetails.setSpouseDetails(spouseDetail);
		}

		quotationDetails.setQuotation(quotation);
		quotationDetails.setQuotationCreateBy(user.getUserCode());

		ArrayList<MedicalDetails> medicalDetailList = new ArrayList<>();

		if (calResp.getMainLifeHealthReq() != null && calResp.getMainLifeHealthReq().get("reqListMain") != null) {
			for (String testCodes : (ArrayList<String>) calResp.getMainLifeHealthReq().get("reqListMain")) {
				MedicalDetails medicalDetail = new MedicalDetails();
				medicalDetail.setCustStatus("main");
				medicalDetail.setMedDetailsCreateBy(user.getUserCode());
				medicalDetail.setMedDetailsCreatedate(new Date());
				medicalDetail.setMedicalReq(medicalReqDao.findOneByMedCode(testCodes));
				medicalDetail.setStatus("Required");
				medicalDetailList.add(medicalDetail);
			}
		}

		if (calResp.getSpouseHealthReq() != null && calResp.getSpouseHealthReq().get("reqListMain") != null) {
			for (String testCodes : (ArrayList<String>) calResp.getSpouseHealthReq().get("reqListMain")) {
				MedicalDetails medicalDetail = new MedicalDetails();
				medicalDetail.setCustStatus("spouse");
				medicalDetail.setMedDetailsCreateBy(user.getUserCode());
				medicalDetail.setMedDetailsCreatedate(new Date());
				medicalDetail.setMedicalReq(medicalReqDao.findOneByMedCode(testCodes));
				medicalDetail.setStatus("Required");
				medicalDetailList.add(medicalDetail);
			}
		}

		ArrayList<Quo_Benef_Details> benef_DetailsList = quotationSaveUtilService.getBenifDetails(
				_invpSaveQuotation.get_riderDetails(), calResp, quotationDetails,
				_invpSaveQuotation.get_personalInfo().get_childrenList(),
				_invpSaveQuotation.get_personalInfo().get_plan().get_term());

		Quo_Benef_Details benef_Details = new Quo_Benef_Details();

		benef_Details.setBenefit(benefitsDao.findOne(21));
		benef_Details.setRierCode("L2");
		benef_Details.setQuo_Benef_CreateBy(user.getUserCode());
		benef_Details.setQuo_Benef_CreateDate(new Date());
		benef_Details.setQuotationDetails(quotationDetails);
		switch (quotationDetails.getPayMode()) {
		case "M":
			benef_Details.setRiderPremium(quotationDetails.getPremiumMonth());
			break;
		case "Q":
			benef_Details.setRiderPremium(quotationDetails.getPremiumQuater());
			break;
		case "H":
			benef_Details.setRiderPremium(quotationDetails.getPremiumHalf());
			break;
		case "Y":
			benef_Details.setRiderPremium(quotationDetails.getPremiumYear());
			break;
		case "S":
			benef_Details.setRiderPremium(quotationDetails.getPremiumSingle());
			break;

		default:
			break;
		}
		benef_Details.setRiderSum(quotationDetails.getBaseSum());
		benef_Details.setRiderTerm(quotationDetails.getPolTerm());

		benef_DetailsList.add(benef_Details);
//
//		for (Quo_Benef_Details quo_Benef_Details : benef_DetailsList) {
//			System.out.println("");
//			System.out.println(quo_Benef_Details.toString());
//			System.out.println("");
//		}

		//////////////////////////// save//////////////////////////////////
		Customer life = (Customer) customerDao.save(mainlife);
//		System.out.println("custSave");
		CustomerDetails mainLifeDetails = customerDetailsDao.save(mainLifeDetail);
//		System.out.println("custDetailSaveSave");
		ArrayList<CustChildDetails> custChildDList = null;
		if (life != null && mainLifeDetails != null) {

			if (spouse != null) {
				Customer sp = customerDao.save(spouse);
//				System.out.println("custSSave");
				CustomerDetails spDetsils = customerDetailsDao.save(spouseDetail);
//				System.out.println("custSDetailSave");
				if (sp == null && spDetsils != null) {
					responseMap.put("status", "Error at Spouse Saving");
					return responseMap;
				}
			}

			ArrayList<Child> cList = (ArrayList<Child>) childDao.save(childList);
//			System.out.println("childSave");
			custChildDList = (ArrayList<CustChildDetails>) custChildDetailsDao.save(custChildDetailsList);
//			System.out.println("childDetailSave");
			if (childList != null && childList.size() > 0) {
				if (cList == null && custChildDList == null) {
					responseMap.put("status", "Error at Child Saving");
					return responseMap;
				}
			}

			quo = quotationDao.save(quotation);
//			System.out.println("quotationSave");
			QuotationDetails quoDetails = quotationDetailDao.save(quotationDetails);

			///////////////////// Add Maturity //////////////////

			benef_DetailsList = quotationSaveUtilService.addMaturity("END1", benef_DetailsList, calResp,
					_invpSaveQuotation.get_personalInfo().get_plan().get_term(), quoDetails);

			///////////////////// Done Add Maturity //////////////////
			///////////////////// Medical Re1q //////////////////////

			for (MedicalDetails medicalDetails : medicalDetailList) {
//				System.out.println(quoDetails.getQdId() + " //////// quo detail id");
				medicalDetails.setQuotationDetails(quoDetails);
			}

			medicalDetailsDao.save(medicalDetailList);

			///////////////////// Done Save Medical req ////////////////

			if (quo != null && quoDetails != null) {
//				for (Quo_Benef_Details benef_Details2 : benef_DetailsList) {
//					System.out.println(benef_Details2.toString());
//					System.out.println("");
//				}
				ArrayList<Quo_Benef_Details> bnfdList = (ArrayList<Quo_Benef_Details>) quoBenifDetailDao
						.save(benef_DetailsList);
//				System.out.println("benDetailsSave");
				if (bnfdList != null) {

					ArrayList<Quo_Benef_Child_Details> childBenifList = quotationSaveUtilService.getChildBenif(bnfdList,
							custChildDList, childList, _invpSaveQuotation.get_personalInfo().get_childrenList(),
							_invpSaveQuotation.get_personalInfo().get_plan().get_term(),
							calculation.get_personalInfo().getFrequance(),
							calculation.get_riderDetails().get_cRiders());

					if (quoBenifChildDetailsDao.save(childBenifList) == null) {
						responseMap.put("status", "Error at Child Benifict Saving");
						return responseMap;
					}

				} else {
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
			Integer userId, Integer qdId) throws Exception {
		CalculationUtils calculationUtils = new CalculationUtils();

		Quotation quo = null;

		HashMap<String, Object> responseMap = new HashMap<>();
		
		QuotationQuickCalResponse calResp = getCalcutatedARTM(calculation);
		if (calResp.isErrorExist()) {
			responseMap.put("status", "Error at calculation");
			return responseMap;
		}

		Products products = productDao.findByProductCode("ARTM");
		Users user = userDao.findOne(userId);

		Occupation occupationMainlife = occupationDao.findByOcupationid(calculation.get_personalInfo().getMocu());
		Occupation occupationSpouse = occupationDao.findByOcupationid(calculation.get_personalInfo().getSocu());

		CustomerDetails mainLifeDetail = quotationSaveUtilService.getCustomerDetail(occupationMainlife,
				_invpSaveQuotation.get_personalInfo(), user);
		CustomerDetails spouseDetail = quotationSaveUtilService.getSpouseDetail(occupationSpouse,
				_invpSaveQuotation.get_personalInfo(), user);

		QuotationDetails quotationDetails = quotationDetailsService.findQuotationDetails(qdId);

		Customer mainlife = quotationDetails.getCustomerDetails().getCustomer();
		Customer spouse = null;
		if (spouseDetail != null) {
			try {
				spouse = quotationDetails.getSpouseDetails().getCustomer();
			} catch (NullPointerException ex) {
				spouse = null;
			}

			if (spouse != null) {
				spouseDetail.setCustomer(spouse);
			} else {
				spouse = new Customer();
				spouse.setCustName(spouseDetail.getCustName());
				spouse.setCustCreateDate(new Date());
				spouse.setCustCreateBy(user.getUser_Name());
				spouseDetail.setCustomer(spouse);
			}

		} else {

		}

		mainLifeDetail.setCustomer(mainlife);

		ArrayList<Child> childList = quotationSaveUtilService
				.getChilds(_invpSaveQuotation.get_personalInfo().get_childrenList());

		ArrayList<CustChildDetails> custChildDetailsList = new ArrayList<>();
		if (childList != null && !childList.isEmpty()) {
			for (Child child : childList) {
				CustChildDetails custChildDetails = new CustChildDetails();
				custChildDetails.setChild(child);
				custChildDetails.setCustomer(mainLifeDetail);
				custChildDetailsList.add(custChildDetails);
			}
		}

		Quotation quotation = quotationDetails.getQuotation();

		QuotationDetails quotationDetails1 = quotationSaveUtilService.getQuotationDetail(calResp, calculation, 0.0);

		quotationDetails1.setCustomerDetails(mainLifeDetail);
		if (spouseDetail != null) {
			quotationDetails1.setSpouseDetails(spouseDetail);
		} else {
			quotationDetails1.setSpouseDetails(null);
		}

		quotationDetails1.setQuotation(quotation);
		quotationDetails1.setQuotationCreateBy(user.getUserCode());

		ArrayList<MedicalDetails> medicalDetailList = new ArrayList<>();

		if (calResp.getMainLifeHealthReq() != null && calResp.getMainLifeHealthReq().get("reqListMain") != null) {
			for (String testCodes : (ArrayList<String>) calResp.getMainLifeHealthReq().get("reqListMain")) {
				MedicalDetails medicalDetail = new MedicalDetails();
				medicalDetail.setCustStatus("main");
				medicalDetail.setMedDetailsCreateBy(user.getUserCode());
				medicalDetail.setMedDetailsCreatedate(new Date());
				medicalDetail.setMedicalReq(medicalReqDao.findOneByMedCode(testCodes));
				medicalDetail.setStatus("Required");
				medicalDetailList.add(medicalDetail);
			}
		}

		if (calResp.getSpouseHealthReq() != null && calResp.getSpouseHealthReq().get("reqListMain") != null) {
			for (String testCodes : (ArrayList<String>) calResp.getSpouseHealthReq().get("reqListMain")) {
				MedicalDetails medicalDetail = new MedicalDetails();
				medicalDetail.setCustStatus("spouse");
				medicalDetail.setMedDetailsCreateBy(user.getUserCode());
				medicalDetail.setMedDetailsCreatedate(new Date());
				medicalDetail.setMedicalReq(medicalReqDao.findOneByMedCode(testCodes));
				medicalDetail.setStatus("Required");
				medicalDetailList.add(medicalDetail);
			}
		}

		ArrayList<Quo_Benef_Details> benef_DetailsList = quotationSaveUtilService.getBenifDetails(
				_invpSaveQuotation.get_riderDetails(), calResp, quotationDetails1,
				_invpSaveQuotation.get_personalInfo().get_childrenList(),
				_invpSaveQuotation.get_personalInfo().get_plan().get_term());

		Quo_Benef_Details benef_Details = new Quo_Benef_Details();
		benef_Details.setBenefit(benefitsDao.findOne(21));
		benef_Details.setRierCode("L2");
		benef_Details.setQuo_Benef_CreateBy(user.getUserCode());
		benef_Details.setQuo_Benef_CreateDate(new Date());
		benef_Details.setQuotationDetails(quotationDetails1);
		switch (quotationDetails1.getPayMode()) {
		case "M":
			benef_Details.setRiderPremium(quotationDetails1.getPremiumMonth());
			break;
		case "Q":
			benef_Details.setRiderPremium(quotationDetails1.getPremiumQuater());
			break;
		case "H":
			benef_Details.setRiderPremium(quotationDetails1.getPremiumHalf());
			break;
		case "Y":
			benef_Details.setRiderPremium(quotationDetails1.getPremiumYear());
			break;
		case "S":
			benef_Details.setRiderPremium(quotationDetails1.getPremiumSingle());
			break;

		default:
			break;
		}
		benef_Details.setRiderSum(quotationDetails1.getBaseSum());
		benef_Details.setRiderTerm(quotationDetails1.getPolTerm());

		benef_DetailsList.add(benef_Details);
		//////////////////////////// save edit//////////////////////////////////

		Customer life = (Customer) customerDao.save(mainlife);
		CustomerDetails mainLifeDetails = customerDetailsDao.save(mainLifeDetail);
		ArrayList<CustChildDetails> custChildDList = null;
		if (life != null && mainLifeDetails != null) {

			if (spouseDetail != null) {
				Customer sp = customerDao.save(spouse);
				CustomerDetails spDetsils = customerDetailsDao.save(spouseDetail);
				if (sp == null && spDetsils != null) {
					responseMap.put("status", "Error at Spouse Updating");
					return responseMap;
				}
			}

			ArrayList<Child> cList = (ArrayList<Child>) childDao.save(childList);
			custChildDList = (ArrayList<CustChildDetails>) custChildDetailsDao.save(custChildDetailsList);
			if (childList != null && childList.size() > 0) {
				if (cList == null && custChildDList == null) {
					responseMap.put("status", "Error at Child Updating");
					return responseMap;
				}
			}

			quo = quotationDao.save(quotation);
			QuotationDetails quoDetails = quotationDetailDao.save(quotationDetails1);

			///////////////////// Add Maturity //////////////////

			benef_DetailsList = quotationSaveUtilService.addMaturity("END1", benef_DetailsList, calResp,
					_invpSaveQuotation.get_personalInfo().get_plan().get_term(), quoDetails);

			///////////////////// Done Add Maturity //////////////////

			///////////////////// Medical Re1q //////////////////////

			for (MedicalDetails medicalDetails : medicalDetailList) {
//				System.out.println(quoDetails.getQdId() + " //////// quo detail id");
				medicalDetails.setQuotationDetails(quoDetails);
			}

			medicalDetailsDao.save(medicalDetailList);

			///////////////////// Done Save Medical req ////////////////

			if (quo != null && quoDetails != null) {
				ArrayList<Quo_Benef_Details> bnfdList = (ArrayList<Quo_Benef_Details>) quoBenifDetailDao
						.save(benef_DetailsList);
				if (bnfdList != null) {

					ArrayList<Quo_Benef_Child_Details> childBenifList = quotationSaveUtilService.getChildBenif(bnfdList,
							custChildDList, childList, _invpSaveQuotation.get_personalInfo().get_childrenList(),
							_invpSaveQuotation.get_personalInfo().get_plan().get_term(),
							calculation.get_personalInfo().getFrequance(),
							calculation.get_riderDetails().get_cRiders());

					if (quoBenifChildDetailsDao.save(childBenifList) == null) {
						responseMap.put("status", "Error at Child Benifict Updating");
						return responseMap;
					}

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

	@Override
	public BigDecimal pensionPremium() throws Exception {
		
		return new BigDecimal(250000.00);
	}
	
	
	
	/*@Override
	public QuotationQuickCalResponse getCalcutatedARTM(QuotationCalculation calculation) throws Exception {
		
		CalculationUtils calculationUtils = null;
		try {

			QuotationQuickCalResponse calResp = new QuotationQuickCalResponse();
			calculationUtils = new CalculationUtils();
			/// Calculate Rebate Premium ///
			//Double rebate = calculationUtils.getRebate(calculation.get_personalInfo().getFrequance());
			//System.out.println(rebate + " : rebate");
			/// Calculate BSA Premium ///
			//BigDecimal bsaYearly = calculateL2(quotationCalculation.get_personalInfo().getMocu(),
			//		quotationCalculation.get_personalInfo().getMage(),
			//		quotationCalculation.get_personalInfo().getTerm(), rebate, new Date(),
			//		quotationCalculation.get_personalInfo().getBsa(), 1, calResp, false);

			
			//BigDecimal bsaPremium = calculateL2(quotationCalculation.get_personalInfo().getMocu(),
			//		quotationCalculation.get_personalInfo().getMage(),
			//		quotationCalculation.get_personalInfo().getTerm(), rebate, new Date(),
			//		quotationCalculation.get_personalInfo().getBsa(),
			//		calculationUtils.getPayterm(quotationCalculation.get_personalInfo().getFrequance()), calResp, true);
			 
			
			BigDecimal bsaPremium = new BigDecimal(calculation.get_personalInfo().getBsa());
			
			calResp.setBasicSumAssured(bsaPremium.doubleValue());
			
			
			//calResp.setBsaYearlyPremium(bsaYearly.doubleValue());
			
			calResp = calculateriders.getRiders(calculation, calResp);

			calResp.setMainLifeHealthReq(healthRequirmentsService.getSumAtRiskDetailsMainLife(calculation));

			if(calculation.get_personalInfo().getSage()!=null &&
					calculation.get_personalInfo().getSgenger()!=null){
				calResp.setSpouseHealthReq(healthRequirmentsService.getSumAtRiskDetailsSpouse(calculation));
			}
			
			
			calResp.setAt6(calculateMaturity().doubleValue());
			calResp.setAt8(calculateMaturity().doubleValue());
			calResp.setAt10(calculateMaturity().doubleValue());

			calResp.setPensionPremium1(pensionPremium().doubleValue());
			calResp.setPensionPremium2(pensionPremium().doubleValue());
			calResp.setPensionPremium3(pensionPremium().doubleValue());
			
			
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
	}*/
	
	
	
	
/*
	
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
*/
}
