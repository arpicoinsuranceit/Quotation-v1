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
import org.arpicoinsurance.groupit.main.service.rider.SFPOService;
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

						System.out.println(term + ";;;;;;;;;;;;;;;;;; child");
						
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

		Double ocuLoading=1.0;
		switch (type) {
		case "BSAS":
			ocuLoading=oculoding.get("SCB");
			if(ocuLoading==null)
				ocuLoading=1.0;
			BigDecimal scb = scbService.calculateSCB(age, term, new Date(), ridsumasu, payFrequency, 1.0,
					ocuLoading);
			calResp.setBsas(scb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + scb.doubleValue());
			calResp.setBsasTerm(term);
			return calResp;
		case "ADB":
			ocuLoading=oculoding.get("ADB");
			if(ocuLoading==null)
				ocuLoading=1.0;
			BigDecimal adb = adbService.calculateADB(ridsumasu, payFrequency, 1.0, ocuLoading);
			calResp.setAdb(adb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + adb.doubleValue());
			calResp.setAdbTerm(term);
			return calResp;
		case "SFPO":
			ocuLoading=oculoding.get("SFPO");
			if(ocuLoading==null)
				ocuLoading=1.0;
			BigDecimal sfpo = sfpoService.calculateSFPO(age, term,new Date(),ridsumasu, payFrequency, 1.0, ocuLoading);
			calResp.setSfpo(sfpo.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + sfpo.doubleValue());
			calResp.setSfpoTerm(term);
			return calResp;
		case "ADBS":
			ocuLoading=oculoding.get("ADBS");
			if(ocuLoading==null)
				ocuLoading=1.0;
			BigDecimal adbs = adbsService.calculateADBS(ridsumasu, payFrequency, 1.0, ocuLoading);
			calResp.setAdbs(adbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + adbs.doubleValue());
			calResp.setAdbsTerm(term);
			return calResp;
		case "ATPB":
			System.out.println("called/////////////////////////////////////////////////////");
			System.out.println(ridsumasu);
			ocuLoading=oculoding.get("ATPB");
			if(ocuLoading==null)
				ocuLoading=1.0;
			BigDecimal atpb = atpbService.calculateATPB(age, term, new Date(), ridsumasu, payFrequency, 1.0, ocuLoading);
			calResp.setAtpb(atpb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + atpb.doubleValue());
			calResp.setAtpbTerm(term);
			return calResp;
		case "TPDASB":
			ocuLoading=oculoding.get("TPDASB");
			if(ocuLoading==null)
				ocuLoading=1.0;
			BigDecimal tpdasb = tpdasbService.calculateTPDASB(age, new Date(), ridsumasu, payFrequency, 1.0,
					ocuLoading);
			calResp.setTpdasb(tpdasb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + tpdasb.doubleValue());
			calResp.setTpdasbTerm(term);
			return calResp;
		case "TPDASBS":
			ocuLoading=oculoding.get("TPDASBS");
			if(ocuLoading==null)
				ocuLoading=1.0;
			BigDecimal tpdasbs = tpdasbsbService.calculateTPDASBS(age, new Date(), ridsumasu, payFrequency, 1.0,
					ocuLoading);
			calResp.setTpdasbs(tpdasbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + tpdasbs.doubleValue());
			calResp.setTpdasbsTerm(term);
			return calResp;
		case "TPDB":
			ocuLoading=oculoding.get("TPDB");
			if(ocuLoading==null)
				ocuLoading=1.0;
			BigDecimal tpdb = tpdbService.calculateTPDB(ridsumasu, payFrequency, 1.0, ocuLoading);
			calResp.setTpdb(tpdb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + tpdb.doubleValue());
			calResp.setTpdbTerm(term);
			return calResp;
		case "TPDBS":
			ocuLoading=oculoding.get("TPDBS");
			if(ocuLoading==null)
				ocuLoading=1.0;
			BigDecimal tpdbs = tpdbsService.calculateTPDBS(ridsumasu, payFrequency, 1.0, ocuLoading);
			calResp.setTpdbs(tpdbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + tpdbs.doubleValue());
			calResp.setTpdbsTerm(term);
			return calResp;
		case "PPDB":
			ocuLoading=oculoding.get("PPDB");
			if(ocuLoading==null)
				ocuLoading=1.0;
			BigDecimal ppdb = ppdbService.calculatePPDB(ridsumasu, payFrequency, 1.0, ocuLoading);
			calResp.setPpdb(ppdb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + ppdb.doubleValue());
			calResp.setPpdbTerm(term);
			return calResp;
		case "PPDBS":
			ocuLoading=oculoding.get("PPDBS");
			if(ocuLoading==null)
				ocuLoading=1.0;
			BigDecimal ppdbs = ppdbsService.calculatePPDBS(ridsumasu, payFrequency, 1.0, ocuLoading);
			calResp.setPpdbs(ppdbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + ppdbs.doubleValue());
			calResp.setPpdbsTerm(term);
			return calResp;
		case "CIB":
			ocuLoading=oculoding.get("CIB");
			if(ocuLoading==null)
				ocuLoading=1.0;
			BigDecimal cib = cibService.calculateCIB(age, term, new Date(), ridsumasu, payFrequency, 1.0,
					ocuLoading);
			calResp.setCib(cib.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + cib.doubleValue());
			calResp.setCibTerm(term);
			return calResp;
		case "CIBS":
			ocuLoading=oculoding.get("SCIB");
			if(ocuLoading==null)
				ocuLoading=1.0;
			BigDecimal scib = scibService.calculateSCIB(age, term, new Date(), ridsumasu, payFrequency, 1.0,
					ocuLoading);
			calResp.setCibs(scib.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + scib.doubleValue());
			calResp.setCibsTerm(term);
			return calResp;
		case "CIBC":
			ocuLoading=oculoding.get("CIBC");
			if(ocuLoading==null)
				ocuLoading=1.0;
			// ** 21-age < term term = 21-age else term
			BigDecimal cibc = cibcService.calculateCIBC(age, term > (21 - 6) ? (21 - 6) : term, new Date(), ridsumasu,
					payFrequency, 1.0);
			calResp.setCibc(calResp.getCibc() + cibc.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + cibc.doubleValue());
			calResp.setCibcTerm(term);
			return calResp;
		case "FEB":
			ocuLoading=oculoding.get("FEB");
			if(ocuLoading==null)
				ocuLoading=1.0;
			BigDecimal feb = febService.calculateFEB(age, term, new Date(), ridsumasu, payFrequency, 1.0,
					ocuLoading);
			calResp.setFeb(feb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + feb.doubleValue());
			calResp.setFebTerm(term);
			return calResp;
		case "FEBS":
			ocuLoading=oculoding.get("FEBS");
			if(ocuLoading==null)
				ocuLoading=1.0;
			BigDecimal febs = febsService.calculateFEBS(age, term, new Date(), ridsumasu, payFrequency, 1.0,
					ocuLoading);
			calResp.setFebs(febs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + febs.doubleValue());
			calResp.setFebsTerm(term);
			return calResp;

		case "MFIBD":
			ocuLoading=oculoding.get("MFIBD");
			if(ocuLoading==null)
				ocuLoading=1.0;
			BigDecimal mfibd = mfibdService.calculateMFIBD(age, term, new Date(), ridsumasu, payFrequency, 1.0,
					ocuLoading);
			calResp.setMifdb(mfibd.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + mfibd.doubleValue());
			calResp.setMifdbTerm(term);
			return calResp;
		case "MFIBT":
			ocuLoading=oculoding.get("MFIBT");
			if(ocuLoading==null)
				ocuLoading=1.0;
			BigDecimal mfibt = mfibtService.calculateMFIBT(age, term, new Date(), ridsumasu, payFrequency, 1.0,
					ocuLoading);
			calResp.setMifdt(mfibt.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + mfibt.doubleValue());
			calResp.setMifdtTerm(term);
			return calResp;
		case "MFIBDT":
			ocuLoading=oculoding.get("MFIBDT");
			if(ocuLoading==null)
				ocuLoading=1.0;
			BigDecimal mfibdt = mfibdtService.calculateMFIBDT(age, term, new Date(), ridsumasu, payFrequency, 1.0,
					ocuLoading);
			calResp.setMifdbt(mfibdt.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + mfibdt.doubleValue());
			calResp.setMifdbtTerm(term);
			return calResp;
		case "HRB":
			ocuLoading=oculoding.get("HRB");
			if(ocuLoading==null)
				ocuLoading=1.0;
			BigDecimal hrb = hrbService.calculateHRB(age, gender, ridsumasu, adultCount, childCount, new Date(),
					payFrequency, 1.0, ocuLoading);
			calResp.setHrb(hrb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + hrb.doubleValue());
			calResp.setHrbTerm(term);
			return calResp;
		case "SUHRB":
			ocuLoading=oculoding.get("SUHRB");
			if(ocuLoading==null)
				ocuLoading=1.0;
			BigDecimal suhrb = suhrbService.calculateSUHRB(age, gender, term, ridsumasu, new Date(), payFrequency, 1.0,
					ocuLoading);
			calResp.setSuhrb(suhrb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + suhrb.doubleValue());
			calResp.setSuhrbTerm(term);
			return calResp;
		case "SUHRBS":
			ocuLoading=oculoding.get("SUHRBS");
			if(ocuLoading==null)
				ocuLoading=1.0;
			BigDecimal suhrbs = suhrbsService.calculateSUHRBS(age, gender, term, ridsumasu, new Date(), payFrequency,
					1.0, ocuLoading);
			calResp.setSuhrbs(suhrbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + suhrbs.doubleValue());
			calResp.setSuhrbsTerm(term);
			return calResp;
		case "SUHRBC":
			ocuLoading=oculoding.get("SUHRBC");
			if(ocuLoading==null)
				ocuLoading=1.0;
			BigDecimal suhrbc = suhrbcService.calculateSUHRBC(age, gender, term, ridsumasu, new Date(), payFrequency,
					1.0);
			calResp.setSuhrbc(calResp.getSuhrbc() + suhrbc.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + suhrbc.doubleValue());
			calResp.setSuhrbcTerm(term);
			return calResp;
		case "HB":
			ocuLoading=oculoding.get("HB");
			if(ocuLoading==null)
				ocuLoading=1.0;
			BigDecimal hb = hbService.calculateHB(age, term, new Date(), ridsumasu, payFrequency, 1.0,
					ocuLoading);
			calResp.setHb(hb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + hb.doubleValue());
			calResp.setHbTerm(term);
			return calResp;
		case "HBS":
			ocuLoading=oculoding.get("HBS");
			if(ocuLoading==null)
				ocuLoading=1.0;
			BigDecimal hbs = hbsService.calculateHBS(28, term, new Date(), ridsumasu, payFrequency, 1.0,
					ocuLoading);
			calResp.setHbs(hbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + hbs.doubleValue());
			calResp.setHbsTerm(term);
			return calResp;
		case "HBC":
			ocuLoading=oculoding.get("HBC");
			if(ocuLoading==null)
				ocuLoading=1.0;
			// ** 21-age < term term = 21-age else term
			BigDecimal hbc = hbcService.calculateHBC(term > (21 - 6) ? (21 - 6) : term, new Date(), ridsumasu,
					payFrequency, 1.0);
			calResp.setHbc(calResp.getHbc() + hbc.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + hbc.doubleValue());
			calResp.setHbcTerm(term);
			return calResp;
		case "WPB":
			ocuLoading=oculoding.get("WPB");
			if(ocuLoading==null)
				ocuLoading=1.0;
			BigDecimal wpb = wpbService.calculateWPB(calResp);
			calResp.setWpb(wpb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + wpb.doubleValue());
			calResp.setWpbTerm(term);
			return calResp;
		case "WPBS":
			ocuLoading=oculoding.get("WPBS");
			if(ocuLoading==null)
				ocuLoading=1.0;
			BigDecimal wpbs = wpbsService.calculateWPBS(calResp);
			calResp.setWpbs(wpbs.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + wpbs.doubleValue());
			calResp.setWpbsTerm(term);
			return calResp;
			
		case "JLB":
			ocuLoading=oculoding.get("JLB");
			if(ocuLoading==null)
				ocuLoading=1.0;
			BigDecimal jlb = jlbService.calculateJLB(age, term, inRate, gender, new Date(), loan, ocuLoading);
			calResp.setJlb(jlb.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + jlb.doubleValue());
			calResp.setJlbTerm(term);
			return calResp;
			
		case "JLBPL":
			ocuLoading=oculoding.get("JLBPL");
			if(ocuLoading==null)
				ocuLoading=1.0;
			BigDecimal jlbpl = jlbplService.calculateJLBPL(age, term, inRate, gender, new Date(), loan, ocuLoading);
			calResp.setJlbpl(jlbpl.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + jlbpl.doubleValue());
			calResp.setJlbplTerm(term);
			return calResp;
			
		case "TPDDTA":
			ocuLoading=oculoding.get("TPDDTA");
			if(ocuLoading==null)
				ocuLoading=1.0;
			BigDecimal tpddta = tpddtaService.calculateTPDDTA(age, term, inRate, gender, new Date(), loan, ocuLoading);
			calResp.setTpddta(tpddta.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + tpddta.doubleValue());
			calResp.setTpddtaTerm(term);
			return calResp;
			
		case "TPDDTAS":
			ocuLoading=oculoding.get("TPDDTAS");
			if(ocuLoading==null)
				ocuLoading=1.0;
			BigDecimal tpddtas = tpddtasService.calculateTPDDTAS(age, term, inRate, gender, new Date(), loan, ocuLoading);
			calResp.setTpddtas(tpddtas.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + tpddtas.doubleValue());
			calResp.setTpddtasTerm(term);
			return calResp;
			
		case "TPDDTAPL":
			ocuLoading=oculoding.get("TPDDTAPL");
			if(ocuLoading==null)
				ocuLoading=1.0;
			BigDecimal tpddtapl = tpddtaplService.calculateTPDDTAPL(age, term, inRate, gender, new Date(), loan, ocuLoading);
			calResp.setTpddtapl(tpddtapl.doubleValue());
			calResp.setAddBenif(calResp.getAddBenif() + tpddtapl.doubleValue());
			calResp.setTpddtaplTerm(term);
			return calResp;
			
		case "TPDDTASPL":
			ocuLoading=oculoding.get("TPDDTASPL");
			if(ocuLoading==null)
				ocuLoading=1.0;
			BigDecimal tpddtaspl = tpddtasplService.calculateTPDDTASPL(age, term, inRate, gender, new Date(), loan, ocuLoading);
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
