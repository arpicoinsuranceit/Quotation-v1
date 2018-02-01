package org.arpicoinsurance.groupit.main.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.dao.RateCardASIPDao;
import org.arpicoinsurance.groupit.main.dao.RateCardASIPFundDao;
import org.arpicoinsurance.groupit.main.helper.QuoInvpCalResp;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.model.RateCardASIP;
import org.arpicoinsurance.groupit.main.model.RateCardASIPFund;
import org.arpicoinsurance.groupit.main.service.ASIPService;
import org.arpicoinsurance.groupit.main.service.CalculateBenifictTermService;
import org.arpicoinsurance.groupit.main.service.rider.ADBSService;
import org.arpicoinsurance.groupit.main.service.rider.ADBService;
import org.arpicoinsurance.groupit.main.service.rider.ATPBService;
import org.arpicoinsurance.groupit.main.service.rider.CIBCService;
import org.arpicoinsurance.groupit.main.service.rider.CIBService;
import org.arpicoinsurance.groupit.main.service.rider.FEBSService;
import org.arpicoinsurance.groupit.main.service.rider.FEBService;
import org.arpicoinsurance.groupit.main.service.rider.HBCService;
import org.arpicoinsurance.groupit.main.service.rider.HBSService;
import org.arpicoinsurance.groupit.main.service.rider.HBService;
import org.arpicoinsurance.groupit.main.service.rider.HRBService;
import org.arpicoinsurance.groupit.main.service.rider.MFIBDService;
import org.arpicoinsurance.groupit.main.service.rider.MFIBDTService;
import org.arpicoinsurance.groupit.main.service.rider.MFIBTService;
import org.arpicoinsurance.groupit.main.service.rider.PPDBSService;
import org.arpicoinsurance.groupit.main.service.rider.PPDBService;
import org.arpicoinsurance.groupit.main.service.rider.SCBService;
import org.arpicoinsurance.groupit.main.service.rider.SCIBService;
import org.arpicoinsurance.groupit.main.service.rider.SUHRBCService;
import org.arpicoinsurance.groupit.main.service.rider.SUHRBSService;
import org.arpicoinsurance.groupit.main.service.rider.SUHRBService;
import org.arpicoinsurance.groupit.main.service.rider.TPDASBSService;
import org.arpicoinsurance.groupit.main.service.rider.TPDASBService;
import org.arpicoinsurance.groupit.main.service.rider.TPDBSService;
import org.arpicoinsurance.groupit.main.service.rider.TPDBService;
import org.arpicoinsurance.groupit.main.service.rider.WPBSService;
import org.arpicoinsurance.groupit.main.service.rider.WPBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ASIPServiceImpl implements ASIPService {

	private Double occupationValue = 1.0;

	private Integer adultCount = 1;
	private Integer childCount = 0;

	@Autowired
	private SCBService scbService;

	@Autowired
	private ATPBService atpbService;

	@Autowired
	private ADBService adbService;

	@Autowired
	private ADBSService adbsService;

	@Autowired
	private TPDASBService tpdasbService;

	@Autowired
	private TPDASBSService tpdasbsbService;

	@Autowired
	private TPDBService tpdbService;

	@Autowired
	private TPDBSService tpdbsService;

	@Autowired
	private PPDBService ppdbService;

	@Autowired
	private PPDBSService ppdbsService;

	@Autowired
	private CIBService cibService;

	@Autowired
	private SCIBService scibService;

	@Autowired
	private CIBCService cibcService;

	@Autowired
	private FEBService febService;

	@Autowired
	private FEBSService febsService;

	@Autowired
	private MFIBDService mfibdService;

	@Autowired
	private MFIBTService mfibtService;

	@Autowired
	private MFIBDTService mfibdtService;

	@Autowired
	private HRBService hrbService;

	@Autowired
	private SUHRBService suhrbService;

	@Autowired
	private SUHRBSService suhrbsService;

	@Autowired
	private SUHRBCService suhrbcService;

	@Autowired
	private HBService hbService;

	@Autowired
	private HBSService hbsService;

	@Autowired
	private HBCService hbcService;

	@Autowired
	private WPBService wpbService;

	@Autowired
	private WPBSService wpbsService;

	@Autowired
	private RateCardASIPDao rateCardASIPDao;
	
	@Autowired
	private RateCardASIPFundDao rateCardASIPFundDao;

	@Autowired
	private CalculateBenifictTermService calculateBenefictTerm;
	
	@Override
	public QuoInvpCalResp getCalcutatedInvp(QuotationCalculation quotationCalculation) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal calculateL2(int term, double bassum, int paytrm)
			throws Exception {
		BigDecimal premium = new BigDecimal(0);
		System.out.println("term : "+term+" bassum : "+bassum+" paytrm : "+paytrm);
		// ((@sum_assured@/@term@)/@payment_frequency@)
		premium = (new BigDecimal(bassum).divide(new BigDecimal(term), 6, RoundingMode.HALF_UP)).divide(new BigDecimal(paytrm), 4, RoundingMode.HALF_UP);
		System.out.println("premium : "+premium.toString());
		return premium;
	}

	@Override
	public Double addRebatetoBSAPremium(double rebate, BigDecimal premium) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal calculateMaturity(int age, int term, double fundcharat, double intrat, Date chedat, double bassum, double bsapremium, int paytrm)
			throws Exception {
		CalculationUtils calculationUtils = new CalculationUtils();
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
		return maturity;
	}

}
