package org.arpicoinsurance.groupit.main.helper;

public class RetirementPremium {
	
	private Integer pensionTerm;
	private Double premium1;
	private Double premium2;
	private Double premium3;
	public Integer getPensionTerm() {
		return pensionTerm;
	}
	public void setPensionTerm(Integer pensionTerm) {
		this.pensionTerm = pensionTerm;
	}
	public Double getPremium1() {
		return premium1;
	}
	public void setPremium1(Double premium1) {
		this.premium1 = premium1;
	}
	public Double getPremium2() {
		return premium2;
	}
	public void setPremium2(Double premium2) {
		this.premium2 = premium2;
	}
	public Double getPremium3() {
		return premium3;
	}
	public void setPremium3(Double premium3) {
		this.premium3 = premium3;
	}
	
	@Override
	public String toString() {
		return "RetirementPremium [pensionTerm=" + pensionTerm + ", premium1=" + premium1 + ", premium2=" + premium2
				+ ", premium3=" + premium3 + "]";
	}  
	

}
