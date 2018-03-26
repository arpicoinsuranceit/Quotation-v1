package org.arpicoinsurance.groupit.main.helper;

import java.util.ArrayList;

public class QuotationQuickCalResponse {
	private Double basicSumAssured = 0.00;
	private Double extraOE= 0.00;
	private Double addBenif= 0.00;
	private Double totPremium= 0.00;
	
	private Double l2=0.00;
	private Double at6= 0.00;
	private Double at8= 0.00;
	private Double at10= 0.00;
	
	private Double guaranteed= 0.00;
	
	private Double adb= 0.00;
	private int adbTerm = 0;
	private Double sfpo= 0.00;
	private int sfpoTerm = 0;
	private Double atpb= 0.00;
	private int atpbTerm = 0;
	private Double cib= 0.00;
	private int cibTerm = 0;
	private Double feb= 0.00;
	private int febTerm = 0;
	private Double hb= 0.00;
	private int hbTerm = 0;
	//private Double hrb= 0.00;
	//private int hrbTerm = 0;
	private Double mifdb= 0.00;
	private int mifdbTerm = 0;
	private Double mifdbt= 0.00;
	private int mifdbtTerm = 0;
	private Double mifdt= 0.00;
	private int mifdtTerm = 0;
	private Double ppdb= 0.00;
	private int ppdbTerm = 0;
	private Double suhrb= 0.00;
	private int suhrbTerm = 0;
	private Double tpdasb= 0.00;
	private int tpdasbTerm = 0;
	private Double tpdb= 0.00;
	private int tpdbTerm = 0;
	private Double wpb= 0.00;
	private int wpbTerm = 0;
	private Double bsas= 0.00;
	private int bsasTerm = 0;
	private Double adbs= 0.00;
	private int adbsTerm = 0;
	private Double cibs= 0.00;
	private int cibsTerm = 0;
	private Double febs= 0.00;
	private int febsTerm = 0;
	private Double hbs= 0.00;
	private int hbsTerm = 0;
	//private Double hrbs= 0.00;
	//private int hrbsTerm = 0;
	private Double ppdbs= 0.00;
	private int ppdbsTerm = 0;
	private Double suhrbs= 0.00;
	private int suhrbsTerm = 0;
	private Double tpdasbs= 0.00;
	private int tpdasbsTerm = 0;
	private Double tpdbs= 0.00;
	private int tpdbsTerm = 0;
	private Double wpbs= 0.00;
	private int wpbsTerm = 0;
	private Double cibc= 0.00;
	private int cibcTerm = 0;
	private Double hbc= 0.00;
	private int hbcTerm = 0;
	//private Double hrbc= 0.00;
	//private int hrbcTerm = 0;
	private Double suhrbc= 0.00;
	private int suhrbcTerm = 0;
	private Double jlb= 0.00;
	private int jlbTerm = 0;
	private Double jlbpl= 0.00;
	private int jlbplTerm = 0;
	private Double tpddta= 0.00;
	private int tpddtaTerm = 0;
	private Double tpddtas= 0.00;
	private int tpddtasTerm = 0;
	private Double tpddtapl= 0.00;
	private int tpddtaplTerm = 0;
	private Double tpddtaspl= 0.00;
	private int tpddtasplTerm = 0;
	
	private Double hrbi = 0.0;
	private int hrbiTerm = 0;
	private Double hrbf = 0.0;
	private int hrbfTerm = 0;
	
	private Double hrbis = 0.0;
	private int hrbisTerm = 0;
	private Double hrbfs = 0.0;
	private int hrbfsTerm = 0;
	
	private Double hrbic = 0.0;
	private int hrbicTerm = 0;
	private Double hrbfc = 0.0;
	private int hrbfcTerm = 0;
	
	
	private ArrayList<DTAShedule> dtaShedules=null;
	
	private boolean isErrorExist = false;
	private String error = null;
	
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
	public Double getAdb() {
		return adb;
	}
	public void setAdb(Double adb) {
		this.adb = adb;
	}
	public Double getAtpb() {
		return atpb;
	}
	public void setAtpb(Double atpb) {
		this.atpb = atpb;
	}
	public Double getCib() {
		return cib;
	}
	public void setCib(Double cib) {
		this.cib = cib;
	}
	public Double getFeb() {
		return feb;
	}
	public void setFeb(Double feb) {
		this.feb = feb;
	}
	public Double getHb() {
		return hb;
	}
	public void setHb(Double hb) {
		this.hb = hb;
	}
	/*
	public Double getHrb() {
		return hrb;
	}
	public void setHrb(Double hrb) {
		this.hrb = hrb;
	}*/
	public Double getMifdb() {
		return mifdb;
	}
	public void setMifdb(Double mifdb) {
		this.mifdb = mifdb;
	}
	public Double getMifdbt() {
		return mifdbt;
	}
	public void setMifdbt(Double mifdbt) {
		this.mifdbt = mifdbt;
	}
	public Double getMifdt() {
		return mifdt;
	}
	public void setMifdt(Double mifdt) {
		this.mifdt = mifdt;
	}
	public Double getPpdb() {
		return ppdb;
	}
	public void setPpdb(Double ppdb) {
		this.ppdb = ppdb;
	}
	public Double getSuhrb() {
		return suhrb;
	}
	public void setSuhrb(Double suhrb) {
		this.suhrb = suhrb;
	}
	public Double getTpdasb() {
		return tpdasb;
	}
	public void setTpdasb(Double tpdasb) {
		this.tpdasb = tpdasb;
	}
	public Double getTpdb() {
		return tpdb;
	}
	public void setTpdb(Double tpdb) {
		this.tpdb = tpdb;
	}
	public Double getWpb() {
		return wpb;
	}
	public void setWpb(Double wpb) {
		this.wpb = wpb;
	}
	public Double getBsas() {
		return bsas;
	}
	public void setBsas(Double bsas) {
		this.bsas = bsas;
	}
	public Double getAdbs() {
		return adbs;
	}
	public void setAdbs(Double adbs) {
		this.adbs = adbs;
	}
	public Double getCibs() {
		return cibs;
	}
	public void setCibs(Double cibs) {
		this.cibs = cibs;
	}
	public Double getFebs() {
		return febs;
	}
	public void setFebs(Double febs) {
		this.febs = febs;
	}
	public Double getHbs() {
		return hbs;
	}
	public void setHbs(Double hbs) {
		this.hbs = hbs;
	}
	/*
	public Double getHrbs() {
		return hrbs;
	}
	public void setHrbs(Double hrbs) {
		this.hrbs = hrbs;
	}*/
	public Double getPpdbs() {
		return ppdbs;
	}
	public void setPpdbs(Double ppdbs) {
		this.ppdbs = ppdbs;
	}
	public Double getSuhrbs() {
		return suhrbs;
	}
	public void setSuhrbs(Double suhrbs) {
		this.suhrbs = suhrbs;
	}
	public Double getTpdasbs() {
		return tpdasbs;
	}
	public void setTpdasbs(Double tpdasbs) {
		this.tpdasbs = tpdasbs;
	}
	public Double getTpdbs() {
		return tpdbs;
	}
	public void setTpdbs(Double tpdbs) {
		this.tpdbs = tpdbs;
	}
	public Double getWpbs() {
		return wpbs;
	}
	public void setWpbs(Double wpbs) {
		this.wpbs = wpbs;
	}
	public Double getCibc() {
		return cibc;
	}
	public void setCibc(Double cibc) {
		this.cibc = cibc;
	}
	public Double getHbc() {
		return hbc;
	}
	public void setHbc(Double hbc) {
		this.hbc = hbc;
	}
	/*
	public Double getHrbc() {
		return hrbc;
	}
	public void setHrbc(Double hrbc) {
		this.hrbc = hrbc;
	}*/
	public Double getSuhrbc() {
		return suhrbc;
	}
	public void setSuhrbc(Double suhrbc) {
		this.suhrbc = suhrbc;
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
	public int getAdbTerm() {
		return adbTerm;
	}
	public void setAdbTerm(int adbTerm) {
		this.adbTerm = adbTerm;
	}
	public int getAtpbTerm() {
		return atpbTerm;
	}
	public void setAtpbTerm(int atpbTerm) {
		this.atpbTerm = atpbTerm;
	}
	public int getCibTerm() {
		return cibTerm;
	}
	public void setCibTerm(int cibTerm) {
		this.cibTerm = cibTerm;
	}
	public int getFebTerm() {
		return febTerm;
	}
	public void setFebTerm(int febTerm) {
		this.febTerm = febTerm;
	}
	public int getHbTerm() {
		return hbTerm;
	}
	public void setHbTerm(int hbTerm) {
		this.hbTerm = hbTerm;
	}
	/*
	public int getHrbTerm() {
		return hrbTerm;
	}
	public void setHrbTerm(int hrbTerm) {
		this.hrbTerm = hrbTerm;
	}*/
	public int getMifdbTerm() {
		return mifdbTerm;
	}
	public void setMifdbTerm(int mifdbTerm) {
		this.mifdbTerm = mifdbTerm;
	}
	public int getMifdbtTerm() {
		return mifdbtTerm;
	}
	public void setMifdbtTerm(int mifdbtTerm) {
		this.mifdbtTerm = mifdbtTerm;
	}
	public int getMifdtTerm() {
		return mifdtTerm;
	}
	public void setMifdtTerm(int mifdtTerm) {
		this.mifdtTerm = mifdtTerm;
	}
	public int getPpdbTerm() {
		return ppdbTerm;
	}
	public void setPpdbTerm(int ppdbTerm) {
		this.ppdbTerm = ppdbTerm;
	}
	public int getSuhrbTerm() {
		return suhrbTerm;
	}
	public void setSuhrbTerm(int suhrbTerm) {
		this.suhrbTerm = suhrbTerm;
	}
	public int getTpdasbTerm() {
		return tpdasbTerm;
	}
	public void setTpdasbTerm(int tpdasbTerm) {
		this.tpdasbTerm = tpdasbTerm;
	}
	public int getTpdbTerm() {
		return tpdbTerm;
	}
	public void setTpdbTerm(int tpdbTerm) {
		this.tpdbTerm = tpdbTerm;
	}
	public int getWpbTerm() {
		return wpbTerm;
	}
	public void setWpbTerm(int wpbTerm) {
		this.wpbTerm = wpbTerm;
	}
	public int getBsasTerm() {
		return bsasTerm;
	}
	public void setBsasTerm(int bsasTerm) {
		this.bsasTerm = bsasTerm;
	}
	public int getAdbsTerm() {
		return adbsTerm;
	}
	public void setAdbsTerm(int adbsTerm) {
		this.adbsTerm = adbsTerm;
	}
	public int getCibsTerm() {
		return cibsTerm;
	}
	public void setCibsTerm(int cibsTerm) {
		this.cibsTerm = cibsTerm;
	}
	public int getFebsTerm() {
		return febsTerm;
	}
	public void setFebsTerm(int febsTerm) {
		this.febsTerm = febsTerm;
	}
	public int getHbsTerm() {
		return hbsTerm;
	}
	public void setHbsTerm(int hbsTerm) {
		this.hbsTerm = hbsTerm;
	}
	/*
	public int getHrbsTerm() {
		return hrbsTerm;
	}
	public void setHrbsTerm(int hrbsTerm) {
		this.hrbsTerm = hrbsTerm;
	}
	*/
	public int getPpdbsTerm() {
		return ppdbsTerm;
	}
	public void setPpdbsTerm(int ppdbsTerm) {
		this.ppdbsTerm = ppdbsTerm;
	}
	public int getSuhrbsTerm() {
		return suhrbsTerm;
	}
	public void setSuhrbsTerm(int suhrbsTerm) {
		this.suhrbsTerm = suhrbsTerm;
	}
	public int getTpdasbsTerm() {
		return tpdasbsTerm;
	}
	public void setTpdasbsTerm(int tpdasbsTerm) {
		this.tpdasbsTerm = tpdasbsTerm;
	}
	public int getTpdbsTerm() {
		return tpdbsTerm;
	}
	public void setTpdbsTerm(int tpdbsTerm) {
		this.tpdbsTerm = tpdbsTerm;
	}
	public int getWpbsTerm() {
		return wpbsTerm;
	}
	public void setWpbsTerm(int wpbsTerm) {
		this.wpbsTerm = wpbsTerm;
	}
	public int getCibcTerm() {
		return cibcTerm;
	}
	public void setCibcTerm(int cibcTerm) {
		this.cibcTerm = cibcTerm;
	}
	public int getHbcTerm() {
		return hbcTerm;
	}
	public void setHbcTerm(int hbcTerm) {
		this.hbcTerm = hbcTerm;
	}
	/*
	public int getHrbcTerm() {
		return hrbcTerm;
	}
	public void setHrbcTerm(int hrbcTerm) {
		this.hrbcTerm = hrbcTerm;
	}
	*/
	public int getSuhrbcTerm() {
		return suhrbcTerm;
	}
	public void setSuhrbcTerm(int suhrbcTerm) {
		this.suhrbcTerm = suhrbcTerm;
	}
	public Double getGuaranteed() {
		return guaranteed;
	}
	public void setGuaranteed(Double guaranteed) {
		this.guaranteed = guaranteed;
	}

	public Double getJlb() {
		return jlb;
	}
	public void setJlb(Double jlb) {
		this.jlb = jlb;
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
	public int getTpddtasplTerm() {
		return tpddtasplTerm;
	}
	public void setTpddtasplTerm(int tpddtasplTerm) {
		this.tpddtasplTerm = tpddtasplTerm;
	}
	public ArrayList<DTAShedule> getDtaShedules() {
		return dtaShedules;
	}
	public void setDtaShedules(ArrayList<DTAShedule> dtaShedules) {
		this.dtaShedules = dtaShedules;
	}

	public Double getL2() {
		return l2;
	}
	public void setL2(Double l2) {
		this.l2 = l2;
	}
	public Double getSfpo() {
		return sfpo;
	}
	public void setSfpo(Double sfpo) {
		this.sfpo = sfpo;
	}
	public int getSfpoTerm() {
		return sfpoTerm;
	}
	public void setSfpoTerm(int sfpoTerm) {
		this.sfpoTerm = sfpoTerm;
	}
	public Double getHrbi() {
		return hrbi;
	}
	public void setHrbi(Double hrbi) {
		this.hrbi = hrbi;
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
	public int getHrbfcTerm() {
		return hrbfcTerm;
	}
	public void setHrbfcTerm(int hrbfcTerm) {
		this.hrbfcTerm = hrbfcTerm;
	}
	
	
	
}
