package org.arpicoinsurance.groupit.main.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;

import org.arpicoinsurance.groupit.main.common.BenifictCalculation;
import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.dao.RateCardINVPDao;
import org.arpicoinsurance.groupit.main.helper.Benifict;
import org.arpicoinsurance.groupit.main.helper.QuoCalResp;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.model.RateCardINVP;
import org.arpicoinsurance.groupit.main.service.INVPService;
import org.arpicoinsurance.groupit.main.service.rider.ADBSService;
import org.arpicoinsurance.groupit.main.service.rider.ADBService;
import org.arpicoinsurance.groupit.main.service.rider.ATPBService;
import org.arpicoinsurance.groupit.main.service.rider.TPDASBSService;
import org.arpicoinsurance.groupit.main.service.rider.TPDASBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class INVEPServiceImpl implements INVPService{
	
	private Double occupationValue=1.0;
	
	
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
	private RateCardINVPDao rateCardINVPDao;
	
	@Override
	public QuoCalResp getCalcutatedInvp(QuotationCalculation quotationCalculation) throws Exception {
		CalculationUtils calculationUtils=null;
		BenifictCalculation benifictCalculation=null;
		try {
			
			
			
			QuoCalResp calResp=new QuoCalResp();
			calculationUtils=new CalculationUtils();
			benifictCalculation=new BenifictCalculation();
			/// Calculate Rebate Premium ///
			Double rebate=calculationUtils.getRebate(quotationCalculation.get_personalInfo().getTerm(), quotationCalculation.get_personalInfo().getFrequance());
			/// Calculate BSA Premium ///
			BigDecimal bsaPremium=calculateL2(quotationCalculation.get_personalInfo().getMage(), quotationCalculation.get_personalInfo().getTerm(), 8.0, new Date(), quotationCalculation.get_personalInfo().getBsa(), calculationUtils.getPayterm(quotationCalculation.get_personalInfo().getFrequance()));
			ArrayList<Benifict> _riderDetails=quotationCalculation.get_riderDetails();
			
			
			
			/// SET VALUES TO QuoCalResp ///
			
			calResp.setBasicSumAssured(addRebatetoBSAPremium(rebate, bsaPremium));
			calResp.setAt6(calculateMaturity(quotationCalculation.get_personalInfo().getMage(),
					quotationCalculation.get_personalInfo().getTerm(), 8.0, new Date(), quotationCalculation.get_personalInfo().getBsa(), calculationUtils.getPayterm(quotationCalculation.get_personalInfo().getFrequance())).doubleValue());
			calResp.setAt8(calculateMaturity(quotationCalculation.get_personalInfo().getMage(),
					quotationCalculation.get_personalInfo().getTerm(), 10.0, new Date(), quotationCalculation.get_personalInfo().getBsa(), calculationUtils.getPayterm(quotationCalculation.get_personalInfo().getFrequance())).doubleValue());
			calResp.setAt10(calculateMaturity(quotationCalculation.get_personalInfo().getMage(),
					quotationCalculation.get_personalInfo().getTerm(), 12.0, new Date(), quotationCalculation.get_personalInfo().getBsa(), calculationUtils.getPayterm(quotationCalculation.get_personalInfo().getFrequance())).doubleValue());
		
			if(_riderDetails!=null) {
				for (Benifict benifict : _riderDetails) {
					calculateBenifPremium(benifict.getType(), benifict.getSumAssured(), quotationCalculation.get_personalInfo().getMgenger(),
							 quotationCalculation.get_personalInfo().getMage(), quotationCalculation.get_personalInfo().getFrequance(),quotationCalculation.get_personalInfo().getTerm()
							 , occupationValue, calResp);
				}
			}
			
			
			
			
			return calResp;
			
		}finally {
			if(calculationUtils!=null) {
				calculationUtils=null;
			}
			if(benifictCalculation!=null) {
				benifictCalculation=null;
			}
		}
		
		
	}
	
	@Override
	public BigDecimal calculateL2(int age, int term, double intrat, Date chedat, double bassum, int paytrm) throws Exception {
		BigDecimal premium = new BigDecimal(0);
		RateCardINVP rateCardINVP = rateCardINVPDao.findByAgeAndTermAndIntratAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term, intrat, chedat, chedat, chedat, chedat);
		System.out.println("age : "+age+" term : "+term+" intrat : "+intrat+" paytrm : "+paytrm+" Sumasu : "+bassum+" SumRate : "+rateCardINVP.getSumasu()+" Rate : "+rateCardINVP.getRate());
		premium = ((new BigDecimal(1000).divide(new BigDecimal(rateCardINVP.getSumasu()),20,RoundingMode.HALF_UP)).multiply(new BigDecimal(bassum))).divide(new BigDecimal(paytrm), 4, RoundingMode.UP);
		return premium;
	}

	@Override
	public Double addRebatetoBSAPremium(double rebate, BigDecimal premium)  throws Exception {
		System.out.println("rebate : "+rebate);
		BigDecimal rebateRate = new BigDecimal(1).subtract((new BigDecimal(rebate).divide(new BigDecimal(100), 6 ,RoundingMode.HALF_UP)));
		System.out.println("rebateRate : "+rebateRate.doubleValue());
		premium = premium.multiply(rebateRate).setScale(0, RoundingMode.HALF_UP);
		return premium.doubleValue();
	}

	@Override
	public BigDecimal calculateMaturity(int age, int term, double intrat, Date chedat, double bassum, int paytrm)
			throws Exception {
		// (@rate@/@sum_assured_rate@)*@sum_assured@
				BigDecimal maturity  = new BigDecimal(0);
				RateCardINVP rateCardINVP = rateCardINVPDao.findByAgeAndTermAndIntratAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term, intrat, chedat, chedat, chedat, chedat);
				System.out.println("age : "+age);
				System.out.println("term : "+term);
				System.out.println("intrat : "+intrat);
				System.out.println("paytrm : "+paytrm);
				System.out.println("Sumasu : "+rateCardINVP.getSumasu());
				System.out.println("SumRate : "+rateCardINVP.getSumasu());
				System.out.println("Rate : "+rateCardINVP.getRate());
				
				maturity = (new BigDecimal(rateCardINVP.getRate()).divide(new BigDecimal(rateCardINVP.getSumasu()),20,RoundingMode.HALF_UP)).multiply(new BigDecimal(bassum)).setScale(0, RoundingMode.HALF_UP);
				return maturity;
			
	}
	
	Double calculateBenifPremium(String type, Double ridsumasu, String gender, Integer age, String payFrequency,
			Integer term, Double occupationValue, QuoCalResp calResp) throws Exception{
		switch (type) {
		case "ADB":
				System.out.println(ridsumasu);
				System.out.println(payFrequency);
				
				BigDecimal adb=adbService.calculateADB(ridsumasu, payFrequency, 1.0);
				calResp.setAdb(adb.doubleValue());
			break;
		case "ADBS":
				BigDecimal adbs= adbsService.calculateADBS(ridsumasu, payFrequency, 1.0);
				calResp.setAdbs(adbs.doubleValue());
			break;
		case "ATPB":
				BigDecimal atpb= atpbService.calculateATPB(age, term, new Date(), ridsumasu, payFrequency, 1.0);
				calResp.setAtpb(atpb.doubleValue());
		break;
		case "TPDASB":
				BigDecimal tpdasb= tpdasbService.calculateTPDASB(age, new Date(), ridsumasu, payFrequency, 1.0);
				calResp.setTpdasb(tpdasb.doubleValue());
		break;
		case "TPDASBS":
				BigDecimal tpdasbs= tpdasbsbService.calculateTPDASBS(age, new Date(), ridsumasu, payFrequency, 0.0);
				calResp.setTpdasbs(tpdasbs.doubleValue());
		break;

		default:
			break;
		}
		
		return 0.0;
	}
	

}
