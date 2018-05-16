package org.arpicoinsurance.groupit.main.helper;

import java.util.ArrayList;
import java.util.HashMap;

public class QuotationQuickCalResponse {
	private Double basicSumAssured = 0.00;
	private Double extraOE= 0.00;
	private Double addBenif= 0.00;
	private Double totPremium= 0.00;
	private Double occuLodingTot = 0.0;
	private Double withoutLoadingTot = 0.0;
	private Double bsaYearlyPremium = 0.0;
	
	
	private Double l2=0.00;
	private Double at6= 0.00;
	private Double at8= 0.00;
	private Double at10= 0.00;
	
	private Double guaranteed= 0.00;
	
	private Double adb= 0.00;
	private Double adbBsa= 0.00;
	private int adbTerm = 0;
	
	private Double sfpo= 0.00;
	private Double sfpoBsa= 0.00;
	private int sfpoTerm = 0;
	
	private Double atpb= 0.00;
	private Double atpbBsa= 0.00;
	private int atpbTerm = 0;
	
	private Double cib= 0.00;
	private Double cibBsa= 0.00;
	private int cibTerm = 0;
	
	private Double feb= 0.00;
	private Double febBsa= 0.00;
	private int febTerm = 0;
	
	private Double hb= 0.00;
	private Double hbBsa= 0.00;
	private int hbTerm = 0;
	
	//private Double hrb= 0.00;
	//private int hrbTerm = 0;
	private Double mifdb= 0.00;
	private Double mifdbBsa= 0.00;
	private int mifdbTerm = 0;
	
	private Double mifdbt= 0.00;
	private Double mifdbtBsa= 0.00;
	private int mifdbtTerm = 0;
	
	private Double mifdt= 0.00;
	private Double mifdtBsa= 0.00;
	private int mifdtTerm = 0;
	
	private Double ppdb= 0.00;
	private Double ppdbBsa= 0.00;
	private int ppdbTerm = 0;
	
	private Double suhrb= 0.00;
	private Double suhrbBsa= 0.00;
	private int suhrbTerm = 0;
	
	private Double tpdasb= 0.00;
	private Double tpdasbBsa= 0.00;
	private int tpdasbTerm = 0;
	
	private Double tpdb= 0.00;
	private Double tpdbBsa= 0.00;
	private int tpdbTerm = 0;
	
	private Double wpb= 0.00;
	private Double wpbBsa= 0.00;
	private int wpbTerm = 0;
	
	private Double bsas= 0.00;
	private Double bsasBsa= 0.00;
	private int bsasTerm = 0;
	
	private Double adbs= 0.00;
	private Double adbsBsa= 0.00;
	private int adbsTerm = 0;
	
	private Double cibs= 0.00;
	private Double cibsBsa= 0.00;
	private int cibsTerm = 0;
	
	private Double febs= 0.00;
	private Double febsBsa= 0.00;
	private int febsTerm = 0;
	
	private Double hbs= 0.00;
	private Double hbsBsa= 0.00;
	private int hbsTerm = 0;
	
	//private Double hrbs= 0.00;
	//private int hrbsTerm = 0;
	
	private Double ppdbs= 0.00;
	private Double ppdbsBsa= 0.00;
	private int ppdbsTerm = 0;
	
	private Double suhrbs= 0.00;
	private Double suhrbsBsa= 0.00;
	private int suhrbsTerm = 0;
	
	private Double tpdasbs= 0.00;
	private Double tpdasbsBsa= 0.00;
	private int tpdasbsTerm = 0;
	
	private Double tpdbs= 0.00;
	private Double tpdbsBsa= 0.00;
	private int tpdbsTerm = 0;
	
	private Double wpbs= 0.00;
	private Double wpbsBsa= 0.00;
	private int wpbsTerm = 0;
	
	private Double cibc= 0.00;
	private Double cibcBsa= 0.00;
	private int cibcTerm = 0;
	
	private Double hbc= 0.00;
	private Double hbcBsa= 0.00;
	private int hbcTerm = 0;
	
	//private Double hrbc= 0.00;
	//private int hrbcTerm = 0;
	
	private Double suhrbc= 0.00;
	private Double suhrbcBsa= 0.00;
	private int suhrbcTerm = 0;
	
	private Double jlb= 0.00;
	private Double jlbBsa= 0.00;
	private int jlbTerm = 0;
	
	private Double jlbpl= 0.00;
	private Double jlbplBsa= 0.00;
	private int jlbplTerm = 0;
	
	private Double tpddta= 0.00;
	private Double tpddtaBsa= 0.00;
	private int tpddtaTerm = 0;
	
	private Double tpddtas= 0.00;
	private Double tpddtasBsa= 0.00;
	private int tpddtasTerm = 0;
	
	private Double tpddtapl= 0.00;
	private Double tpddtaplBsa= 0.00;
	private int tpddtaplTerm = 0;
	
	private Double tpddtaspl= 0.00;
	private Double tpddtasplBsa= 0.00;
	private int tpddtasplTerm = 0;
	
	private Double hrbi = 0.0;
	private Double hrbiBsa = 0.0;
	private int hrbiTerm = 0;
	
	private Double hrbf = 0.0;
	private Double hrbfBsa = 0.0;
	private int hrbfTerm = 0;
	
	private Double hrbis = 0.0;
	private Double hrbisBsa = 0.0;
	private int hrbisTerm = 0;
	
	private Double hrbfs = 0.0;
	private Double hrbfsBsa = 0.0;
	private int hrbfsTerm = 0;
	
	private Double hrbic = 0.0;
	private Double hrbicBsa = 0.0;
	private int hrbicTerm = 0;
	
	private Double hrbfc = 0.0;
	private Double hrbfcBsa = 0.0;
	private int hrbfcTerm = 0;
	
	private Double shcbi = 0.0;
	private Double shcbiBsa = 0.0;
	private int shcbiTerm = 0;
	
	private Double shcbic = 0.0;
	private Double shcbicBsa = 0.0;
	private int shcbicTerm = 0;
	
	private Double shcbis = 0.0;
	private Double shcbisBsa = 0.0;
	private int shcbisTerm = 0;
	
	private Double shcbf = 0.0;
	private Double shcbfBsa = 0.0;
	private int shcbfTerm = 0;
	
	private Double shcbfc = 0.0;
	private Double shcbfcBsa = 0.0;
	private int shcbfcTerm = 0;
	
	private Double shcbfs = 0.0;
	private Double shcbfsBsa = 0.0;
	private int shcbfsTerm = 0;
	
	private ArrayList<DTAShedule> dtaShedules=null;
	
	private boolean isErrorExist = false;
	private String error = null;
	
	private boolean isWarningExist = false;
	private String warning = null;
	
	private HashMap<String, Object> mainLifeHealthReq = null;
	private HashMap<String, Object> spouseHealthReq = null;
	
	private boolean isArp = false;
	private String payTerm; 
	
	public Double getBasicSumAssured() {
		return basicSumAssured;
	}
	public void setBasicSumAssured(Double basicSumAssured) {
		this.basicSumAssured = basicSumAssured;
	}
	public Double getExtraOE() {
		return extraOE;
	}
	public void setExtraOE(Double extraOE) {
		this.extraOE = extraOE;
	}
	public Double getAddBenif() {
		return addBenif;
	}
	public void setAddBenif(Double addBenif) {
		this.addBenif = addBenif;
	}
	public Double getTotPremium() {
		return totPremium;
	}
	public void setTotPremium(Double totPremium) {
		this.totPremium = totPremium;
	}
	public Double getOccuLodingTot() {
		return occuLodingTot;
	}
	public void setOccuLodingTot(Double occuLodingTot) {
		this.occuLodingTot = occuLodingTot;
	}
	public Double getWithoutLoadingTot() {
		return withoutLoadingTot;
	}
	public void setWithoutLoadingTot(Double withoutLoadingTot) {
		this.withoutLoadingTot = withoutLoadingTot;
	}
	public Double getBsaYearlyPremium() {
		return bsaYearlyPremium;
	}
	public void setBsaYearlyPremium(Double bsaYearlyPremium) {
		this.bsaYearlyPremium = bsaYearlyPremium;
	}
	public Double getL2() {
		return l2;
	}
	public void setL2(Double l2) {
		this.l2 = l2;
	}
	public Double getAt6() {
		return at6;
	}
	public void setAt6(Double at6) {
		this.at6 = at6;
	}
	public Double getAt8() {
		return at8;
	}
	public void setAt8(Double at8) {
		this.at8 = at8;
	}
	public Double getAt10() {
		return at10;
	}
	public void setAt10(Double at10) {
		this.at10 = at10;
	}
	public Double getGuaranteed() {
		return guaranteed;
	}
	public void setGuaranteed(Double guaranteed) {
		this.guaranteed = guaranteed;
	}
	public Double getAdb() {
		return adb;
	}
	public void setAdb(Double adb) {
		this.adb = adb;
	}
	public Double getAdbBsa() {
		return adbBsa;
	}
	public void setAdbBsa(Double adbBsa) {
		this.adbBsa = adbBsa;
	}
	public int getAdbTerm() {
		return adbTerm;
	}
	public void setAdbTerm(int adbTerm) {
		this.adbTerm = adbTerm;
	}
	public Double getSfpo() {
		return sfpo;
	}
	public void setSfpo(Double sfpo) {
		this.sfpo = sfpo;
	}
	public Double getSfpoBsa() {
		return sfpoBsa;
	}
	public void setSfpoBsa(Double sfpoBsa) {
		this.sfpoBsa = sfpoBsa;
	}
	public int getSfpoTerm() {
		return sfpoTerm;
	}
	public void setSfpoTerm(int sfpoTerm) {
		this.sfpoTerm = sfpoTerm;
	}
	public Double getAtpb() {
		return atpb;
	}
	public void setAtpb(Double atpb) {
		this.atpb = atpb;
	}
	public Double getAtpbBsa() {
		return atpbBsa;
	}
	public void setAtpbBsa(Double atpbBsa) {
		this.atpbBsa = atpbBsa;
	}
	public int getAtpbTerm() {
		return atpbTerm;
	}
	public void setAtpbTerm(int atpbTerm) {
		this.atpbTerm = atpbTerm;
	}
	public Double getCib() {
		return cib;
	}
	public void setCib(Double cib) {
		this.cib = cib;
	}
	public Double getCibBsa() {
		return cibBsa;
	}
	public void setCibBsa(Double cibBsa) {
		this.cibBsa = cibBsa;
	}
	public int getCibTerm() {
		return cibTerm;
	}
	public void setCibTerm(int cibTerm) {
		this.cibTerm = cibTerm;
	}
	public Double getFeb() {
		return feb;
	}
	public void setFeb(Double feb) {
		this.feb = feb;
	}
	public Double getFebBsa() {
		return febBsa;
	}
	public void setFebBsa(Double febBsa) {
		this.febBsa = febBsa;
	}
	public int getFebTerm() {
		return febTerm;
	}
	public void setFebTerm(int febTerm) {
		this.febTerm = febTerm;
	}
	public Double getHb() {
		return hb;
	}
	public void setHb(Double hb) {
		this.hb = hb;
	}
	public Double getHbBsa() {
		return hbBsa;
	}
	public void setHbBsa(Double hbBsa) {
		this.hbBsa = hbBsa;
	}
	public int getHbTerm() {
		return hbTerm;
	}
	public void setHbTerm(int hbTerm) {
		this.hbTerm = hbTerm;
	}
	public Double getMifdb() {
		return mifdb;
	}
	public void setMifdb(Double mifdb) {
		this.mifdb = mifdb;
	}
	public Double getMifdbBsa() {
		return mifdbBsa;
	}
	public void setMifdbBsa(Double mifdbBsa) {
		this.mifdbBsa = mifdbBsa;
	}
	public int getMifdbTerm() {
		return mifdbTerm;
	}
	public void setMifdbTerm(int mifdbTerm) {
		this.mifdbTerm = mifdbTerm;
	}
	public Double getMifdbt() {
		return mifdbt;
	}
	public void setMifdbt(Double mifdbt) {
		this.mifdbt = mifdbt;
	}
	public Double getMifdbtBsa() {
		return mifdbtBsa;
	}
	public void setMifdbtBsa(Double mifdbtBsa) {
		this.mifdbtBsa = mifdbtBsa;
	}
	public int getMifdbtTerm() {
		return mifdbtTerm;
	}
	public void setMifdbtTerm(int mifdbtTerm) {
		this.mifdbtTerm = mifdbtTerm;
	}
	public Double getMifdt() {
		return mifdt;
	}
	public void setMifdt(Double mifdt) {
		this.mifdt = mifdt;
	}
	public Double getMifdtBsa() {
		return mifdtBsa;
	}
	public void setMifdtBsa(Double mifdtBsa) {
		this.mifdtBsa = mifdtBsa;
	}
	public int getMifdtTerm() {
		return mifdtTerm;
	}
	public void setMifdtTerm(int mifdtTerm) {
		this.mifdtTerm = mifdtTerm;
	}
	public Double getPpdb() {
		return ppdb;
	}
	public void setPpdb(Double ppdb) {
		this.ppdb = ppdb;
	}
	public Double getPpdbBsa() {
		return ppdbBsa;
	}
	public void setPpdbBsa(Double ppdbBsa) {
		this.ppdbBsa = ppdbBsa;
	}
	public int getPpdbTerm() {
		return ppdbTerm;
	}
	public void setPpdbTerm(int ppdbTerm) {
		this.ppdbTerm = ppdbTerm;
	}
	public Double getSuhrb() {
		return suhrb;
	}
	public void setSuhrb(Double suhrb) {
		this.suhrb = suhrb;
	}
	public Double getSuhrbBsa() {
		return suhrbBsa;
	}
	public void setSuhrbBsa(Double suhrbBsa) {
		this.suhrbBsa = suhrbBsa;
	}
	public int getSuhrbTerm() {
		return suhrbTerm;
	}
	public void setSuhrbTerm(int suhrbTerm) {
		this.suhrbTerm = suhrbTerm;
	}
	public Double getTpdasb() {
		return tpdasb;
	}
	public void setTpdasb(Double tpdasb) {
		this.tpdasb = tpdasb;
	}
	public Double getTpdasbBsa() {
		return tpdasbBsa;
	}
	public void setTpdasbBsa(Double tpdasbBsa) {
		this.tpdasbBsa = tpdasbBsa;
	}
	public int getTpdasbTerm() {
		return tpdasbTerm;
	}
	public void setTpdasbTerm(int tpdasbTerm) {
		this.tpdasbTerm = tpdasbTerm;
	}
	public Double getTpdb() {
		return tpdb;
	}
	public void setTpdb(Double tpdb) {
		this.tpdb = tpdb;
	}
	public Double getTpdbBsa() {
		return tpdbBsa;
	}
	public void setTpdbBsa(Double tpdbBsa) {
		this.tpdbBsa = tpdbBsa;
	}
	public int getTpdbTerm() {
		return tpdbTerm;
	}
	public void setTpdbTerm(int tpdbTerm) {
		this.tpdbTerm = tpdbTerm;
	}
	public Double getWpb() {
		return wpb;
	}
	public void setWpb(Double wpb) {
		this.wpb = wpb;
	}
	public Double getWpbBsa() {
		return wpbBsa;
	}
	public void setWpbBsa(Double wpbBsa) {
		this.wpbBsa = wpbBsa;
	}
	public int getWpbTerm() {
		return wpbTerm;
	}
	public void setWpbTerm(int wpbTerm) {
		this.wpbTerm = wpbTerm;
	}
	public Double getBsas() {
		return bsas;
	}
	public void setBsas(Double bsas) {
		this.bsas = bsas;
	}
	public Double getBsasBsa() {
		return bsasBsa;
	}
	public void setBsasBsa(Double bsasBsa) {
		this.bsasBsa = bsasBsa;
	}
	public int getBsasTerm() {
		return bsasTerm;
	}
	public void setBsasTerm(int bsasTerm) {
		this.bsasTerm = bsasTerm;
	}
	public Double getAdbs() {
		return adbs;
	}
	public void setAdbs(Double adbs) {
		this.adbs = adbs;
	}
	public Double getAdbsBsa() {
		return adbsBsa;
	}
	public void setAdbsBsa(Double adbsBsa) {
		this.adbsBsa = adbsBsa;
	}
	public int getAdbsTerm() {
		return adbsTerm;
	}
	public void setAdbsTerm(int adbsTerm) {
		this.adbsTerm = adbsTerm;
	}
	public Double getCibs() {
		return cibs;
	}
	public void setCibs(Double cibs) {
		this.cibs = cibs;
	}
	public Double getCibsBsa() {
		return cibsBsa;
	}
	public void setCibsBsa(Double cibsBsa) {
		this.cibsBsa = cibsBsa;
	}
	public int getCibsTerm() {
		return cibsTerm;
	}
	public void setCibsTerm(int cibsTerm) {
		this.cibsTerm = cibsTerm;
	}
	public Double getFebs() {
		return febs;
	}
	public void setFebs(Double febs) {
		this.febs = febs;
	}
	public Double getFebsBsa() {
		return febsBsa;
	}
	public void setFebsBsa(Double febsBsa) {
		this.febsBsa = febsBsa;
	}
	public int getFebsTerm() {
		return febsTerm;
	}
	public void setFebsTerm(int febsTerm) {
		this.febsTerm = febsTerm;
	}
	public Double getHbs() {
		return hbs;
	}
	public void setHbs(Double hbs) {
		this.hbs = hbs;
	}
	public Double getHbsBsa() {
		return hbsBsa;
	}
	public void setHbsBsa(Double hbsBsa) {
		this.hbsBsa = hbsBsa;
	}
	public int getHbsTerm() {
		return hbsTerm;
	}
	public void setHbsTerm(int hbsTerm) {
		this.hbsTerm = hbsTerm;
	}
	public Double getPpdbs() {
		return ppdbs;
	}
	public void setPpdbs(Double ppdbs) {
		this.ppdbs = ppdbs;
	}
	public Double getPpdbsBsa() {
		return ppdbsBsa;
	}
	public void setPpdbsBsa(Double ppdbsBsa) {
		this.ppdbsBsa = ppdbsBsa;
	}
	public int getPpdbsTerm() {
		return ppdbsTerm;
	}
	public void setPpdbsTerm(int ppdbsTerm) {
		this.ppdbsTerm = ppdbsTerm;
	}
	public Double getSuhrbs() {
		return suhrbs;
	}
	public void setSuhrbs(Double suhrbs) {
		this.suhrbs = suhrbs;
	}
	public Double getSuhrbsBsa() {
		return suhrbsBsa;
	}
	public void setSuhrbsBsa(Double suhrbsBsa) {
		this.suhrbsBsa = suhrbsBsa;
	}
	public int getSuhrbsTerm() {
		return suhrbsTerm;
	}
	public void setSuhrbsTerm(int suhrbsTerm) {
		this.suhrbsTerm = suhrbsTerm;
	}
	public Double getTpdasbs() {
		return tpdasbs;
	}
	public void setTpdasbs(Double tpdasbs) {
		this.tpdasbs = tpdasbs;
	}
	public Double getTpdasbsBsa() {
		return tpdasbsBsa;
	}
	public void setTpdasbsBsa(Double tpdasbsBsa) {
		this.tpdasbsBsa = tpdasbsBsa;
	}
	public int getTpdasbsTerm() {
		return tpdasbsTerm;
	}
	public void setTpdasbsTerm(int tpdasbsTerm) {
		this.tpdasbsTerm = tpdasbsTerm;
	}
	public Double getTpdbs() {
		return tpdbs;
	}
	public void setTpdbs(Double tpdbs) {
		this.tpdbs = tpdbs;
	}
	public Double getTpdbsBsa() {
		return tpdbsBsa;
	}
	public void setTpdbsBsa(Double tpdbsBsa) {
		this.tpdbsBsa = tpdbsBsa;
	}
	public int getTpdbsTerm() {
		return tpdbsTerm;
	}
	public void setTpdbsTerm(int tpdbsTerm) {
		this.tpdbsTerm = tpdbsTerm;
	}
	public Double getWpbs() {
		return wpbs;
	}
	public void setWpbs(Double wpbs) {
		this.wpbs = wpbs;
	}
	public Double getWpbsBsa() {
		return wpbsBsa;
	}
	public void setWpbsBsa(Double wpbsBsa) {
		this.wpbsBsa = wpbsBsa;
	}
	public int getWpbsTerm() {
		return wpbsTerm;
	}
	public void setWpbsTerm(int wpbsTerm) {
		this.wpbsTerm = wpbsTerm;
	}
	public Double getCibc() {
		return cibc;
	}
	public void setCibc(Double cibc) {
		this.cibc = cibc;
	}
	public Double getCibcBsa() {
		return cibcBsa;
	}
	public void setCibcBsa(Double cibcBsa) {
		this.cibcBsa = cibcBsa;
	}
	public int getCibcTerm() {
		return cibcTerm;
	}
	public void setCibcTerm(int cibcTerm) {
		this.cibcTerm = cibcTerm;
	}
	public Double getHbc() {
		return hbc;
	}
	public void setHbc(Double hbc) {
		this.hbc = hbc;
	}
	public Double getHbcBsa() {
		return hbcBsa;
	}
	public void setHbcBsa(Double hbcBsa) {
		this.hbcBsa = hbcBsa;
	}
	public int getHbcTerm() {
		return hbcTerm;
	}
	public void setHbcTerm(int hbcTerm) {
		this.hbcTerm = hbcTerm;
	}
	public Double getSuhrbc() {
		return suhrbc;
	}
	public void setSuhrbc(Double suhrbc) {
		this.suhrbc = suhrbc;
	}
	public Double getSuhrbcBsa() {
		return suhrbcBsa;
	}
	public void setSuhrbcBsa(Double suhrbcBsa) {
		this.suhrbcBsa = suhrbcBsa;
	}
	public int getSuhrbcTerm() {
		return suhrbcTerm;
	}
	public void setSuhrbcTerm(int suhrbcTerm) {
		this.suhrbcTerm = suhrbcTerm;
	}
	public Double getJlb() {
		return jlb;
	}
	public void setJlb(Double jlb) {
		this.jlb = jlb;
	}
	public Double getJlbBsa() {
		return jlbBsa;
	}
	public void setJlbBsa(Double jlbBsa) {
		this.jlbBsa = jlbBsa;
	}
	public int getJlbTerm() {
		return jlbTerm;
	}
	public void setJlbTerm(int jlbTerm) {
		this.jlbTerm = jlbTerm;
	}
	public Double getJlbpl() {
		return jlbpl;
	}
	public void setJlbpl(Double jlbpl) {
		this.jlbpl = jlbpl;
	}
	public Double getJlbplBsa() {
		return jlbplBsa;
	}
	public void setJlbplBsa(Double jlbplBsa) {
		this.jlbplBsa = jlbplBsa;
	}
	public int getJlbplTerm() {
		return jlbplTerm;
	}
	public void setJlbplTerm(int jlbplTerm) {
		this.jlbplTerm = jlbplTerm;
	}
	public Double getTpddta() {
		return tpddta;
	}
	public void setTpddta(Double tpddta) {
		this.tpddta = tpddta;
	}
	public Double getTpddtaBsa() {
		return tpddtaBsa;
	}
	public void setTpddtaBsa(Double tpddtaBsa) {
		this.tpddtaBsa = tpddtaBsa;
	}
	public int getTpddtaTerm() {
		return tpddtaTerm;
	}
	public void setTpddtaTerm(int tpddtaTerm) {
		this.tpddtaTerm = tpddtaTerm;
	}
	public Double getTpddtas() {
		return tpddtas;
	}
	public void setTpddtas(Double tpddtas) {
		this.tpddtas = tpddtas;
	}
	public Double getTpddtasBsa() {
		return tpddtasBsa;
	}
	public void setTpddtasBsa(Double tpddtasBsa) {
		this.tpddtasBsa = tpddtasBsa;
	}
	public int getTpddtasTerm() {
		return tpddtasTerm;
	}
	public void setTpddtasTerm(int tpddtasTerm) {
		this.tpddtasTerm = tpddtasTerm;
	}
	public Double getTpddtapl() {
		return tpddtapl;
	}
	public void setTpddtapl(Double tpddtapl) {
		this.tpddtapl = tpddtapl;
	}
	public Double getTpddtaplBsa() {
		return tpddtaplBsa;
	}
	public void setTpddtaplBsa(Double tpddtaplBsa) {
		this.tpddtaplBsa = tpddtaplBsa;
	}
	public int getTpddtaplTerm() {
		return tpddtaplTerm;
	}
	public void setTpddtaplTerm(int tpddtaplTerm) {
		this.tpddtaplTerm = tpddtaplTerm;
	}
	public Double getTpddtaspl() {
		return tpddtaspl;
	}
	public void setTpddtaspl(Double tpddtaspl) {
		this.tpddtaspl = tpddtaspl;
	}
	public Double getTpddtasplBsa() {
		return tpddtasplBsa;
	}
	public void setTpddtasplBsa(Double tpddtasplBsa) {
		this.tpddtasplBsa = tpddtasplBsa;
	}
	public int getTpddtasplTerm() {
		return tpddtasplTerm;
	}
	public void setTpddtasplTerm(int tpddtasplTerm) {
		this.tpddtasplTerm = tpddtasplTerm;
	}
	public Double getHrbi() {
		return hrbi;
	}
	public void setHrbi(Double hrbi) {
		this.hrbi = hrbi;
	}
	public Double getHrbiBsa() {
		return hrbiBsa;
	}
	public void setHrbiBsa(Double hrbiBsa) {
		this.hrbiBsa = hrbiBsa;
	}
	public int getHrbiTerm() {
		return hrbiTerm;
	}
	public void setHrbiTerm(int hrbiTerm) {
		this.hrbiTerm = hrbiTerm;
	}
	public Double getHrbf() {
		return hrbf;
	}
	public void setHrbf(Double hrbf) {
		this.hrbf = hrbf;
	}
	public Double getHrbfBsa() {
		return hrbfBsa;
	}
	public void setHrbfBsa(Double hrbfBsa) {
		this.hrbfBsa = hrbfBsa;
	}
	public int getHrbfTerm() {
		return hrbfTerm;
	}
	public void setHrbfTerm(int hrbfTerm) {
		this.hrbfTerm = hrbfTerm;
	}
	public Double getHrbis() {
		return hrbis;
	}
	public void setHrbis(Double hrbis) {
		this.hrbis = hrbis;
	}
	public Double getHrbisBsa() {
		return hrbisBsa;
	}
	public void setHrbisBsa(Double hrbisBsa) {
		this.hrbisBsa = hrbisBsa;
	}
	public int getHrbisTerm() {
		return hrbisTerm;
	}
	public void setHrbisTerm(int hrbisTerm) {
		this.hrbisTerm = hrbisTerm;
	}
	public Double getHrbfs() {
		return hrbfs;
	}
	public void setHrbfs(Double hrbfs) {
		this.hrbfs = hrbfs;
	}
	public Double getHrbfsBsa() {
		return hrbfsBsa;
	}
	public void setHrbfsBsa(Double hrbfsBsa) {
		this.hrbfsBsa = hrbfsBsa;
	}
	public int getHrbfsTerm() {
		return hrbfsTerm;
	}
	public void setHrbfsTerm(int hrbfsTerm) {
		this.hrbfsTerm = hrbfsTerm;
	}
	public Double getHrbic() {
		return hrbic;
	}
	public void setHrbic(Double hrbic) {
		this.hrbic = hrbic;
	}
	public Double getHrbicBsa() {
		return hrbicBsa;
	}
	public void setHrbicBsa(Double hrbicBsa) {
		this.hrbicBsa = hrbicBsa;
	}
	public int getHrbicTerm() {
		return hrbicTerm;
	}
	public void setHrbicTerm(int hrbicTerm) {
		this.hrbicTerm = hrbicTerm;
	}
	public Double getHrbfc() {
		return hrbfc;
	}
	public void setHrbfc(Double hrbfc) {
		this.hrbfc = hrbfc;
	}
	public Double getHrbfcBsa() {
		return hrbfcBsa;
	}
	public void setHrbfcBsa(Double hrbfcBsa) {
		this.hrbfcBsa = hrbfcBsa;
	}
	public int getHrbfcTerm() {
		return hrbfcTerm;
	}
	public void setHrbfcTerm(int hrbfcTerm) {
		this.hrbfcTerm = hrbfcTerm;
	}
	public Double getShcbi() {
		return shcbi;
	}
	public void setShcbi(Double shcbi) {
		this.shcbi = shcbi;
	}
	public Double getShcbiBsa() {
		return shcbiBsa;
	}
	public void setShcbiBsa(Double shcbiBsa) {
		this.shcbiBsa = shcbiBsa;
	}
	public int getShcbiTerm() {
		return shcbiTerm;
	}
	public void setShcbiTerm(int shcbiTerm) {
		this.shcbiTerm = shcbiTerm;
	}
	public Double getShcbic() {
		return shcbic;
	}
	public void setShcbic(Double shcbic) {
		this.shcbic = shcbic;
	}
	public Double getShcbicBsa() {
		return shcbicBsa;
	}
	public void setShcbicBsa(Double shcbicBsa) {
		this.shcbicBsa = shcbicBsa;
	}
	public int getShcbicTerm() {
		return shcbicTerm;
	}
	public void setShcbicTerm(int shcbicTerm) {
		this.shcbicTerm = shcbicTerm;
	}
	public Double getShcbis() {
		return shcbis;
	}
	public void setShcbis(Double shcbis) {
		this.shcbis = shcbis;
	}
	public Double getShcbisBsa() {
		return shcbisBsa;
	}
	public void setShcbisBsa(Double shcbisBsa) {
		this.shcbisBsa = shcbisBsa;
	}
	public int getShcbisTerm() {
		return shcbisTerm;
	}
	public void setShcbisTerm(int shcbisTerm) {
		this.shcbisTerm = shcbisTerm;
	}
	public Double getShcbf() {
		return shcbf;
	}
	public void setShcbf(Double shcbf) {
		this.shcbf = shcbf;
	}
	public Double getShcbfBsa() {
		return shcbfBsa;
	}
	public void setShcbfBsa(Double shcbfBsa) {
		this.shcbfBsa = shcbfBsa;
	}
	public int getShcbfTerm() {
		return shcbfTerm;
	}
	public void setShcbfTerm(int shcbfTerm) {
		this.shcbfTerm = shcbfTerm;
	}
	public Double getShcbfc() {
		return shcbfc;
	}
	public void setShcbfc(Double shcbfc) {
		this.shcbfc = shcbfc;
	}
	public Double getShcbfcBsa() {
		return shcbfcBsa;
	}
	public void setShcbfcBsa(Double shcbfcBsa) {
		this.shcbfcBsa = shcbfcBsa;
	}
	public int getShcbfcTerm() {
		return shcbfcTerm;
	}
	public void setShcbfcTerm(int shcbfcTerm) {
		this.shcbfcTerm = shcbfcTerm;
	}
	public Double getShcbfs() {
		return shcbfs;
	}
	public void setShcbfs(Double shcbfs) {
		this.shcbfs = shcbfs;
	}
	public Double getShcbfsBsa() {
		return shcbfsBsa;
	}
	public void setShcbfsBsa(Double shcbfsBsa) {
		this.shcbfsBsa = shcbfsBsa;
	}
	public int getShcbfsTerm() {
		return shcbfsTerm;
	}
	public void setShcbfsTerm(int shcbfsTerm) {
		this.shcbfsTerm = shcbfsTerm;
	}
	public ArrayList<DTAShedule> getDtaShedules() {
		return dtaShedules;
	}
	public void setDtaShedules(ArrayList<DTAShedule> dtaShedules) {
		this.dtaShedules = dtaShedules;
	}
	public boolean isErrorExist() {
		return isErrorExist;
	}
	public void setErrorExist(boolean isErrorExist) {
		this.isErrorExist = isErrorExist;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public boolean isWarningExist() {
		return isWarningExist;
	}
	public void setWarningExist(boolean isWarningExist) {
		this.isWarningExist = isWarningExist;
	}
	public String getWarning() {
		return warning;
	}
	public void setWarning(String warning) {
		this.warning = warning;
	}
	
	public HashMap<String, Object> getMainLifeHealthReq() {
		return mainLifeHealthReq;
	}
	public void setMainLifeHealthReq(HashMap<String, Object> mainLifeHealthReq) {
		this.mainLifeHealthReq = mainLifeHealthReq;
	}
	public HashMap<String, Object> getSpouseHealthReq() {
		return spouseHealthReq;
	}
	public void setSpouseHealthReq(HashMap<String, Object> spouseHealthReq) {
		this.spouseHealthReq = spouseHealthReq;
	}
	public boolean isArp() {
		return isArp;
	}
	public void setArp(boolean isArp) {
		this.isArp = isArp;
	}
	public String getPayTerm() {
		return payTerm;
	}
	public void setPayTerm(String payTerm) {
		this.payTerm = payTerm;
	}
	
	
	
	
}
