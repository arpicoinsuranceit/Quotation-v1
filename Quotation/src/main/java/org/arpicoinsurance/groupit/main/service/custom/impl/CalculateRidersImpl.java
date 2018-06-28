package org.arpicoinsurance.groupit.main.service.custom.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.arpicoinsurance.groupit.main.dao.BenefitsDao;
import org.arpicoinsurance.groupit.main.dao.RateCardARPDao;
import org.arpicoinsurance.groupit.main.dao.RateCardARTMDeathDao;
import org.arpicoinsurance.groupit.main.dao.RateCardATFESCDao;
import org.arpicoinsurance.groupit.main.dao.RateCardCIBCDao;
import org.arpicoinsurance.groupit.main.dao.RateCardCIBDao;
import org.arpicoinsurance.groupit.main.dao.RateCardHBCDao;
import org.arpicoinsurance.groupit.main.dao.RateCardHBDao;
import org.arpicoinsurance.groupit.main.dao.RateCardHRBFDao;
import org.arpicoinsurance.groupit.main.dao.RateCardHRBIDao;
import org.arpicoinsurance.groupit.main.dao.RateCardJLBDao;
import org.arpicoinsurance.groupit.main.dao.RateCardMFIBDDao;
import org.arpicoinsurance.groupit.main.dao.RateCardMFIBDTDao;
import org.arpicoinsurance.groupit.main.dao.RateCardMFIBTDao;
import org.arpicoinsurance.groupit.main.dao.RateCardSFPODao;
import org.arpicoinsurance.groupit.main.dao.RateCardSUHRBDao;
import org.arpicoinsurance.groupit.main.dao.RateCardTPDASBDao;
import org.arpicoinsurance.groupit.main.dao.RateCardTPDDTADao;
import org.arpicoinsurance.groupit.main.dao.RateCardTPDDTASDao;
import org.arpicoinsurance.groupit.main.helper.Benifict;
import org.arpicoinsurance.groupit.main.helper.Children;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.helper.Spouse;
import org.arpicoinsurance.groupit.main.model.RateCardARP;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.service.CalculateBenifictTermService;
import org.arpicoinsurance.groupit.main.service.OccupationLodingServce;
import org.arpicoinsurance.groupit.main.service.custom.CalculateRiders;
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
import org.arpicoinsurance.groupit.main.service.rider.HRBFService;
import org.arpicoinsurance.groupit.main.service.rider.HRBIService;
import org.arpicoinsurance.groupit.main.service.rider.JLBPLService;
import org.arpicoinsurance.groupit.main.service.rider.JLBService;
import org.arpicoinsurance.groupit.main.service.rider.L2Service;
import org.arpicoinsurance.groupit.main.service.rider.MFIBDService;
import org.arpicoinsurance.groupit.main.service.rider.MFIBDTService;
import org.arpicoinsurance.groupit.main.service.rider.MFIBTService;
import org.arpicoinsurance.groupit.main.service.rider.PPDBSService;
import org.arpicoinsurance.groupit.main.service.rider.PPDBService;
import org.arpicoinsurance.groupit.main.service.rider.SCBService;
import org.arpicoinsurance.groupit.main.service.rider.SCIBService;
import org.arpicoinsurance.groupit.main.service.rider.SFPOService;
import org.arpicoinsurance.groupit.main.service.rider.SHCBFService;
import org.arpicoinsurance.groupit.main.service.rider.SHCBIService;
import org.arpicoinsurance.groupit.main.service.rider.SUHRBCService;
import org.arpicoinsurance.groupit.main.service.rider.SUHRBSService;
import org.arpicoinsurance.groupit.main.service.rider.SUHRBService;
import org.arpicoinsurance.groupit.main.service.rider.TPDASBSService;
import org.arpicoinsurance.groupit.main.service.rider.TPDASBService;
import org.arpicoinsurance.groupit.main.service.rider.TPDBSService;
import org.arpicoinsurance.groupit.main.service.rider.TPDBService;
import org.arpicoinsurance.groupit.main.service.rider.TPDDTAPLService;
import org.arpicoinsurance.groupit.main.service.rider.TPDDTASPLService;
import org.arpicoinsurance.groupit.main.service.rider.TPDDTASService;
import org.arpicoinsurance.groupit.main.service.rider.TPDDTAService;
import org.arpicoinsurance.groupit.main.service.rider.WPBSService;
import org.arpicoinsurance.groupit.main.service.rider.WPBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CalculateRidersImpl implements CalculateRiders {

	@Autowired
	private SCBService scbService;

	@Autowired 
	private L2Service l2service;
	@Autowired
	private RateCardARPDao rateCardARPDao;

	@Autowired
	private ATPBService atpbService;

	@Autowired
	private ADBService adbService;

	@Autowired
	private ADBSService adbsService;

	@Autowired
	private SFPOService sfpoService;

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

	// @Autowired
	// private HRBService hrbService;

	@Autowired
	private HRBFService hrbfService;

	@Autowired
	private HRBIService hrbiService;

	@Autowired
	private SUHRBService suhrbService;

	@Autowired
	private SUHRBSService suhrbsService;

	@Autowired
	private SUHRBCService suhrbcService;

	@Autowired
	private SHCBFService shcbfService;

	@Autowired
	private SHCBIService shcbiService;

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
	private JLBService jlbService;

	@Autowired
	private JLBPLService jlbplService;

	@Autowired
	private TPDDTAService tpddtaService;

	@Autowired
	private TPDDTAPLService tpddtaplService;

	@Autowired
	private TPDDTASService tpddtasService;

	@Autowired
	private TPDDTASPLService tpddtasplService;

	@Autowired
	private OccupationLodingServce occupationLoding;

	@Autowired
	private CalculateBenifictTermService calculateBenefictTerm;

	@Autowired
	private RateCardATFESCDao rateCardATFESCDao;

	@Autowired
	private RateCardTPDASBDao rateCardTPDASBDao;

	@Autowired
	private RateCardSFPODao rateCardSFPODao;

	@Autowired
	private RateCardCIBDao rateCardCIBDao;

	@Autowired
	private RateCardCIBCDao rateCardCIBCDao;

	@Autowired
	private RateCardMFIBDDao rateCardMFIBDDao;
	
	@Autowired
	private RateCardARTMDeathDao rateCardArtmDeathDao;

	@Autowired
	private RateCardMFIBTDao rateCardMFIBTDao;

	@Autowired
	private RateCardMFIBDTDao rateCardMFIBDTDao;

	@Autowired
	private RateCardSUHRBDao rateCardSUHRBDao;

	@Autowired
	private RateCardHBDao rateCardHBDao;

	@Autowired
	private RateCardHBCDao rateCardHBCDao;

	@Autowired
	private RateCardJLBDao rateCardJLBDao;

	@Autowired
	private RateCardTPDDTADao rateCardTPDDTADao;

	@Autowired
	private RateCardTPDDTASDao rateCardTPDDTASDao;

	@Autowired
	private RateCardHRBFDao rateCardHRBFDao;

	@Autowired
	private RateCardHRBIDao rateCardHRBIDao;
	
	@Autowired
	private BenefitsDao benefictDao;

	@Override
	public QuotationQuickCalResponse getRiders(QuotationCalculation quotationCalculation,
			QuotationQuickCalResponse calResp) throws Exception {
		Integer adultCount = 1;
		Integer childCount = 0;

		Double inrate = 0.0;
		if (quotationCalculation.get_personalInfo().getIntrate() != null
				&& quotationCalculation.get_personalInfo().getIntrate() > 0) {
			inrate = quotationCalculation.get_personalInfo().getIntrate();
		}

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

		if (_mRiders != null) {
			adultCount = 1;
			for (Benifict benifict : _mRiders) {

				if (benifict.getType().equals("HRBF")) {

					if (quotationCalculation.get_personalInfo().getSage() != null
							&& quotationCalculation.get_personalInfo().getSgenger() != null
							&& quotationCalculation.get_personalInfo().getSocu() != null) {
						if (_sRiders != null) {
							for (Benifict benifict2 : _sRiders) {
								if (benifict2.getType().equals("HRBFS")) {
									adultCount += 1;
									// System.out.println(adultCount);
								}
							}
						}
					}

					if (quotationCalculation.get_personalInfo().getChildrens() != null
							&& quotationCalculation.get_personalInfo().getChildrens().size() > 0) {
						if (_cRiders != null) {
							for (Children children : quotationCalculation.get_personalInfo().getChildrens()) {
								if (children.is_cHrbfc()) {
									childCount += 1;
								}
							}
						}
					}
				}

				calResp = calculateMainlifeRiders(quotationCalculation.get_personalInfo().getMage(), benifict.getType(),
						quotationCalculation.get_personalInfo().getTerm(), benifict.getSumAssured(),
						quotationCalculation.get_personalInfo().getMgenger(),
						quotationCalculation.get_personalInfo().getFrequance(),
						quotationCalculation.get_personalInfo().getMocu(), calResp, adultCount, childCount, inrate, quotationCalculation.get_product());

			}
		}

		if (quotationCalculation.get_personalInfo().getSage() != null
				&& quotationCalculation.get_personalInfo().getSgenger() != null
				&& quotationCalculation.get_personalInfo().getSocu() != null) {

			if (_sRiders != null) {

				for (Benifict benifict : _sRiders) {
					// System.out.println(quotationCalculation.get_personalInfo().getTerm()
					// + "?????????????????????????-------------------");
					calResp = calculateMainlifeRiders(quotationCalculation.get_personalInfo().getSage(),
							benifict.getType(), quotationCalculation.get_personalInfo().getTerm(),
							benifict.getSumAssured(), quotationCalculation.get_personalInfo().getSgenger(),
							quotationCalculation.get_personalInfo().getFrequance(),
							quotationCalculation.get_personalInfo().getSocu(), calResp, adultCount, childCount, inrate, quotationCalculation.get_product());

				}
			}
		}
		if (quotationCalculation.get_personalInfo().getChildrens() != null
				&& quotationCalculation.get_personalInfo().getChildrens().size() > 0) {
			for (Children children : quotationCalculation.get_personalInfo().getChildrens()) {
				// System.out.println(children.get_cTitle() +
				// "?????????????????????????????????????????????? title");
				if (_cRiders != null) {
					for (Benifict benifict : _cRiders) {
						Integer term = quotationCalculation.get_personalInfo().getTerm();
						// System.out.println("product :" + quotationCalculation.get_product());
						if (quotationCalculation.get_product().equals("ARP")) {
							Integer maxterm = calculateBenefictTerm.calculateChildBenifictTermARP(children.get_cAge(),
									benifict.getType(), quotationCalculation.get_personalInfo().getTerm(),
									quotationCalculation.get_personalInfo().getPayingterm());
							
							
							
							Integer valiedTerm = maxterm > term ? term : maxterm;
							term = valiedTerm;
							
							
							if (term < 5) {
								calResp.setErrorExist(true);
								calResp.setError(
										"Can't get benifict fof child because 21 - ( Child Age + Pay Term) must be greate than 5");
								return calResp;
							}
						} else {
							Integer maxterm = calculateBenefictTerm.calculateBenifictTerm(children.get_cAge(), benifict.getType(),
									quotationCalculation.get_personalInfo().getTerm());

							Integer valiedTerm = maxterm > term ? term : maxterm;
							term = valiedTerm;
						}

						String benfName = benifict.getType();

						// System.out.println(term + ";;;;;;;;;;;;;;;;;; child");

						switch (benfName) {
						case "CIBC":
							if (children.is_cCibc()) {
								// System.out.println(children.get_cTitle()
								// + "MALE FEMALE ???????????????????????????????????????????????????????????");
								calculateBenifPremium(benifict.getType(), benifict.getSumAssured(),
										children.get_cTitle(), children.get_cAge(),
										quotationCalculation.get_personalInfo().getFrequance(), term, 0, calResp,
										adultCount, childCount, -1.0, -1.0, quotationCalculation.get_product());
							}
							break;

						case "HBC":
							System.out.println("HBC : " + term);
							if (children.is_cHbc()) {
								calculateBenifPremium(benifict.getType(), benifict.getSumAssured(),
										children.get_cTitle(), children.get_cAge(),
										quotationCalculation.get_personalInfo().getFrequance(), term, 0, calResp,
										adultCount, childCount, -1.0, -1.0, quotationCalculation.get_product());
							}
							break;
						/*
						 * case "SUHRBC": if (children.is_cSuhrbc()) {
						 * calculateBenifPremium(benifict.getType(), benifict.getSumAssured(),
						 * children.get_cTitle(), children.get_cAge(),
						 * quotationCalculation.get_personalInfo().getFrequance(), term, 0, calResp,
						 * adultCount, childCount, -1.0, -1.0); } break;
						 */
						case "SUHRBC":
							if (children.is_cSuhrbc()) {
								calculateBenifPremium(benifict.getType(), benifict.getSumAssured(),
										children.get_cTitle(), children.get_cAge(),
										quotationCalculation.get_personalInfo().getFrequance(), term, 0, calResp,
										adultCount, childCount, -1.0, -1.0, quotationCalculation.get_product());
							}
							break;
						case "HRBIC":
							if (children.is_cHrbic()) {
								calculateBenifPremium(benifict.getType(), benifict.getSumAssured(),
										children.get_cTitle(), children.get_cAge(),
										quotationCalculation.get_personalInfo().getFrequance(), term, 0, calResp,
										adultCount, childCount, -1.0, -1.0, quotationCalculation.get_product());
							}
							break;

						default:
							break;
						}
					}
				}
			}
		}
		return calResp;
	}

	@Override
	public QuotationQuickCalResponse calculateMainlifeRiders(Integer age, String type, Integer payTerm, Double bsa,
			String gender, String frequance, Integer ocu, QuotationQuickCalResponse calResp, Integer adultCount,
			Integer childCount, Double inrate, String productCode) throws Exception {

		System.out.println(age);
		
		Integer term = calculateBenefictTerm.calculateBenifictTerm(age, type, payTerm);

		calculateBenifPremium(type, bsa, gender, age, frequance, term, ocu, calResp, adultCount, childCount, bsa,
				inrate, productCode);

		return calResp;
	}

	@Override
	public QuotationQuickCalResponse calculateBenifPremium(String type, Double ridsumasu, String gender, Integer age,
			String payFrequency, Integer term, Integer occupation_id, QuotationQuickCalResponse calResp,
			Integer adultCount, Integer childCount, Double loan, Double inRate, String productCode) throws Exception {

		// System.out.println(occupation_id + " ////////////// ocu ID");

		System.out.println(term);
		
		Map<String, Double> oculoding = occupationLoding.getOccupationLoding(occupation_id);

		Double relife = 1.0;

		Double ocuLoading = 1.0;
		switch (type) {
		
		case "L2":
			System.out.println("L2");
			if(productCode.equalsIgnoreCase("ARTM")) {
				
				System.out.println("call L2");
				
				if(benefictDao.findByRiderCode("L2").getActive() == 0) {
					calResp.setErrorExist(true);
					calResp.setError("L2 under Maintenance, Please untick or reload page");
					return calResp;
				}
				
				ocuLoading = oculoding.get("L2");
				if (ocuLoading == null)
					ocuLoading = 1.0;

				//Integer maxTermToBenefictL2 = rateCardArtmDeathDao.findFirstByOrderByTermDesc().getTerm();
				Integer valiedTermL2 =  term;

				
				BigDecimal l2 = l2service.calculateL2(ridsumasu, valiedTermL2, age, payFrequency, ocuLoading);

				calResp = setLodingDetails(ocuLoading, l2.doubleValue(), calResp);

				if(!(l2.doubleValue() > 0)) {
					calResp.setErrorExist(true);
					calResp.setError("L2 Premium going low");
					return calResp;
				}
				
				calResp.setL2(l2.doubleValue());
				calResp.setL2Sum(ridsumasu);
				calResp.setAddBenif(calResp.getAddBenif() + l2.doubleValue());
				calResp.setL2term(valiedTermL2);
			}
			
			return calResp;
		
		
		case "BSAS":
			
			if(benefictDao.findByRiderCode("SCB").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("SCB under Maintenance, Please untick or reload page");
				return calResp;
			}
			
			ocuLoading = oculoding.get("SCB");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictSCB = rateCardATFESCDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermSCB = maxTermToBenefictSCB > term ? term : maxTermToBenefictSCB;

			if (calResp.isArp()) {
				RateCardARP rateCardARP = rateCardARPDao
						.findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age,
								valiedTermSCB, calResp.getPayTerm(), new Date(), new Date(), new Date(), new Date());
				relife = rateCardARP.getRate();
			}

			// System.out.println(relife + ": relife");

			BigDecimal scb = scbService.calculateSCB(age, valiedTermSCB, new Date(), ridsumasu, payFrequency, relife,
					ocuLoading);

			if(!(scb.doubleValue() > 0)) {
				calResp.setErrorExist(true);
				calResp.setError("SCB Premium going low");
				return calResp;
			}
			
			calResp = setLodingDetails(ocuLoading, scb.doubleValue(), calResp);

			calResp.setBsas(scb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + scb.doubleValue());
			calResp.setBsasTerm(valiedTermSCB);
			return calResp;

		case "ADB":
			if(benefictDao.findByRiderCode("ADB").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("ADB under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("ADB");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			if (calResp.isArp()) {
				RateCardARP rateCardARP = rateCardARPDao
						.findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term,
								calResp.getPayTerm(), new Date(), new Date(), new Date(), new Date());
				relife = rateCardARP.getRate();
			}

			// System.out.println(relife + ": relife");

			BigDecimal adb = adbService.calculateADB(ridsumasu, payFrequency, relife, ocuLoading);

			if(!(adb.doubleValue() > 0)) {
				calResp.setErrorExist(true);
				calResp.setError("ADB Premium going low");
				return calResp;
			}
			
			calResp = setLodingDetails(ocuLoading, adb.doubleValue(), calResp);

			calResp.setAdb(adb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + adb.doubleValue());
			calResp.setAdbTerm(term);
			return calResp;

		case "SFPO":
			if(benefictDao.findByRiderCode("SFPO").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("SFPO under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("SFPO");
			if (ocuLoading == null)
				ocuLoading = 1.0;
			Integer maxTermToBenefictSFPO = rateCardSFPODao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermSFPO = maxTermToBenefictSFPO > term ? term : maxTermToBenefictSFPO;

			BigDecimal sfpo = sfpoService.calculateSFPO(age, valiedTermSFPO, new Date(), ridsumasu, payFrequency, 1.0,
					ocuLoading);
			
			if(!(sfpo.doubleValue() > 0)) {
				calResp.setErrorExist(true);
				calResp.setError("SFPO Premium going low");
				return calResp;
			}
			
			calResp = setLodingDetails(ocuLoading, sfpo.doubleValue(), calResp);
			calResp.setSfpo(sfpo.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + sfpo.doubleValue());
			calResp.setSfpoTerm(valiedTermSFPO);
			return calResp;

		case "ADBS":
			if(benefictDao.findByRiderCode("ADBS").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("ADBS under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("ADBS");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			if (calResp.isArp()) {
				RateCardARP rateCardARP = rateCardARPDao
						.findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term,
								calResp.getPayTerm(), new Date(), new Date(), new Date(), new Date());
				relife = rateCardARP.getRate();
			}

			// System.out.println(relife + ": relife");

			BigDecimal adbs = adbsService.calculateADBS(ridsumasu, payFrequency, relife, ocuLoading);
			
			if(!(adbs.doubleValue() > 0)) {
				calResp.setErrorExist(true);
				calResp.setError("ADBS Premium going low");
				return calResp;
			}
			
			calResp = setLodingDetails(ocuLoading, adbs.doubleValue(), calResp);
			calResp.setAdbs(adbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + adbs.doubleValue());
			calResp.setAdbsTerm(term);
			return calResp;

		case "ATPB":
			// System.out.println("called/////////////////////////////////////////////////////");
			// System.out.println(ridsumasu);
			
			if(benefictDao.findByRiderCode("ATPB").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("ATPB under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("ATPB");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictATPB = rateCardATFESCDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermATPB = maxTermToBenefictATPB > term ? term : maxTermToBenefictATPB;

			if (calResp.isArp()) {
				RateCardARP rateCardARP = rateCardARPDao
						.findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age,
								valiedTermATPB, calResp.getPayTerm(), new Date(), new Date(), new Date(), new Date());
				relife = rateCardARP.getRate();
			}

			// System.out.println(relife + ": relife");

			BigDecimal atpb = atpbService.calculateATPB(age, valiedTermATPB, new Date(), ridsumasu, payFrequency,
					relife, ocuLoading);
			
			if(!(atpb.doubleValue() > 0)) {
				calResp.setErrorExist(true);
				calResp.setError("ATPB Premium going low");
				return calResp;
			}
			
			calResp = setLodingDetails(ocuLoading, atpb.doubleValue(), calResp);
			calResp.setAtpb(atpb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + atpb.doubleValue());
			calResp.setAtpbTerm(valiedTermATPB);
			return calResp;

		case "TPDASB":
			if(benefictDao.findByRiderCode("TPDASB").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("TPDASB under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("TPDASB");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictTPDASB = rateCardTPDASBDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermTPDASB = maxTermToBenefictTPDASB > term ? term : maxTermToBenefictTPDASB;

			if (calResp.isArp()) {
				RateCardARP rateCardARP = rateCardARPDao
						.findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age,
								valiedTermTPDASB, calResp.getPayTerm(), new Date(), new Date(), new Date(), new Date());
				relife = rateCardARP.getRate();
			}

			// System.out.println(relife + ": relife");

			BigDecimal tpdasb = tpdasbService.calculateTPDASB(age, valiedTermTPDASB, new Date(), ridsumasu,
					payFrequency, relife, ocuLoading);
			
			if(!(tpdasb.doubleValue() > 0)) {
				calResp.setErrorExist(true);
				calResp.setError("TPDASB Premium going low");
				return calResp;
			}
			
			calResp = setLodingDetails(ocuLoading, tpdasb.doubleValue(), calResp);
			calResp.setTpdasb(tpdasb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + tpdasb.doubleValue());
			calResp.setTpdasbTerm(valiedTermTPDASB);
			return calResp;

		case "TPDASBS":
			if(benefictDao.findByRiderCode("TPDASBS").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("TPDASBS under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("TPDASBS");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictTPDASBS = rateCardTPDASBDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermTPDASBS = maxTermToBenefictTPDASBS > term ? term : maxTermToBenefictTPDASBS;

			if (calResp.isArp()) {
				RateCardARP rateCardARP = rateCardARPDao
						.findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age,
								valiedTermTPDASBS, calResp.getPayTerm(), new Date(), new Date(), new Date(),
								new Date());
				relife = rateCardARP.getRate();
			}

			// System.out.println(relife + ": relife");

			BigDecimal tpdasbs = tpdasbsbService.calculateTPDASBS(age, valiedTermTPDASBS, new Date(), ridsumasu,
					payFrequency, relife, ocuLoading);
			
			if(!(tpdasbs.doubleValue() > 0)) {
				calResp.setErrorExist(true);
				calResp.setError("TPDASBS Premium going low");
				return calResp;
			}
			
			calResp = setLodingDetails(ocuLoading, tpdasbs.doubleValue(), calResp);
			calResp.setTpdasbs(tpdasbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + tpdasbs.doubleValue());
			calResp.setTpdasbsTerm(valiedTermTPDASBS);
			return calResp;

		case "TPDB":
			if(benefictDao.findByRiderCode("TPDB").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("TPDB under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("TPDB");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			if (calResp.isArp()) {
				RateCardARP rateCardARP = rateCardARPDao
						.findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term,
								calResp.getPayTerm(), new Date(), new Date(), new Date(), new Date());
				relife = rateCardARP.getRate();
			}

			// System.out.println(relife + ": relife");

			BigDecimal tpdb = tpdbService.calculateTPDB(ridsumasu, payFrequency, relife, ocuLoading);
			
			if(!(tpdb.doubleValue() > 0)) {
				calResp.setErrorExist(true);
				calResp.setError("TPDB Premium going low");
				return calResp;
			}
			
			calResp = setLodingDetails(ocuLoading, tpdb.doubleValue(), calResp);
			calResp.setTpdb(tpdb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + tpdb.doubleValue());
			calResp.setTpdbTerm(term);
			return calResp;

		case "TPDBS":
			if(benefictDao.findByRiderCode("TPDBS").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("TPDBS under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("TPDBS");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			if (calResp.isArp()) {
				RateCardARP rateCardARP = rateCardARPDao
						.findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term,
								calResp.getPayTerm(), new Date(), new Date(), new Date(), new Date());
				relife = rateCardARP.getRate();
			}

			// System.out.println(relife + ": relife");

			BigDecimal tpdbs = tpdbsService.calculateTPDBS(ridsumasu, payFrequency, relife, ocuLoading);
			
			if(!(tpdbs.doubleValue() > 0)) {
				calResp.setErrorExist(true);
				calResp.setError("TPDBS Premium going low");
				return calResp;
			}
			
			calResp = setLodingDetails(ocuLoading, tpdbs.doubleValue(), calResp);
			calResp.setTpdbs(tpdbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + tpdbs.doubleValue());
			calResp.setTpdbsTerm(term);
			return calResp;

		case "PPDB":
			
			if(benefictDao.findByRiderCode("PPDB").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("PPDB under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("PPDB");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			if (calResp.isArp()) {
				RateCardARP rateCardARP = rateCardARPDao
						.findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term,
								calResp.getPayTerm(), new Date(), new Date(), new Date(), new Date());
				relife = rateCardARP.getRate();
			}

			// System.out.println(relife + ": relife");

			BigDecimal ppdb = ppdbService.calculatePPDB(ridsumasu, payFrequency, relife, ocuLoading);
			
			if(!(ppdb.doubleValue() > 0)) {
				calResp.setErrorExist(true);
				calResp.setError("PPDB Premium going low");
				return calResp;
			}
			
			calResp = setLodingDetails(ocuLoading, ppdb.doubleValue(), calResp);
			calResp.setPpdb(ppdb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + ppdb.doubleValue());
			calResp.setPpdbTerm(term);
			return calResp;
		case "PPDBS":
			if(benefictDao.findByRiderCode("PPDBS").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("PPDBS under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("PPDBS");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			if (calResp.isArp()) {
				RateCardARP rateCardARP = rateCardARPDao
						.findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term,
								calResp.getPayTerm(), new Date(), new Date(), new Date(), new Date());
				relife = rateCardARP.getRate();
			}

			// System.out.println(relife + ": relife");

			BigDecimal ppdbs = ppdbsService.calculatePPDBS(ridsumasu, payFrequency, relife, ocuLoading);
			
			if(!(ppdbs.doubleValue() > 0)) {
				calResp.setErrorExist(true);
				calResp.setError("PPDBS Premium going low");
				return calResp;
			}
			
			calResp = setLodingDetails(ocuLoading, ppdbs.doubleValue(), calResp);
			calResp.setPpdbs(ppdbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + ppdbs.doubleValue());
			calResp.setPpdbsTerm(term);
			return calResp;
		case "CIB":
			if(benefictDao.findByRiderCode("CIB").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("CIB under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("CIB");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictCIB = rateCardCIBDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermCIB = maxTermToBenefictCIB > term ? term : maxTermToBenefictCIB;

			if (calResp.isArp()) {
				RateCardARP rateCardARP = rateCardARPDao
						.findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age,
								valiedTermCIB, calResp.getPayTerm(), new Date(), new Date(), new Date(), new Date());
				relife = rateCardARP.getRate();
			}

			// System.out.println(relife + ": relife");

			BigDecimal cib = cibService.calculateCIB(age, valiedTermCIB, new Date(), ridsumasu, payFrequency, relife,
					ocuLoading);
			
			if(!(cib.doubleValue() > 0)) {
				calResp.setErrorExist(true);
				calResp.setError("CIB Premium going low");
				return calResp;
			}
			
			calResp = setLodingDetails(ocuLoading, cib.doubleValue(), calResp);
			calResp.setCib(cib.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + cib.doubleValue());
			calResp.setCibTerm(valiedTermCIB);
			return calResp;
		case "CIBS":
			if(benefictDao.findByRiderCode("SCIB").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("SCIB under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("SCIB");
			if (ocuLoading == null)
				ocuLoading = 1.0;
			Integer maxTermToBenefictCIBS = rateCardCIBDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermCIBS = maxTermToBenefictCIBS > term ? term : maxTermToBenefictCIBS;

			if (calResp.isArp()) {
				RateCardARP rateCardARP = rateCardARPDao
						.findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age,
								valiedTermCIBS, calResp.getPayTerm(), new Date(), new Date(), new Date(), new Date());
				relife = rateCardARP.getRate();
			}

			// System.out.println(relife + ": relife");

			BigDecimal scib = scibService.calculateSCIB(age, valiedTermCIBS, new Date(), ridsumasu, payFrequency,
					relife, ocuLoading);
			
			if(!(scib.doubleValue() > 0)) {
				calResp.setErrorExist(true);
				calResp.setError("SCIB Premium going low");
				return calResp;
			}
			
			calResp = setLodingDetails(ocuLoading, scib.doubleValue(), calResp);
			calResp.setCibs(scib.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + scib.doubleValue());
			calResp.setCibsTerm(valiedTermCIBS);
			return calResp;

		case "CIBC":
			if(benefictDao.findByRiderCode("CIBC").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("CIBC under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("CIBC");
			if (ocuLoading == null)
				ocuLoading = 1.0;
			// ** 21-age < term term = 21-age else term

			term = term > (21 - age) ? (21 - age) : term;
			Integer maxTermToBenefictCIBC = rateCardCIBCDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermCIBC = maxTermToBenefictCIBC > term ? term : maxTermToBenefictCIBC;

			if (calResp.isArp()) {
				RateCardARP rateCardARP = rateCardARPDao
						.findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age,
								valiedTermCIBC, calResp.getPayTerm(), new Date(), new Date(), new Date(), new Date());
				relife = rateCardARP.getRate();
			}

			// System.out.println(relife + ": relife");

			BigDecimal cibc = cibcService.calculateCIBC(age, valiedTermCIBC, new Date(), ridsumasu, payFrequency,
					relife);
			
			if(!(cibc.doubleValue() > 0)) {
				calResp.setErrorExist(true);
				calResp.setError("CIBC Premium going low");
				return calResp;
			}
			
			calResp = setLodingDetails(ocuLoading, cibc.doubleValue(), calResp);
			calResp.setCibc(calResp.getCibc() + cibc.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + cibc.doubleValue());
			calResp.setCibcTerm(valiedTermCIBC);
			return calResp;

		case "FEB":
			if(benefictDao.findByRiderCode("FEB").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("FEB under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("FEB");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictFEB = rateCardATFESCDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermFEB = maxTermToBenefictFEB > term ? term : maxTermToBenefictFEB;

			if (calResp.isArp()) {
				RateCardARP rateCardARP = rateCardARPDao
						.findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age,
								valiedTermFEB, calResp.getPayTerm(), new Date(), new Date(), new Date(), new Date());
				relife = rateCardARP.getRate();
			}

			// System.out.println(relife + ": relife");

			BigDecimal feb = febService.calculateFEB(age, valiedTermFEB, new Date(), ridsumasu, payFrequency, relife,
					ocuLoading);
			
			if(!(feb.doubleValue() > 0)) {
				calResp.setErrorExist(true);
				calResp.setError("FEB Premium going low");
				return calResp;
			}
			
			calResp = setLodingDetails(ocuLoading, feb.doubleValue(), calResp);
			calResp.setFeb(feb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + feb.doubleValue());
			calResp.setFebTerm(valiedTermFEB);
			return calResp;

		case "FEBS":
			if(benefictDao.findByRiderCode("FEBS").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("FEBS under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("FEBS");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictFEBS = rateCardATFESCDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermFEBS = maxTermToBenefictFEBS > term ? term : maxTermToBenefictFEBS;

			if (calResp.isArp()) {
				RateCardARP rateCardARP = rateCardARPDao
						.findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age,
								valiedTermFEBS, calResp.getPayTerm(), new Date(), new Date(), new Date(), new Date());
				relife = rateCardARP.getRate();
			}

			// System.out.println(relife + ": relife");

			BigDecimal febs = febsService.calculateFEBS(age, valiedTermFEBS, new Date(), ridsumasu, payFrequency,
					relife, ocuLoading);
			
			if(!(febs.doubleValue() > 0)) {
				calResp.setErrorExist(true);
				calResp.setError("FEBS Premium going low");
				return calResp;
			}
			
			calResp = setLodingDetails(ocuLoading, febs.doubleValue(), calResp);
			calResp.setFebs(febs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + febs.doubleValue());
			calResp.setFebsTerm(valiedTermFEBS);
			return calResp;

		case "MFIBD":
			if(benefictDao.findByRiderCode("MFIBD").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("MFIBD under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("MFIBD");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictMFIBD = rateCardMFIBDDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermMFIBD = maxTermToBenefictMFIBD > term ? term : maxTermToBenefictMFIBD;

			if (calResp.isArp()) {
				RateCardARP rateCardARP = rateCardARPDao
						.findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age,
								valiedTermMFIBD, calResp.getPayTerm(), new Date(), new Date(), new Date(), new Date());
				relife = rateCardARP.getRate();
			}

			// System.out.println(relife + ": relife");

			if (ridsumasu.doubleValue() > calResp.getBsaYearlyPremium()) {
				calResp.setErrorExist(true);
				BigDecimal val = new BigDecimal(calResp.getBsaYearlyPremium());
				calResp.setError("MFIBD MAx Value is " + val.setScale(2, RoundingMode.HALF_UP).doubleValue());
				return calResp;
			}

			BigDecimal mfibd = mfibdService.calculateMFIBD(age, valiedTermMFIBD, new Date(), ridsumasu, payFrequency,
					relife, ocuLoading);
			
			if(!(mfibd.doubleValue() > 0)) {
				calResp.setErrorExist(true);
				calResp.setError("MFIBD Premium going low");
				return calResp;
			}
			
			calResp = setLodingDetails(ocuLoading, mfibd.doubleValue(), calResp);
			calResp.setMifdb(mfibd.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + mfibd.doubleValue());
			calResp.setMifdbTerm(valiedTermMFIBD);
			return calResp;

		case "MFIBT":
			if(benefictDao.findByRiderCode("MFIBT").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("MFIBT under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("MFIBT");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictMFIBT = rateCardMFIBTDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermMFIBT = maxTermToBenefictMFIBT > term ? term : maxTermToBenefictMFIBT;

			if (calResp.isArp()) {
				RateCardARP rateCardARP = rateCardARPDao
						.findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age,
								valiedTermMFIBT, calResp.getPayTerm(), new Date(), new Date(), new Date(), new Date());
				relife = rateCardARP.getRate();
			}

			// System.out.println(relife + ": relife");

			if (ridsumasu.doubleValue() > calResp.getBsaYearlyPremium()) {
				calResp.setErrorExist(true);
				BigDecimal val = new BigDecimal(calResp.getBsaYearlyPremium());
				calResp.setError("MFIBT MAx Value is " + val.setScale(2, RoundingMode.HALF_UP).doubleValue());
				return calResp;
			}

			BigDecimal mfibt = mfibtService.calculateMFIBT(age, valiedTermMFIBT, new Date(), ridsumasu, payFrequency,
					relife, ocuLoading);
			
			if(!(mfibt.doubleValue() > 0)) {
				calResp.setErrorExist(true);
				calResp.setError("MFIBT Premium going low");
				return calResp;
			}
			
			calResp = setLodingDetails(ocuLoading, mfibt.doubleValue(), calResp);
			calResp.setMifdt(mfibt.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + mfibt.doubleValue());
			calResp.setMifdtTerm(valiedTermMFIBT);
			return calResp;
		case "MFIBDT":
			if(benefictDao.findByRiderCode("MFIBDT").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("MFIBDT under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("MFIBDT");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictMFIBDT = rateCardMFIBDTDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermMFIBDT = maxTermToBenefictMFIBDT > term ? term : maxTermToBenefictMFIBDT;

			if (calResp.isArp()) {
				RateCardARP rateCardARP = rateCardARPDao
						.findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age,
								valiedTermMFIBDT, calResp.getPayTerm(), new Date(), new Date(), new Date(), new Date());
				relife = rateCardARP.getRate();
			}

			// System.out.println(relife + ": relife");

			if (ridsumasu.doubleValue() > calResp.getBsaYearlyPremium()) {
				calResp.setErrorExist(true);
				BigDecimal val = new BigDecimal(calResp.getBsaYearlyPremium());
				calResp.setError("MFIBDT MAx Value is " + val.setScale(2, RoundingMode.HALF_UP).doubleValue());
				return calResp;
			}

			BigDecimal mfibdt = mfibdtService.calculateMFIBDT(age, valiedTermMFIBDT, new Date(), ridsumasu,
					payFrequency, relife, ocuLoading);
			
			if(!(mfibdt.doubleValue() > 0)) {
				calResp.setErrorExist(true);
				calResp.setError("MFIBDT Premium going low");
				return calResp;
			}
			
			calResp = setLodingDetails(ocuLoading, mfibdt.doubleValue(), calResp);
			calResp.setMifdbt(mfibdt.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + mfibdt.doubleValue());
			calResp.setMifdbtTerm(valiedTermMFIBDT);
			return calResp;
		/*
		 * case "HRB": ocuLoading = oculoding.get("HRB"); if (ocuLoading == null)
		 * ocuLoading = 1.0;
		 * 
		 * BigDecimal hrb = hrbService.calculateHRB(age, gender, ridsumasu, adultCount,
		 * childCount, new Date(), payFrequency, 1.0, ocuLoading);
		 * calResp.setHrb(hrb.doubleValue()); calResp.setAddBenif(calResp.getAddBenif()
		 * + hrb.doubleValue()); calResp.setHrbTerm(term); return calResp;
		 */

		case "HRBF":
			if(benefictDao.findByRiderCode("HCBF").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("HCBF under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("HRB");
			if (ocuLoading == null)
				ocuLoading = 1.0;
			// Term calculation ///////

			Integer maxTermToBenefictHRBF = rateCardHRBFDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermHRBF = maxTermToBenefictHRBF > term ? term : maxTermToBenefictHRBF;

			if (calResp.isArp()) {
				RateCardARP rateCardARP = rateCardARPDao
						.findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age,
								valiedTermHRBF, calResp.getPayTerm(), new Date(), new Date(), new Date(), new Date());
				relife = rateCardARP.getRate();
			}

			// System.out.println(relife + ": relife");

			// System.out.println(ocuLoading + " Occu Loading HRBF");
			// Integer valiedTermHRBF = 10;

			BigDecimal hrbf = null;
			try {
				hrbf = hrbfService.calculateHRBF(age, valiedTermHRBF, ridsumasu, adultCount, childCount, new Date(),
						payFrequency, relife, ocuLoading);
				
				if(!(hrbf.doubleValue() > 0)) {
					calResp.setErrorExist(true);
					calResp.setError("HCBF Premium going low");
					return calResp;
				}
				
				calResp = setLodingDetails(ocuLoading, hrbf.doubleValue(), calResp);
				calResp.setHrbf(hrbf.doubleValue());
				calResp.setAddBenif(calResp.getAddBenif() + hrbf.doubleValue());
				calResp.setHrbfTerm(valiedTermHRBF);
			} catch (Exception e) {
				hrbf = new BigDecimal(0);
				calResp.setWarning("Please get HRBF for spouse or child to calculate HRBF");
				calResp.setWarningExist(true);
				e.printStackTrace();
			}

			return calResp;

		case "HRBI":
			if(benefictDao.findByRiderCode("HCBI").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("HCBI under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("HRB");
			if (ocuLoading == null)
				ocuLoading = 1.0;
			// Term calculation ///////

			Integer maxTermToBenefictHRBI = rateCardHRBIDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermHRBI = maxTermToBenefictHRBI > term ? term : maxTermToBenefictHRBI;

			if (calResp.isArp()) {
				RateCardARP rateCardARP = rateCardARPDao
						.findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age,
								valiedTermHRBI, calResp.getPayTerm(), new Date(), new Date(), new Date(), new Date());
				relife = rateCardARP.getRate();
			}

			// System.out.println(relife + ": relife");

			// System.out.println(ocuLoading + " Occu Loading HRBI");

			// Integer valiedTermHRBI = 10;

			BigDecimal hrbi = hrbiService.calculateHRBI(age, valiedTermHRBI, gender, ridsumasu, new Date(),
					payFrequency, relife, ocuLoading);
			
			if(!(hrbi.doubleValue() > 0)) {
				calResp.setErrorExist(true);
				calResp.setError("HCBI Premium going low");
				return calResp;
			}
			
			
			calResp = setLodingDetails(ocuLoading, hrbi.doubleValue(), calResp);
			calResp.setHrbi(hrbi.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + hrbi.doubleValue());
			calResp.setHrbiTerm(valiedTermHRBI);
			return calResp;

		case "HRBIS":
			if(benefictDao.findByRiderCode("HCBIS").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("HCBIS under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("HRBS");
			if (ocuLoading == null)
				ocuLoading = 1.0;
			// Term calculation ///////

			Integer maxTermToBenefictHRBIS = rateCardHRBIDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermHRBIS = maxTermToBenefictHRBIS > term ? term : maxTermToBenefictHRBIS;

			// Integer valiedTermHRBIS = 10;
			if (calResp.isArp()) {
				RateCardARP rateCardARP = rateCardARPDao
						.findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age,
								valiedTermHRBIS, calResp.getPayTerm(), new Date(), new Date(), new Date(), new Date());
				relife = rateCardARP.getRate();
			}

			// System.out.println(relife + ": relife");

			BigDecimal hrbis = hrbiService.calculateHRBI(age, valiedTermHRBIS, gender, ridsumasu, new Date(),
					payFrequency, relife, ocuLoading);
			
			if(!(hrbis.doubleValue() > 0)) {
				calResp.setErrorExist(true);
				calResp.setError("HCBIS Premium going low");
				return calResp;
			}
			
			calResp = setLodingDetails(ocuLoading, hrbis.doubleValue(), calResp);
			calResp.setHrbis(hrbis.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + hrbis.doubleValue());
			calResp.setHrbisTerm(valiedTermHRBIS);
			return calResp;

		case "HRBIC":
			if(benefictDao.findByRiderCode("HCBIC").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("HCBIC under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("HRBIC");
			if (ocuLoading == null)
				ocuLoading = 1.0;
			// Term calculation ///////

			Integer maxTermToBenefictHRBIC = 21;
			Integer valiedTermHRBIC = maxTermToBenefictHRBIC > term ? term : maxTermToBenefictHRBIC;

			if (calResp.isArp()) {
				RateCardARP rateCardARP = rateCardARPDao
						.findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age,
								valiedTermHRBIC, calResp.getPayTerm(), new Date(), new Date(), new Date(), new Date());
				relife = rateCardARP.getRate();
			}

			// System.out.println(relife + ": relife");

			// Integer valiedTermHRBIC = 10;

			BigDecimal hrbic = hrbiService.calculateHRBI(age, valiedTermHRBIC, gender, ridsumasu, new Date(),
					payFrequency, relife, ocuLoading);
			
			
			if(!(hrbic.doubleValue() > 0)) {
				calResp.setErrorExist(true);
				calResp.setError("HCBIC Premium going low");
				return calResp;
			}
			
			
			calResp = setLodingDetails(ocuLoading, hrbic.doubleValue(), calResp);
			Double resent = calResp.getHrbic() != null ? calResp.getHrbic() : 0.0;
			calResp.setHrbic(hrbic.doubleValue() + resent);
			calResp.setAddBenif(calResp.getAddBenif() + hrbic.doubleValue());
			calResp.setHrbicTerm(valiedTermHRBIC);
			return calResp;

		case "SHCBF":
			if(benefictDao.findByRiderCode("SHCBF").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("SHCBF under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("SUHRB");
			if (ocuLoading == null)
				ocuLoading = 1.0;
			// Term calculation ///////
			/*
			 * Integer maxTermToBenefictHRBF =
			 * rateCardHRBFDao.findFirstByOrderByTermDesc().getTerm(); Integer
			 * valiedTermHRBF = maxTermToBenefictHRBF > term ? term : maxTermToBenefictHRBF;
			 */

			Integer valiedTermSHCBF = 10;

			if (calResp.isArp()) {
				RateCardARP rateCardARP = rateCardARPDao
						.findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age,
								valiedTermSHCBF, calResp.getPayTerm(), new Date(), new Date(), new Date(), new Date());
				relife = rateCardARP.getRate();
			}

			// System.out.println(relife + ": relife");

			BigDecimal suhbf = shcbfService.calculateSHCBF(age, valiedTermSHCBF, ridsumasu, adultCount, childCount,
					new Date(), payFrequency, relife, ocuLoading);
			
			if(!(suhbf.doubleValue() > 0)) {
				calResp.setErrorExist(true);
				calResp.setError("SHCBF Premium going low");
				return calResp;
			}
			
			
			calResp = setLodingDetails(ocuLoading, suhbf.doubleValue(), calResp);
			calResp.setHrbf(suhbf.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + suhbf.doubleValue());
			calResp.setHrbfTerm(valiedTermSHCBF);
			return calResp;

		case "SUHRB":
			if(benefictDao.findByRiderCode("SHCBI").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("SHCBI under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("SUHRB");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictSUHRB = rateCardSUHRBDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermSUHRB = maxTermToBenefictSUHRB > term ? term : maxTermToBenefictSUHRB;

			if (calResp.isArp()) {
				RateCardARP rateCardARP = rateCardARPDao
						.findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age,
								valiedTermSUHRB, calResp.getPayTerm(), new Date(), new Date(), new Date(), new Date());
				relife = rateCardARP.getRate();
			}

			// System.out.println(relife + ": relife");

			BigDecimal suhrb = shcbiService.calculateSHCBI(age, gender, valiedTermSUHRB, ridsumasu, new Date(),
					payFrequency, relife, ocuLoading);
			
			if(!(suhrb.doubleValue() > 0)) {
				calResp.setErrorExist(true);
				calResp.setError("SHCBI Premium going low");
				return calResp;
			}
			calResp = setLodingDetails(ocuLoading, suhrb.doubleValue(), calResp);
			calResp.setShcbi(suhrb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + suhrb.doubleValue());
			calResp.setShcbiTerm(valiedTermSUHRB);
			calResp.setSuhrb(suhrb.doubleValue());
			calResp.setSuhrbTerm(valiedTermSUHRB);
			return calResp;
		/*
		 * case "SUHRB": ocuLoading = oculoding.get("SUHRB"); if (ocuLoading == null)
		 * ocuLoading = 1.0;
		 * 
		 * Integer maxTermToBenefictSUHRB =
		 * rateCardSUHRBDao.findFirstByOrderByTermDesc().getTerm(); Integer
		 * valiedTermSUHRB = maxTermToBenefictSUHRB > term ? term :
		 * maxTermToBenefictSUHRB;
		 * 
		 * BigDecimal suhrb = suhrbService.calculateSUHRB(age, gender, valiedTermSUHRB,
		 * ridsumasu, new Date(), payFrequency, 1.0, ocuLoading); calResp =
		 * setLodingDetails(ocuLoading, suhrb.doubleValue(), calResp);
		 * calResp.setSuhrb(suhrb.doubleValue());
		 * calResp.setAddBenif(calResp.getAddBenif() + suhrb.doubleValue());
		 * calResp.setSuhrbTerm(valiedTermSUHRB); return calResp;
		 */

		case "SUHRBS":
			if(benefictDao.findByRiderCode("SHCBIS").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("SHCBIS under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("SUHRBS");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictSUHRBS = rateCardSUHRBDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermSUHRBS = maxTermToBenefictSUHRBS > term ? term : maxTermToBenefictSUHRBS;

			if (calResp.isArp()) {
				RateCardARP rateCardARP = rateCardARPDao
						.findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age,
								valiedTermSUHRBS, calResp.getPayTerm(), new Date(), new Date(), new Date(), new Date());
				relife = rateCardARP.getRate();
			}

			// System.out.println(relife + ": relife");

			BigDecimal suhrbs = suhrbsService.calculateSUHRBS(age, gender, valiedTermSUHRBS, ridsumasu, new Date(),
					payFrequency, relife, ocuLoading);
			
			if(!(suhrbs.doubleValue() > 0)) {
				calResp.setErrorExist(true);
				calResp.setError("SHCBIS Premium going low");
				return calResp;
			}
			
			calResp = setLodingDetails(ocuLoading, suhrbs.doubleValue(), calResp);
			calResp.setShcbis(suhrbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + suhrbs.doubleValue());
			calResp.setShcbisTerm(valiedTermSUHRBS);
			calResp.setSuhrbs(suhrbs.doubleValue());
			// calResp.setAddBenif(calResp.getAddBenif() + suhrbs.doubleValue());
			calResp.setSuhrbsTerm(valiedTermSUHRBS);
			return calResp;

		/*
		 * case "SUHRBS": ocuLoading = oculoding.get("SUHRBS"); if (ocuLoading == null)
		 * ocuLoading = 1.0;
		 * 
		 * Integer maxTermToBenefictSUHRBS =
		 * rateCardSUHRBDao.findFirstByOrderByTermDesc().getTerm(); Integer
		 * valiedTermSUHRBS = maxTermToBenefictSUHRBS > term ? term :
		 * maxTermToBenefictSUHRBS;
		 * 
		 * BigDecimal suhrbs = suhrbsService.calculateSUHRBS(age, gender,
		 * valiedTermSUHRBS, ridsumasu, new Date(), payFrequency, 1.0, ocuLoading);
		 * calResp = setLodingDetails(ocuLoading, suhrbs.doubleValue(), calResp);
		 * calResp.setSuhrbs(suhrbs.doubleValue());
		 * calResp.setAddBenif(calResp.getAddBenif() + suhrbs.doubleValue());
		 * calResp.setSuhrbsTerm(valiedTermSUHRBS); return calResp;
		 */

		case "SUHRBC":
			if(benefictDao.findByRiderCode("SHCBIC").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("SHCBIC under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("SUHRBC");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictSUHRBC = rateCardSUHRBDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermSUHRBC = maxTermToBenefictSUHRBC > term ? term : maxTermToBenefictSUHRBC;

			if (calResp.isArp()) {
				RateCardARP rateCardARP = rateCardARPDao
						.findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age,
								valiedTermSUHRBC, calResp.getPayTerm(), new Date(), new Date(), new Date(), new Date());
				relife = rateCardARP.getRate();
			}

			// System.out.println(relife + ": relife");

			BigDecimal suhrbc = suhrbcService.calculateSUHRBC(age, gender, valiedTermSUHRBC, ridsumasu, new Date(),
					payFrequency, relife);

			
			if(!(suhrbc.doubleValue() > 0)) {
				calResp.setErrorExist(true);
				calResp.setError("SHCBIC Premium going low");
				return calResp;
			}
			
			
			calResp = setLodingDetails(ocuLoading, suhrbc.doubleValue(), calResp);
			calResp.setShcbic(calResp.getShcbic() + suhrbc.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + suhrbc.doubleValue());
			calResp.setShcbicTerm(valiedTermSUHRBC);
			calResp.setSuhrbc(calResp.getSuhrbc() + suhrbc.doubleValue());
			// calResp.setAddBenif(calResp.getAddBenif() + suhrbc.doubleValue());
			calResp.setSuhrbcTerm(valiedTermSUHRBC);
			return calResp;
		/*
		 * case "SUHRBC": ocuLoading = oculoding.get("SUHRBC"); if (ocuLoading == null)
		 * ocuLoading = 1.0;
		 * 
		 * Integer maxTermToBenefictSUHRBC =
		 * rateCardSUHRBDao.findFirstByOrderByTermDesc().getTerm(); Integer
		 * valiedTermSUHRBC = maxTermToBenefictSUHRBC > term ? term :
		 * maxTermToBenefictSUHRBC;
		 * 
		 * BigDecimal suhrbc = suhrbcService.calculateSUHRBC(age, gender,
		 * valiedTermSUHRBC, ridsumasu, new Date(), payFrequency, 1.0);
		 * 
		 * calResp = setLodingDetails(ocuLoading, suhrbc.doubleValue(), calResp);
		 * calResp.setSuhrbc(calResp.getSuhrbc() + suhrbc.doubleValue());
		 * calResp.setAddBenif(calResp.getAddBenif() + suhrbc.doubleValue());
		 * calResp.setSuhrbcTerm(valiedTermSUHRBC); return calResp;
		 */

		case "HB":
			if(benefictDao.findByRiderCode("HB").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("HB under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("HB");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictHB = rateCardHBDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermHB = maxTermToBenefictHB > term ? term : maxTermToBenefictHB;

			if (ridsumasu.doubleValue() > calResp.getBsaYearlyPremium() * 0.1) {
				calResp.setErrorExist(true);
				BigDecimal val = new BigDecimal(calResp.getBsaYearlyPremium()).multiply(new BigDecimal(0.1));
				calResp.setError("HB MAx Value is " + val.setScale(2, RoundingMode.HALF_UP).doubleValue());
				return calResp;
			}

			if (calResp.isArp()) {
				RateCardARP rateCardARP = rateCardARPDao
						.findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age,
								valiedTermHB, calResp.getPayTerm(), new Date(), new Date(), new Date(), new Date());
				relife = rateCardARP.getRate();
			}

			// System.out.println(relife + ": relife");

			BigDecimal hb = hbService.calculateHB(age, valiedTermHB, new Date(), ridsumasu, payFrequency, relife,
					ocuLoading);

			if(!(hb.doubleValue() > 0)) {
				calResp.setErrorExist(true);
				calResp.setError("HB Premium going low");
				return calResp;
			}
			calResp = setLodingDetails(ocuLoading, hb.doubleValue(), calResp);
			calResp.setHb(hb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + hb.doubleValue());
			calResp.setHbTerm(valiedTermHB);
			return calResp;

		case "HBS":
			if(benefictDao.findByRiderCode("HBS").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("HBS under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("HBS");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictHBS = rateCardHBDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermHBS = maxTermToBenefictHBS > term ? term : maxTermToBenefictHBS;

			if (calResp.isArp()) {
				RateCardARP rateCardARP = rateCardARPDao
						.findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age,
								valiedTermHBS, calResp.getPayTerm(), new Date(), new Date(), new Date(), new Date());
				relife = rateCardARP.getRate();
			}

			// System.out.println(relife + ": relife");

			BigDecimal hbs = hbsService.calculateHBS(age, valiedTermHBS, new Date(), ridsumasu, payFrequency, relife,
					ocuLoading);
			

		
			if(!(hbs.doubleValue() > 0)) {
				calResp.setErrorExist(true);
				calResp.setError("HBS Premium going low");
				return calResp;
			}
			
			calResp = setLodingDetails(ocuLoading, hbs.doubleValue(), calResp);
			calResp.setHbs(hbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + hbs.doubleValue());
			calResp.setHbsTerm(valiedTermHBS);
			return calResp;

		case "HBC":
			if(benefictDao.findByRiderCode("HBC").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("HBC under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("HBC");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			System.out.println("calculation term : "+ term );
			
			term = term > (21 - age) ? (21 - age) : term;

			Integer maxTermToBenefictHBC = rateCardHBCDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermHBC = maxTermToBenefictHBC > term ? term : maxTermToBenefictHBC;

			System.out.println("calculation age : "+ term );
			System.out.println("calculation age : "+ age );
			System.out.println("calculation term : "+ valiedTermHBC );
			System.out.println("calculation payterm : "+ calResp.getPayTerm() );
			System.out.println("calculation date : "+ new Date() );
			
			if (calResp.isArp()) {

				RateCardARP rateCardARP = rateCardARPDao
						.findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age,
								valiedTermHBC, calResp.getPayTerm(), new Date(), new Date(), new Date(), new Date());
				relife = rateCardARP.getRate();
			}

			System.out.println(relife + ": relife");

			// ** 21-age < term term = 21-age else term
			BigDecimal hbc = hbcService.calculateHBC(valiedTermHBC, new Date(), ridsumasu, payFrequency, relife);
			
			if(!(hbc.doubleValue() > 0)) {
				calResp.setErrorExist(true);
				calResp.setError("HBC Premium going low");
				return calResp;
			}
			
			calResp = setLodingDetails(ocuLoading, hbc.doubleValue(), calResp);
			calResp.setHbc(calResp.getHbc() + hbc.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + hbc.doubleValue());
			calResp.setHbcTerm(valiedTermHBC);
			return calResp;

		case "WPB":
			if(benefictDao.findByRiderCode("WPB").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("WPB under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("WPB");
			// System.out.println(ocuLoading + " wpb oculoading");
			if (ocuLoading == null)
				ocuLoading = 1.0;
			// System.out.println(ocuLoading + " wpb oculoading");
			BigDecimal wpb = null;
			if(productCode.equalsIgnoreCase("ARTM")) {
				wpb = wpbService.calculateARTMWPB(calResp, ocuLoading);
				
				if(!(wpb.doubleValue() > 0)) {
					calResp.setErrorExist(true);
					calResp.setError("WPB Premium going low");
					return calResp;
				}

				
			}else {
				wpb = wpbService.calculateWPB(calResp, ocuLoading);
				
				if(!(wpb.doubleValue() > 0)) {
					calResp.setErrorExist(true);
					calResp.setError("WPB Premium going low");
					return calResp;
				}
			}
			calResp = setLodingDetails(ocuLoading, wpb.doubleValue(), calResp);
			calResp.setWpb(wpb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + wpb.doubleValue());
			calResp.setWpbTerm(term);
			return calResp;
		case "WPBS":
			if(benefictDao.findByRiderCode("WPBS").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("WPBS under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("WPBS");
			if (ocuLoading == null)
				ocuLoading = 1.0;
			BigDecimal wpbs = wpbsService.calculateWPBS(calResp, ocuLoading);
			
			if(!(wpbs.doubleValue() > 0)) {
				calResp.setErrorExist(true);
				calResp.setError("WPBS Premium going low");
				return calResp;
			}
			calResp = setLodingDetails(ocuLoading, wpbs.doubleValue(), calResp);
			calResp.setWpbs(wpbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + wpbs.doubleValue());
			calResp.setWpbsTerm(term);
			return calResp;

		case "JLB":
			if(benefictDao.findByRiderCode("JLB").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("JLB under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("JLB");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictJLB = rateCardJLBDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermJLB = maxTermToBenefictJLB > term ? term : maxTermToBenefictJLB;

			BigDecimal jlb = jlbService.calculateJLB(age, valiedTermJLB, inRate, gender, new Date(), loan, ocuLoading);
			
			if(!(jlb.doubleValue() > 0)) {
				calResp.setErrorExist(true);
				calResp.setError("JLB Premium going low");
				return calResp;
			}
			
			calResp = setLodingDetails(ocuLoading, jlb.doubleValue(), calResp);
			calResp.setJlb(jlb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + jlb.doubleValue());
			calResp.setJlbTerm(valiedTermJLB);
			return calResp;

		case "JLBPL":
			if(benefictDao.findByRiderCode("JLBPL").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("JLBPL under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("JLBPL");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictJLBPL = rateCardJLBDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermJLBPL = maxTermToBenefictJLBPL > term ? term : maxTermToBenefictJLBPL;

			BigDecimal jlbpl = jlbplService.calculateJLBPL(age, valiedTermJLBPL, inRate, gender, new Date(), loan,
					ocuLoading);
			
			if(!(jlbpl.doubleValue() > 0)) {
				calResp.setErrorExist(true);
				calResp.setError("JLBPL Premium going low");
				return calResp;
			}
			
			calResp = setLodingDetails(ocuLoading, jlbpl.doubleValue(), calResp);
			calResp.setJlbpl(jlbpl.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + jlbpl.doubleValue());
			calResp.setJlbplTerm(valiedTermJLBPL);
			return calResp;

		case "TPDDTA":
			if(benefictDao.findByRiderCode("TPDDTA").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("TPDDTA under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("TPDDTA");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictTPDDTA = rateCardTPDDTADao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermTPDDTA = maxTermToBenefictTPDDTA > term ? term : maxTermToBenefictTPDDTA;

			BigDecimal tpddta = tpddtaService.calculateTPDDTA(age, valiedTermTPDDTA, inRate, gender, new Date(), loan,
					ocuLoading);
			
			if(!(tpddta.doubleValue() > 0)) {
				calResp.setErrorExist(true);
				calResp.setError("TPDDTA Premium going low");
				return calResp;
			}
			
			calResp = setLodingDetails(ocuLoading, tpddta.doubleValue(), calResp);
			calResp.setTpddta(tpddta.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + tpddta.doubleValue());
			calResp.setTpddtaTerm(valiedTermTPDDTA);
			return calResp;

		case "TPDDTAS":
			if(benefictDao.findByRiderCode("TPDDTAS").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("TPDDTAS under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("TPDDTAS");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictTPDDTAS = rateCardTPDDTASDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermTPDDTAS = maxTermToBenefictTPDDTAS > term ? term : maxTermToBenefictTPDDTAS;

			BigDecimal tpddtas = tpddtasService.calculateTPDDTAS(age, valiedTermTPDDTAS, inRate, gender, new Date(),
					loan, ocuLoading);
			
			
			if(!(tpddtas.doubleValue() > 0)) {
				calResp.setErrorExist(true);
				calResp.setError("TPDDTAS Premium going low");
				return calResp;
			}
			
			calResp = setLodingDetails(ocuLoading, tpddtas.doubleValue(), calResp);
			calResp.setTpddtas(tpddtas.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + tpddtas.doubleValue());
			calResp.setTpddtasTerm(valiedTermTPDDTAS);
			return calResp;

		case "TPDDTAPL":
			if(benefictDao.findByRiderCode("TPDDTAPL").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("TPDDTAPL under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("TPDDTAPL");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictTPDDTAPL = rateCardTPDDTADao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermTPDDTAPL = maxTermToBenefictTPDDTAPL > term ? term : maxTermToBenefictTPDDTAPL;

			BigDecimal tpddtapl = tpddtaplService.calculateTPDDTAPL(age, valiedTermTPDDTAPL, inRate, gender, new Date(),
					loan, ocuLoading);
			
			if(!(tpddtapl.doubleValue() > 0)) {
				calResp.setErrorExist(true);
				calResp.setError("TPDDTAPL Premium going low");
				return calResp;
			}
			calResp = setLodingDetails(ocuLoading, tpddtapl.doubleValue(), calResp);
			calResp.setTpddtapl(tpddtapl.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + tpddtapl.doubleValue());
			calResp.setTpddtaplTerm(valiedTermTPDDTAPL);
			return calResp;

		case "TPDDTASPL":
			if(benefictDao.findByRiderCode("TPDDTASPL").getActive() == 0) {
				calResp.setErrorExist(true);
				calResp.setError("TPDDTASPL under Maintenance, Please untick or reload page");
				return calResp;
			}
			ocuLoading = oculoding.get("TPDDTASPL");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictTPDDTASPL = rateCardTPDDTASDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermTPDDTASPL = maxTermToBenefictTPDDTASPL > term ? term : maxTermToBenefictTPDDTASPL;

			BigDecimal tpddtaspl = tpddtasplService.calculateTPDDTASPL(age, valiedTermTPDDTASPL, inRate, gender,
					new Date(), loan, ocuLoading);
			
			if(!(tpddtaspl.doubleValue() > 0)) {
				calResp.setErrorExist(true);
				calResp.setError("TPDDTASPL Premium going low");
				return calResp;
			}
			
			calResp = setLodingDetails(ocuLoading, tpddtaspl.doubleValue(), calResp);
			calResp.setTpddtaspl(tpddtaspl.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + tpddtaspl.doubleValue());
			calResp.setTpddtasplTerm(valiedTermTPDDTASPL);
			return calResp;

		/////////////////////////////////////////////////////////////////////////
		default:
			// calResp.setHrbsTerm(term);
			calResp.setHrbfsTerm(term);
			return calResp;
		}

	}

	private QuotationQuickCalResponse setLodingDetails(double ocuLoading, double premium,
			QuotationQuickCalResponse calResp) {

		Double bsa = new BigDecimal(premium).divide(new BigDecimal(ocuLoading), RoundingMode.HALF_UP).doubleValue();

		// System.out.println("bsa for occu : " + bsa);

		calResp.setWithoutLoadingTot(calResp.getWithoutLoadingTot() + bsa);
		calResp.setOccuLodingTot(calResp.getOccuLodingTot() + (premium - bsa));

		return calResp;
	}

}
