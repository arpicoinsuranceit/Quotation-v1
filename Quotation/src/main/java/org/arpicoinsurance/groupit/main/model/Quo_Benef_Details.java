package org.arpicoinsurance.groupit.main.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Quo_Benef_Details implements Serializable{
	private Integer quo_Benef_DetailsId;
	private Double riderPremium;
	private Integer riderTerm;
	private Double riderSum;
	private String riderCode;
	
	private QuotationDetails quotationDetails;
	private Benefits benefit;
	
	private String quo_Benef_CreateBy;
	private Date quo_Benef_CreateDate;
	private String quo_Benef_ModifyBy;
	private Date quo_Benef_ModifyDate;
	
	public Quo_Benef_Details() {
		// TODO Auto-generated constructor stub
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getQuo_Benef_DetailsId() {
		return quo_Benef_DetailsId;
	}
	public void setQuo_Benef_DetailsId(Integer quo_Benef_DetailsId) {
		this.quo_Benef_DetailsId = quo_Benef_DetailsId;
	}
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="quodetails_Id",nullable=false)
	public QuotationDetails getQuotationDetails() {
		return quotationDetails;
	}
	public void setQuotationDetails(QuotationDetails quotationDetails) {
		this.quotationDetails = quotationDetails;
	}
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="benef_Id",nullable=false)
	public Benefits getBenefit() {
		return benefit;
	}
	public void setBenefit(Benefits benefId) {
		this.benefit = benefId;
	}
	
	public Double getRiderPremium() {
		return riderPremium;
	}
	public void setRiderPremium(Double riderPremium) {
		this.riderPremium = riderPremium;
	}
	public Integer getRiderTerm() {
		return riderTerm;
	}
	public void setRiderTerm(Integer riderTerm) {
		this.riderTerm = riderTerm;
	}
	public String getQuo_Benef_CreateBy() {
		return quo_Benef_CreateBy;
	}
	public void setQuo_Benef_CreateBy(String quo_Benef_CreateBy) {
		this.quo_Benef_CreateBy = quo_Benef_CreateBy;
	}
	public Date getQuo_Benef_CreateDate() {
		return quo_Benef_CreateDate;
	}
	public void setQuo_Benef_CreateDate(Date quo_Benef_CreateDate) {
		this.quo_Benef_CreateDate = quo_Benef_CreateDate;
	}
	public String getQuo_Benef_ModifyBy() {
		return quo_Benef_ModifyBy;
	}
	public void setQuo_Benef_ModifyBy(String quo_Benef_ModifyBy) {
		this.quo_Benef_ModifyBy = quo_Benef_ModifyBy;
	}
	public Date getQuo_Benef_ModifyDate() {
		return quo_Benef_ModifyDate;
	}
	public void setQuo_Benef_ModifyDate(Date quo_Benef_ModifyDate) {
		this.quo_Benef_ModifyDate = quo_Benef_ModifyDate;
	}

	public Double getRiderSum() {
		return riderSum;
	}

	public void setRiderSum(Double riderSum) {
		this.riderSum = riderSum;
	}

	public String getRierCode() {
		return riderCode;
	}

	public void setRierCode(String riderCode) {
		this.riderCode = riderCode;
	}

	@Override
	public String toString() {
		return "Quo_Benef_Details [quo_Benef_DetailsId=" + quo_Benef_DetailsId + ", riderPremium=" + riderPremium
				+ ", riderTerm=" + riderTerm + ", riderSum=" + riderSum + ", riderCode=" + riderCode
				+ ", quotationDetails=" + quotationDetails + ", benefit=" + benefit + ", quo_Benef_CreateBy="
				+ quo_Benef_CreateBy + ", quo_Benef_CreateDate=" + quo_Benef_CreateDate + ", quo_Benef_ModifyBy="
				+ quo_Benef_ModifyBy + ", quo_Benef_ModifyDate=" + quo_Benef_ModifyDate + "]";
	}
	
	
	
}
