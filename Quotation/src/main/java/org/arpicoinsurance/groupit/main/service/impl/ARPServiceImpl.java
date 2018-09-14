package org.arpicoinsurance.groupit.main.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.common.WebClient;
import org.arpicoinsurance.groupit.main.dao.RateCardENDDao;
import org.arpicoinsurance.groupit.main.dao.RateCardSurenderDao;
import org.arpicoinsurance.groupit.main.dao.SurrendervalDao;
import org.arpicoinsurance.groupit.main.dao.UsersDao;
import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.helper.SurrenderValHelper;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
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
import org.arpicoinsurance.groupit.main.dao.RateCardARPDao;
import org.arpicoinsurance.groupit.main.model.RateCardEND;
import org.arpicoinsurance.groupit.main.model.RateCardSurender;
import org.arpicoinsurance.groupit.main.model.Surrendervals;
import org.arpicoinsurance.groupit.main.model.Users;
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
import org.arpicoinsurance.groupit.main.model.RateCardARP;
import org.arpicoinsurance.groupit.main.service.ARPService;
import org.arpicoinsurance.groupit.main.service.HealthRequirmentsService;
import org.arpicoinsurance.groupit.main.service.QuotationDetailsService;
import org.arpicoinsurance.groupit.main.service.custom.CalculateRiders;
import org.arpicoinsurance.groupit.main.service.custom.QuotationSaveUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ARPServiceImpl implements ARPService {

	ArrayList<Quo_Benef_Child_Details> childBenifList = new ArrayList<>();

	@Autowired
	private RateCardENDDao rateCardENDDao;

	@Autowired
	private OccupationLodingDao occupationLodingDao;

	@Autowired
	private RateCardARPDao rateCardARPDao;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private UsersDao userDao;

	@Autowired
	private OccupationDao occupationDao;

	@Autowired
	private CustomerDao customerDao;

	@Autowired
	private BenefitsDao benefitsDao;

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
	private MedicalReqDao medicalReqDao;

	@Autowired
	private Quo_Benef_Child_DetailsDao quoBenifChildDetailsDao;

	@Autowired
	private MedicalDetailsDao medicalDetailsDao;

	@Autowired
	private CalculateRiders calculateriders;

	@Autowired
	private QuotationSaveUtilService quotationSaveUtilService;

	@Autowired
	private QuotationDetailsService quotationDetailsService;

	@Autowired
	private HealthRequirmentsService healthRequirmentsService;

	@Autowired
	private RateCardSurenderDao rateCardSurenderDao;

	@Autowired
	private SurrendervalDao surrenderValDao;

	@Override
	public QuotationQuickCalResponse getCalcutatedArp(QuotationCalculation quotationCalculation) throws Exception {

		CalculationUtils calculationUtils = null;
		try {

			QuotationQuickCalResponse calResp = new QuotationQuickCalResponse();
			calculationUtils = new CalculationUtils();
			Double rebate = calculationUtils.getRebate(quotationCalculation.get_personalInfo().getFrequance());
			// System.out.println(rebate + " : rebate");
			BigDecimal bsaPremium = calculateL2(quotationCalculation.get_personalInfo().getMocu(),
					quotationCalculation.get_personalInfo().getMage(),
					quotationCalculation.get_personalInfo().getTerm(),
					quotationCalculation.get_personalInfo().getPayingterm(), rebate, new Date(),
					quotationCalculation.get_personalInfo().getBsa(),
					quotationCalculation.get_personalInfo().getFrequance(), calResp, true);

			BigDecimal bsaMonthly = calculateL2(quotationCalculation.get_personalInfo().getMocu(),
					quotationCalculation.get_personalInfo().getMage(),
					quotationCalculation.get_personalInfo().getTerm(),
					quotationCalculation.get_personalInfo().getPayingterm(), calculationUtils.getRebate("M"),
					new Date(), quotationCalculation.get_personalInfo().getBsa(), "M", calResp, false);

			BigDecimal bsaYearly = bsaMonthly.multiply(new BigDecimal(12)).setScale(2);
			// System.out.println(bsaYearly);

			// calResp.setBasicSumAssured(calculationUtils.addRebatetoBSAPremium(rebate,
			// bsaPremium));
			calResp.setBasicSumAssured(bsaPremium.doubleValue());

			calResp.setBsaYearlyPremium(bsaYearly.doubleValue());

			calResp = calculateriders.getRiders(quotationCalculation, calResp);

			calResp.setMainLifeHealthReq(healthRequirmentsService.getSumAtRiskDetailsMainLife(quotationCalculation));

			if (quotationCalculation.get_personalInfo().getSage() != null
					&& quotationCalculation.get_personalInfo().getSgenger() != null) {
				calResp.setSpouseHealthReq(healthRequirmentsService.getSumAtRiskDetailsSpouse(quotationCalculation));
			}

			calResp.setAt6(calculateMaturity(quotationCalculation.get_personalInfo().getTerm(),
					quotationCalculation.get_personalInfo().getBsa()).doubleValue());

			// System.out.println(calResp.getBasicSumAssured());
			Double tot = calResp.getBasicSumAssured() + calResp.getAddBenif();
			Double adminFee = calculationUtils.getAdminFee(quotationCalculation.get_personalInfo().getFrequance());
			Double tax = calculationUtils.getTaxAmount(tot + adminFee);
			Double extraOE = adminFee + tax;
			calResp.setExtraOE(extraOE);
			calResp.setTotPremium(tot + extraOE);

			calResp.setSurrenderValHelpers(calculateSurrendervals(quotationCalculation.get_personalInfo().getMage(),
					quotationCalculation.get_personalInfo().getTerm(),
					quotationCalculation.get_personalInfo().getPayingterm(),
					quotationCalculation.get_personalInfo().getBsa(),
					quotationCalculation.get_personalInfo().getFrequance(), (tot + extraOE)));

			return calResp;

		} finally {
			if (calculationUtils != null) {
				calculationUtils = null;
			}
		}
	}

	@Override
	public BigDecimal calculateL2(int ocu, int age, int term, String rlfterm, double rebate, Date chedat, double bassum,
			String payFrequency, QuotationQuickCalResponse calResp, boolean isAddOccuLoading) throws Exception {

		calResp.setArp(true);
		calResp.setPayTerm(rlfterm);

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

		// System.out.println("ARP bassum : " + bassum + " age : " + age + " term : " +
		// term + " rebate : " + rebate
		// + " payFrequency : " + payFrequency + " rlfterm : " + rlfterm);
		BigDecimal premium = new BigDecimal(0);

		RateCardEND rateCardEND = rateCardENDDao.findByAgeAndTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(
				age, term, chedat, chedat, chedat, chedat);
		// System.out.println("rateCardARP : " + rateCardEND.getRate());
		RateCardARP rateCardARP = rateCardARPDao
				.findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term, rlfterm,
						chedat, chedat, chedat, chedat);
		// System.out.println("rateCardARPRelief : " + rateCardARP.getRate());

		try {
			if (payFrequency.equalsIgnoreCase("S")) {
				// ((((@rate@-(@rate@*@rebate@/100))/1000)*@sum_assured@)) *@relief@
				premium = (((new BigDecimal(rateCardEND.getRate())
						.subtract(((new BigDecimal(rateCardEND.getRate()).multiply(new BigDecimal(rebate)))
								.divide(new BigDecimal(100), 6, RoundingMode.HALF_UP)))).divide(new BigDecimal(1000), 6,
										RoundingMode.HALF_UP)).multiply(new BigDecimal(bassum)))
												.multiply(new BigDecimal(rateCardARP.getRate()))
												.setScale(0, RoundingMode.HALF_UP);
			} else {
				// ((((@rate@-(@rate@*@rebate@/100))/1000)*@sum_assured@)/@payment_frequency@)
				// *@relief@
				premium = ((((new BigDecimal(rateCardEND.getRate())
						.subtract(((new BigDecimal(rateCardEND.getRate()).multiply(new BigDecimal(rebate)))
								.divide(new BigDecimal(100), 6, RoundingMode.HALF_UP)))).divide(new BigDecimal(1000), 6,
										RoundingMode.HALF_UP)).multiply(new BigDecimal(bassum))).divide(
												new BigDecimal(new CalculationUtils().getPayterm(payFrequency)), 10,
												RoundingMode.HALF_UP)).multiply(new BigDecimal(rateCardARP.getRate()))
														.setScale(0, RoundingMode.HALF_UP);
			}
		} catch (Exception e) {
			throw new NullPointerException("Rates not fount at ARP Product calculation");
		}
		// System.out.println("premium : " + premium.toString());

		BigDecimal occuLodingPremium = premium.multiply(new BigDecimal(rate)).setScale(0, RoundingMode.HALF_UP);
		if (isAddOccuLoading) {
			// System.out.println(calResp.getWithoutLoadingTot() +
			// "occunnnnnnnnnnnnnnnnnnnnnnnnnnnn");
			// System.out.println(calResp.getWithoutLoadingTot() + premium.doubleValue());
			calResp.setWithoutLoadingTot(calResp.getWithoutLoadingTot() + premium.doubleValue());
			// System.out.println(calResp.getWithoutLoadingTot() +
			// "occunnnnnnnnnnnnnnnnnnnnnnnnnnnn");
			calResp.setOccuLodingTot(calResp.getOccuLodingTot() + occuLodingPremium.subtract(premium).doubleValue());
		}
		return occuLodingPremium;
	}

	@Override
	public BigDecimal calculateMaturity(int term, double bassum) throws Exception {
		// @sum_assured@ + ((@sum_assured@*0.025)*@term@)
		BigDecimal maturity = new BigDecimal(0);
		// System.out.println("term : " + term + " bassum : " + bassum);
		maturity = (new BigDecimal(bassum)
				.add(((new BigDecimal(bassum).multiply(new BigDecimal(0.025))).multiply(new BigDecimal(term)))))
						.setScale(0, RoundingMode.HALF_UP);
		// System.out.println("maturity : " + maturity.toString());
		return maturity;
	}

	@Override
	public HashMap<String, Object> saveQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation,
			Integer id) throws Exception {

		Quotation quo = null;
		HashMap<String, Object> responseMap = new HashMap<>();

		if (productDao.findByProductCode("ARP").getActive() == 0) {
			responseMap.put("status", "This Function is Currently Unavailable Due to Maintenance");
			return responseMap;
		}

		QuotationQuickCalResponse calResp = getCalcutatedArp(calculation);
		if (calResp.isErrorExist()) {
			responseMap.put("status", "Error at calculation");
			return responseMap;
		}

		Products products = productDao.findByProductCode("ARP");
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

		QuotationDetails quotationDetails = quotationSaveUtilService.getQuotationDetail(calResp, calculation, 0.0);
		// quotationDetails.setTopTerm(Integer.parseInt(calculation.get_personalInfo().getPayingterm()));
		Quotation quotation = new Quotation();
		quotationDetails.setCustomerDetails(mainLifeDetail);
		if (spouseDetail != null) {
			quotationDetails.setSpouseDetails(spouseDetail);
		}
		quotation.setStatus("active");
		quotation.setUser(user);
		quotation.setProducts(products);

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
		benef_Details.setQuo_Benef_CreateBy(user.getUserCode());
		benef_Details.setQuo_Benef_CreateDate(new Date());
		benef_Details.setQuotationDetails(quotationDetails);
		benef_Details.setRierCode("L2");
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

		List<Surrendervals> surrendervalsList = new ArrayList<>();

		//////////////////////////// save//////////////////////////////////
		Customer life = (Customer) customerDao.save(mainlife);
		CustomerDetails mainLifeDetails = customerDetailsDao.save(mainLifeDetail);
		ArrayList<CustChildDetails> custChildDList = null;
		if (life != null && mainLifeDetails != null) {

			if (spouse != null) {
				Customer sp = customerDao.save(spouse);
				CustomerDetails spDetsils = customerDetailsDao.save(spouseDetail);
				if (sp == null && spDetsils != null) {
					responseMap.put("status", "Error at Spouse Saving");
					return responseMap;
				}
			}

			ArrayList<Child> cList = (ArrayList<Child>) childDao.save(childList);
			custChildDList = (ArrayList<CustChildDetails>) custChildDetailsDao.save(custChildDetailsList);
			if (childList != null && childList.size() > 0) {
				if (cList == null && custChildDList == null) {
					responseMap.put("status", "Error at Child Saving");
					return responseMap;
				}
			}

			quo = quotationDao.save(quotation);
			QuotationDetails quoDetails = quotationDetailDao.save(quotationDetails);

			for (SurrenderValHelper surrenderValHelper : calResp.getSurrenderValHelpers()) {
				Surrendervals surrendervals = new Surrendervals();
				surrendervals.setCreateBy(user.getUserCode());
				surrendervals.setCreatedate(new Date());
				surrendervals.setIsumas(surrenderValHelper.getIsumas());
				surrendervals.setMature(surrenderValHelper.getMature());
				surrendervals.setPadtrm(Integer.parseInt(surrenderValHelper.getPadtrm()));
				surrendervals.setPaidup(surrenderValHelper.getPaidup());
				surrendervals.setPolyer(Integer.parseInt(surrenderValHelper.getPolyer()));
				surrendervals.setPrmpad(surrenderValHelper.getPrmpad());
				surrendervals.setPrmpyr(surrenderValHelper.getPrmpyr());
				surrendervals.setQuotationDetails(quoDetails);
				surrendervals.setSurrnd(surrenderValHelper.getSurrnd());

				surrendervalsList.add(surrendervals);
			}

			surrenderValDao.save(surrendervalsList);

			/////////// Add Maturity///////////////////////

			benef_DetailsList = quotationSaveUtilService.addMaturity("ARP", benef_DetailsList, calResp,
					_invpSaveQuotation.get_personalInfo().get_plan().get_term(), quoDetails);

			///////////////////////////// END ADD MATURITY////////////////////////

			///////////////////// Medical Re1q //////////////////////

			for (MedicalDetails medicalDetails : medicalDetailList) {
				// System.out.println(quoDetails.getQdId() + " //////// quo detail id");
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
							calculation.get_personalInfo().getFrequance(), calculation.get_riderDetails().get_cRiders(),
							calResp);

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

		if (productDao.findByProductCode("ARP").getActive() == 0) {
			responseMap.put("status", "This Function is Currently Unavailable Due to Maintenance");
			return responseMap;
		}

		QuotationQuickCalResponse calResp = getCalcutatedArp(calculation);
		if (calResp.isErrorExist()) {
			responseMap.put("status", "Error at calculation");
			return responseMap;
		}

		Products products = productDao.findByProductCode("ARP");
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
		Integer count = quotationDetailDao.countByQuotation(quotation);
		quotation.setStatus("active");

		QuotationDetails quotationDetails1 = quotationSaveUtilService.getQuotationDetail(calResp, calculation, 0.0);
		quotationDetails1.setSeqnum(count + 1);
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

		List<Surrendervals> surrendervalsList = new ArrayList<>();

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

			for (SurrenderValHelper surrenderValHelper : calResp.getSurrenderValHelpers()) {
				Surrendervals surrendervals = new Surrendervals();
				surrendervals.setCreateBy(user.getUserCode());
				surrendervals.setCreatedate(new Date());
				surrendervals.setIsumas(surrenderValHelper.getIsumas());
				surrendervals.setMature(surrenderValHelper.getMature());
				surrendervals.setPadtrm(Integer.parseInt(surrenderValHelper.getPadtrm()));
				surrendervals.setPaidup(surrenderValHelper.getPaidup());
				surrendervals.setPolyer(Integer.parseInt(surrenderValHelper.getPolyer()));
				surrendervals.setPrmpad(surrenderValHelper.getPrmpad());
				surrendervals.setPrmpyr(surrenderValHelper.getPrmpyr());
				surrendervals.setQuotationDetails(quoDetails);
				surrendervals.setSurrnd(surrenderValHelper.getSurrnd());

				surrendervalsList.add(surrendervals);
			}

			surrenderValDao.save(surrendervalsList);

			/////////// Add Maturity///////////////////////

			benef_DetailsList = quotationSaveUtilService.addMaturity("ARP", benef_DetailsList, calResp,
					_invpSaveQuotation.get_personalInfo().get_plan().get_term(), quoDetails);

			///////////////////////////// END ADD MATURITY////////////////////////

			///////////////////// Medical Re1q //////////////////////

			for (MedicalDetails medicalDetails : medicalDetailList) {
				// System.out.println(quoDetails.getQdId() + " //////// quo detail id");
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
							calculation.get_personalInfo().getFrequance(), calculation.get_riderDetails().get_cRiders(),
							calResp);
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
	public List<SurrenderValHelper> calculateSurrendervals(int age, int term, String rlf_term, double bassum,
			String payFrequency, double total_premium) throws Exception {

		// (((@sum_assured@*0.025)*@term@)+@sum_assured@)
		int surrender_year = 3;
		BigDecimal mature_benifit = calculateMaturity(term, bassum);

		// int top_term = Integer.parseInt(productData.get("term").toString());
		// int current_age = Integer.parseInt(productData.get("age").toString());
		BigDecimal paidup_val = new BigDecimal(0);
		BigDecimal surrender_val = new BigDecimal(0);
		BigDecimal tot_prm_paid = new BigDecimal(0);
		int balance_term = 0;
		int paid_term = 0;
		int polyer = 0;
		int payment_frequency = new CalculationUtils().getPayterm(payFrequency);
		BigDecimal isum_assure = new BigDecimal(0);

		List<SurrenderValHelper> surrenderValHelpers = new ArrayList<>();

		for (int i = 1; i <= term; i++) {
			int ageIncrement = (age + i);
			BigDecimal prm_per_year = new BigDecimal(0);

			if (!(rlf_term.equalsIgnoreCase("S"))) {
				int rlfterm = Integer.parseInt(rlf_term);
				if (i <= rlfterm) {
					paid_term = i;
					polyer = i;
					balance_term = term - paid_term;

					/* Increase Sum Assured */
					// (((((@sum_assured@)*0.025)/@payment_frequency@)*(@paid_term@*@payment_frequency@))+@sum_assured@)
					isum_assure = (((new BigDecimal(bassum).multiply(new BigDecimal(0.025)))
							.divide(new BigDecimal(payment_frequency), 4, RoundingMode.HALF_UP)
							.multiply((new BigDecimal(paid_term).multiply(new BigDecimal(payment_frequency)))))
									.add(new BigDecimal(bassum)));

					/* Premium per year */
					// (@total_premium@*@payment_frequency@)
					prm_per_year = new BigDecimal(payment_frequency).multiply(new BigDecimal(total_premium));

					/* Total Premium paid */
					tot_prm_paid = tot_prm_paid.add(prm_per_year);

					if (surrender_year <= i) {
						/* Paid up Value */
						// (((@isum_assure@)*(@paid_term@))/(@rlf_term@))
						paidup_val = (isum_assure.multiply(new BigDecimal(paid_term))).divide(new BigDecimal(rlfterm),
								4, RoundingMode.HALF_UP);

						/* Surrender Value */
						// ((@paidup_val@*@surren@)/1000)
						RateCardSurender rateCardSurender = rateCardSurenderDao
								.findByAgeAndTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(ageIncrement,
										balance_term, new Date(), new Date(), new Date(), new Date());
						// System.out.println("Rate : "+rateCardSurender != null ?
						// rateCardSurender.getRate() : 0);
						try {
						surrender_val = (paidup_val
								.multiply(new BigDecimal((rateCardSurender == null ? 0 : rateCardSurender.getRate()))))
										.divide(new BigDecimal(1000), 0, RoundingMode.HALF_UP);
						} catch (Exception e) {
							throw new NullPointerException("Surrender Valuse calculation Error");
						}
					}
				} else {
					polyer = i;
					balance_term = term - i;
					tot_prm_paid = new BigDecimal(0);

					/* Increase Sum Assured */
					// (((((@sum_assured@)*0.025)))+@isum_assure@)
					isum_assure = (new BigDecimal(bassum).multiply(new BigDecimal(0.025))).add(isum_assure);

					/* Paid up Value */
					// ((@isum_assure@)*(@paid_term@))/(@paid_term@)
					paidup_val = isum_assure;

					/* Surrender Value */
					// ((@paidup_val@*@surren@)/1000)
					RateCardSurender rateCardSurender = rateCardSurenderDao
							.findByAgeAndTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(ageIncrement,
									balance_term, new Date(), new Date(), new Date(), new Date());
					// System.out.println(rateCardSurender);
					// System.out.println("ageIncrement : "+ageIncrement+" balance_term :
					// "+balance_term);
					// System.out.println("Rate : "+(rateCardSurender == null ? 0 :
					// rateCardSurender.getRate()));
					try {
					surrender_val = (paidup_val
							.multiply(new BigDecimal((rateCardSurender == null ? 0 : rateCardSurender.getRate()))))
									.divide(new BigDecimal(1000), 0, RoundingMode.HALF_UP);
					} catch (Exception e) {
						throw new NullPointerException("Surrender Value Calculation Error");
					}
				}
			} else {
				polyer = i;
				paid_term = i;
				balance_term = term - i;
				tot_prm_paid = new BigDecimal(0);
				if (i == 1) {
					isum_assure = new BigDecimal(bassum);
				}
				/* Increase Sum Assured */
				// (((((@sum_assured@)*0.025)))+@isum_assure@)
				isum_assure = ((new BigDecimal(bassum).multiply(new BigDecimal(0.025))).add(isum_assure));

				/* Paid up Value */
				// ((@isum_assure@)*(@paid_term@))/(@paid_term@)
				paidup_val = isum_assure;

				/* Surrender Value */
				// ((@paidup_val@*@surren@)/1000)
				RateCardSurender rateCardSurender = rateCardSurenderDao
						.findByAgeAndTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(ageIncrement,
								balance_term, new Date(), new Date(), new Date(), new Date());
				// System.out.println("Rate : "+rateCardSurender != null ?
				// rateCardSurender.getRate() : 0);
				try {
				surrender_val = (paidup_val
						.multiply(new BigDecimal((rateCardSurender == null ? 0 : rateCardSurender.getRate()))))
								.divide(new BigDecimal(1000), 0, RoundingMode.HALF_UP);
				} catch (Exception e) {
					throw new NullPointerException("Surrender Value Calculation Error");
				}
			}

			SurrenderValHelper helper = new SurrenderValHelper();
			helper.setPolyer(String.valueOf(polyer));
			helper.setPadtrm(String.valueOf(paid_term));
			helper.setToptrm(term);
			helper.setIsumas(isum_assure.doubleValue());
			helper.setPaidup(paidup_val.doubleValue());
			helper.setSurrnd(surrender_val.doubleValue());
			helper.setMature(mature_benifit.doubleValue());
			helper.setPrmpyr(prm_per_year.doubleValue());
			helper.setPrmpad(tot_prm_paid.doubleValue());

			surrenderValHelpers.add(helper);

			/*
			 * System.out.println("polyer : " + String.valueOf(polyer));
			 * System.out.println("padtrm : " + String.valueOf(paid_term));
			 * System.out.println("toptrm : " + term); System.out.println("isumas : " +
			 * isum_assure.doubleValue()); System.out.println("paidup : " +
			 * paidup_val.doubleValue()); System.out.println("surrnd : " +
			 * surrender_val.doubleValue()); System.out.println("mature : " +
			 * mature_benifit.doubleValue()); System.out.println("prmpyr : " +
			 * prm_per_year.doubleValue()); System.out.println("prmpad : " +
			 * tot_prm_paid.doubleValue());
			 */

		}

		return surrenderValHelpers;

	}

}
