package org.arpicoinsurance.groupit.main.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.dao.BenefitsDao;
import org.arpicoinsurance.groupit.main.dao.ChildDao;
import org.arpicoinsurance.groupit.main.dao.CustChildDetailsDao;
import org.arpicoinsurance.groupit.main.dao.CustomerDao;
import org.arpicoinsurance.groupit.main.dao.CustomerDetailsDao;
import org.arpicoinsurance.groupit.main.dao.MedicalDetailsDao;
import org.arpicoinsurance.groupit.main.dao.MedicalReqDao;
import org.arpicoinsurance.groupit.main.dao.OccupationDao;
import org.arpicoinsurance.groupit.main.dao.OccupationLodingDao;
import org.arpicoinsurance.groupit.main.dao.ProductDao;
import org.arpicoinsurance.groupit.main.dao.Quo_Benef_Child_DetailsDao;
import org.arpicoinsurance.groupit.main.dao.Quo_Benef_DetailsDao;
import org.arpicoinsurance.groupit.main.dao.QuotationDao;
import org.arpicoinsurance.groupit.main.dao.QuotationDetailsDao;
import org.arpicoinsurance.groupit.main.dao.RateCardENDDao;
import org.arpicoinsurance.groupit.main.dao.UsersDao;
import org.arpicoinsurance.groupit.main.helper.InvpSavePersonalInfo;
import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.model.Benefits;
import org.arpicoinsurance.groupit.main.model.Child;
import org.arpicoinsurance.groupit.main.model.CustChildDetails;
import org.arpicoinsurance.groupit.main.model.Customer;
import org.arpicoinsurance.groupit.main.model.CustomerDetails;
import org.arpicoinsurance.groupit.main.model.MedicalDetails;
import org.arpicoinsurance.groupit.main.model.Occupation;
import org.arpicoinsurance.groupit.main.model.OcupationLoading;
import org.arpicoinsurance.groupit.main.model.Products;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Child_Details;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Details;
import org.arpicoinsurance.groupit.main.model.Quotation;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;
import org.arpicoinsurance.groupit.main.model.RateCardEND;
import org.arpicoinsurance.groupit.main.model.Users;
import org.arpicoinsurance.groupit.main.service.ENDService;
import org.arpicoinsurance.groupit.main.service.HealthRequirmentsService;
import org.arpicoinsurance.groupit.main.service.QuotationDetailsService;
import org.arpicoinsurance.groupit.main.service.custom.CalculateRiders;
import org.arpicoinsurance.groupit.main.service.custom.QuotationSaveUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
public class ENDServiceImpl implements ENDService {

	ArrayList<Quo_Benef_Child_Details> childBenifList = new ArrayList<>();

	@Autowired
	private QuotationSaveUtilService quotationSaveUtilService;

	@Autowired
	private RateCardENDDao rateCardENDDao;

	@Autowired
	private OccupationLodingDao occupationLodingDao;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private UsersDao userDao;

	@Autowired
	private OccupationDao occupationDao;

	@Autowired
	private BenefitsDao benefitsDao;

	@Autowired
	private CustomerDao customerDao;

	@Autowired
	private MedicalReqDao medicalReqDao;

	@Autowired
	private ChildDao childDao;

	@Autowired
	private CustomerDetailsDao customerDetailsDao;

	@Autowired
	private CustChildDetailsDao custChildDetailsDao;

	@Autowired
	private QuotationDao quotationDao;

	@Autowired
	private QuotationDetailsDao quotationDetailDao;

	@Autowired
	private Quo_Benef_DetailsDao quoBenifDetailDao;

	@Autowired
	private MedicalDetailsDao medicalDetailsDao;

	@Autowired
	private Quo_Benef_Child_DetailsDao quoBenifChildDetailsDao;

	@Autowired
	private CalculateRiders calculateriders;

	@Autowired
	private QuotationDetailsService quotationDetailsService;

	@Autowired
	private HealthRequirmentsService healthRequirmentsService;

	@Override
	public QuotationQuickCalResponse getCalcutatedEnd(QuotationCalculation quotationCalculation) throws Exception {

		System.out.println(quotationCalculation.get_personalInfo().getMgenger());

		CalculationUtils calculationUtils = null;
		try {

			QuotationQuickCalResponse calResp = new QuotationQuickCalResponse();
			calculationUtils = new CalculationUtils();
			/// Calculate Rebate Premium ///
			Double rebate = calculationUtils.getRebate(quotationCalculation.get_personalInfo().getFrequance());
			/// Calculate BSA Premium ///
			BigDecimal bsaPremium = calculateL2(quotationCalculation.get_personalInfo().getMocu(),
					quotationCalculation.get_personalInfo().getMage(),
					quotationCalculation.get_personalInfo().getTerm(), rebate, new Date(),
					quotationCalculation.get_personalInfo().getBsa(),
					calculationUtils.getPayterm(quotationCalculation.get_personalInfo().getFrequance()), calResp);

			calResp.setBasicSumAssured(bsaPremium.doubleValue());
			calResp = calculateriders.getRiders(quotationCalculation, calResp);

			calResp.setMainLifeHealthReq(healthRequirmentsService.getSumAtRiskDetailsMainLife(quotationCalculation));

			// if(quotationCalculation.get_personalInfo().getSage()!=null &&
			// quotationCalculation.get_personalInfo().getSgenger()!=null){
			calResp.setSpouseHealthReq(healthRequirmentsService.getSumAtRiskDetailsSpouse(quotationCalculation));

			System.out.println((calResp.getMainLifeHealthReq() != null) + " Cal Helth req");

			calResp.setAt6(calculateMaturity(quotationCalculation.get_personalInfo().getTerm(),
					quotationCalculation.get_personalInfo().getBsa()).doubleValue());
			calResp.setGuaranteed(calculateMaturity(quotationCalculation.get_personalInfo().getTerm(),
					quotationCalculation.get_personalInfo().getBsa()).doubleValue());

			Double tot = calResp.getBasicSumAssured() + calResp.getAddBenif();
			Double adminFee = calculationUtils.getAdminFee(quotationCalculation.get_personalInfo().getFrequance());
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
	public BigDecimal calculateL2(int ocu, int age, int term, double rebate, Date chedat, double bassum, int paytrm,
			QuotationQuickCalResponse calResp) throws Exception {

		Occupation occupation = occupationDao.findByOcupationid(ocu);
		Benefits benefits = benefitsDao.findByRiderCode("L2");
		OcupationLoading ocupationLoading = occupationLodingDao.findByOccupationAndBenefits(occupation, benefits);
		Double rate = 1.0;
		if (ocupationLoading != null) {
			rate = ocupationLoading.getValue();
			if (rate == null) {
				rate = 1.0;
			}
		}
		// TODO Auto-generated method stub
		System.out.println("END bassum : " + bassum + " age : " + age + " term : " + term + " paytrm : " + paytrm);
		BigDecimal premium = new BigDecimal(0);

		RateCardEND rateCardEND = rateCardENDDao.findByAgeAndTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(
				age, term, chedat, chedat, chedat, chedat);
		System.out.println("rateCardEND : " + rateCardEND.getRate());

		// (((@rate@-(@rate@*@rebate@/100))/1000)*@sum_assured@)/@payment_frequency@
		premium = ((((new BigDecimal(rateCardEND.getRate())
				.subtract(((new BigDecimal(rateCardEND.getRate()).multiply(new BigDecimal(rebate)))
						.divide(new BigDecimal(100), 6, RoundingMode.HALF_UP)))).divide(new BigDecimal(1000), 6,
								RoundingMode.HALF_UP)).multiply(new BigDecimal(bassum))).divide(new BigDecimal(paytrm),
										10, RoundingMode.HALF_UP)).setScale(0, RoundingMode.HALF_UP);

		System.out.println("premium : " + premium.toString());

		BigDecimal occuLodingPremium = premium.multiply(new BigDecimal(rate));
		calResp.setWithoutLoadingTot(calResp.getWithoutLoadingTot() + premium.doubleValue());
		calResp.setOccuLodingTot(calResp.getOccuLodingTot() + occuLodingPremium.subtract(premium).doubleValue());

		return occuLodingPremium;
	}

	@Override
	public BigDecimal calculateMaturity(int term, double bassum) throws Exception {
		// @sum_assured@ + ((@sum_assured@*0.025)*@term@)
		BigDecimal maturity = new BigDecimal(0);
		System.out.println("term : " + term + " bassum : " + bassum);
		maturity = (new BigDecimal(bassum)
				.add(((new BigDecimal(bassum).multiply(new BigDecimal(0.025))).multiply(new BigDecimal(term)))))
						.setScale(0, RoundingMode.HALF_UP);
		System.out.println("maturity : " + maturity.toString());
		return maturity;
	}

	// save quotation
	@Override
	public String saveQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation, Integer id)
			throws Exception {

		QuotationQuickCalResponse calResp = getCalcutatedEnd(calculation);
		Products products = productDao.findByProductCode("END1");
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
		String custCode = _invpSaveQuotation.get_personalInfo().get_mainlife().get_mCustCode();
		if (custCode == null) {

			

			try {
				final String uri = "http://10.10.10.12:8080/Infosys/testABC";
				RestTemplate restTemplate = new RestTemplate();
				String result = restTemplate.postForObject(uri, _invpSaveQuotation.get_personalInfo(), String.class);

				System.out.println(result);

				mainlife.setCustCode(result);

			}catch (Exception e) {
				final String uri = "http://localhost:8085/testABC";
				RestTemplate restTemplate = new RestTemplate();
				String result = restTemplate.postForObject(uri, _invpSaveQuotation.get_personalInfo(), String.class);

				System.out.println(result);

				mainlife.setCustCode(result);

			}
			
			
		}else {
			mainlife.setCustCode(custCode);
		}
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
				medicalDetail.setStatus("Active");
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
				medicalDetail.setStatus("Active");
				medicalDetailList.add(medicalDetail);
			}
		}

		ArrayList<Quo_Benef_Details> benef_DetailsList = quotationSaveUtilService.getBenifDetails(
				_invpSaveQuotation.get_riderDetails(), calResp, quotationDetails,
				_invpSaveQuotation.get_personalInfo().get_childrenList(),
				_invpSaveQuotation.get_personalInfo().get_plan().get_term());

		Quo_Benef_Details benef_Details = new Quo_Benef_Details();

		benef_Details.setBenefit(benefitsDao.findOne(21));
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

		for (Quo_Benef_Details quo_Benef_Details : benef_DetailsList) {
			System.out.println("");
			System.out.println(quo_Benef_Details.toString());
			System.out.println("");
		}

		//////////////////////////// save//////////////////////////////////
		Customer life = (Customer) customerDao.save(mainlife);
		System.out.println("custSave");
		CustomerDetails mainLifeDetails = customerDetailsDao.save(mainLifeDetail);
		System.out.println("custDetailSaveSave");
		ArrayList<CustChildDetails> custChildDList = null;
		if (life != null && mainLifeDetails != null) {

			if (spouse != null) {
				Customer sp = customerDao.save(spouse);
				System.out.println("custSSave");
				CustomerDetails spDetsils = customerDetailsDao.save(spouseDetail);
				System.out.println("custSDetailSave");
				if (sp == null && spDetsils != null) {
					return "Error at Spouse Saving";
				}
			}

			ArrayList<Child> cList = (ArrayList<Child>) childDao.save(childList);
			System.out.println("childSave");
			custChildDList = (ArrayList<CustChildDetails>) custChildDetailsDao.save(custChildDetailsList);
			System.out.println("childDetailSave");
			if (childList != null && childList.size() > 0) {
				if (cList == null && custChildDList == null) {
					return "Error at Child Saving";
				}
			}

			Quotation quo = quotationDao.save(quotation);
			System.out.println("quotationSave");
			QuotationDetails quoDetails = quotationDetailDao.save(quotationDetails);

			///////////////////// Add Maturity //////////////////

			benef_DetailsList = quotationSaveUtilService.addMaturity("END1", benef_DetailsList, calResp,
					_invpSaveQuotation.get_personalInfo().get_plan().get_term(), quoDetails);

			///////////////////// Done Add Maturity //////////////////
			///////////////////// Medical Re1q //////////////////////

			for (MedicalDetails medicalDetails : medicalDetailList) {
				System.out.println(quoDetails.getQdId() + " //////// quo detail id");
				medicalDetails.setQuotationDetails(quoDetails);
			}

			medicalDetailsDao.save(medicalDetailList);

			///////////////////// Done Save Medical req ////////////////

			if (quo != null && quoDetails != null) {
				for (Quo_Benef_Details benef_Details2 : benef_DetailsList) {
					System.out.println(benef_Details2.toString());
					System.out.println("");
				}
				ArrayList<Quo_Benef_Details> bnfdList = (ArrayList<Quo_Benef_Details>) quoBenifDetailDao
						.save(benef_DetailsList);
				System.out.println("benDetailsSave");
				if (bnfdList != null) {

					ArrayList<Quo_Benef_Child_Details> childBenifList = quotationSaveUtilService.getChildBenif(bnfdList,
							custChildDList, childList, _invpSaveQuotation.get_personalInfo().get_childrenList(),
							_invpSaveQuotation.get_personalInfo().get_plan().get_term(),
							calculation.get_personalInfo().getFrequance(),
							calculation.get_riderDetails().get_cRiders());

					if (quoBenifChildDetailsDao.save(childBenifList) == null) {
						return "Error at Child Benifict Saving";
					}

				} else {
					return "Error at Benifict Saving";
				}
			} else {
				return "Error at Quotation Saving";
			}

		} else {
			return "Error at MainLife Saving";
		}

		return "Success";

	}

	@Override
	public String editQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation, Integer userId,
			Integer qdId) throws Exception {
		CalculationUtils calculationUtils = new CalculationUtils();

		QuotationQuickCalResponse calResp = getCalcutatedEnd(calculation);

		Products products = productDao.findByProductCode("END1");
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
				medicalDetail.setStatus("Active");
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
				medicalDetail.setStatus("Active");
				medicalDetailList.add(medicalDetail);
			}
		}

		ArrayList<Quo_Benef_Details> benef_DetailsList = quotationSaveUtilService.getBenifDetails(
				_invpSaveQuotation.get_riderDetails(), calResp, quotationDetails1,
				_invpSaveQuotation.get_personalInfo().get_childrenList(),
				_invpSaveQuotation.get_personalInfo().get_plan().get_term());

		Quo_Benef_Details benef_Details = new Quo_Benef_Details();
		benef_Details.setBenefit(benefitsDao.findOne(21));
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
					return "Error at Spouse Saving";
				}
			}

			ArrayList<Child> cList = (ArrayList<Child>) childDao.save(childList);
			custChildDList = (ArrayList<CustChildDetails>) custChildDetailsDao.save(custChildDetailsList);
			if (childList != null && childList.size() > 0) {
				if (cList == null && custChildDList == null) {
					return "Error at Child Updating";
				}
			}

			Quotation quo = quotationDao.save(quotation);
			QuotationDetails quoDetails = quotationDetailDao.save(quotationDetails1);

			///////////////////// Add Maturity //////////////////

			benef_DetailsList = quotationSaveUtilService.addMaturity("END1", benef_DetailsList, calResp,
					_invpSaveQuotation.get_personalInfo().get_plan().get_term(), quoDetails);

			///////////////////// Done Add Maturity //////////////////

			///////////////////// Medical Re1q //////////////////////

			for (MedicalDetails medicalDetails : medicalDetailList) {
				System.out.println(quoDetails.getQdId() + " //////// quo detail id");
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
						return "Error at Child Benifict Updating";
					}

				} else {
					return "Error at Benifict Updating";
				}
			} else {
				return "Error at Quotation Updating";
			}

		} else {
			return "Error at MainLife Updating";
		}

		return "Success";
	}
}
