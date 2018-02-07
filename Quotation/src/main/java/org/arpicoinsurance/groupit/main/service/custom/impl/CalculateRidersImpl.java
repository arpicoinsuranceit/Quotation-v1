package org.arpicoinsurance.groupit.main.service.custom.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

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

	@Override
	public QuotationQuickCalResponse getRiders(QuotationCalculation quotationCalculation,
			QuotationQuickCalResponse calResp) throws Exception {
		Integer adultCount = 1;
		Integer childCount = 0;

		Double inrate=0.0;
		if(quotationCalculation.get_personalInfo().getIntrate()!=null && quotationCalculation.get_personalInfo().getIntrate()>0 ) {
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
		}

		if (quotationCalculation.get_personalInfo().getSage() != null
				&& quotationCalculation.get_personalInfo().getSgenger() != null
				&& quotationCalculation.get_personalInfo().getSocu() != null) {

			if (_sRiders != null) {

				for (Benifict benifict : _sRiders) {

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
						Integer term = calculateBenefictTerm.calculateBenifictTerm(children.get_cAge(),
								benifict.getType(), quotationCalculation.get_personalInfo().getTerm());
						String benfName = benifict.getType();

						switch (benfName) {
						case "CIBC":
							if (children.is_cCibc()) {
								calculateBenifPremium(benifict.getType(), benifict.getSumAssured(),
										children.get_cTitle(), children.get_cAge(),
										quotationCalculation.get_personalInfo().getFrequance(), term, 0, calResp,
										adultCount, childCount, -1.0, -1.0);
							}
							break;

						case "HBC":
							if (children.is_cHbc()) {
								calculateBenifPremium(benifict.getType(), benifict.getSumAssured(),
										children.get_cTitle(), children.get_cAge(),
										quotationCalculation.get_personalInfo().getFrequance(), term, 0, calResp,
										adultCount, childCount, -1.0, -1.0);
							}
							break;

						case "SUHRBC":
							if (children.is_cSuhrbc()) {
								calculateBenifPremium(benifict.getType(), benifict.getSumAssured(),
										children.get_cTitle(), children.get_cAge(),
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

		calculateBenifPremium(type, bsa, gender, age, frequance, term, ocu, calResp, adultCount, childCount, bsa, inrate);

		return calResp;
	}

	@Override
	public QuotationQuickCalResponse calculateBenifPremium(String type, Double ridsumasu, String gender, Integer age,
			String payFrequency, Integer term, Integer occupation_id, QuotationQuickCalResponse calResp,
			Integer adultCount, Integer childCount, Double loan, Double inRate) throws Exception {
		Map<String, Double> oculoding = occupationLoding.getOccupationLoding(occupation_id);

		switch (type) {
		case "BSAS":
			BigDecimal scb = scbService.calculateSCB(age, term, new Date(), ridsumasu, payFrequency, 1.0,
					oculoding.get("SCB"));
			calResp.setBsas(scb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + scb.doubleValue());
			calResp.setBsasTerm(term);
			return calResp;
		case "ADB":
			BigDecimal adb = adbService.calculateADB(ridsumasu, payFrequency, 1.0, oculoding.get("ADB"));
			calResp.setAdb(adb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + adb.doubleValue());
			calResp.setAdbTerm(term);
			return calResp;
		case "ADBS":
			BigDecimal adbs = adbsService.calculateADBS(ridsumasu, payFrequency, 1.0, 1.0);
			calResp.setAdbs(adbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + adbs.doubleValue());
			calResp.setAdbsTerm(term);
			return calResp;
		case "ATPB":
			BigDecimal atpb = atpbService.calculateATPB(age, term, new Date(), ridsumasu, payFrequency, 1.0, 1.0);
			calResp.setAtpb(atpb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + atpb.doubleValue());
			calResp.setAtpbTerm(term);
			return calResp;
		case "TPDASB":
			BigDecimal tpdasb = tpdasbService.calculateTPDASB(age, new Date(), ridsumasu, payFrequency, 1.0,
					oculoding.get("TPDASB"));
			calResp.setTpdasb(tpdasb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + tpdasb.doubleValue());
			calResp.setTpdasbTerm(term);
			return calResp;
		case "TPDASBS":
			BigDecimal tpdasbs = tpdasbsbService.calculateTPDASBS(age, new Date(), ridsumasu, payFrequency, 1.0,
					oculoding.get("TPDASBS"));
			calResp.setTpdasbs(tpdasbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + tpdasbs.doubleValue());
			calResp.setTpdasbsTerm(term);
			return calResp;
		case "TPDB":
			BigDecimal tpdb = tpdbService.calculateTPDB(ridsumasu, payFrequency, 1.0, oculoding.get("TPDB"));
			calResp.setTpdb(tpdb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + tpdb.doubleValue());
			calResp.setTpdbTerm(term);
			return calResp;
		case "TPDBS":
			BigDecimal tpdbs = tpdbsService.calculateTPDBS(ridsumasu, payFrequency, 1.0, oculoding.get("TPDBS"));
			calResp.setTpdbs(tpdbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + tpdbs.doubleValue());
			calResp.setTpdbsTerm(term);
			return calResp;
		case "PPDB":
			BigDecimal ppdb = ppdbService.calculatePPDB(ridsumasu, payFrequency, 1.0, oculoding.get("PPDB"));
			calResp.setPpdb(ppdb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + ppdb.doubleValue());
			calResp.setPpdbTerm(term);
			return calResp;
		case "PPDBS":
			BigDecimal ppdbs = ppdbsService.calculatePPDBS(ridsumasu, payFrequency, 1.0, oculoding.get("PPDBS"));
			calResp.setPpdbs(ppdbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + ppdbs.doubleValue());
			calResp.setPpdbsTerm(term);
			return calResp;
		case "CIB":
			BigDecimal cib = cibService.calculateCIB(age, term, new Date(), ridsumasu, payFrequency, 1.0,
					oculoding.get("CIB"));
			calResp.setCib(cib.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + cib.doubleValue());
			calResp.setCibTerm(term);
			return calResp;
		case "CIBS":
			BigDecimal scib = scibService.calculateSCIB(age, term, new Date(), ridsumasu, payFrequency, 1.0,
					oculoding.get("SCIB"));
			calResp.setCibs(scib.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + scib.doubleValue());
			calResp.setCibsTerm(term);
			return calResp;
		case "CIBC":
			// ** 21-age < term term = 21-age else term
			BigDecimal cibc = cibcService.calculateCIBC(age, term > (21 - 6) ? (21 - 6) : term, new Date(), ridsumasu,
					payFrequency, 1.0);
			calResp.setCibc(calResp.getCibc() + cibc.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + cibc.doubleValue());
			calResp.setCibcTerm(term);
			return calResp;
		case "FEB":
			BigDecimal feb = febService.calculateFEB(age, term, new Date(), ridsumasu, payFrequency, 1.0,
					oculoding.get("FEB"));
			calResp.setFeb(feb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + feb.doubleValue());
			calResp.setFebTerm(term);
			return calResp;
		case "FEBS":
			BigDecimal febs = febsService.calculateFEBS(age, term, new Date(), ridsumasu, payFrequency, 1.0,
					oculoding.get("FEBS"));
			calResp.setFebs(febs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + febs.doubleValue());
			calResp.setFebsTerm(term);
			return calResp;

		case "MFIBD":
			BigDecimal mfibd = mfibdService.calculateMFIBD(age, term, new Date(), ridsumasu, payFrequency, 1.0,
					oculoding.get("MFIBD"));
			calResp.setMifdb(mfibd.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + mfibd.doubleValue());
			calResp.setMifdbTerm(term);
			return calResp;
		case "MFIBT":
			BigDecimal mfibt = mfibtService.calculateMFIBT(age, term, new Date(), ridsumasu, payFrequency, 1.0,
					oculoding.get("MFIBT"));
			calResp.setMifdt(mfibt.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + mfibt.doubleValue());
			calResp.setMifdtTerm(term);
			return calResp;
		case "MFIBDT":
			BigDecimal mfibdt = mfibdtService.calculateMFIBDT(age, term, new Date(), ridsumasu, payFrequency, 1.0,
					oculoding.get("MFIBDT"));
			calResp.setMifdbt(mfibdt.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + mfibdt.doubleValue());
			calResp.setMifdbtTerm(term);
			return calResp;
		case "HRB":
			BigDecimal hrb = hrbService.calculateHRB(age, gender, ridsumasu, adultCount, childCount, new Date(),
					payFrequency, 1.0, oculoding.get("HRB"));
			calResp.setHrb(hrb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + hrb.doubleValue());
			calResp.setHrbTerm(term);
			return calResp;
		case "SUHRB":
			BigDecimal suhrb = suhrbService.calculateSUHRB(age, gender, term, ridsumasu, new Date(), payFrequency, 1.0,
					oculoding.get("SUHRB"));
			calResp.setSuhrb(suhrb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + suhrb.doubleValue());
			calResp.setSuhrbTerm(term);
			return calResp;
		case "SUHRBS":
			BigDecimal suhrbs = suhrbsService.calculateSUHRBS(age, gender, term, ridsumasu, new Date(), payFrequency,
					1.0, oculoding.get("SUHRBS"));
			calResp.setSuhrbs(suhrbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + suhrbs.doubleValue());
			calResp.setSuhrbsTerm(term);
			return calResp;
		case "SUHRBC":
			BigDecimal suhrbc = suhrbcService.calculateSUHRBC(age, gender, term, ridsumasu, new Date(), payFrequency,
					1.0);
			calResp.setSuhrbc(calResp.getSuhrbc() + suhrbc.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + suhrbc.doubleValue());
			calResp.setSuhrbcTerm(term);
			return calResp;
		case "HB":
			BigDecimal hb = hbService.calculateHB(age, term, new Date(), ridsumasu, payFrequency, 1.0,
					oculoding.get("HB"));
			calResp.setHb(hb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + hb.doubleValue());
			calResp.setHbTerm(term);
			return calResp;
		case "HBS":
			BigDecimal hbs = hbsService.calculateHBS(28, term, new Date(), ridsumasu, payFrequency, 1.0,
					oculoding.get("HBS"));
			calResp.setHbs(hbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + hbs.doubleValue());
			calResp.setHbsTerm(term);
			return calResp;
		case "HBC":
			// ** 21-age < term term = 21-age else term
			BigDecimal hbc = hbcService.calculateHBC(term > (21 - 6) ? (21 - 6) : term, new Date(), ridsumasu,
					payFrequency, 1.0);
			calResp.setHbc(calResp.getHbc() + hbc.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + hbc.doubleValue());
			calResp.setHbcTerm(term);
			return calResp;
		case "WPB":
			BigDecimal wpb = wpbService.calculateWPB(calResp);
			calResp.setWpb(wpb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + wpb.doubleValue());
			calResp.setWpbTerm(term);
			return calResp;
		case "WPBS":
			BigDecimal wpbs = wpbsService.calculateWPBS(calResp);
			calResp.setWpbs(wpbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + wpbs.doubleValue());
			calResp.setWpbsTerm(term);
			return calResp;
			
		case "JLB":
			BigDecimal jlb = jlbService.calculateJLB(age, term, inRate, gender, new Date(), loan, oculoding.get("JLB"));
			calResp.setJlb(jlb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + jlb.doubleValue());
			calResp.setJlbTerm(term);
			return calResp;
			
		case "JLBPL":
			BigDecimal jlbpl = jlbplService.calculateJLBPL(age, term, inRate, gender, new Date(), loan, oculoding.get("JLB"));
			calResp.setJlbpl(jlbpl.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + jlbpl.doubleValue());
			calResp.setJlbplTerm(term);
			return calResp;
			
		case "TPDDTA":
			BigDecimal tpddta = tpddtaService.calculateTPDDTA(age, term, inRate, gender, new Date(), loan, oculoding.get("JLB"));
			calResp.setTpddta(tpddta.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + tpddta.doubleValue());
			calResp.setTpddtaTerm(term);
			return calResp;
			
		case "TPDDTAS":
			BigDecimal tpddtas = tpddtasService.calculateTPDDTAS(age, term, inRate, gender, new Date(), loan, oculoding.get("JLB"));
			calResp.setTpddtas(tpddtas.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + tpddtas.doubleValue());
			calResp.setTpddtasTerm(term);
			return calResp;
			
		case "TPDDTAPL":
			BigDecimal tpddtapl = tpddtaplService.calculateTPDDTAPL(age, term, inRate, gender, new Date(), loan, oculoding.get("JLB"));
			calResp.setTpddtapl(tpddtapl.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + tpddtapl.doubleValue());
			calResp.setTpddtaplTerm(term);
			return calResp;
			
		case "TPDDTASPL":
			BigDecimal tpddtaspl = tpddtasplService.calculateTPDDTASPL(age, term, inRate, gender, new Date(), loan, oculoding.get("JLB"));
			calResp.setTpddtaspl(tpddtaspl.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + tpddtaspl.doubleValue());
			calResp.setTpddtasplTerm(term);
			return calResp;
			
			
/////////////////////////////////////////////////////////////////////////
		default:
			return calResp;
		}

	}

}
