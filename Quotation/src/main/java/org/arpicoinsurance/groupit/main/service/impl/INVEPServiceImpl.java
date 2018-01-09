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
import org.arpicoinsurance.groupit.main.service.rider.CIBCService;
import org.arpicoinsurance.groupit.main.service.rider.CIBService;
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
public class INVEPServiceImpl implements INVPService{
	
	private Double occupationValue=1.0;
	
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
		case "SCB":
			BigDecimal scb=scbService.calculateSCB(age, term, new Date(), ridsumasu, payFrequency, 1.0);
			calResp.setBsas(scb.doubleValue());
		break;
		case "ADB":
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
				BigDecimal tpdasbs= tpdasbsbService.calculateTPDASBS(age, new Date(), ridsumasu, payFrequency, 1.0);
				calResp.setTpdasbs(tpdasbs.doubleValue());
		break;
		case "TPDB":
			BigDecimal tpdb= tpdbService.calculateTPDB(ridsumasu, payFrequency, 1.0);
			calResp.setTpdb(tpdb.doubleValue());
		break;
		case "TPDBS":
				BigDecimal tpdbs= tpdbsService.calculateTPDBS(ridsumasu, payFrequency, 1.0);
				calResp.setTpdbs(tpdbs.doubleValue());
		break;
		case "PPDB":
			BigDecimal ppdb= ppdbService.calculatePPDB(ridsumasu, payFrequency, 1.0);
			calResp.setPpdb(ppdb.doubleValue());
		break;
		case "PPDBS":
				BigDecimal ppdbs= ppdbsService.calculatePPDBS(ridsumasu, payFrequency, 1.0);
				calResp.setPpdbs(ppdbs.doubleValue());
		break;
		case "CIB":
			BigDecimal cib= cibService.calculateCIB(age, term, new Date(), ridsumasu, payFrequency, 1.0);
			calResp.setCib(cib.doubleValue());
		break;
		case "SCIB":
			BigDecimal scib= scibService.calculateSCIB(age, term, new Date(), ridsumasu, payFrequency, 1.0);
			calResp.setCibs(scib.doubleValue());
		break;
		case "CIBC":
			//** 21-age < term term = 21-age else term
			BigDecimal cibc= cibcService.calculateCIBC(6, term > (21-6) ? (21-6) : term, new Date(), ridsumasu, payFrequency, 1.0);
			calResp.setCibc(cibc.doubleValue());
		break;
		case "FEB":
			BigDecimal feb= febService.calculateFEB(age, term, new Date(), ridsumasu, payFrequency, 1.0);
			calResp.setFeb(feb.doubleValue());
		break;
		case "MFIBD":
			BigDecimal mfibd= mfibdService.calculateMFIBD(age, term, new Date(), ridsumasu, payFrequency, 1.0);
			calResp.setMifdb(mfibd.doubleValue());
		break;
		case "MFIBT":
			BigDecimal mfibt= mfibtService.calculateMFIBT(age, term, new Date(), ridsumasu, payFrequency, 1.0);
			calResp.setMifdt(mfibt.doubleValue());
		break;
		case "MFIBDT":
			BigDecimal mfibdt= mfibdtService.calculateMFIBDT(age, term, new Date(), ridsumasu, payFrequency, 1.0);
			calResp.setMifdbt(mfibdt.doubleValue());
		break;
		case "HRB":
			BigDecimal hrb= hrbService.calculateHRB(age, gender, ridsumasu, 2, 0, new Date(), payFrequency, 1.0);
			calResp.setHrb(hrb.doubleValue());
		break;
		case "SUHRB":
			BigDecimal suhrb= suhrbService.calculateSUHRB(age, gender, term, ridsumasu, new Date(), payFrequency, 1.0);
			calResp.setSuhrb(suhrb.doubleValue());
		break;
		case "SUHRBS":
			BigDecimal suhrbs= suhrbsService.calculateSUHRBS(28, "F", term, ridsumasu, new Date(), payFrequency, 1.0);
			calResp.setSuhrbs(suhrbs.doubleValue());
		break;
		case "SUHRBC":
			BigDecimal suhrbc= suhrbcService.calculateSUHRBC(6, gender, term, ridsumasu, new Date(), payFrequency, 1.0);
			calResp.setSuhrbc(suhrbc.doubleValue());
		break;
		case "HB":
			BigDecimal hb= hbService.calculateHB(age, term, new Date(), ridsumasu, payFrequency, 1.0);
			calResp.setHb(hb.doubleValue());
		break;
		case "HBS":
			BigDecimal hbs= hbsService.calculateHBS(28, term, new Date(), ridsumasu, payFrequency, 1.0);
			calResp.setHbs(hbs.doubleValue());
		break;
		case "HBC":
			//** 21-age < term term = 21-age else term
			BigDecimal hbc= hbcService.calculateHBC(term > (21-6) ? (21-6) : term, new Date(), ridsumasu, payFrequency, 1.0);
			calResp.setHbc(hbc.doubleValue());
		break;
		case "WPB":
			BigDecimal wpb= wpbService.calculateWPB(calResp);
			calResp.setWpb(wpb.doubleValue());
		break;
		case "WPBS":
			BigDecimal wpbs= wpbsService.calculateWPBS(calResp);
			calResp.setWpbs(wpbs.doubleValue());
		break;

		default:
			break;
		}
		
		return 0.0;
	}
	

}
