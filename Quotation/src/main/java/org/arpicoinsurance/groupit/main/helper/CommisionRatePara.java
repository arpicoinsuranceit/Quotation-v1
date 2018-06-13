package org.arpicoinsurance.groupit.main.helper;

public class CommisionRatePara {
	
	private String prdcod;
	private int comyer;
	private int frmtrm;
	private int toterm;
	
	
	public CommisionRatePara() {
		super();
	}
	
	public CommisionRatePara(String prdcod, int frmtrm, int toterm) {
		super();
		this.prdcod = prdcod;
		this.frmtrm = frmtrm;
		this.toterm = toterm;
	}

	public String getPrdcod() {
		return prdcod;
	}
	public void setPrdcod(String prdcod) {
		this.prdcod = prdcod;
	}
	public int getComyer() {
		return comyer;
	}
	public void setComyer(int comyer) {
		this.comyer = comyer;
	}
	public int getFrmtrm() {
		return frmtrm;
	}
	public void setFrmtrm(int frmtrm) {
		this.frmtrm = frmtrm;
	}
	public int getToterm() {
		return toterm;
	}
	public void setToterm(int toterm) {
		this.toterm = toterm;
	}
	

}
