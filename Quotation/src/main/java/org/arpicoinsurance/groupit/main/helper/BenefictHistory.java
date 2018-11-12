package org.arpicoinsurance.groupit.main.helper;

public class BenefictHistory {

	private String pprnum;
	private String prdcod;
	private String riderCode;
	private Double totPremium;
	private Double sumAssuredTot;
	private Double premiumTot;
	private String type;

	public String getPprnum() {
		return pprnum;
	}

	public void setPprnum(String pprnum) {
		this.pprnum = pprnum;
	}

	public String getPrdcod() {
		return prdcod;
	}

	public void setPrdcod(String prdcod) {
		this.prdcod = prdcod;
	}

	public String getRiderCode() {
		return riderCode;
	}

	public void setRiderCode(String riderCode) {
		this.riderCode = riderCode;
	}

	public Double getTotPremium() {
		return totPremium;
	}

	public void setTotPremium(Double totPremium) {
		this.totPremium = totPremium;
	}

	public Double getSumAssuredTot() {
		return sumAssuredTot;
	}

	public void setSumAssuredTot(Double sumAssuredTot) {
		this.sumAssuredTot = sumAssuredTot;
	}

	public Double getPremiumTot() {
		return premiumTot;
	}

	public void setPremiumTot(Double premiumTot) {
		this.premiumTot = premiumTot;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
