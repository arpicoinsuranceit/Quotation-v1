package org.arpicoinsurance.groupit.main.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;

import org.arpicoinsurance.groupit.main.common.BenifictCalculation;
import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.dao.RateCardATFESCDao;
import org.arpicoinsurance.groupit.main.dao.RateCardINVPDao;
import org.arpicoinsurance.groupit.main.helper.Benifict;
import org.arpicoinsurance.groupit.main.helper.Children;
import org.arpicoinsurance.groupit.main.helper.QuoCalResp;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.model.RateCardATFESC;
import org.arpicoinsurance.groupit.main.model.RateCardINVP;
import org.arpicoinsurance.groupit.main.service.CalculateBenifictTermService;
import org.arpicoinsurance.groupit.main.service.INVPService;
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
public class INVPServiceImpl implements INVPService {

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
	private RateCardINVPDao rateCardINVPDao;

	@Autowired
	private RateCardATFESCDao rateCardATFESCDao;
	
	@Autowired
	private CalculateBenifictTermService calculateBenefictTerm;

	@Override
	public QuoCalResp getCalcutatedInvp(QuotationCalculation quotationCalculation) throws Exception {

		CalculationUtils calculationUtils = null;
		BenifictCalculation benifictCalculation = null;
		try {

			QuoCalResp calResp = new QuoCalResp();
			calculationUtils = new CalculationUtils();
			benifictCalculation = new BenifictCalculation();
			/// Calculate Rebate Premium ///
			Double rebate = calculationUtils.getRebate(quotationCalculation.get_personalInfo().getTerm(),
					quotationCalculation.get_personalInfo().getFrequance());
			/// Calculate Admin Fee Premium ///
			Double admfee = calculationUtils.getAdminFee(quotationCalculation.get_personalInfo().getFrequance());
			/// Calculate BSA Premium ///
			BigDecimal bsaPremium = calculateL2(quotationCalculation.get_personalInfo().getMage(),
					quotationCalculation.get_personalInfo().getTerm(), 8.0, new Date(),
					quotationCalculation.get_personalInfo().getBsa(),
					calculationUtils.getPayterm(quotationCalculation.get_personalInfo().getFrequance()));

			ArrayList<Benifict> _mRiders = null;
			ArrayList<Benifict> _sRiders = null;
			ArrayList<Benifict> _cRiders = null;
			if (quotationCalculation.get_riderDetails() != null) {
				if (quotationCalculation.get_riderDetails().get_mRiders() != null) {
					_mRiders = quotationCalculation.get_riderDetails().get_mRiders();
				}
				if (quotationCalculation.get_riderDetails().get_sRiders() != null) {
					_sRiders = quotationCalculation.get_riderDetails().get_sRiders();
				}
				if (quotationCalculation.get_riderDetails().get_cRiders() != null) {
					_cRiders = quotationCalculation.get_riderDetails().get_cRiders();
				}

			}
			/// SET VALUES TO QuoCalResp ///

			

			if (_mRiders != null) {
				for (Benifict benifict : _mRiders) {
					adultCount = 1;
					if (benifict.getType().equals("HRB")) {
						if (_sRiders != null) {
							for (Benifict benifict2 : _sRiders) {
								if (benifict2.getType().equals("HRBS")) {
									adultCount += 1;
								}
							}
						}
						if (_cRiders != null) {
							for (Children children : quotationCalculation.get_personalInfo().getChildrens()) {
								if (children.is_cHrbc()) {
									childCount += 1;
								}
							}
						}
					}
					Integer term = calculateBenefictTerm.calculateBenifictTerm(
							quotationCalculation.get_personalInfo().getMage(), benifict.getType(),
							quotationCalculation.get_personalInfo().getTerm());
					calculateBenifPremium(benifict.getType(), benifict.getSumAssured(),
							quotationCalculation.get_personalInfo().getMgenger(),
							quotationCalculation.get_personalInfo().getMage(),
							quotationCalculation.get_personalInfo().getFrequance(), term, occupationValue, calResp);

				}
			}

			if (quotationCalculation.get_personalInfo().getSage() != null
					&& quotationCalculation.get_personalInfo().getSgenger() != null
					&& quotationCalculation.get_personalInfo().getSocu() != null) {

				if (_sRiders != null) {

					for (Benifict benifict : _sRiders) {
						Integer term = calculateBenefictTerm.calculateBenifictTerm(
								quotationCalculation.get_personalInfo().getSage(), benifict.getType(),
								quotationCalculation.get_personalInfo().getTerm());
						calculateBenifPremium(benifict.getType(), benifict.getSumAssured(),
								quotationCalculation.get_personalInfo().getMgenger(),
								quotationCalculation.get_personalInfo().getSage(),
								quotationCalculation.get_personalInfo().getFrequance(), term, occupationValue, calResp);
					}
				}
			}

			if (quotationCalculation.get_personalInfo().getChildrens() != null
					&& quotationCalculation.get_personalInfo().getChildrens().size() > 0) {
				for (Children children : quotationCalculation.get_personalInfo().getChildrens()) {
					if (_cRiders != null) {
						for (Benifict benifict : _cRiders) {
							Integer term = calculateBenefictTerm.calculateBenifictTerm(
									children.get_cAge(), benifict.getType(),
									quotationCalculation.get_personalInfo().getTerm());
							String benfName = benifict.getType();

							switch (benfName) {
							case "CIBC":
								if (children.is_cCibc()) {
									calculateBenifPremium(benifict.getType(), benifict.getSumAssured(),
											quotationCalculation.get_personalInfo().getMgenger(),
											children.get_cAge(),
											quotationCalculation.get_personalInfo().getFrequance(), term,
											occupationValue, calResp);
								}
								break;

							case "HBC":
								if (children.is_cHbc()) {
									calculateBenifPremium(benifict.getType(), benifict.getSumAssured(),
											quotationCalculation.get_personalInfo().getMgenger(),
											children.get_cAge(),
											quotationCalculation.get_personalInfo().getFrequance(), term,
											occupationValue, calResp);
								}
								break;

							case "SUHRBC":
								if (children.is_cSuhrbc()) {
									calculateBenifPremium(benifict.getType(), benifict.getSumAssured(),
											quotationCalculation.get_personalInfo().getMgenger(),
											children.get_cAge(),
											quotationCalculation.get_personalInfo().getFrequance(), term,
											occupationValue, calResp);
								}
								break;

							default:
								break;
							}
						}
					}
				}
			}

			calResp.setBasicSumAssured(addRebatetoBSAPremium(rebate, bsaPremium));			
			calResp.setAt6(calculateMaturity(quotationCalculation.get_personalInfo().getMage(),
					quotationCalculation.get_personalInfo().getTerm(), 8.0, new Date(),
					quotationCalculation.get_personalInfo().getBsa(),
					calculationUtils.getPayterm(quotationCalculation.get_personalInfo().getFrequance())).doubleValue());
			calResp.setAt8(calculateMaturity(quotationCalculation.get_personalInfo().getMage(),
					quotationCalculation.get_personalInfo().getTerm(), 10.0, new Date(),
					quotationCalculation.get_personalInfo().getBsa(),
					calculationUtils.getPayterm(quotationCalculation.get_personalInfo().getFrequance())).doubleValue());
			calResp.setAt10(calculateMaturity(quotationCalculation.get_personalInfo().getMage(),
					quotationCalculation.get_personalInfo().getTerm(), 12.0, new Date(),
					quotationCalculation.get_personalInfo().getBsa(),
					calculationUtils.getPayterm(quotationCalculation.get_personalInfo().getFrequance())).doubleValue());
			/// Calculate Tax Amount Premium ///
			Double taxamt = calculationUtils.getTaxAmount((calResp.getBasicSumAssured()+calResp.getAddBenif()));
			/// get Invest Life Premium ///
			getInvestLifePremium(quotationCalculation.get_personalInfo().getMage(), quotationCalculation.get_personalInfo().getTerm(), 
					new Date(), quotationCalculation.get_personalInfo().getBsa(), calResp.getBasicSumAssured(), 
					calculationUtils.getPayterm(quotationCalculation.get_personalInfo().getFrequance()));
			calResp.setExtraOE(admfee+taxamt);
			calResp.setTotPremium(calResp.getBasicSumAssured()+calResp.getAddBenif()+ calResp.getExtraOE());
			return calResp;

		} finally {
			if (calculationUtils != null) {
				calculationUtils = null;
			}
			if (benifictCalculation != null) {
				benifictCalculation = null;
			}
		}

	}

	@Override
	public BigDecimal calculateL2(int age, int term, double intrat, Date chedat, double bassum, int paytrm)
			throws Exception {
		BigDecimal premium = new BigDecimal(0);
		RateCardINVP rateCardINVP = rateCardINVPDao
				.findByAgeAndTermAndIntratAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term, intrat,
						chedat, chedat, chedat, chedat);
		System.out.println(
				"age : " + age + " term : " + term + " intrat : " + intrat + " paytrm : " + paytrm + " Sumasu : "
						+ bassum + " SumRate : " + rateCardINVP.getSumasu() + " Rate : " + rateCardINVP.getRate());
		premium = ((new BigDecimal(1000).divide(new BigDecimal(rateCardINVP.getSumasu()), 20, RoundingMode.HALF_UP))
				.multiply(new BigDecimal(bassum))).divide(new BigDecimal(paytrm), 4, RoundingMode.UP);
		return premium;
	}

	@Override
	public Double addRebatetoBSAPremium(double rebate, BigDecimal premium) throws Exception {
		System.out.println("rebate : " + rebate);
		BigDecimal rebateRate = new BigDecimal(1)
				.subtract((new BigDecimal(rebate).divide(new BigDecimal(100), 6, RoundingMode.HALF_UP)));
		System.out.println("rebateRate : " + rebateRate.doubleValue());
		premium = premium.multiply(rebateRate).setScale(0, RoundingMode.HALF_UP);
		return premium.doubleValue();
	}

	@Override
	public BigDecimal calculateMaturity(int age, int term, double intrat, Date chedat, double bassum, int paytrm)
			throws Exception {
		// (@rate@/@sum_assured_rate@)*@sum_assured@
		BigDecimal maturity = new BigDecimal(0);
		RateCardINVP rateCardINVP = rateCardINVPDao
				.findByAgeAndTermAndIntratAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term, intrat,
						chedat, chedat, chedat, chedat);
		System.out.println("age : " + age);
		System.out.println("term : " + term);
		System.out.println("intrat : " + intrat);
		System.out.println("paytrm : " + paytrm);
		System.out.println("Sumasu : " + rateCardINVP.getSumasu());
		System.out.println("SumRate : " + rateCardINVP.getSumasu());
		System.out.println("Rate : " + rateCardINVP.getRate());

		maturity = (new BigDecimal(rateCardINVP.getRate()).divide(new BigDecimal(rateCardINVP.getSumasu()), 20,
				RoundingMode.HALF_UP)).multiply(new BigDecimal(bassum)).setScale(0, RoundingMode.HALF_UP);
		return maturity;

	}

	Double calculateBenifPremium(String type, Double ridsumasu, String gender, Integer age, String payFrequency,
			Integer term, Double occupationValue, QuoCalResp calResp) throws Exception {

		switch (type) {
		case "BSAS":
			BigDecimal scb = scbService.calculateSCB(age, term, new Date(), ridsumasu, payFrequency, 1.0);
			calResp.setBsas(scb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif()+scb.doubleValue());
			break;
		case "ADB":
			BigDecimal adb = adbService.calculateADB(ridsumasu, payFrequency, 1.0);
			calResp.setAdb(adb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + adb.doubleValue());
			break;
		case "ADBS":
			BigDecimal adbs = adbsService.calculateADBS(ridsumasu, payFrequency, 1.0);
			calResp.setAdbs(adbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif()+adbs.doubleValue());
			break;
		case "ATPB":
			BigDecimal atpb = atpbService.calculateATPB(age, term, new Date(), ridsumasu, payFrequency, 1.0);
			calResp.setAtpb(atpb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif()+atpb.doubleValue());
			break;
		case "TPDASB":
			BigDecimal tpdasb = tpdasbService.calculateTPDASB(age, new Date(), ridsumasu, payFrequency, 1.0);
			calResp.setTpdasb(tpdasb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif()+tpdasb.doubleValue());
			break;
		case "TPDASBS":
			BigDecimal tpdasbs = tpdasbsbService.calculateTPDASBS(age, new Date(), ridsumasu, payFrequency, 1.0);
			calResp.setTpdasbs(tpdasbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif()+tpdasbs.doubleValue());
			break;
		case "TPDB":
			BigDecimal tpdb = tpdbService.calculateTPDB(ridsumasu, payFrequency, 1.0);
			calResp.setTpdb(tpdb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif()+tpdb.doubleValue());
			break;
		case "TPDBS":
			BigDecimal tpdbs = tpdbsService.calculateTPDBS(ridsumasu, payFrequency, 1.0);
			calResp.setTpdbs(tpdbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif()+tpdbs.doubleValue());
			break;
		case "PPDB":
			BigDecimal ppdb = ppdbService.calculatePPDB(ridsumasu, payFrequency, 1.0);
			calResp.setPpdb(ppdb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif()+ppdb.doubleValue());
			break;
		case "PPDBS":
			BigDecimal ppdbs = ppdbsService.calculatePPDBS(ridsumasu, payFrequency, 1.0);
			calResp.setPpdbs(ppdbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif()+ppdbs.doubleValue());
			break;
		case "CIB":
			BigDecimal cib = cibService.calculateCIB(age, term, new Date(), ridsumasu, payFrequency, 1.0);
			calResp.setCib(cib.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif()+cib.doubleValue());
			break;
		case "CIBS":
			BigDecimal scib = scibService.calculateSCIB(age, term, new Date(), ridsumasu, payFrequency, 1.0);
			calResp.setCibs(scib.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif()+scib.doubleValue());
			break;
		case "CIBC":
			// ** 21-age < term term = 21-age else term
			BigDecimal cibc = cibcService.calculateCIBC(6, term > (21 - 6) ? (21 - 6) : term, new Date(), ridsumasu,
					payFrequency, 1.0);
			calResp.setCibc(calResp.getCibc() + cibc.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif()+cibc.doubleValue());
			break;
		case "FEB":
			BigDecimal feb = febService.calculateFEB(age, term, new Date(), ridsumasu, payFrequency, 1.0);
			calResp.setFeb(feb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif()+feb.doubleValue());
			break;
		case "FEBS":
			BigDecimal febs = febsService.calculateFEBS(age, term, new Date(), ridsumasu, payFrequency, 1.0);
			calResp.setFebs(febs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif()+febs.doubleValue());
			break;

		case "MFIBD":
			BigDecimal mfibd = mfibdService.calculateMFIBD(age, term, new Date(), ridsumasu, payFrequency, 1.0);
			calResp.setMifdb(mfibd.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif()+mfibd.doubleValue());
			break;
		case "MFIBT":
			BigDecimal mfibt = mfibtService.calculateMFIBT(age, term, new Date(), ridsumasu, payFrequency, 1.0);
			calResp.setMifdt(mfibt.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif()+mfibt.doubleValue());
			break;
		case "MFIBDT":
			BigDecimal mfibdt = mfibdtService.calculateMFIBDT(age, term, new Date(), ridsumasu, payFrequency, 1.0);
			calResp.setMifdbt(mfibdt.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif()+mfibdt.doubleValue());
			break;
		case "HRB":
			System.out.println(age + "******************************************** " + gender + " " + ridsumasu + " "
					+ adultCount + " " + childCount);
			BigDecimal hrb = hrbService.calculateHRB(age, gender, ridsumasu, adultCount, childCount, new Date(),
					payFrequency, 1.0);
			calResp.setHrb(hrb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif()+hrb.doubleValue());
			break;
		case "SUHRB":
			BigDecimal suhrb = suhrbService.calculateSUHRB(age, gender, term, ridsumasu, new Date(), payFrequency, 1.0);
			calResp.setSuhrb(suhrb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif()+suhrb.doubleValue());
			break;
		case "SUHRBS":
			BigDecimal suhrbs = suhrbsService.calculateSUHRBS(28, "F", term, ridsumasu, new Date(), payFrequency, 1.0);
			calResp.setSuhrbs(suhrbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif()+suhrbs.doubleValue());
			break;
		case "SUHRBC":
			BigDecimal suhrbc = suhrbcService.calculateSUHRBC(6, gender, term, ridsumasu, new Date(), payFrequency,
					1.0);
			calResp.setSuhrbc(calResp.getSuhrbc() + suhrbc.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif()+suhrbc.doubleValue());
			break;
		case "HB":
			BigDecimal hb = hbService.calculateHB(age, term, new Date(), ridsumasu, payFrequency, 1.0);
			calResp.setHb(hb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif()+hb.doubleValue());
			break;
		case "HBS":
			BigDecimal hbs = hbsService.calculateHBS(28, term, new Date(), ridsumasu, payFrequency, 1.0);
			calResp.setHbs(hbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif()+hbs.doubleValue());
			break;
		case "HBC":
			// ** 21-age < term term = 21-age else term
			BigDecimal hbc = hbcService.calculateHBC(term > (21 - 6) ? (21 - 6) : term, new Date(), ridsumasu,
					payFrequency, 1.0);
			calResp.setHbc(calResp.getHbc() + hbc.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif()+hbc.doubleValue());
			break;
		case "WPB":
			BigDecimal wpb = wpbService.calculateWPB(calResp);
			calResp.setWpb(wpb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif()+wpb.doubleValue());
			break;
		case "WPBS":
			BigDecimal wpbs = wpbsService.calculateWPBS(calResp);
			calResp.setWpbs(wpbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif()+wpbs.doubleValue());
			break;

		default:
			break;
		}

		return 0.0;
	}

	@Override
	public BigDecimal getInvestLifePremium(int age, int term, Date chedat, double bassum, double premium, int paytrm) throws Exception {
		BigDecimal lifpos = new BigDecimal(0);
		RateCardATFESC rateCardATFESC = rateCardATFESCDao.findByAgeAndTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term, chedat, chedat, chedat, chedat);
		System.out.println("age : "+age+" term : "+term+" BSA premium : "+premium+" paytrm : "+paytrm+" Rate : "+rateCardATFESC.getRate());
		lifpos = ((new BigDecimal(bassum).multiply(new BigDecimal(rateCardATFESC.getRate()))).divide(new BigDecimal("1000"))).divide(new BigDecimal(paytrm), 4, RoundingMode.DOWN);
		System.out.println("lifpos : "+lifpos.doubleValue()+" invpos : "+(premium-lifpos.doubleValue()));
		return lifpos;
	}


}
