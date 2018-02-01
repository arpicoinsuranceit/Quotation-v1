package org.arpicoinsurance.groupit.main.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.arpicoinsurance.groupit.main.common.BenifictCalculation;
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
import org.arpicoinsurance.groupit.main.dao.RateCardATFESCDao;
import org.arpicoinsurance.groupit.main.dao.RateCardENDDao;
import org.arpicoinsurance.groupit.main.dao.RateCardINVPDao;
import org.arpicoinsurance.groupit.main.dao.UsersDao;
import org.arpicoinsurance.groupit.main.helper.Benifict;
import org.arpicoinsurance.groupit.main.helper.Children;
import org.arpicoinsurance.groupit.main.helper.InvpSavePersonalInfo;
import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuoEndCalResp;
import org.arpicoinsurance.groupit.main.helper.QuoInvpCalResp;
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
import org.arpicoinsurance.groupit.main.model.RateCardEND;
import org.arpicoinsurance.groupit.main.model.RateCardINVP;
import org.arpicoinsurance.groupit.main.model.Users;
import org.arpicoinsurance.groupit.main.service.CalculateBenifictTermService;
import org.arpicoinsurance.groupit.main.service.ENDService;
import org.arpicoinsurance.groupit.main.service.OccupationLodingServce;
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
public class ENDServiceImpl implements ENDService {

	@Autowired
	private RateCardENDDao rateCardENDDao;
	
	private Double occupationValue = 1.0;

	ArrayList<Quo_Benef_Child_Details> childBenifList = new ArrayList<>();

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
	private CalculateBenifictTermService calculateBenefictTerm;
	
	@Autowired
	private OccupationLodingServce occupationLoding;
	
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
	private RateCardINVPDao rateCardINVPDao;

	@Autowired
	private RateCardATFESCDao rateCardATFESCDao;

	@Autowired
	private Quo_Benef_Child_DetailsDao quoBenifChildDetailsDao;
	
	@Override
	public QuoEndCalResp getCalcutatedEnd(QuotationCalculation quotationCalculation) throws Exception {
		Integer adultCount = 1;
		Integer childCount = 0;
		System.out.println(quotationCalculation.get_personalInfo().getMgender());

		CalculationUtils calculationUtils = null;
		BenifictCalculation benifictCalculation = null;
		try {

			QuoEndCalResp calResp = new QuoEndCalResp();
			calculationUtils = new CalculationUtils();
			benifictCalculation = new BenifictCalculation();
			/// Calculate Rebate Premium ///
			Double rebate = calculationUtils.getRebate(quotationCalculation.get_personalInfo().getFrequance());
			/// Calculate BSA Premium ///
			BigDecimal bsaPremium = calculateL2(quotationCalculation.get_personalInfo().getMocu(),quotationCalculation.get_personalInfo().getMage(),
					quotationCalculation.get_personalInfo().getTerm(), rebate, new Date(),
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
			/// SET VALUES TO QuoInvpCalResp ///

			if (_mRiders != null) {
				for (Benifict benifict : _mRiders) {
					adultCount = 1;
					if (benifict.getType().equals("HRB")) {
						if (quotationCalculation.get_personalInfo().getSage() != null
								&& quotationCalculation.get_personalInfo().getSgender() != null
								&& quotationCalculation.get_personalInfo().getSocu() != null) {
							if (_sRiders != null) {
								for (Benifict benifict2 : _sRiders) {
									if (benifict2.getType().equals("HRBS")) {
										adultCount += 1;
									}
								}
							}
						}

						if (quotationCalculation.get_personalInfo().getChildrens() != null
								&& quotationCalculation.get_personalInfo().getChildrens().size() > 0) {
							if (_cRiders != null) {
								for (Children children : quotationCalculation.get_personalInfo().getChildrens()) {
									if (children.is_cHrbc()) {
										childCount += 1;
									}
								}
							}
						}
					}
					
					Integer term = calculateBenefictTerm.calculateBenifictTerm(
							quotationCalculation.get_personalInfo().getMage(), benifict.getType(),
							quotationCalculation.get_personalInfo().getTerm());
					
					calculateBenifPremium(benifict.getType(), benifict.getSumAssured(),
							quotationCalculation.get_personalInfo().getMgender(),
							quotationCalculation.get_personalInfo().getMage(),
							quotationCalculation.get_personalInfo().getFrequance(), term, quotationCalculation.get_personalInfo().getMocu(), calResp,
							adultCount, childCount);
					
			
				}
			}

			//calculate spouse riders premium
			if (quotationCalculation.get_personalInfo().getSage() != null
					&& quotationCalculation.get_personalInfo().getSgender() != null
					&& quotationCalculation.get_personalInfo().getSocu() != null) {

				if (_sRiders != null) {

					for (Benifict benifict : _sRiders) {
						Integer term = calculateBenefictTerm.calculateBenifictTerm(
								quotationCalculation.get_personalInfo().getSage(), benifict.getType(),
								quotationCalculation.get_personalInfo().getTerm());
						calculateBenifPremium(benifict.getType(), benifict.getSumAssured(),
								quotationCalculation.get_personalInfo().getSgender(),
								quotationCalculation.get_personalInfo().getSage(),
								quotationCalculation.get_personalInfo().getFrequance(), term, quotationCalculation.get_personalInfo().getSocu(), calResp,
								adultCount, childCount);
					}
				}
			}

			//calculate children riders premium
			if (quotationCalculation.get_personalInfo().getChildrens() != null
					&& quotationCalculation.get_personalInfo().getChildrens().size() > 0) {
				for (Children children : quotationCalculation.get_personalInfo().getChildrens()) {
					if (_cRiders != null) {
						for (Benifict benifict : _cRiders) {
							Integer term = calculateBenefictTerm.calculateBenifictTerm(children.get_cAge(),
									benifict.getType(), quotationCalculation.get_personalInfo().getTerm());
							String benfName = benifict.getType();

							switch (benfName) {
							case "CIBC":
								if (children.is_cCibc()) {
									calculateBenifPremium(benifict.getType(), benifict.getSumAssured(),
											children.get_cTitle(), children.get_cAge(),
											quotationCalculation.get_personalInfo().getFrequance(), term,
											0, calResp, adultCount, childCount);
								}
								break;

							case "HBC":
								if (children.is_cHbc()) {
									calculateBenifPremium(benifict.getType(), benifict.getSumAssured(),
											children.get_cTitle(), children.get_cAge(),
											quotationCalculation.get_personalInfo().getFrequance(), term,
											0, calResp, adultCount, childCount);
								}
								break;

							case "SUHRBC":
								if (children.is_cSuhrbc()) {
									calculateBenifPremium(benifict.getType(), benifict.getSumAssured(),
											children.get_cTitle(), children.get_cAge(),
											quotationCalculation.get_personalInfo().getFrequance(), term,
											0, calResp, adultCount, childCount);
								}
								break;

							default:
								break;
							}
						}
					}
				}
			}

			calResp.setBasicSumAssured(bsaPremium.doubleValue());
			calResp.setGuaranteed(calculateMaturity(quotationCalculation.get_personalInfo().getTerm(),
					quotationCalculation.get_personalInfo().getBsa()).doubleValue());
			
			Double tot=calResp.getBasicSumAssured() + calResp.getAddBenif();
			Double adminFee = calculationUtils.getAdminFee(quotationCalculation.get_personalInfo().getFrequance());
			Double tax=calculationUtils.getTaxAmount(tot + adminFee);
			Double extraOE=adminFee + tax;
			calResp.setExtraOE(extraOE);
			calResp.setTotPremium(tot + extraOE);
			
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
	public BigDecimal calculateL2(int ocu,int age, int term, double rebate, Date chedat, double bassum, int paytrm)
			throws Exception {
		
		Occupation occupation = occupationDao.findByOcupationid(ocu);
		Benefits benefits= benefitsDao.findByRiderCode("L2");
		OcupationLoading ocupationLoading = occupationLodingDao.findByOccupationAndBenefits(occupation, benefits);
		
		
		Double rate=ocupationLoading.getValue();
		
		// TODO Auto-generated method stub
		System.out.println("END bassum : "+bassum+" age : "+age+" term : "+term+" paytrm : "+paytrm);
		BigDecimal premium = new BigDecimal(0);
		
		RateCardEND rateCardEND = rateCardENDDao.findByAgeAndTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term, chedat, chedat, chedat, chedat);
		System.out.println("rateCardEND : "+rateCardEND.getRate());
		
		// (((@rate@-(@rate@*@rebate@/100))/1000)*@sum_assured@)/@payment_frequency@
		premium = ((((new BigDecimal(rateCardEND.getRate()).subtract(((new BigDecimal(rateCardEND.getRate()).multiply(new BigDecimal(rebate))).divide(new BigDecimal(100), 6, RoundingMode.HALF_UP)))).divide(new BigDecimal(1000), 6, RoundingMode.HALF_UP)).multiply(new BigDecimal(bassum))).divide(new BigDecimal(paytrm), 10, RoundingMode.HALF_UP)).setScale(0, RoundingMode.HALF_UP);
					
		System.out.println("premium : "+premium.toString());
		return premium.multiply(new BigDecimal(rate));
	}

	@Override
	public BigDecimal calculateMaturity(int term, double bassum) throws Exception {
		// @sum_assured@ + ((@sum_assured@*0.025)*@term@)
		BigDecimal maturity = new BigDecimal(0);
		System.out.println("term : "+term+" bassum : "+bassum);
		maturity = (new BigDecimal(bassum).add(((new BigDecimal(bassum).multiply(new BigDecimal(0.025))).multiply(new BigDecimal(term))))).setScale(0, RoundingMode.HALF_UP);
		System.out.println("maturity : "+maturity.toString());
		return maturity;
	}


	Double calculateBenifPremium(String type, Double ridsumasu, String gender, Integer age, String payFrequency,
			Integer term, Integer occupation_id, QuoEndCalResp calResp, Integer adultCount, Integer childCount)
			throws Exception {
		
		Map<String, Double> oculoding = occupationLoding.getOccupationLoding(occupation_id);

		switch (type) {
		case "BSAS":
			
			System.out.println("callesssssssssssssssssssssssssssssssss");
			BigDecimal scb = scbService.calculateSCB(age, term, new Date(), ridsumasu, payFrequency, 1.0, oculoding.get("SCB"));
			calResp.setBsas(scb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + scb.doubleValue());
			calResp.setBsasTerm(term);
			break;
		case "ADB":
			BigDecimal adb = adbService.calculateADB(ridsumasu, payFrequency, 1.0, oculoding.get("ADB"));
			calResp.setAdb(adb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + adb.doubleValue());
			calResp.setAdbTerm(term);
			break;
		case "ADBS":
			BigDecimal adbs = adbsService.calculateADBS(ridsumasu, payFrequency, 1.0, oculoding.get("ADBS"));
			calResp.setAdbs(adbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + adbs.doubleValue());
			calResp.setAdbsTerm(term);
			break;
		case "ATPB":
			System.out.println(oculoding.get("ATPB")+" $$$$$$$$$$$$$");
			BigDecimal atpb = atpbService.calculateATPB(age, term, new Date(), ridsumasu, payFrequency, 1.0, 1);
			calResp.setAtpb(atpb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + atpb.doubleValue());
			calResp.setAtpbTerm(term);
			break;
		case "TPDASB":
			BigDecimal tpdasb = tpdasbService.calculateTPDASB(age, new Date(), ridsumasu, payFrequency, 1.0, oculoding.get("TPDASB"));
			calResp.setTpdasb(tpdasb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + tpdasb.doubleValue());
			calResp.setTpdasbTerm(term);
			break;
		case "TPDASBS":
			BigDecimal tpdasbs = tpdasbsbService.calculateTPDASBS(age, new Date(), ridsumasu, payFrequency, 1.0, oculoding.get("TPDASBS"));
			calResp.setTpdasbs(tpdasbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + tpdasbs.doubleValue());
			calResp.setTpdasbsTerm(term);
			break;
		case "TPDB":
			BigDecimal tpdb = tpdbService.calculateTPDB(ridsumasu, payFrequency, 1.0, oculoding.get("TPDB"));
			calResp.setTpdb(tpdb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + tpdb.doubleValue());
			calResp.setTpdbTerm(term);
			break;
		case "TPDBS":
			BigDecimal tpdbs = tpdbsService.calculateTPDBS(ridsumasu, payFrequency, 1.0, oculoding.get("TPDBS"));
			calResp.setTpdbs(tpdbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + tpdbs.doubleValue());
			calResp.setTpdbsTerm(term);
			break;
		case "PPDB":
			BigDecimal ppdb = ppdbService.calculatePPDB(ridsumasu, payFrequency, 1.0, oculoding.get("PPDB"));
			calResp.setPpdb(ppdb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + ppdb.doubleValue());
			calResp.setPpdbTerm(term);
			break;
		case "PPDBS":
			BigDecimal ppdbs = ppdbsService.calculatePPDBS(ridsumasu, payFrequency, 1.0, oculoding.get("PPDBS"));
			calResp.setPpdbs(ppdbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + ppdbs.doubleValue());
			calResp.setPpdbsTerm(term);
			break;
		case "CIB":
			BigDecimal cib = cibService.calculateCIB(age, term, new Date(), ridsumasu, payFrequency, 1.0, oculoding.get("CIB"));
			calResp.setCib(cib.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + cib.doubleValue());
			calResp.setCibTerm(term);
			break;
		case "CIBS":
			BigDecimal scib = scibService.calculateSCIB(age, term, new Date(), ridsumasu, payFrequency, 1.0, oculoding.get("SCIB"));
			calResp.setCibs(scib.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + scib.doubleValue());
			calResp.setCibsTerm(term);
			break;
		case "CIBC":
			// ** 21-age < term term = 21-age else term
			BigDecimal cibc = cibcService.calculateCIBC(age, term > (21 - 6) ? (21 - 6) : term, new Date(), ridsumasu,
					payFrequency, 1.0);
			calResp.setCibc(calResp.getCibc() + cibc.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + cibc.doubleValue());
			calResp.setCibcTerm(term);
			break;
		case "FEB":
			BigDecimal feb = febService.calculateFEB(age, term, new Date(), ridsumasu, payFrequency, 1.0, oculoding.get("FEB"));
			calResp.setFeb(feb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + feb.doubleValue());
			calResp.setFebTerm(term);
			break;
		case "FEBS":
			BigDecimal febs = febsService.calculateFEBS(age, term, new Date(), ridsumasu, payFrequency, 1.0, oculoding.get("FEBS"));
			calResp.setFebs(febs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + febs.doubleValue());
			calResp.setFebsTerm(term);
			break;

		case "MFIBD":
			BigDecimal mfibd = mfibdService.calculateMFIBD(age, term, new Date(), ridsumasu, payFrequency, 1.0, oculoding.get("MFIBD"));
			calResp.setMifdb(mfibd.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + mfibd.doubleValue());
			calResp.setMifdbTerm(term);
			break;
		case "MFIBT":
			BigDecimal mfibt = mfibtService.calculateMFIBT(age, term, new Date(), ridsumasu, payFrequency, 1.0, oculoding.get("MFIBT"));
			calResp.setMifdt(mfibt.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + mfibt.doubleValue());
			calResp.setMifdtTerm(term);
			break;
		case "MFIBDT":
			BigDecimal mfibdt = mfibdtService.calculateMFIBDT(age, term, new Date(), ridsumasu, payFrequency, 1.0, oculoding.get("MFIBDT"));
			calResp.setMifdbt(mfibdt.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + mfibdt.doubleValue());
			calResp.setMifdbtTerm(term);
			break;
		case "HRB":
			BigDecimal hrb = hrbService.calculateHRB(age, gender, ridsumasu, adultCount, childCount, new Date(),
					payFrequency, 1.0, oculoding.get("HRB"));
			calResp.setHrb(hrb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + hrb.doubleValue());
			calResp.setHrbTerm(term);
			break;
		case "SUHRB":
			BigDecimal suhrb = suhrbService.calculateSUHRB(age, gender, term, ridsumasu, new Date(), payFrequency, 1.0, oculoding.get("SUHRB"));
			calResp.setSuhrb(suhrb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + suhrb.doubleValue());
			calResp.setSuhrbTerm(term);
			break;
		case "SUHRBS":
			BigDecimal suhrbs = suhrbsService.calculateSUHRBS(age, gender, term, ridsumasu, new Date(), payFrequency,
					1.0, oculoding.get("SUHRBS"));
			calResp.setSuhrbs(suhrbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + suhrbs.doubleValue());
			calResp.setSuhrbsTerm(term);
			break;
		case "SUHRBC":
			BigDecimal suhrbc = suhrbcService.calculateSUHRBC(age, gender, term, ridsumasu, new Date(), payFrequency,
					1.0);
			calResp.setSuhrbc(calResp.getSuhrbc() + suhrbc.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + suhrbc.doubleValue());
			calResp.setSuhrbcTerm(term);
			break;
		case "HB":
			BigDecimal hb = hbService.calculateHB(age, term, new Date(), ridsumasu, payFrequency, 1.0, oculoding.get("HB"));
			calResp.setHb(hb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + hb.doubleValue());
			calResp.setHbTerm(term);
			break;
		case "HBS":
			BigDecimal hbs = hbsService.calculateHBS(28, term, new Date(), ridsumasu, payFrequency, 1.0, oculoding.get("HBS"));
			calResp.setHbs(hbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + hbs.doubleValue());
			calResp.setHbsTerm(term);
			break;
		case "HBC":
			// ** 21-age < term term = 21-age else term
			BigDecimal hbc = hbcService.calculateHBC(term > (21 - 6) ? (21 - 6) : term, new Date(), ridsumasu,
					payFrequency, 1.0);
			calResp.setHbc(calResp.getHbc() + hbc.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + hbc.doubleValue());
			calResp.setHbcTerm(term);
			break;
		case "WPB":
			BigDecimal wpb = wpbService.calculateWPB(calResp);
			calResp.setWpb(wpb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + wpb.doubleValue());
			calResp.setWpbTerm(term);
			break;
		case "WPBS":
			BigDecimal wpbs = wpbsService.calculateWPBS(calResp);
			calResp.setWpbs(wpbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + wpbs.doubleValue());
			calResp.setWpbsTerm(term);
			break;

		default:
			break;
		}

		return 0.0;
	}

	//save quotation
	@Override
	public String saveQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation, Integer id)
			throws Exception {

		QuoEndCalResp calResp = getCalcutatedEnd(calculation);
		Products products = productDao.findByProductCode("END1");
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

	/////////////////////////////////////// Additional Methods
	/////////////////////////////////////// ////////////////////////////

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
					child.setChildRelation(children.get_cTitle() == "F" ? "Daughter" : "Son");

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

	private QuotationDetails getQuotationDetail(QuoEndCalResp calResp, QuotationCalculation calculation)
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
			quotationDetails.setLifePos(0.00);
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

	private ArrayList<Quo_Benef_Details> getBenifDetails(RiderDetails get_riderDetails, QuoEndCalResp calResp,
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
