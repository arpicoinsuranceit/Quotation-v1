package org.arpicoinsurance.groupit.main.helper;

import java.util.ArrayList;

public class PersonalInfo {
	private Integer mage;
	private String mgenger;
	private Integer mocu;
	private Integer sage;
	private String sgenger;
	private Integer socu;
	private Integer term;
	private String payingterm;
	private String frequance;
	private Double bsa;
	private Double intrate;
	private Double msfb;
	private Integer pensionPaingTerm;
	private Integer retAge;
	private Double mPreviousSumAtRisk;
	private Double sPreviousSumAtRisk;
	
	private ArrayList<Children> childrens;

	
	public Integer getMage() {
		return mage;
	}

	public void setMage(Integer mage) {
		this.mage = mage;
	}

	public String getMgenger() {
		return mgenger;
	}

	public void setMgenger(String mgenger) {
		this.mgenger = mgenger;
	}

	public Integer getMocu() {
		return mocu;
	}

	public void setMocu(Integer mocu) {
		this.mocu = mocu;
	}

	public Integer getSage() {
		return sage;
	}

	public void setSage(Integer sage) {
		this.sage = sage;
	}

	public String getSgenger() {
		return sgenger;
	}

	public void setSgenger(String sgenger) {
		this.sgenger = sgenger;
	}

	public Integer getSocu() {
		return socu;
	}

	public void setSocu(Integer socu) {
		this.socu = socu;
	}

	public Integer getTerm() {
		return term;
	}

	public void setTerm(Integer term) {
		this.term = term;
	}

	public String getPayingterm() {
		return payingterm;
	}

	public void setPayingterm(String payingterm) {
		this.payingterm = payingterm;
	}

	public String getFrequance() {
		return frequance;
	}

	public void setFrequance(String frequance) {
		this.frequance = frequance;
	}

	public ArrayList<Children> getChildrens() {
		return childrens;
	}

	public void setChildrens(ArrayList<Children> childrens) {
		this.childrens = childrens;
	}

	public Double getBsa() {
		return bsa;
	}

	public void setBsa(Double bsa) {
		this.bsa = bsa;
	}
	public Double getIntrate() {
		return intrate;
	}

	public void setIntrate(Double intrate) {
		this.intrate = intrate;

	}
	public Double getMsfb() {
		return msfb;
	}

	public void setMsfb(Double msfb) {
		this.msfb = msfb;
	}

	public Double getmPreviousSumAtRisk() {
		return mPreviousSumAtRisk;
	}

	public void setmPreviousSumAtRisk(Double mPreviousSumAtRisk) {
		this.mPreviousSumAtRisk = mPreviousSumAtRisk;
	}

	public Double getsPreviousSumAtRisk() {
		return sPreviousSumAtRisk;
	}

	public void setsPreviousSumAtRisk(Double sPreviousSumAtRisk) {
		this.sPreviousSumAtRisk = sPreviousSumAtRisk;
	}

	public Integer getPensionPaingTerm() {
		return pensionPaingTerm;
	}

	public void setPensionPaingTerm(Integer pensionPaingTerm) {
		this.pensionPaingTerm = pensionPaingTerm;
	}

	public Integer getRetAge() {
		return retAge;
	}

	public void setRetAge(Integer retAge) {
		this.retAge = retAge;
	}

	@Override
	public String toString() {
		String childrenString = "children : ";

		if (childrens != null) {

			for (Children children : childrens) {
				childrenString += children.toString();
			}
		}
		
		return "PersonalInfo [mage=" + mage + ", mgenger=" + mgenger + ", mocu=" + mocu + ", sage=" + sage
				+ ", sgenger=" + sgenger + ", socu=" + socu + ", term=" + term + ", payingterm=" + payingterm
				+ ", frequance=" + frequance + ", bsa=" + bsa + ", intrate=" + intrate + ", msfb=" + msfb
				+ ", pensionPaingTerm=" + pensionPaingTerm + ", retAge=" + retAge + ", mPreviousSumAtRisk="
				+ mPreviousSumAtRisk + ", sPreviousSumAtRisk=" + sPreviousSumAtRisk + ", childrens=" + childrens + "]" + childrenString;
	}
		
}


