package org.arpicoinsurance.groupit.main.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
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
import org.arpicoinsurance.groupit.main.dao.NomineeDao;
import org.arpicoinsurance.groupit.main.dao.OccupationDao;
import org.arpicoinsurance.groupit.main.dao.OccupationLodingDao;
import org.arpicoinsurance.groupit.main.dao.ProductDao;
import org.arpicoinsurance.groupit.main.dao.Quo_Benef_Child_DetailsDao;
import org.arpicoinsurance.groupit.main.dao.Quo_Benef_DetailsDao;
import org.arpicoinsurance.groupit.main.dao.QuotationDao;
import org.arpicoinsurance.groupit.main.dao.QuotationDetailsDao;
import org.arpicoinsurance.groupit.main.dao.RateCardASIPDao;
import org.arpicoinsurance.groupit.main.dao.RateCardASIPFundDao;
import org.arpicoinsurance.groupit.main.dao.UsersDao;
import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;

import org.arpicoinsurance.groupit.main.model.Benefits;
import org.arpicoinsurance.groupit.main.model.Child;
import org.arpicoinsurance.groupit.main.model.CustChildDetails;
import org.arpicoinsurance.groupit.main.model.Customer;
import org.arpicoinsurance.groupit.main.model.CustomerDetails;
import org.arpicoinsurance.groupit.main.model.MedicalDetails;
import org.arpicoinsurance.groupit.main.model.Nominee;
import org.arpicoinsurance.groupit.main.model.Occupation;
import org.arpicoinsurance.groupit.main.model.OcupationLoading;
import org.arpicoinsurance.groupit.main.model.Products;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Child_Details;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Details;
import org.arpicoinsurance.groupit.main.model.Quotation;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;

import org.arpicoinsurance.groupit.main.model.RateCardASIP;
import org.arpicoinsurance.groupit.main.model.RateCardASIPFund;
import org.arpicoinsurance.groupit.main.model.Users;
import org.arpicoinsurance.groupit.main.service.ASIPService;
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
public class ASIPServiceImpl implements ASIPService {

	ArrayList<Quo_Benef_Child_Details> childBenifList = new ArrayList<>();

	@Autowired
	private QuotationSaveUtilService quotationSaveUtilService;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private UsersDao userDao;

	@Autowired
	private RateCardASIPDao rateCardASIPDao;

	@Autowired
	private RateCardASIPFundDao rateCardASIPFundDao;

	@Autowired
	private OccupationDao occupationDao;

	@Autowired
	private BenefitsDao benefitsDao;

	@Autowired
	private OccupationLodingDao occupationLodingDao;

	@Autowired
	private CustomerDao customerDao;

	@Autowired
	private ChildDao childDao;

	@Autowired
	private CustomerDetailsDao customerDetailsDao;

	@Autowired
	private CustChildDetailsDao custChildDetailsDao;

	@Autowired
	private MedicalReqDao medicalReqDao;

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
	public QuotationQuickCalResponse getCalcutatedASIP(QuotationCalculation quotationCalculation) throws Exception {

		CalculationUtils calculationUtils = null;
		try {

			QuotationQuickCalResponse calResp = new QuotationQuickCalResponse();
			calculationUtils = new CalculationUtils();
			/// Calculate Rebate Premium ///
			Double rebate = calculationUtils.getRebate(quotationCalculation.get_personalInfo().getTerm(),
					quotationCalculation.get_personalInfo().getFrequance());
			/// Calculate BSA Premium ///
			BigDecimal bsaPremium = calculateL2(quotationCalculation.get_personalInfo().getMocu(),
					quotationCalculation.get_personalInfo().getTerm(), quotationCalculation.get_personalInfo().getBsa(),
					calculationUtils.getPayterm(quotationCalculation.get_personalInfo().getFrequance()), calResp, true);

			BigDecimal bsaYearly = calculateL2(quotationCalculation.get_personalInfo().getMocu(),
					quotationCalculation.get_personalInfo().getTerm(), quotationCalculation.get_personalInfo().getBsa(),
					1, calResp, false);

			calResp.setMainLifeHealthReq(healthRequirmentsService.getSumAtRiskDetailsMainLife(quotationCalculation));

			// if(quotationCalculation.get_personalInfo().getSage()!=null &&
			// quotationCalculation.get_personalInfo().getSgenger()!=null){
			calResp.setSpouseHealthReq(healthRequirmentsService.getSumAtRiskDetailsSpouse(quotationCalculation));

			calResp.setBasicSumAssured(calculationUtils.addRebatetoBSAPremium(rebate, bsaPremium));
			calResp.setBsaYearlyPremium(bsaYearly.doubleValue());
			calResp = calculateriders.getRiders(quotationCalculation, calResp);

			calResp.setAt6(calculateMaturity(quotationCalculation.get_personalInfo().getMage(),
					quotationCalculation.get_personalInfo().getTerm(), 0.01, 8.5, new Date(),
					quotationCalculation.get_personalInfo().getBsa(), bsaPremium.doubleValue(),
					calculationUtils.getPayterm(quotationCalculation.get_personalInfo().getFrequance())).doubleValue());

			calResp.setAt8(calculateMaturity(quotationCalculation.get_personalInfo().getMage(),
					quotationCalculation.get_personalInfo().getTerm(), 0.01, 9.5, new Date(),
					quotationCalculation.get_personalInfo().getBsa(), bsaPremium.doubleValue(),
					calculationUtils.getPayterm(quotationCalculation.get_personalInfo().getFrequance())).doubleValue());

			calResp.setAt10(calculateMaturity(quotationCalculation.get_personalInfo().getMage(),
					quotationCalculation.get_personalInfo().getTerm(), 0.01, 10.5, new Date(),
					quotationCalculation.get_personalInfo().getBsa(), bsaPremium.doubleValue(),
					calculationUtils.getPayterm(quotationCalculation.get_personalInfo().getFrequance())).doubleValue());

			System.out.println(calResp.getBasicSumAssured());
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
	public BigDecimal calculateL2(int ocu, int term, double bassum, int paytrm, QuotationQuickCalResponse calResp,
			boolean isAddOccuLoading) throws Exception {
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
		BigDecimal premium = new BigDecimal(0);
		System.out.println("term : " + term + " bassum : " + bassum + " paytrm : " + paytrm);
		// ((@sum_assured@/@term@)/@payment_frequency@)
		premium = (new BigDecimal(bassum).divide(new BigDecimal(term), 6, RoundingMode.HALF_UP))
				.divide(new BigDecimal(paytrm), 0, RoundingMode.HALF_UP);
		System.out.println("premium : " + premium.toString());

		BigDecimal occuLodingPremium = premium.multiply(new BigDecimal(rate));
		if (isAddOccuLoading) {
			calResp.setWithoutLoadingTot(calResp.getWithoutLoadingTot() + premium.doubleValue());
			calResp.setOccuLodingTot(calResp.getOccuLodingTot() + occuLodingPremium.subtract(premium).doubleValue());
		}
		return premium.multiply(new BigDecimal(rate));
	}

	@Override
	public BigDecimal calculateMaturity(int age, int term, double fundcharat, double intrat, Date chedat, double bassum,
			double bsapremium, int paytrm) throws Exception {
		BigDecimal maturity = new BigDecimal(0);
		BigDecimal open_fund = new BigDecimal("0");
		BigDecimal fund_amount = new BigDecimal("0");
		BigDecimal dividend_income = new BigDecimal("0");
		BigDecimal mortality_charges = new BigDecimal("0");
		BigDecimal fund_managment_charge = new BigDecimal("0");
		BigDecimal close_bal = new BigDecimal("0");

		BigDecimal premium = new BigDecimal(bsapremium);
		BigDecimal basicsumasu = new BigDecimal(bassum);
		BigDecimal total_amount = new BigDecimal("0");

		BigDecimal fund_charge = new BigDecimal(fundcharat).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal interest_rate = new BigDecimal(intrat).setScale(2, BigDecimal.ROUND_HALF_UP);

		System.out.println("term : " + term + " fundcharat : " + fundcharat + " intrat : " + intrat + " paytrm : "
				+ paytrm + " bassum : " + bassum + " bsapremium : " + bsapremium);
		for (int i = 1; i <= term; ++i) {

			// overidepara.put("current_year", String.valueOf(i));
			int polyear;
			if (i > 3) {
				polyear = 3;
			} else {
				polyear = i;
			}

			RateCardASIPFund rateCardASIPFund = rateCardASIPFundDao
					.findByTermAndPolyearAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(term, polyear, chedat,
							chedat, chedat, chedat);
			BigDecimal fund_allo_rate = new BigDecimal(rateCardASIPFund.getRate()).setScale(2,
					BigDecimal.ROUND_HALF_UP);

			RateCardASIP rateCardASIP = rateCardASIPDao.findByAgeAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(
					age, chedat, chedat, chedat, chedat);
			BigDecimal rate = new BigDecimal(rateCardASIP.getRate()).divide(new BigDecimal(10000), 8,
					BigDecimal.ROUND_HALF_UP);
			// System.out.println("age : "+age+" polyear : "+polyear+" fund_allo_rate :
			// "+fund_allo_rate+ " rate : "+rate);

			fund_amount = premium.multiply(fund_allo_rate).setScale(6, BigDecimal.ROUND_HALF_UP);
			// System.out.println("fund_allo_rate : " + fund_allo_rate + " fund_charge : " +
			// fund_charge + " rate : " + rate + " interest_rate : " + interest_rate + "
			// fund_amount : " + fund_amount);

			for (int j = 1; j <= paytrm; ++j) {

				open_fund = open_fund.add(fund_amount).setScale(8, BigDecimal.ROUND_HALF_UP);
				// System.out.println("open_fund : " + open_fund);

				dividend_income = open_fund.multiply(interest_rate).divide(new BigDecimal(100))
						.divide(new BigDecimal(paytrm), 4, BigDecimal.ROUND_HALF_UP);
				mortality_charges = basicsumasu.add(open_fund).multiply(rate).divide(new BigDecimal(paytrm), 4,
						BigDecimal.ROUND_HALF_UP);
				fund_managment_charge = open_fund.add(dividend_income).add(mortality_charges.negate())
						.multiply(fund_charge).divide(new BigDecimal(paytrm), 4, BigDecimal.ROUND_HALF_UP);
				close_bal = open_fund.add(dividend_income).add(mortality_charges.negate())
						.add(fund_managment_charge.negate()).setScale(4, BigDecimal.ROUND_HALF_UP);
				open_fund = close_bal;
				// System.out.println("dividend_income : " + dividend_income + "
				// mortality_charges : " + mortality_charges + " fund_managment_charge : " +
				// fund_managment_charge + " close_bal: " + close_bal);
				fund_amount = new BigDecimal(0);

				fund_amount = premium.multiply(fund_allo_rate);

				// System.out.println("fund_amount : " + fund_amount);
				total_amount = close_bal;

				/*
				 * System.out.println(" dividend_income : +" + dividend_income +
				 * " mortality_charges : " + mortality_charges + " fund_managment_charge : " +
				 * fund_managment_charge + " close_bal : " + close_bal + " fund_allo_rate : " +
				 * fund_allo_rate + " fund_charge : " + fund_charge + " rate : " + rate +
				 * " interest_rate : " + interest_rate);
				 */

			}

			age++;

		}

		System.out.println("maturity " + intrat + " : " + total_amount.setScale(0, BigDecimal.ROUND_HALF_UP) + " ---- "
				+ total_amount.toString());
		maturity = total_amount;
		return maturity.setScale(0, BigDecimal.ROUND_UP);
	}

	@Override
	public HashMap<String, Object> saveQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation,
			Integer id) throws Exception {

		Quotation quo = null;
		HashMap<String, Object> responseMap = new HashMap<>();

		QuotationQuickCalResponse calResp = getCalcutatedASIP(calculation);
		if (calResp.isErrorExist()) {
			responseMap.put("status", "Error at calculation");
			return responseMap;
		}

		Products products = productDao.findByProductCode("ASIP");
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
		benef_Details.setRierCode("L2");
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

			/////////// Add Maturity///////////////////////

			benef_DetailsList = quotationSaveUtilService.addMaturity("ASIP", benef_DetailsList, calResp,
					_invpSaveQuotation.get_personalInfo().get_plan().get_term(), quoDetails);

			///////////////////////////// END ADD MATURITY////////////////////////
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

		QuotationQuickCalResponse calResp = getCalcutatedASIP(calculation);
		if (calResp.isErrorExist()) {
			responseMap.put("status", "Error at calculation");
			return responseMap;
		}

		Products products = productDao.findByProductCode("INVP");
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
		benef_Details.setRierCode("L2");
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

			/////////// Add Maturity///////////////////////

			benef_DetailsList = quotationSaveUtilService.addMaturity("ASIP", benef_DetailsList, calResp,
					_invpSaveQuotation.get_personalInfo().get_plan().get_term(), quoDetails);

			///////////////////////////// END ADD MATURITY////////////////////////
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

}
