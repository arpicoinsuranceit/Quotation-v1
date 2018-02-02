package org.arpicoinsurance.groupit.main.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.common.DateConverter;
import org.arpicoinsurance.groupit.main.dao.BenefitsDao;
import org.arpicoinsurance.groupit.main.dao.ChildDao;
import org.arpicoinsurance.groupit.main.dao.CustChildDetailsDao;
import org.arpicoinsurance.groupit.main.dao.CustomerDao;
import org.arpicoinsurance.groupit.main.dao.CustomerDetailsDao;
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
import org.arpicoinsurance.groupit.main.helper.Benifict;
import org.arpicoinsurance.groupit.main.helper.Children;
import org.arpicoinsurance.groupit.main.helper.InvpSavePersonalInfo;
import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;

import org.arpicoinsurance.groupit.main.helper.RiderDetails;
import org.arpicoinsurance.groupit.main.model.Benefits;
import org.arpicoinsurance.groupit.main.model.Child;
import org.arpicoinsurance.groupit.main.model.CustChildDetails;
import org.arpicoinsurance.groupit.main.model.Customer;
import org.arpicoinsurance.groupit.main.model.CustomerDetails;
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
import org.arpicoinsurance.groupit.main.service.CalculateBenifictTermService;
import org.arpicoinsurance.groupit.main.service.custom.CalculateRiders;
import org.arpicoinsurance.groupit.main.service.rider.CIBCService;
import org.arpicoinsurance.groupit.main.service.rider.HBCService;
import org.arpicoinsurance.groupit.main.service.rider.SUHRBCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ASIPServiceImpl implements ASIPService {

	ArrayList<Quo_Benef_Child_Details> childBenifList = new ArrayList<>();
	
	@Autowired
	private ProductDao productDao;

	@Autowired
	private UsersDao userDao;
	
	@Autowired
	private CIBCService cibcService;

	@Autowired
	private SUHRBCService suhrbcService;

	@Autowired
	private HBCService hbcService;
	@Autowired
	private RateCardASIPDao rateCardASIPDao;
	
	@Autowired
	private RateCardASIPFundDao rateCardASIPFundDao;

	@Autowired
	private CalculateBenifictTermService calculateBenefictTerm;
	
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
	private QuotationDao quotationDao;

	@Autowired
	private QuotationDetailsDao quotationDetailDao;

	@Autowired
	private Quo_Benef_DetailsDao quoBenifDetailDao;

	@Autowired
	private Quo_Benef_Child_DetailsDao quoBenifChildDetailsDao;
	
	@Autowired
	private CalculateRiders calculateriders;
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
					quotationCalculation.get_personalInfo().getTerm(),
					quotationCalculation.get_personalInfo().getBsa(),
					calculationUtils.getPayterm(quotationCalculation.get_personalInfo().getFrequance()));
			calResp = calculateriders.getRiders(quotationCalculation, calResp);
			
			
			
			calResp.setBasicSumAssured(calculationUtils.addRebatetoBSAPremium(rebate, bsaPremium));
			calResp.setAt6(calculateMaturity(quotationCalculation.get_personalInfo().getMage(),
					quotationCalculation.get_personalInfo().getTerm(),
					0.01, 
					8.0, 
					new Date(), 
					quotationCalculation.get_personalInfo().getBsa(),
					bsaPremium.doubleValue(), 
					calculationUtils.getPayterm(quotationCalculation.get_personalInfo().getFrequance())).doubleValue());
					
			calResp.setAt8(calculateMaturity(quotationCalculation.get_personalInfo().getMage(),
					quotationCalculation.get_personalInfo().getTerm(),
					0.01,
					9.5, 
					new Date(), 
					quotationCalculation.get_personalInfo().getBsa(),
					bsaPremium.doubleValue(), 
					calculationUtils.getPayterm(quotationCalculation.get_personalInfo().getFrequance())).doubleValue());
					
			calResp.setAt10(calculateMaturity(quotationCalculation.get_personalInfo().getMage(),
					quotationCalculation.get_personalInfo().getTerm(),
					0.01, 
					12.0, 
					new Date(), 
					quotationCalculation.get_personalInfo().getBsa(),
					bsaPremium.doubleValue(), 
					calculationUtils.getPayterm(quotationCalculation.get_personalInfo().getFrequance())).doubleValue());
					
			System.out.println( calResp.getBasicSumAssured());
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
	public BigDecimal calculateL2(int ocu, int term, double bassum, int paytrm)
			throws Exception {
		Occupation occupation = occupationDao.findByOcupationid(ocu);
		Benefits benefits= benefitsDao.findByRiderCode("L2");
		OcupationLoading ocupationLoading = occupationLodingDao.findByOccupationAndBenefits(occupation, benefits);
		Double rate=ocupationLoading.getValue();
		
		BigDecimal premium = new BigDecimal(0);
		System.out.println("term : "+term+" bassum : "+bassum+" paytrm : "+paytrm);
		// ((@sum_assured@/@term@)/@payment_frequency@)
		premium = (new BigDecimal(bassum).divide(new BigDecimal(term), 6, RoundingMode.HALF_UP)).divide(new BigDecimal(paytrm), 4, RoundingMode.HALF_UP);
		System.out.println("premium : "+premium.toString());
		
		return premium.multiply(new BigDecimal(rate));
	}

	

	@Override
	public BigDecimal calculateMaturity(int age, int term, double fundcharat, double intrat, Date chedat, double bassum, double bsapremium, int paytrm)
			throws Exception {
		BigDecimal maturity  = new BigDecimal(0);
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
        
		System.out.println("term : "+term+" fundcharat : "+fundcharat+" intrat : "+intrat+" paytrm : "+paytrm+" bassum : "+bassum+" bsapremium : "+bsapremium);
		for (int i = 1; i <= term; ++i) {

            //overidepara.put("current_year", String.valueOf(i));
			int polyear;
			if(i > 3){
				polyear = 3;
			} else {
				polyear = i;
			}
			
			
			RateCardASIPFund rateCardASIPFund = rateCardASIPFundDao.findByTermAndPolyearAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(term, polyear, chedat, chedat, chedat, chedat);
            BigDecimal fund_allo_rate = new BigDecimal(rateCardASIPFund.getRate()).setScale(2, BigDecimal.ROUND_HALF_UP);
            
            RateCardASIP rateCardASIP = rateCardASIPDao.findByAgeAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, chedat, chedat, chedat, chedat);
            BigDecimal rate = new BigDecimal(rateCardASIP.getRate()).divide(new BigDecimal(10000), 8, BigDecimal.ROUND_HALF_UP);
            //System.out.println("age : "+age+" polyear : "+polyear+" fund_allo_rate : "+fund_allo_rate+ " rate : "+rate);

            fund_amount = premium.multiply(fund_allo_rate).setScale(6, BigDecimal.ROUND_HALF_UP);
            //System.out.println("fund_allo_rate : " + fund_allo_rate + " fund_charge : " + fund_charge + " rate : " + rate + " interest_rate : " + interest_rate + " fund_amount : " + fund_amount);

            for (int j = 1; j <= paytrm; ++j) {

                open_fund = open_fund.add(fund_amount).setScale(8, BigDecimal.ROUND_HALF_UP);
                //System.out.println("open_fund : " + open_fund);

                dividend_income = open_fund.multiply(interest_rate).divide(new BigDecimal(100)).divide(new BigDecimal(paytrm), 4, BigDecimal.ROUND_HALF_UP);
                mortality_charges = basicsumasu.add(open_fund).multiply(rate).divide(new BigDecimal(paytrm), 4, BigDecimal.ROUND_HALF_UP);
                fund_managment_charge = open_fund.add(dividend_income).add(mortality_charges.negate()).multiply(fund_charge).divide(new BigDecimal(paytrm), 4, BigDecimal.ROUND_HALF_UP);
                close_bal = open_fund.add(dividend_income).add(mortality_charges.negate()).add(fund_managment_charge.negate()).setScale(4, BigDecimal.ROUND_HALF_UP);
                open_fund = close_bal;
                //System.out.println("dividend_income : " + dividend_income + " mortality_charges : " + mortality_charges + " fund_managment_charge : " + fund_managment_charge + " close_bal: " + close_bal);
                fund_amount = new BigDecimal(0);

                fund_amount = premium.multiply(fund_allo_rate);
                

                //System.out.println("fund_amount : " + fund_amount);
                total_amount = close_bal;

                /*
                System.out.println(" dividend_income : +" + dividend_income
                        + " mortality_charges : " + mortality_charges + " fund_managment_charge : " + fund_managment_charge
                        + " close_bal : " + close_bal + " fund_allo_rate : " + fund_allo_rate + " fund_charge : " + fund_charge
                        + " rate : " + rate + " interest_rate : " + interest_rate);
                        */
                
                 
            }
            
            age++;

        }
		
		System.out.println("maturity "+intrat+" : "+total_amount.setScale(0, BigDecimal.ROUND_HALF_UP)+" ---- "+total_amount.toString());
		maturity = total_amount;
		return maturity.setScale(2, RoundingMode.HALF_UP);
	}

	

	@Override
	public String saveQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation, Integer id)
			throws Exception {
		QuotationQuickCalResponse calResp = getCalcutatedASIP(calculation);
		Products products = productDao.findByProductCode("INVP");
		Users user = userDao.findOne(id);
		Occupation occupationMainlife = occupationDao.findByOcupationid(calculation.get_personalInfo().getMocu());
		Occupation occupationSpouse = occupationDao.findByOcupationid(calculation.get_personalInfo().getSocu());

		CustomerDetails mainLifeDetail = getCustomerDetail(occupationMainlife, _invpSaveQuotation.get_personalInfo(),
				user);

		CustomerDetails spouseDetail = getSpouseDetail(occupationSpouse, _invpSaveQuotation.get_personalInfo(), user);

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
			spouseDetail.setCustomer(spouse);
		}

		ArrayList<Child> childList = getChilds(_invpSaveQuotation.get_personalInfo().get_childrenList());

		ArrayList<CustChildDetails> custChildDetailsList = new ArrayList<>();
		if (childList != null && !childList.isEmpty())
			for (Child child : childList) {
				CustChildDetails custChildDetails = new CustChildDetails();
				custChildDetails.setChild(child);
				custChildDetails.setCustomer(mainLifeDetail);
				custChildDetailsList.add(custChildDetails);
			}

		QuotationDetails quotationDetails = getQuotationDetail(calResp, calculation);

		Quotation quotation = new Quotation();
		quotation.setCustomerDetails(mainLifeDetail);
		if (spouseDetail != null)
			quotation.setSpouseDetails(spouseDetail);
		quotation.setStatus("active");
		quotation.setUser(user);
		quotation.setProducts(products);

		quotationDetails.setQuotation(quotation);

		ArrayList<Quo_Benef_Details> benef_DetailsList = getBenifDetails(_invpSaveQuotation.get_riderDetails(), calResp,
				quotationDetails, _invpSaveQuotation.get_personalInfo().get_childrenList(),
				_invpSaveQuotation.get_personalInfo().get_plan().get_term());

		//////////////////////////// save//////////////////////////////////
		Customer life = (Customer) customerDao.save(mainlife);
		CustomerDetails mainLifeDetails = customerDetailsDao.save(mainLifeDetail);
		ArrayList<CustChildDetails> custChildDList = null;
		if (life != null && mainLifeDetails != null) {

			if (spouse != null) {
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
					return "Error at Child Saving";
				}
			}

			Quotation quo = quotationDao.save(quotation);
			QuotationDetails quoDetails = quotationDetailDao.save(quotationDetails);

			if (quo != null && quoDetails != null) {
				ArrayList<Quo_Benef_Details> bnfdList = (ArrayList<Quo_Benef_Details>) quoBenifDetailDao
						.save(benef_DetailsList);
				if (bnfdList != null) {

					ArrayList<Quo_Benef_Child_Details> childBenifList = getChildBenif(bnfdList, custChildDList,
							childList, _invpSaveQuotation.get_personalInfo().get_childrenList(),
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

	
	//Additional
	
	private CustomerDetails getSpouseDetail(Occupation occupationSpouse, InvpSavePersonalInfo get_personalInfo,
			Users user) {
		CustomerDetails spouseDetail = null;
		if (get_personalInfo.get_spouse() != null && get_personalInfo.get_spouse().is_sActive() == true) {
			try {

				spouseDetail = new CustomerDetails();
				spouseDetail.setCustName(get_personalInfo.get_spouse().get_sName());
				spouseDetail.setCustCivilStatus("Married");
				spouseDetail.setCustCreateBy(user.getUser_Name());
				spouseDetail.setCustCreateDate(new Date());
				spouseDetail.setCustDob(new DateConverter().stringToDate(get_personalInfo.get_spouse().get_sDob()));
				spouseDetail.setCustGender(get_personalInfo.get_spouse().get_sGender());
				spouseDetail.setCustNic(get_personalInfo.get_spouse().get_sNic());
				spouseDetail.setCustTitle(get_personalInfo.get_spouse().get_sTitle());
				spouseDetail.setOccupation(occupationSpouse);

				return spouseDetail;
			} finally {
				if (spouseDetail != null) {
					spouseDetail = null;
				}
			}
		}
		return null;
	}

	private CustomerDetails getCustomerDetail(Occupation occupationMainlife, InvpSavePersonalInfo get_personalInfo,
			Users user) {
		CustomerDetails mainLifeDetail = null;
		try {
			mainLifeDetail = new CustomerDetails();
			mainLifeDetail.setCustName(get_personalInfo.get_mainlife().get_mName());
			mainLifeDetail.setCustCivilStatus(get_personalInfo.get_spouse().is_sActive() ? "Married" : "Single");
			mainLifeDetail.setCustCreateBy(user.getUser_Name());
			mainLifeDetail.setCustCreateDate(new Date());
			mainLifeDetail.setCustDob(new DateConverter().stringToDate(get_personalInfo.get_mainlife().get_mDob()));
			mainLifeDetail.setCustEmail(get_personalInfo.get_mainlife().get_mEmail());
			mainLifeDetail.setCustGender(get_personalInfo.get_mainlife().get_mGender());
			mainLifeDetail.setCustNic(get_personalInfo.get_mainlife().get_mNic());
			mainLifeDetail.setCustTel(get_personalInfo.get_mainlife().get_mMobile());
			mainLifeDetail.setCustTitle(get_personalInfo.get_mainlife().get_mTitle());
			mainLifeDetail.setOccupation(occupationMainlife);

			return mainLifeDetail;
		} finally {
			if (mainLifeDetail != null) {
				mainLifeDetail = null;
			}
		}
	}

	private ArrayList<Child> getChilds(ArrayList<Children> get_childrenList) {
		ArrayList<Child> childList = null;
		if (get_childrenList != null && get_childrenList.size() > 0) {
			try {
				childList = new ArrayList<Child>();
				for (Children children : get_childrenList) {
					Child child = new Child();
					child.setChildName(children.get_cName());
					child.setChildGender(children.get_cTitle());
					child.setChildDob(new DateConverter().stringToDate(children.get_cDob()));
					child.setChildNic(children.get_cNic());
					child.setChildRelation(children.get_cTitle() == "G" ? "Daughter" : "Son");

					childList.add(child);
				}

				return childList;
			} finally {
				if (childList != null) {
					childList = null;
				}
			}
		}
		return null;

	}

	private QuotationDetails getQuotationDetail(QuotationQuickCalResponse calResp, QuotationCalculation calculation)
			throws Exception {
		QuotationDetails quotationDetails = null;
		CalculationUtils calculationUtils = null;
		try {
			calculationUtils = new CalculationUtils();
			Double adminFee = calculationUtils.getAdminFee(calculation.get_personalInfo().getFrequance());
			Double taxAmount = calculationUtils.getTaxAmount(adminFee + calResp.getTotPremium() - calResp.getExtraOE());

			quotationDetails = new QuotationDetails();
			quotationDetails.setBaseSum(calculation.get_personalInfo().getBsa());
			quotationDetails.setInterestRate(8.5);
			quotationDetails.setAdminFee(adminFee);
			quotationDetails.setLifePos(0.0);
			quotationDetails.setInvestmentPos(calResp.getBasicSumAssured() - quotationDetails.getLifePos());

			quotationDetails.setPayMode(calculation.get_personalInfo().getFrequance());
			quotationDetails.setPayTerm(calculation.get_personalInfo().getTerm());
			quotationDetails.setPolicyFee(calculationUtils.getPolicyFee());
			quotationDetails.setTaxAmount(taxAmount);
			switch (calculation.get_personalInfo().getFrequance()) {
			case "M":
				quotationDetails.setPremiumMonth(calResp.getBasicSumAssured());
				quotationDetails.setPremiumMonthT(calResp.getTotPremium() - calResp.getExtraOE());
				break;
			case "Q":
				quotationDetails.setPremiumQuater(calResp.getBasicSumAssured());
				quotationDetails.setPremiumQuaterT(calResp.getTotPremium() - calResp.getExtraOE());
				break;
			case "H":
				quotationDetails.setPremiumHalf(calResp.getBasicSumAssured());
				quotationDetails.setPremiumHalfT(calResp.getTotPremium() - calResp.getExtraOE());
				break;
			case "Y":
				quotationDetails.setPremiumYear(calResp.getBasicSumAssured());
				quotationDetails.setPremiumYearT(calResp.getTotPremium() - calResp.getExtraOE());
				break;
			default:
				break;
			}
			return quotationDetails;
		} finally {
			if (quotationDetails != null) {
				quotationDetails = null;
			}
		}

	}
	
	private ArrayList<Quo_Benef_Details> getBenifDetails(RiderDetails get_riderDetails, QuotationQuickCalResponse calResp,
			QuotationDetails quotationDetails, List<Children> childrenList, Integer integer) throws Exception {
		ArrayList<Quo_Benef_Details> benef_DetailList = null;
		try {
			benef_DetailList = new ArrayList<>();
			ArrayList<Benifict> benifictListM = get_riderDetails.get_mRiders();
			if (benifictListM != null && benifictListM.size() > 0) {
				for (Benifict benifict : benifictListM) {
					Quo_Benef_Details benef_Details = new Quo_Benef_Details();
					Benefits benifict2 = benefitsDao.findByRiderCode(benifict.getType());
					if (benifict2 != null) {
						benef_Details.setBenefit(benifict2);
					} else {
						System.out.println("******************" + benifict.getType() + "******Error");
					}

					benef_Details.setQuotationDetails(quotationDetails);
					benef_Details.setRiderSum(benifict.getSumAssured());

					String type = benifict.getType();

					switch (type) {

					case "ADB":
						benef_Details.setRiderPremium(calResp.getAdb());
						benef_Details.setRiderTerm(calResp.getAdbTerm());
						benef_Details.setRierCode(type);
						break;
					case "ATPB":
						benef_Details.setRiderPremium(calResp.getAtpb());
						benef_Details.setRiderTerm(calResp.getAtpbTerm());
						benef_Details.setRierCode(type);
						break;
					case "TPDASB":
						benef_Details.setRiderPremium(calResp.getTpdasb());
						benef_Details.setRiderTerm(calResp.getTpdasbTerm());
						benef_Details.setRierCode(type);
						break;
					case "TPDB":
						benef_Details.setRiderPremium(calResp.getTpdb());
						benef_Details.setRiderTerm(calResp.getTpdbTerm());
						benef_Details.setRierCode(type);
						break;
					case "PPDB":
						benef_Details.setRiderPremium(calResp.getPpdb());
						benef_Details.setRiderTerm(calResp.getPpdbTerm());
						benef_Details.setRierCode(type);
						break;
					case "CIB":
						benef_Details.setRiderPremium(calResp.getCib());
						benef_Details.setRiderTerm(calResp.getCibTerm());
						benef_Details.setRierCode(type);
						break;
					case "FEB":
						benef_Details.setRiderPremium(calResp.getFeb());
						benef_Details.setRiderTerm(calResp.getFebTerm());
						benef_Details.setRierCode(type);
						break;
					case "MFIBD":
						benef_Details.setRiderPremium(calResp.getMifdb());
						benef_Details.setRiderTerm(calResp.getMifdbTerm());
						benef_Details.setRierCode(type);
						break;
					case "MFIBT":
						benef_Details.setRiderPremium(calResp.getMifdt());
						benef_Details.setRiderTerm(calResp.getMifdtTerm());
						benef_Details.setRierCode(type);
						break;
					case "MFIBDT":
						benef_Details.setRiderPremium(calResp.getMifdbt());
						benef_Details.setRiderTerm(calResp.getMifdbtTerm());
						benef_Details.setRierCode(type);
						break;
					case "HRB":
						benef_Details.setRiderPremium(calResp.getHrb());
						benef_Details.setRiderTerm(calResp.getHrbTerm());
						benef_Details.setRierCode(type);
						break;
					case "SUHRB":
						benef_Details.setRiderPremium(calResp.getSuhrb());
						benef_Details.setRiderTerm(calResp.getSuhrbTerm());
						benef_Details.setRierCode(type);
						break;
					case "HB":
						benef_Details.setRiderPremium(calResp.getHb());
						benef_Details.setRiderTerm(calResp.getHbTerm());
						benef_Details.setRierCode(type);
						break;
					case "WPB":
						benef_Details.setRiderPremium(calResp.getWpb());
						benef_Details.setRiderTerm(calResp.getWpbTerm());
						benef_Details.setRierCode(type);
						break;

					default:
						break;
					}

					benef_DetailList.add(benef_Details);

				}
			}

			ArrayList<Benifict> benifictListS = get_riderDetails.get_sRiders();

			if (benifictListS != null && benifictListS.size() > 0) {
				for (Benifict benifict : benifictListS) {
					Quo_Benef_Details benef_Details = new Quo_Benef_Details();
					if (benifict.getType().equals("BSAS"))
						benifict.setType("SCB");
					if (benifict.getType().equals("CIBS"))
						benifict.setType("SCIB");
					Benefits benifict2 = benefitsDao.findByRiderCode(benifict.getType());

					if (benifict2 != null) {
						benef_Details.setBenefit(benifict2);
					} else {
						System.out.println("******************" + benifict.getType() + "******Error");
					}
					benef_Details.setBenefit(benifict2);
					benef_Details.setQuotationDetails(quotationDetails);
					benef_Details.setRiderSum(benifict.getSumAssured());

					String type = benifict.getType();

					switch (type) {

					case "SCB":
						benef_Details.setRiderPremium(calResp.getBsas());
						benef_Details.setRiderTerm(calResp.getBsasTerm());
						benef_Details.setRierCode(type);
						break;
					case "ADBS":
						benef_Details.setRiderPremium(calResp.getAdbs());
						benef_Details.setRiderTerm(calResp.getAdbsTerm());
						benef_Details.setRierCode(type);
						break;
					case "TPDASBS":
						benef_Details.setRiderPremium(calResp.getTpdasbs());
						benef_Details.setRiderTerm(calResp.getTpdasbsTerm());
						benef_Details.setRierCode(type);
						break;
					case "TPDBS":
						benef_Details.setRiderPremium(calResp.getTpdbs());
						benef_Details.setRiderTerm(calResp.getTpdbsTerm());
						benef_Details.setRierCode(type);
						break;
					case "PPDBS":
						benef_Details.setRiderPremium(calResp.getPpdbs());
						benef_Details.setRiderTerm(calResp.getPpdbsTerm());
						benef_Details.setRierCode(type);
						break;
					case "SCIB":
						benef_Details.setRiderPremium(calResp.getCibs());
						benef_Details.setRiderTerm(calResp.getCibsTerm());
						benef_Details.setRierCode(type);
						break;
					case "FEBS":
						benef_Details.setRiderPremium(calResp.getFebs());
						benef_Details.setRiderTerm(calResp.getFebsTerm());
						benef_Details.setRierCode(type);
						break;
					case "HRBS":
						benef_Details.setRiderPremium(calResp.getHrbs());
						benef_Details.setRiderTerm(calResp.getHrbsTerm());
						benef_Details.setRierCode(type);
						break;
					case "SUHRBS":
						benef_Details.setRiderPremium(calResp.getSuhrbs());
						benef_Details.setRiderTerm(calResp.getSuhrbsTerm());
						benef_Details.setRierCode(type);
						break;
					case "HBS":
						benef_Details.setRiderPremium(calResp.getHbs());
						benef_Details.setRiderTerm(calResp.getHbsTerm());
						benef_Details.setRierCode(type);
						break;
					case "WPBS":
						benef_Details.setRiderPremium(calResp.getWpbs());
						benef_Details.setRiderTerm(calResp.getWpbsTerm());
						benef_Details.setRierCode(type);
						break;

					default:
						break;
					}

					benef_DetailList.add(benef_Details);

				}
			}

			ArrayList<Benifict> benifictListC = get_riderDetails.get_cRiders();

			if (benifictListC != null && benifictListC.size() > 0) {
				for (Benifict benifict : benifictListC) {
					Quo_Benef_Details benef_Details = new Quo_Benef_Details();
					Benefits benifict2 = benefitsDao.findByRiderCode(benifict.getType());
					if (benifict2 != null) {
						benef_Details.setBenefit(benifict2);
					} else {
						System.out.println("******************" + benifict.getType() + "******Error");
					}
					benef_Details.setBenefit(benifict2);
					benef_Details.setQuotationDetails(quotationDetails);
					benef_Details.setRiderSum(benifict.getSumAssured());

					String type = benifict.getType();

					switch (type) {

					case "CIBC":
						benef_Details.setRiderPremium(calResp.getCibc());
						benef_Details.setRierCode(type);
						break;
					case "SUHRBC":
						benef_Details.setRiderPremium(calResp.getSuhrbc());
						benef_Details.setRierCode(type);
						break;
					case "HBC":
						benef_Details.setRiderPremium(calResp.getHbc());
						benef_Details.setRierCode(type);
						break;
					case "HRBC":
						benef_Details.setRiderPremium(0.0);
						benef_Details.setRierCode(type);
						break;

					default:
						break;
					}

					benef_DetailList.add(benef_Details);
				}
			}

			return benef_DetailList;
		} finally {
			if (benef_DetailList != null) {
				benef_DetailList = null;
			}
		}
	}

	private ArrayList<Quo_Benef_Child_Details> getChildBenif(ArrayList<Quo_Benef_Details> benef_DetailsList,
			ArrayList<CustChildDetails> custChildDetailsList, ArrayList<Child> childList,
			ArrayList<Children> get_childrenList, Integer term, String frequancy, ArrayList<Benifict> benifictListC)
			throws Exception {

		Double cib = null;
		Double suhrb = null;
		Double hb = null;

		Quo_Benef_Details cibc_Benef_Details = null;
		Quo_Benef_Details hrbc_Benef_Details = null;
		Quo_Benef_Details suhrbc_Benef_Details = null;
		Quo_Benef_Details hbc_Benef_Details = null;

		if (benef_DetailsList != null && !benef_DetailsList.isEmpty()) {
			for (Quo_Benef_Details benef_Details : benef_DetailsList) {
				String type = benef_Details.getRierCode();
				switch (type) {
				case "CIBC":
					cibc_Benef_Details = benef_Details;
					break;
				case "SUHRBC":
					suhrbc_Benef_Details = benef_Details;
					break;
				case "HRBC":
					hrbc_Benef_Details = benef_Details;
					break;
				case "HBC":
					hbc_Benef_Details = benef_Details;
					break;

				default:
					break;
				}
			}
		}
		if (benifictListC != null && !benifictListC.isEmpty())
			for (Benifict benifict : benifictListC) {
				if (benifict.getType().equals("CIBC"))
					cib = benifict.getSumAssured();
				if (benifict.getType().equals("SUHRBC"))
					suhrb = benifict.getSumAssured();
				if (benifict.getType().equals("HBC"))
					hb = benifict.getSumAssured();
			}

		ArrayList<Quo_Benef_Child_Details> childBenifList = new ArrayList<>();
		for (Children children : get_childrenList) {
			for (Child child : childList) {
				if (child.getChildName().equals(children.get_cName())) {
					System.out.println("hit*******************");
					for (CustChildDetails childDetails : custChildDetailsList) {
						if (childDetails.getChild().equals(child)) {
							System.out.println("hit1*******************");

							if (children.is_cCibc()) {
								System.out.println("addddddddddddddddddddddddddddddddddddddd1");
								Quo_Benef_Child_Details benef_Child_Details = new Quo_Benef_Child_Details();

								Integer valiedTerm = calculateBenefictTerm.calculateBenifictTerm(children.get_cAge(),
										"CIBC", term);
								benef_Child_Details.setTerm(valiedTerm);

								BigDecimal cibc = cibcService.calculateCIBC(children.get_cAge(),
										term > (21 - 6) ? (21 - 6) : term, new Date(), cib, frequancy, 1.0);

								benef_Child_Details.setCustChildDetails(childDetails);
								benef_Child_Details.setTerm(valiedTerm);
								benef_Child_Details.setPremium(cibc.doubleValue());
								System.out.println(cibc_Benef_Details.getQuo_Benef_DetailsId()
										+ "===============================================1");
								benef_Child_Details.setQuo_Benef_Details(cibc_Benef_Details);
								childBenifList.add(benef_Child_Details);
							}
							if (children.is_cSuhrbc()) {
								System.out.println("addddddddddddddddddddddddddddddddddddddd2");
								Quo_Benef_Child_Details benef_Child_Details = new Quo_Benef_Child_Details();

								Integer valiedTerm = calculateBenefictTerm.calculateBenifictTerm(children.get_cAge(),
										"SUHRBC", term);
								benef_Child_Details.setTerm(valiedTerm);
								System.out.println(children.get_cTitle() + "                                  test");
								BigDecimal cibc = suhrbcService.calculateSUHRBC(children.get_cAge(),
										child.getChildGender(), valiedTerm, suhrb, new Date(), frequancy, 1.0);

								benef_Child_Details.setCustChildDetails(childDetails);
								benef_Child_Details.setTerm(valiedTerm);
								benef_Child_Details.setPremium(cibc.doubleValue());
								benef_Child_Details.setQuo_Benef_Details(suhrbc_Benef_Details);
								childBenifList.add(benef_Child_Details);
							}
							if (children.is_cHbc()) {
								System.out.println("addddddddddddddddddddddddddddddddddddddd3");
								Quo_Benef_Child_Details benef_Child_Details = new Quo_Benef_Child_Details();

								Integer valiedTerm = calculateBenefictTerm.calculateBenifictTerm(children.get_cAge(),
										"HBC", term);
								benef_Child_Details.setTerm(valiedTerm);
								BigDecimal cibc = hbcService.calculateHBC(valiedTerm, new Date(), hb, frequancy, 1.0);

								benef_Child_Details.setCustChildDetails(childDetails);
								benef_Child_Details.setTerm(valiedTerm);
								benef_Child_Details.setPremium(cibc.doubleValue());
								benef_Child_Details.setQuo_Benef_Details(hbc_Benef_Details);
								childBenifList.add(benef_Child_Details);
							}
							if (children.is_cHrbc()) {
								System.out.println("addddddddddddddddddddddddddddddddddddddd4");
								Quo_Benef_Child_Details benef_Child_Details = new Quo_Benef_Child_Details();

								Integer valiedTerm = calculateBenefictTerm.calculateBenifictTerm(children.get_cAge(),
										"HBC", term);
								benef_Child_Details.setTerm(valiedTerm);

								benef_Child_Details.setCustChildDetails(childDetails);
								benef_Child_Details.setTerm(valiedTerm);
								benef_Child_Details.setPremium(0.0);
								benef_Child_Details.setQuo_Benef_Details(hrbc_Benef_Details);
								childBenifList.add(benef_Child_Details);
							}
						}
					}
				}
			}
		}
		System.out.println(childBenifList.size() + "                            444444");
		return childBenifList;
	}
}
