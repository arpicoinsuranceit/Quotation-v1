package org.arpicoinsurance.groupit.main.helper;

import java.util.ArrayList;

public class QuotationView {
	private Integer quoDetailId;
	private QuoCustomer custDetails;
	private ArrayList<QuoBenf> mainLifeBenf;
	private ArrayList<QuoBenf> spouseBenf;
	private ArrayList<QuoBenf> childBenf;
	public Integer getQuoDetailId() {
		return quoDetailId;
	}
	public void setQuoDetailId(Integer quoDetailId) {
		this.quoDetailId = quoDetailId;
	}
	public QuoCustomer getCustDetails() {
		return custDetails;
	}
	public void setCustDetails(QuoCustomer custDetails) {
		this.custDetails = custDetails;
	}
	public ArrayList<QuoBenf> getMainLifeBenf() {
		return mainLifeBenf;
	}
	public void setMainLifeBenf(ArrayList<QuoBenf> mainLifeBenf) {
		this.mainLifeBenf = mainLifeBenf;
	}
	public ArrayList<QuoBenf> getSpouseBenf() {
		return spouseBenf;
	}
	public void setSpouseBenf(ArrayList<QuoBenf> spouseBenf) {
		this.spouseBenf = spouseBenf;
	}
	public ArrayList<QuoBenf> getChildBenf() {
		return childBenf;
	}
	public void setChildBenf(ArrayList<QuoBenf> childBenf) {
		this.childBenf = childBenf;
	}
	
	
}
