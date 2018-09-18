package org.arpicoinsurance.groupit.main.helper;

public class SurrenderValHelper {
	private String polyer;
	private String padtrm;
	private Integer toptrm;
	private Double isumas;
	private Double paidup;
	private Double surrnd;
	private Double mature;
	private Double prmpyr;
	private Double prmpad;
	
	public String getPolyer() {
		return polyer;
	}
	public void setPolyer(String polyer) {
		this.polyer = polyer;
	}
	public String getPadtrm() {
		return padtrm;
	}
	public void setPadtrm(String padtrm) {
		this.padtrm = padtrm;
	}
	public Integer getToptrm() {
		return toptrm;
	}
	public void setToptrm(Integer toptrm) {
		this.toptrm = toptrm;
	}
	public Double getIsumas() {
		return isumas;
	}
	public void setIsumas(Double isumas) {
		this.isumas = isumas;
	}
	public Double getPaidup() {
		return paidup;
	}
	public void setPaidup(Double paidup) {
		this.paidup = paidup;
	}
	public Double getSurrnd() {
		return surrnd;
	}
	public void setSurrnd(Double surrnd) {
		this.surrnd = surrnd;
	}
	public Double getMature() {
		return mature;
	}
	public void setMature(Double mature) {
		this.mature = mature;
	}
	public Double getPrmpyr() {
		return prmpyr;
	}
	public void setPrmpyr(Double prmpyr) {
		this.prmpyr = prmpyr;
	}
	public Double getPrmpad() {
		return prmpad;
	}
	public void setPrmpad(Double prmpad) {
		this.prmpad = prmpad;
	}
	@Override
	public String toString() {
		return "SurrenderValHelper [polyer=" + polyer + ", padtrm=" + padtrm + ", toptrm=" + toptrm + ", isumas="
				+ isumas + ", paidup=" + paidup + ", surrnd=" + surrnd + ", mature=" + mature + ", prmpyr=" + prmpyr
				+ ", prmpad=" + prmpad + "]";
	}
}
