package org.arpicoinsurance.groupit.main.service.custom.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

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
import org.arpicoinsurance.groupit.main.service.rider.HRBService;
import org.arpicoinsurance.groupit.main.service.rider.JLBPLService;
import org.arpicoinsurance.groupit.main.service.rider.JLBService;
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

	//@Autowired
	//private HRBService hrbService;
	
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
		/*if (_mRiders != null) {
			for (Benifict benifict : _mRiders) {
				adultCount = 1;
				if (benifict.getType().equals("HRB")) {

					if (quotationCalculation.get_personalInfo().getSage() != null
							&& quotationCalculation.get_personalInfo().getSgenger() != null
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

				calResp = calculateMainlifeRiders(quotationCalculation.get_personalInfo().getMage(), benifict.getType(),
						quotationCalculation.get_personalInfo().getTerm(), benifict.getSumAssured(),
						quotationCalculation.get_personalInfo().getMgenger(),
						quotationCalculation.get_personalInfo().getFrequance(),
						quotationCalculation.get_personalInfo().getMocu(), calResp, adultCount, childCount, inrate);

			}
		}*/
		
		
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
									System.out.println(adultCount);
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
						quotationCalculation.get_personalInfo().getMocu(), calResp, adultCount, childCount, inrate);

			}
		}
		

		if (quotationCalculation.get_personalInfo().getSage() != null
				&& quotationCalculation.get_personalInfo().getSgenger() != null
				&& quotationCalculation.get_personalInfo().getSocu() != null) {

			if (_sRiders != null) {

				for (Benifict benifict : _sRiders) {
					System.out.println(quotationCalculation.get_personalInfo().getTerm()
							+ "?????????????????????????-------------------");
					calResp = calculateMainlifeRiders(quotationCalculation.get_personalInfo().getSage(),
							benifict.getType(), quotationCalculation.get_personalInfo().getTerm(),
							benifict.getSumAssured(), quotationCalculation.get_personalInfo().getSgenger(),
							quotationCalculation.get_personalInfo().getFrequance(),
							quotationCalculation.get_personalInfo().getSocu(), calResp, adultCount, childCount, inrate);

				}
			}
		}
		if (quotationCalculation.get_personalInfo().getChildrens() != null
				&& quotationCalculation.get_personalInfo().getChildrens().size() > 0) {
			for (Children children : quotationCalculation.get_personalInfo().getChildrens()) {
				if (_cRiders != null) {
					for (Benifict benifict : _cRiders) {
						Integer term = 0;
						System.out.println("product :" +quotationCalculation.get_product());
						if (quotationCalculation.get_product().equals("ARP")) {
							term = calculateBenefictTerm.calculateChildBenifictTermARP(children.get_cAge(), benifict.getType(),
									quotationCalculation.get_personalInfo().getTerm(), quotationCalculation.get_personalInfo().getPayingterm());
							if(term < 5) {
								calResp.setErrorExist(true);
								calResp.setError("Can't get benifict fof child because 21 - ( Child Age + Pay Term) must be greate than 5");
								return calResp;
							}
						} else {
							term = calculateBenefictTerm.calculateBenifictTerm(children.get_cAge(), benifict.getType(),
									quotationCalculation.get_personalInfo().getTerm());
						}

						String benfName = benifict.getType();

						System.out.println(term + ";;;;;;;;;;;;;;;;;; child");

						switch (benfName) {
						case "CIBC":
							if (children.is_cCibc()) {
								calculateBenifPremium(benifict.getType(), benifict.getSumAssured(),
										children.get_cTitle().equals("Son") ? "M" : "F" , children.get_cAge(),
										quotationCalculation.get_personalInfo().getFrequance(), term, 0, calResp,
										adultCount, childCount, -1.0, -1.0);
							}
							break;

						case "HBC":
							if (children.is_cHbc()) {
								calculateBenifPremium(benifict.getType(), benifict.getSumAssured(),
										children.get_cTitle().equals("Son") ? "M" : "F", children.get_cAge(),
										quotationCalculation.get_personalInfo().getFrequance(), term, 0, calResp,
										adultCount, childCount, -1.0, -1.0);
							}
							break;
						/*
						case "SUHRBC":
							if (children.is_cSuhrbc()) {
								calculateBenifPremium(benifict.getType(), benifict.getSumAssured(),
										children.get_cTitle(), children.get_cAge(),
										quotationCalculation.get_personalInfo().getFrequance(), term, 0, calResp,
										adultCount, childCount, -1.0, -1.0);
							}
							break;*/
						case "SUHRBC":
							if (children.is_cSuhrbc()) {
								calculateBenifPremium(benifict.getType(), benifict.getSumAssured(),
										children.get_cTitle().equals("Son") ? "M" : "F", children.get_cAge(),
										quotationCalculation.get_personalInfo().getFrequance(), term, 0, calResp,
										adultCount, childCount, -1.0, -1.0);
							}
							break;
						case "HRBIC":
							if (children.is_cHrbic()) {
								calculateBenifPremium(benifict.getType(), benifict.getSumAssured(),
										children.get_cTitle().equals("Son") ? "M" : "F", children.get_cAge(),
										quotationCalculation.get_personalInfo().getFrequance(), term, 0, calResp,
										adultCount, childCount, -1.0, -1.0);
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
			Integer childCount, Double inrate) throws Exception {

		Integer term = calculateBenefictTerm.calculateBenifictTerm(age, type, payTerm);
		//Integer term = payTerm;
		calculateBenifPremium(type, bsa, gender, age, frequance, term, ocu, calResp, adultCount, childCount, bsa,
				inrate);
		return calResp;
	}

	@Override
	public QuotationQuickCalResponse calculateBenifPremium(String type, Double ridsumasu, String gender, Integer age,
			String payFrequency, Integer term, Integer occupation_id, QuotationQuickCalResponse calResp,
			Integer adultCount, Integer childCount, Double loan, Double inRate) throws Exception {
		
		System.out.println(occupation_id + " ////////////// ocu ID");
		
		Map<String, Double> oculoding = occupationLoding.getOccupationLoding(occupation_id);

		Double ocuLoading = 1.0;
		switch (type) {
		case "BSAS":
			ocuLoading = oculoding.get("SCB");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictSCB = rateCardATFESCDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermSCB = maxTermToBenefictSCB > term ? term : maxTermToBenefictSCB;

			BigDecimal scb = scbService.calculateSCB(age, valiedTermSCB, new Date(), ridsumasu, payFrequency, 1.0,
					ocuLoading);

			calResp = setLodingDetails(ocuLoading, scb.doubleValue(), calResp);
			
			calResp.setBsas(scb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + scb.doubleValue());
			calResp.setBsasTerm(valiedTermSCB);
			return calResp;

		case "ADB":
			ocuLoading = oculoding.get("ADB");
			if (ocuLoading == null)
				ocuLoading = 1.0;
			BigDecimal adb = adbService.calculateADB(ridsumasu, payFrequency, 1.0, ocuLoading);
			
			calResp = setLodingDetails(ocuLoading, adb.doubleValue(), calResp);
			
			calResp.setAdb(adb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + adb.doubleValue());
			calResp.setAdbTerm(term);
			return calResp;

		case "SFPO":
			ocuLoading = oculoding.get("SFPO");
			if (ocuLoading == null)
				ocuLoading = 1.0;
			Integer maxTermToBenefictSFPO = rateCardSFPODao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermSFPO = maxTermToBenefictSFPO > term ? term : maxTermToBenefictSFPO;

			
			BigDecimal sfpo = sfpoService.calculateSFPO(age, valiedTermSFPO, new Date(), ridsumasu, payFrequency, 1.0,
					ocuLoading);
			calResp = setLodingDetails(ocuLoading, sfpo.doubleValue(), calResp);
			calResp.setSfpo(sfpo.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + sfpo.doubleValue());
			calResp.setSfpoTerm(valiedTermSFPO);
			return calResp;

		case "ADBS":
			ocuLoading = oculoding.get("ADBS");
			if (ocuLoading == null)
				ocuLoading = 1.0;
			BigDecimal adbs = adbsService.calculateADBS(ridsumasu, payFrequency, 1.0, ocuLoading);
			calResp = setLodingDetails(ocuLoading, adbs.doubleValue(), calResp);
			calResp.setAdbs(adbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + adbs.doubleValue());
			calResp.setAdbsTerm(term);
			return calResp;

		case "ATPB":
			System.out.println("called/////////////////////////////////////////////////////");
			System.out.println(ridsumasu);
			ocuLoading = oculoding.get("ATPB");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictATPB = rateCardATFESCDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermATPB = maxTermToBenefictATPB > term ? term : maxTermToBenefictATPB;

			BigDecimal atpb = atpbService.calculateATPB(age, valiedTermATPB, new Date(), ridsumasu, payFrequency, 1.0,
					ocuLoading);
			calResp = setLodingDetails(ocuLoading, atpb.doubleValue(), calResp);
			calResp.setAtpb(atpb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + atpb.doubleValue());
			calResp.setAtpbTerm(valiedTermATPB);
			return calResp;
		case "TPDASB":
			ocuLoading = oculoding.get("TPDASB");
			if (ocuLoading == null)
				ocuLoading = 1.0;
			
			Integer maxTermToBenefictTPDASB = rateCardTPDASBDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermTPDASB = maxTermToBenefictTPDASB > term ? term : maxTermToBenefictTPDASB;
			
			BigDecimal tpdasb = tpdasbService.calculateTPDASB(age, valiedTermTPDASB, new Date(), ridsumasu, payFrequency, 1.0,
					ocuLoading);
			calResp = setLodingDetails(ocuLoading, tpdasb.doubleValue(), calResp);
			calResp.setTpdasb(tpdasb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + tpdasb.doubleValue());
			calResp.setTpdasbTerm(valiedTermTPDASB);
			return calResp;
		case "TPDASBS":
			ocuLoading = oculoding.get("TPDASBS");
			if (ocuLoading == null)
				ocuLoading = 1.0;
			
			Integer maxTermToBenefictTPDASBS = rateCardTPDASBDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermTPDASBS = maxTermToBenefictTPDASBS > term ? term : maxTermToBenefictTPDASBS;
			
			BigDecimal tpdasbs = tpdasbsbService.calculateTPDASBS(age, valiedTermTPDASBS, new Date(), ridsumasu, payFrequency, 1.0,
					ocuLoading);
			calResp = setLodingDetails(ocuLoading, tpdasbs.doubleValue(), calResp);
			calResp.setTpdasbs(tpdasbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + tpdasbs.doubleValue());
			calResp.setTpdasbsTerm(valiedTermTPDASBS);
			return calResp;
		case "TPDB":
			ocuLoading = oculoding.get("TPDB");
			if (ocuLoading == null)
				ocuLoading = 1.0;
			
			BigDecimal tpdb = tpdbService.calculateTPDB(ridsumasu, payFrequency, 1.0, ocuLoading);
			calResp = setLodingDetails(ocuLoading, tpdb.doubleValue(), calResp);
			calResp.setTpdb(tpdb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + tpdb.doubleValue());
			calResp.setTpdbTerm(term);
			return calResp;
		case "TPDBS":
			ocuLoading = oculoding.get("TPDBS");
			if (ocuLoading == null)
				ocuLoading = 1.0;
			BigDecimal tpdbs = tpdbsService.calculateTPDBS(ridsumasu, payFrequency, 1.0, ocuLoading);
			calResp = setLodingDetails(ocuLoading, tpdbs.doubleValue(), calResp);
			calResp.setTpdbs(tpdbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + tpdbs.doubleValue());
			calResp.setTpdbsTerm(term);
			return calResp;
		case "PPDB":
			ocuLoading = oculoding.get("PPDB");
			if (ocuLoading == null)
				ocuLoading = 1.0;
			BigDecimal ppdb = ppdbService.calculatePPDB(ridsumasu, payFrequency, 1.0, ocuLoading);
			calResp = setLodingDetails(ocuLoading, ppdb.doubleValue(), calResp);
			calResp.setPpdb(ppdb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + ppdb.doubleValue());
			calResp.setPpdbTerm(term);
			return calResp;
		case "PPDBS":
			ocuLoading = oculoding.get("PPDBS");
			if (ocuLoading == null)
				ocuLoading = 1.0;
			BigDecimal ppdbs = ppdbsService.calculatePPDBS(ridsumasu, payFrequency, 1.0, ocuLoading);
			calResp = setLodingDetails(ocuLoading, ppdbs.doubleValue(), calResp);
			calResp.setPpdbs(ppdbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + ppdbs.doubleValue());
			calResp.setPpdbsTerm(term);
			return calResp;
		case "CIB":
			ocuLoading = oculoding.get("CIB");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictCIB = rateCardCIBDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermCIB = maxTermToBenefictCIB > term ? term : maxTermToBenefictCIB;

			BigDecimal cib = cibService.calculateCIB(age, valiedTermCIB, new Date(), ridsumasu, payFrequency, 1.0,
					ocuLoading);
			calResp = setLodingDetails(ocuLoading, cib.doubleValue(), calResp);
			calResp.setCib(cib.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + cib.doubleValue());
			calResp.setCibTerm(valiedTermCIB);
			return calResp;
		case "CIBS":
			ocuLoading = oculoding.get("SCIB");
			if (ocuLoading == null)
				ocuLoading = 1.0;
			Integer maxTermToBenefictCIBS = rateCardCIBDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermCIBS = maxTermToBenefictCIBS > term ? term : maxTermToBenefictCIBS;

			BigDecimal scib = scibService.calculateSCIB(age, valiedTermCIBS, new Date(), ridsumasu, payFrequency, 1.0,
					ocuLoading);
			calResp = setLodingDetails(ocuLoading, scib.doubleValue(), calResp);
			calResp.setCibs(scib.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + scib.doubleValue());
			calResp.setCibsTerm(valiedTermCIBS);
			return calResp;

		case "CIBC":
			ocuLoading = oculoding.get("CIBC");
			if (ocuLoading == null)
				ocuLoading = 1.0;
			// ** 21-age < term term = 21-age else term

			term = term > (21 - age) ? (21 - age) : term;
			Integer maxTermToBenefictCIBC = rateCardCIBCDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermCIBC = maxTermToBenefictCIBC > term ? term : maxTermToBenefictCIBC;

			BigDecimal cibc = cibcService.calculateCIBC(age, valiedTermCIBC, new Date(), ridsumasu, payFrequency, 1.0);
			calResp = setLodingDetails(ocuLoading, cibc.doubleValue(), calResp);
			calResp.setCibc(calResp.getCibc() + cibc.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + cibc.doubleValue());
			calResp.setCibcTerm(valiedTermCIBC);
			return calResp;

		case "FEB":
			ocuLoading = oculoding.get("FEB");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictFEB = rateCardATFESCDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermFEB = maxTermToBenefictFEB > term ? term : maxTermToBenefictFEB;

			BigDecimal feb = febService.calculateFEB(age, valiedTermFEB, new Date(), ridsumasu, payFrequency, 1.0,
					ocuLoading);
			calResp = setLodingDetails(ocuLoading, feb.doubleValue(), calResp);
			calResp.setFeb(feb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + feb.doubleValue());
			calResp.setFebTerm(valiedTermFEB);
			return calResp;

		case "FEBS":
			ocuLoading = oculoding.get("FEBS");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictFEBS = rateCardATFESCDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermFEBS = maxTermToBenefictFEBS > term ? term : maxTermToBenefictFEBS;

			BigDecimal febs = febsService.calculateFEBS(age, valiedTermFEBS, new Date(), ridsumasu, payFrequency, 1.0,
					ocuLoading);
			calResp = setLodingDetails(ocuLoading, febs.doubleValue(), calResp);
			calResp.setFebs(febs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + febs.doubleValue());
			calResp.setFebsTerm(valiedTermFEBS);
			return calResp;

		case "MFIBD":
			ocuLoading = oculoding.get("MFIBD");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictMFIBD = rateCardMFIBDDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermMFIBD = maxTermToBenefictMFIBD > term ? term : maxTermToBenefictMFIBD;

			
			if(ridsumasu.doubleValue() > calResp.getBsaYearlyPremium()) {
				calResp.setErrorExist(true);
				BigDecimal val = new BigDecimal(calResp.getBsaYearlyPremium());
				calResp.setError("MFIBD MAx Value is " + val.setScale(2, RoundingMode.HALF_UP).doubleValue());
				return calResp;
			}
			
			
			BigDecimal mfibd = mfibdService.calculateMFIBD(age, valiedTermMFIBD, new Date(), ridsumasu, payFrequency,
					1.0, ocuLoading);
			calResp = setLodingDetails(ocuLoading, mfibd.doubleValue(), calResp);
			calResp.setMifdb(mfibd.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + mfibd.doubleValue());
			calResp.setMifdbTerm(valiedTermMFIBD);
			return calResp;

		case "MFIBT":
			ocuLoading = oculoding.get("MFIBT");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictMFIBT = rateCardMFIBTDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermMFIBT = maxTermToBenefictMFIBT > term ? term : maxTermToBenefictMFIBT;

			if(ridsumasu.doubleValue() > calResp.getBsaYearlyPremium()) {
				calResp.setErrorExist(true);
				BigDecimal val = new BigDecimal(calResp.getBsaYearlyPremium());
				calResp.setError("MFIBT MAx Value is " + val.setScale(2, RoundingMode.HALF_UP).doubleValue());
				return calResp;
			}
			
			BigDecimal mfibt = mfibtService.calculateMFIBT(age, valiedTermMFIBT, new Date(), ridsumasu, payFrequency,
					1.0, ocuLoading);
			calResp = setLodingDetails(ocuLoading, mfibt.doubleValue(), calResp);
			calResp.setMifdt(mfibt.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + mfibt.doubleValue());
			calResp.setMifdtTerm(valiedTermMFIBT);
			return calResp;
		case "MFIBDT":
			ocuLoading = oculoding.get("MFIBDT");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictMFIBDT = rateCardMFIBDTDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermMFIBDT = maxTermToBenefictMFIBDT > term ? term : maxTermToBenefictMFIBDT;

			if(ridsumasu.doubleValue() > calResp.getBsaYearlyPremium()) {
				calResp.setErrorExist(true);
				BigDecimal val = new BigDecimal(calResp.getBsaYearlyPremium());
				calResp.setError("MFIBDT MAx Value is " + val.setScale(2, RoundingMode.HALF_UP).doubleValue());
				return calResp;
			}
			
			BigDecimal mfibdt = mfibdtService.calculateMFIBDT(age, valiedTermMFIBDT, new Date(), ridsumasu,
					payFrequency, 1.0, ocuLoading);
			calResp = setLodingDetails(ocuLoading, mfibdt.doubleValue(), calResp);
			calResp.setMifdbt(mfibdt.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + mfibdt.doubleValue());
			calResp.setMifdbtTerm(valiedTermMFIBDT);
			return calResp;
		/*case "HRB":
			ocuLoading = oculoding.get("HRB");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			BigDecimal hrb = hrbService.calculateHRB(age, gender, ridsumasu, adultCount, childCount, new Date(),
					payFrequency, 1.0, ocuLoading);
			calResp.setHrb(hrb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + hrb.doubleValue());
			calResp.setHrbTerm(term);
			return calResp;
			*/
			
		case "HRBF":
			ocuLoading = oculoding.get("HRBF");
			if (ocuLoading == null)
				ocuLoading = 1.0;
			// Term calculation ///////
			
			Integer maxTermToBenefictHRBF = rateCardHRBFDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermHRBF = maxTermToBenefictHRBF > term ? term : maxTermToBenefictHRBF;
			

			//Integer valiedTermHRBF = 10;
			
			BigDecimal hrbf = null;
			try {
				hrbf = hrbfService.calculateHRBF(age, valiedTermHRBF , ridsumasu, adultCount, childCount, new Date(),
						payFrequency, 1.0, ocuLoading);
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
			ocuLoading = oculoding.get("HRBI");
			if (ocuLoading == null)
				ocuLoading = 1.0;
			// Term calculation ///////
			
			Integer maxTermToBenefictHRBI = rateCardHRBIDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermHRBI = maxTermToBenefictHRBI > term ? term : maxTermToBenefictHRBI;
			

			//Integer valiedTermHRBI = 10;
			
			BigDecimal hrbi = hrbiService.calculateHRBI (age, valiedTermHRBI , gender, ridsumasu, new Date(),
					payFrequency, 1.0, ocuLoading);
			calResp = setLodingDetails(ocuLoading, hrbi.doubleValue(), calResp);
			calResp.setHrbi(hrbi.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + hrbi.doubleValue());
			calResp.setHrbiTerm(valiedTermHRBI);
			return calResp;
			
		case "HRBIS":
			ocuLoading = oculoding.get("HRBIS");
			if (ocuLoading == null)
				ocuLoading = 1.0;
			// Term calculation ///////
			
			Integer maxTermToBenefictHRBIS = rateCardHRBIDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermHRBIS = maxTermToBenefictHRBIS > term ? term : maxTermToBenefictHRBIS;
			

			//Integer valiedTermHRBIS = 10;
			
			BigDecimal hrbis = hrbiService.calculateHRBI (age, valiedTermHRBIS , gender, ridsumasu, new Date(),
					payFrequency, 1.0, ocuLoading);
			calResp = setLodingDetails(ocuLoading, hrbis.doubleValue(), calResp);
			calResp.setHrbis(hrbis.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + hrbis.doubleValue());
			calResp.setHrbisTerm(valiedTermHRBIS);
			return calResp;
			
		case "HRBIC":
			ocuLoading = oculoding.get("HRBIC");
			if (ocuLoading == null)
				ocuLoading = 1.0;
			// Term calculation ///////
			
			Integer maxTermToBenefictHRBIC = 21;
			Integer valiedTermHRBIC = maxTermToBenefictHRBIC > term ? term : maxTermToBenefictHRBIC;
			

			//Integer valiedTermHRBIC = 10;
			
			BigDecimal hrbic = hrbiService.calculateHRBI (age, valiedTermHRBIC , gender, ridsumasu, new Date(),
					payFrequency, 1.0, ocuLoading);
			calResp = setLodingDetails(ocuLoading, hrbic.doubleValue(), calResp);
			Double resent =calResp.getHrbic() != null ?  calResp.getHrbic() : 0.0;
			calResp.setHrbic(hrbic.doubleValue()+resent);
			calResp.setAddBenif(calResp.getAddBenif() + hrbic.doubleValue());
			calResp.setHrbicTerm(valiedTermHRBIC);
			return calResp;

		case "SHCBF":
			ocuLoading = oculoding.get("SHCBF");
			if (ocuLoading == null)
				ocuLoading = 1.0;
			// Term calculation ///////
			/*
			Integer maxTermToBenefictHRBF = rateCardHRBFDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermHRBF = maxTermToBenefictHRBF > term ? term : maxTermToBenefictHRBF;
			*/

			Integer valiedTermSHCBF = 10;
			
			BigDecimal suhbf = shcbfService.calculateSHCBF(age, valiedTermSHCBF , ridsumasu, adultCount, childCount, new Date(),
					payFrequency, 1.0, ocuLoading);
			calResp = setLodingDetails(ocuLoading, suhbf.doubleValue(), calResp);
			calResp.setHrbf(suhbf.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + suhbf.doubleValue());
			calResp.setHrbfTerm(valiedTermSHCBF);
			return calResp;
			
		case "SUHRB":
			ocuLoading = oculoding.get("SUHRB");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictSUHRB = rateCardSUHRBDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermSUHRB = maxTermToBenefictSUHRB > term ? term : maxTermToBenefictSUHRB;

			BigDecimal suhrb = shcbiService.calculateSHCBI(age, gender, valiedTermSUHRB, ridsumasu, new Date(),
					payFrequency, 1.0, ocuLoading);
			calResp = setLodingDetails(ocuLoading, suhrb.doubleValue(), calResp);
			calResp.setShcbi(suhrb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + suhrb.doubleValue());
			calResp.setShcbiTerm(valiedTermSUHRB);
			calResp.setSuhrb(suhrb.doubleValue());
			//calResp.setAddBenif(calResp.getAddBenif() + suhrb.doubleValue());
			calResp.setSuhrbTerm(valiedTermSUHRB);
			return calResp;
		/*case "SUHRB":
			ocuLoading = oculoding.get("SUHRB");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictSUHRB = rateCardSUHRBDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermSUHRB = maxTermToBenefictSUHRB > term ? term : maxTermToBenefictSUHRB;

			BigDecimal suhrb = suhrbService.calculateSUHRB(age, gender, valiedTermSUHRB, ridsumasu, new Date(),
					payFrequency, 1.0, ocuLoading);
			calResp = setLodingDetails(ocuLoading, suhrb.doubleValue(), calResp);
			calResp.setSuhrb(suhrb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + suhrb.doubleValue());
			calResp.setSuhrbTerm(valiedTermSUHRB);
			return calResp;*/
			
		case "SUHRBS":
			ocuLoading = oculoding.get("SUHRBS");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictSUHRBS = rateCardSUHRBDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermSUHRBS = maxTermToBenefictSUHRBS > term ? term : maxTermToBenefictSUHRBS;

			BigDecimal suhrbs = suhrbsService.calculateSUHRBS(age, gender, valiedTermSUHRBS, ridsumasu, new Date(),
					payFrequency, 1.0, ocuLoading);
			calResp = setLodingDetails(ocuLoading, suhrbs.doubleValue(), calResp);
			calResp.setShcbis(suhrbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + suhrbs.doubleValue());
			calResp.setShcbisTerm(valiedTermSUHRBS);
			calResp.setSuhrbs(suhrbs.doubleValue());
			//calResp.setAddBenif(calResp.getAddBenif() + suhrbs.doubleValue());
			calResp.setSuhrbsTerm(valiedTermSUHRBS);
			return calResp;
			
		/*case "SUHRBS":
			ocuLoading = oculoding.get("SUHRBS");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictSUHRBS = rateCardSUHRBDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermSUHRBS = maxTermToBenefictSUHRBS > term ? term : maxTermToBenefictSUHRBS;

			BigDecimal suhrbs = suhrbsService.calculateSUHRBS(age, gender, valiedTermSUHRBS, ridsumasu, new Date(),
					payFrequency, 1.0, ocuLoading);
			calResp = setLodingDetails(ocuLoading, suhrbs.doubleValue(), calResp);
			calResp.setSuhrbs(suhrbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + suhrbs.doubleValue());
			calResp.setSuhrbsTerm(valiedTermSUHRBS);
			return calResp;*/

		case "SUHRBC":
			ocuLoading = oculoding.get("SUHRBC");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictSUHRBC = rateCardSUHRBDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermSUHRBC = maxTermToBenefictSUHRBC > term ? term : maxTermToBenefictSUHRBC;

			BigDecimal suhrbc = suhrbcService.calculateSUHRBC(age, gender, valiedTermSUHRBC, ridsumasu, new Date(),
					payFrequency, 1.0);

			calResp = setLodingDetails(ocuLoading, suhrbc.doubleValue(), calResp);
			calResp.setShcbic(calResp.getShcbic() + suhrbc.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + suhrbc.doubleValue());
			calResp.setShcbicTerm(valiedTermSUHRBC);
			calResp.setSuhrbc(calResp.getSuhrbc() + suhrbc.doubleValue());
			//calResp.setAddBenif(calResp.getAddBenif() + suhrbc.doubleValue());
			calResp.setSuhrbcTerm(valiedTermSUHRBC);
			return calResp;
		/*case "SUHRBC":
			ocuLoading = oculoding.get("SUHRBC");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictSUHRBC = rateCardSUHRBDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermSUHRBC = maxTermToBenefictSUHRBC > term ? term : maxTermToBenefictSUHRBC;

			BigDecimal suhrbc = suhrbcService.calculateSUHRBC(age, gender, valiedTermSUHRBC, ridsumasu, new Date(),
					payFrequency, 1.0);

			calResp = setLodingDetails(ocuLoading, suhrbc.doubleValue(), calResp);
			calResp.setSuhrbc(calResp.getSuhrbc() + suhrbc.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + suhrbc.doubleValue());
			calResp.setSuhrbcTerm(valiedTermSUHRBC);
			return calResp;*/

		case "HB":
			ocuLoading = oculoding.get("HB");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictHB = rateCardHBDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermHB = maxTermToBenefictHB > term ? term : maxTermToBenefictHB;

			if(ridsumasu.doubleValue() > calResp.getBsaYearlyPremium()*0.1) {
				calResp.setErrorExist(true);
				BigDecimal val = new BigDecimal(calResp.getBsaYearlyPremium()).multiply(new BigDecimal(0.1));
				calResp.setError("HB MAx Value is " + val.setScale(2, RoundingMode.HALF_UP).doubleValue());
				return calResp;
			}
			
			BigDecimal hb = hbService.calculateHB(age, valiedTermHB, new Date(), ridsumasu, payFrequency, 1.0,
					ocuLoading);
			
			
			calResp = setLodingDetails(ocuLoading, hb.doubleValue(), calResp);
			calResp.setHb(hb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + hb.doubleValue());
			calResp.setHbTerm(valiedTermHB);
			return calResp;

		case "HBS":
			ocuLoading = oculoding.get("HBS");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictHBS = rateCardHBDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermHBS = maxTermToBenefictHBS > term ? term : maxTermToBenefictHBS;

			BigDecimal hbs = hbsService.calculateHBS(28, valiedTermHBS, new Date(), ridsumasu, payFrequency, 1.0,
					ocuLoading);
			calResp = setLodingDetails(ocuLoading, hbs.doubleValue(), calResp);
			calResp.setHbs(hbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + hbs.doubleValue());
			calResp.setHbsTerm(valiedTermHBS);
			return calResp;

		case "HBC":
			ocuLoading = oculoding.get("HBC");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			term = term > (21 - age) ? (21 - age) : term;

			Integer maxTermToBenefictHBC = rateCardHBCDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermHBC = maxTermToBenefictHBC > term ? term : maxTermToBenefictHBC;

			// ** 21-age < term term = 21-age else term
			BigDecimal hbc = hbcService.calculateHBC(valiedTermHBC, new Date(), ridsumasu, payFrequency, 1.0);
			calResp = setLodingDetails(ocuLoading, hbc.doubleValue(), calResp);
			calResp.setHbc(calResp.getHbc() + hbc.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + hbc.doubleValue());
			calResp.setHbcTerm(valiedTermHBC);
			return calResp;

		case "WPB":
			ocuLoading = oculoding.get("WPB");
			System.out.println(ocuLoading + "   wpb oculoading");
			if (ocuLoading == null)
				ocuLoading = 1.0;
			System.out.println(ocuLoading + "   wpb oculoading");
			BigDecimal wpb = wpbService.calculateWPB(calResp, ocuLoading);
			calResp = setLodingDetails(ocuLoading, wpb.doubleValue(), calResp);
			calResp.setWpb(wpb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + wpb.doubleValue());
			calResp.setWpbTerm(term);
			return calResp;
		case "WPBS":
			ocuLoading = oculoding.get("WPBS");
			if (ocuLoading == null)
				ocuLoading = 1.0;
			BigDecimal wpbs = wpbsService.calculateWPBS(calResp, ocuLoading);
			calResp = setLodingDetails(ocuLoading, wpbs.doubleValue(), calResp);
			calResp.setWpbs(wpbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + wpbs.doubleValue());
			calResp.setWpbsTerm(term);
			return calResp;

		case "JLB":
			ocuLoading = oculoding.get("JLB");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictJLB = rateCardJLBDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermJLB = maxTermToBenefictJLB > term ? term : maxTermToBenefictJLB;

			BigDecimal jlb = jlbService.calculateJLB(age, valiedTermJLB, inRate, gender, new Date(), loan, ocuLoading);
			calResp = setLodingDetails(ocuLoading, jlb.doubleValue(), calResp);
			calResp.setJlb(jlb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + jlb.doubleValue());
			calResp.setJlbTerm(valiedTermJLB);
			return calResp;

		case "JLBPL":
			ocuLoading = oculoding.get("JLBPL");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictJLBPL = rateCardJLBDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermJLBPL = maxTermToBenefictJLBPL > term ? term : maxTermToBenefictJLBPL;

			BigDecimal jlbpl = jlbplService.calculateJLBPL(age, valiedTermJLBPL, inRate, gender, new Date(), loan,
					ocuLoading);
			calResp = setLodingDetails(ocuLoading, jlbpl.doubleValue(), calResp);
			calResp.setJlbpl(jlbpl.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + jlbpl.doubleValue());
			calResp.setJlbplTerm(valiedTermJLBPL);
			return calResp;

		case "TPDDTA":
			ocuLoading = oculoding.get("TPDDTA");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictTPDDTA = rateCardTPDDTADao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermTPDDTA = maxTermToBenefictTPDDTA > term ? term : maxTermToBenefictTPDDTA;

			BigDecimal tpddta = tpddtaService.calculateTPDDTA(age, valiedTermTPDDTA, inRate, gender, new Date(), loan,
					ocuLoading);
			calResp = setLodingDetails(ocuLoading, tpddta.doubleValue(), calResp);
			calResp.setTpddta(tpddta.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + tpddta.doubleValue());
			calResp.setTpddtaTerm(valiedTermTPDDTA);
			return calResp;

		case "TPDDTAS":
			ocuLoading = oculoding.get("TPDDTAS");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictTPDDTAS = rateCardTPDDTASDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermTPDDTAS = maxTermToBenefictTPDDTAS > term ? term : maxTermToBenefictTPDDTAS;

			BigDecimal tpddtas = tpddtasService.calculateTPDDTAS(age, valiedTermTPDDTAS, inRate, gender, new Date(),
					loan, ocuLoading);
			calResp = setLodingDetails(ocuLoading, tpddtas.doubleValue(), calResp);
			calResp.setTpddtas(tpddtas.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + tpddtas.doubleValue());
			calResp.setTpddtasTerm(valiedTermTPDDTAS);
			return calResp;

		case "TPDDTAPL":
			ocuLoading = oculoding.get("TPDDTAPL");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictTPDDTAPL = rateCardTPDDTADao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermTPDDTAPL = maxTermToBenefictTPDDTAPL > term ? term : maxTermToBenefictTPDDTAPL;

			BigDecimal tpddtapl = tpddtaplService.calculateTPDDTAPL(age, valiedTermTPDDTAPL, inRate, gender, new Date(),
					loan, ocuLoading);
			calResp = setLodingDetails(ocuLoading, tpddtapl.doubleValue(), calResp);
			calResp.setTpddtapl(tpddtapl.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + tpddtapl.doubleValue());
			calResp.setTpddtaplTerm(valiedTermTPDDTAPL);
			return calResp;

		case "TPDDTASPL":
			ocuLoading = oculoding.get("TPDDTASPL");
			if (ocuLoading == null)
				ocuLoading = 1.0;

			Integer maxTermToBenefictTPDDTASPL = rateCardTPDDTASDao.findFirstByOrderByTermDesc().getTerm();
			Integer valiedTermTPDDTASPL = maxTermToBenefictTPDDTASPL > term ? term : maxTermToBenefictTPDDTASPL;

			BigDecimal tpddtaspl = tpddtasplService.calculateTPDDTASPL(age, valiedTermTPDDTASPL, inRate, gender,
					new Date(), loan, ocuLoading);
			calResp = setLodingDetails(ocuLoading, tpddtaspl.doubleValue(), calResp);
			calResp.setTpddtaspl(tpddtaspl.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + tpddtaspl.doubleValue());
			calResp.setTpddtasplTerm(valiedTermTPDDTASPL);
			return calResp;

		/////////////////////////////////////////////////////////////////////////
		default:
			//calResp.setHrbsTerm(term);
			calResp.setHrbfsTerm(term);
			return calResp;
		}

	}

	private QuotationQuickCalResponse setLodingDetails(double ocuLoading, double premium,
			QuotationQuickCalResponse calResp) {
		
		Double bsa = new BigDecimal(premium).divide(new BigDecimal(ocuLoading),RoundingMode.HALF_UP).doubleValue();
		
		System.out.println("bsa for occu : " +bsa);
		
		calResp.setWithoutLoadingTot(calResp.getWithoutLoadingTot()+bsa);
		calResp.setOccuLodingTot(calResp.getOccuLodingTot()+(premium-bsa));
		
		return calResp;
	}

}
