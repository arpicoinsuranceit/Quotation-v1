package org.arpicoinsurance.groupit.main.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.dao.BenefitsDao;
import org.arpicoinsurance.groupit.main.dao.CustomerDao;
import org.arpicoinsurance.groupit.main.dao.CustomerDetailsDao;
import org.arpicoinsurance.groupit.main.dao.OccupationDao;
import org.arpicoinsurance.groupit.main.dao.ProductDao;
import org.arpicoinsurance.groupit.main.dao.Quo_Benef_DetailsDao;
import org.arpicoinsurance.groupit.main.dao.QuotationDao;
import org.arpicoinsurance.groupit.main.dao.QuotationDetailsDao;
import org.arpicoinsurance.groupit.main.dao.RateCardDTADao;
import org.arpicoinsurance.groupit.main.dao.SheduleDao;
import org.arpicoinsurance.groupit.main.dao.UsersDao;
import org.arpicoinsurance.groupit.main.helper.DTAHelper;
import org.arpicoinsurance.groupit.main.helper.DTAShedule;
import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.model.Child;
import org.arpicoinsurance.groupit.main.model.CustChildDetails;
import org.arpicoinsurance.groupit.main.model.Customer;
import org.arpicoinsurance.groupit.main.model.CustomerDetails;
import org.arpicoinsurance.groupit.main.model.Occupation;
import org.arpicoinsurance.groupit.main.model.Products;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Child_Details;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Details;
import org.arpicoinsurance.groupit.main.model.Quotation;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;
import org.arpicoinsurance.groupit.main.model.RateCardDTA;
import org.arpicoinsurance.groupit.main.model.Shedule;
import org.arpicoinsurance.groupit.main.model.Users;
import org.arpicoinsurance.groupit.main.service.DTAService;
import org.arpicoinsurance.groupit.main.service.QuotationDetailsService;
import org.arpicoinsurance.groupit.main.service.custom.CalculateRiders;
import org.arpicoinsurance.groupit.main.service.custom.QuotationSaveUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DTAServiceImpl implements DTAService {

	@Autowired
	private RateCardDTADao rateCardDTADao;

	@Autowired
	private CalculateRiders calculateriders;

	@Autowired
	private QuotationSaveUtilService quotationSaveUtilService;

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
	private CustomerDetailsDao customerDetailsDao;

	@Autowired
	private QuotationDao quotationDao;

	@Autowired
	private QuotationDetailsDao quotationDetailDao;

	@Autowired
	private Quo_Benef_DetailsDao quoBenifDetailDao;


	@Autowired
	private SheduleDao sheduleDao;
	
	@Autowired
	private QuotationDetailsService quotationDetailsService;

	@Override
	public DTAHelper calculateL2(int age, int term, double intrat, String sex, Date chedat, double loanamt)
			throws Exception {
		DTAHelper dtaHelper = new DTAHelper();

		System.out.println(
				"age : " + age + " term : " + term + " intrat : " + intrat + " sex : " + sex + " loanamt : " + loanamt);
		
		BigDecimal amount = new BigDecimal(loanamt);
		BigDecimal total_premium = new BigDecimal(0);
		ArrayList<DTAShedule> dtaSheduleList = new ArrayList<>();
		for (int i = 1; i <= term; ++i) {

			RateCardDTA rateCardDTA = rateCardDTADao
					.findByAgeAndTermAndSexAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, i, sex, chedat,
							chedat, chedat, chedat);

			// annuity for term
			double annuity = 1 + (intrat / 100);
			annuity = Math.pow(annuity, ((term - (i - 1)) * -1));
			annuity = 1 - annuity;
			annuity /= (intrat / 100);

			// annuity for term -1
			double annuity2 = 1 + (intrat / 100);
			annuity2 = Math.pow(annuity2, ((term - i) * -1));
			annuity2 = 1 - annuity2;
			annuity2 /= (intrat / 100);

			BigDecimal outstanding = amount.multiply(new BigDecimal(annuity2 / annuity)).setScale(8,
					RoundingMode.HALF_UP);

			BigDecimal reduction = amount.subtract(outstanding).setScale(8, RoundingMode.HALF_UP);

			// @loan_reduction@*@rate@/1000
			BigDecimal premium = (reduction.multiply(new BigDecimal(rateCardDTA.getRate())))
					.divide(new BigDecimal(1000), 0, BigDecimal.ROUND_HALF_UP);

			DTAShedule shedule = new DTAShedule();

			shedule.setLonred(reduction.doubleValue());
			shedule.setOutsum(amount.doubleValue());
			shedule.setOutyer(term - (i - 1));
			shedule.setPolYear(i);
			shedule.setPremum(premium.doubleValue());
			shedule.setPrmrat(rateCardDTA.getRate());

			dtaSheduleList.add(shedule);

			total_premium = total_premium.add(premium);

			System.out.println("polyer : " + String.valueOf(i));
			System.out.println("outyer : " + String.valueOf(term - (i - 1)));
			System.out.println("outsum : " + amount.toPlainString());
			System.out.println("lonred : " + reduction.toPlainString());
			System.out.println("prmrat : " + rateCardDTA.getRate());
			System.out.println("premum : " + premium.toPlainString());

			amount = outstanding;

		}
		dtaHelper.setBsa(total_premium);
		dtaHelper.setDtaSheduleList(dtaSheduleList);
		System.out.println("total_premium : " + total_premium.toString());
		return dtaHelper;
	}

	@Override
	public QuotationQuickCalResponse getCalcutatedDta(QuotationCalculation quotationCalculation) throws Exception {
		CalculationUtils calculationUtils = null;
		try {

			QuotationQuickCalResponse calResp = new QuotationQuickCalResponse();
			calculationUtils = new CalculationUtils();

			Double rebate = calculationUtils.getRebate(quotationCalculation.get_personalInfo().getTerm(),
					quotationCalculation.get_personalInfo().getFrequance());

			DTAHelper dtaHelper = calculateL2(quotationCalculation.get_personalInfo().getMage(),
					quotationCalculation.get_personalInfo().getTerm(),
					quotationCalculation.get_personalInfo().getIntrate(),
					quotationCalculation.get_personalInfo().getMgenger(), new Date(),
					quotationCalculation.get_personalInfo().getBsa());

			BigDecimal bsaPremium = dtaHelper.getBsa();

			calResp.setDtaShedules(dtaHelper.getDtaSheduleList());
			calResp.setDtaShedules(dtaHelper.getDtaSheduleList());
			
			calResp.setBasicSumAssured(calculationUtils.addRebatetoBSAPremium(rebate, bsaPremium));
			
			System.out.println("Premium ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;" + calResp.getBasicSumAssured());
			
			calResp = calculateriders.getRiders(quotationCalculation, calResp);
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
	public String saveQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation, Integer id)
			throws Exception {


		QuotationQuickCalResponse calResp = getCalcutatedDta(calculation);

		Products products = productDao.findByProductCode("DTA");
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
		mainLifeDetail.setCustomer(mainlife);

		Customer spouse = null;
		if (_invpSaveQuotation.get_personalInfo().get_spouse() != null
				&& _invpSaveQuotation.get_personalInfo().get_spouse().is_sActive()) {
			spouse = new Customer();
			spouse.setCustName(spouseDetail.getCustName());
			spouse.setCustCreateDate(new Date());
			spouse.setCustCreateBy(user.getUser_Name());
			spouseDetail.setCustomer(spouse);
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
		quotationDetails.setInterestRate(calculation.get_personalInfo().getIntrate());

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

		//////////////////////////// save//////////////////////////////////
		Customer life = (Customer) customerDao.save(mainlife);
		CustomerDetails mainLifeDetails = customerDetailsDao.save(mainLifeDetail);
		if (life != null && mainLifeDetails != null) {

			if (spouse != null) {
				Customer sp = customerDao.save(spouse);
				CustomerDetails spDetsils = customerDetailsDao.save(spouseDetail);
				if (sp == null && spDetsils != null) {
					return "Error at Spouse Saving";
				}
			}

			Quotation quo = quotationDao.save(quotation);
			QuotationDetails quoDetails = quotationDetailDao.save(quotationDetails);

			ArrayList<Shedule> sheduleList = quotationSaveUtilService.getSheduleDtaDtapl(calResp, quoDetails);

			if (quo != null && quoDetails != null) {
				ArrayList<Quo_Benef_Details> bnfdList = (ArrayList<Quo_Benef_Details>) quoBenifDetailDao
						.save(benef_DetailsList);
				if (sheduleDao.save(sheduleList) != null) {
					if (bnfdList == null) {
						return "Error at Benifict Saving";
					}
				} else {
					return "Error at Shedule Saving";
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
		
		QuotationQuickCalResponse calResp = getCalcutatedDta(calculation);

		Products products = productDao.findByProductCode("DTA");
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
		quotationDetails1.setInterestRate(calculation.get_personalInfo().getIntrate());

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
		
		if (life != null && mainLifeDetails != null) {

			if (spouseDetail != null) {
				Customer sp = customerDao.save(spouse);
				CustomerDetails spDetsils = customerDetailsDao.save(spouseDetail);
				if (sp == null && spDetsils != null) {
					return "Error at Spouse Saving";
				}
			}

			Quotation quo = quotationDao.save(quotation);
			QuotationDetails quoDetails = quotationDetailDao.save(quotationDetails1);

			ArrayList<Shedule> sheduleList = quotationSaveUtilService.getSheduleDtaDtapl(calResp, quoDetails);

			if (quo != null && quoDetails != null) {
				ArrayList<Quo_Benef_Details> bnfdList = (ArrayList<Quo_Benef_Details>) quoBenifDetailDao
						.save(benef_DetailsList);
				if (sheduleDao.save(sheduleList) != null) {
					if (bnfdList == null) {
						return "Error at Benifict Saving";
					}
				} else {
					return "Error at Shedule Saving";
				}
			} else {
				return "Error at Quotation Saving";
			}

		} else {
			return "Error at MainLife Saving";
		}

		return "Success";
	}

}
