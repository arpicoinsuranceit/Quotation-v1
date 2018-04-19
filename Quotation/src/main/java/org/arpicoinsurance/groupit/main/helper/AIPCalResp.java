package org.arpicoinsurance.groupit.main.helper;

import java.util.List;

public class AIPCalResp {
	private Double maturaty;
	private Double maturaty10;
	private Double maturaty12;
	private Boolean errorExist;
	private String error;
	private List<AipCalShedule> aipCalShedules;
	
	private Double extraOe;
	
	public Double getMaturaty() {
		return maturaty;
	}
	public void setMaturaty(Double maturaty) {
		this.maturaty = maturaty;
	}
	public List<AipCalShedule> getAipCalShedules() {
		return aipCalShedules;
	}
	public void setAipCalShedules(List<AipCalShedule> aipCalShedules) {
		this.aipCalShedules = aipCalShedules;
	}
	public Double getMaturaty10() {
		return maturaty10;
	}
	public void setMaturaty10(Double maturaty10) {
		this.maturaty10 = maturaty10;
	}
	public Double getMaturaty12() {
		return maturaty12;
	}
	public void setMaturaty12(Double maturaty12) {
		this.maturaty12 = maturaty12;
	}
	public Boolean getErrorExist() {
		return errorExist;
	}
	public void setErrorExist(Boolean errorExist) {
		this.errorExist = errorExist;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public Double getExtraOe() {
		return extraOe;
	}
	public void setExtraOe(Double extraOe) {
		this.extraOe = extraOe;
	}
}
