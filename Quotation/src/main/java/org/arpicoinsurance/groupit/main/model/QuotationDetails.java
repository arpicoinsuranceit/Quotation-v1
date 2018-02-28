package org.arpicoinsurance.groupit.main.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class QuotationDetails implements Serializable{
	private Integer qdId;
	private String paingTerm;
	private Integer polTerm;
	private Double baseSum;
	private String payMode;
	private Double premiumMonth;
	private Double premiumMonthT;
	private Double premiumQuater;
	private Double premiumQuaterT;
	private Double premiumHalf;
	private Double premiumHalfT;
	private Double premiumYear;
	private Double premiumYearT;
	private Double premiumSingle;
	private Double premiumSingleT;
	
	
	private Double policyFee;
	private Double taxAmount;
	private Double adminFee;
	
	private Double interestRate;
	private Double investmentPos;
	private Double lifePos;
	
	private Quotation quotation;
	private CustomerDetails customerDetails;
	private CustomerDetails spouseDetails;
	
	private String quotationCreateBy;
	private Date quotationCreateDate;
	private String quotationModifyBy;
	private Date quotationModifyDate;
	
	public QuotationDetails() {}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getQdId() {
		return qdId;
	}

	public void setQdId(Integer qdId) {
		this.qdId = qdId;
	}

	public String getPaingTerm() {
		return paingTerm;
	}

	public void setPaingTerm(String paingTerm) {
		this.paingTerm = paingTerm;
	}

	public Integer getPolTerm() {
		return polTerm;
	}

	public void setPolTerm(Integer polTerm) {
		this.polTerm = polTerm;
	}

	public Double getBaseSum() {
		return baseSum;
	}

	public void setBaseSum(Double baseSum) {
		this.baseSum = baseSum;
	}

	public String getPayMode() {
		return payMode;
	}

	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}

	public Double getPremiumMonth() {
		return premiumMonth;
	}

	public void setPremiumMonth(Double premiumMonth) {
		this.premiumMonth = premiumMonth;
	}

	public Double getPremiumMonthT() {
		return premiumMonthT;
	}

	public void setPremiumMonthT(Double premiumMonthT) {
		this.premiumMonthT = premiumMonthT;
	}

	public Double getPremiumQuater() {
		return premiumQuater;
	}

	public void setPremiumQuater(Double premiumQuater) {
		this.premiumQuater = premiumQuater;
	}

	public Double getPremiumQuaterT() {
		return premiumQuaterT;
	}

	public void setPremiumQuaterT(Double premiumQuaterT) {
		this.premiumQuaterT = premiumQuaterT;
	}

	public Double getPremiumHalf() {
		return premiumHalf;
	}

	public void setPremiumHalf(Double premiumHalf) {
		this.premiumHalf = premiumHalf;
	}

	public Double getPremiumHalfT() {
		return premiumHalfT;
	}

	public void setPremiumHalfT(Double premiumHalfT) {
		this.premiumHalfT = premiumHalfT;
	}

	public Double getPremiumYear() {
		return premiumYear;
	}

	public void setPremiumYear(Double premiumYear) {
		this.premiumYear = premiumYear;
	}

	public Double getPremiumYearT() {
		return premiumYearT;
	}

	public void setPremiumYearT(Double premiumYearT) {
		this.premiumYearT = premiumYearT;
	}

	public Double getPremiumSingle() {
		return premiumSingle;
	}

	public void setPremiumSingle(Double premiumSingle) {
		this.premiumSingle = premiumSingle;
	}

	public Double getPremiumSingleT() {
		return premiumSingleT;
	}

	public void setPremiumSingleT(Double premiumSingleT) {
		this.premiumSingleT = premiumSingleT;
	}

	public Double getPolicyFee() {
		return policyFee;
	}

	public void setPolicyFee(Double policyFee) {
		this.policyFee = policyFee;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Double getAdminFee() {
		return adminFee;
	}

	public void setAdminFee(Double adminFee) {
		this.adminFee = adminFee;
	}

	public Double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(Double interestRate) {
		this.interestRate = interestRate;
	}

	public Double getInvestmentPos() {
		return investmentPos;
	}

	public void setInvestmentPos(Double investmentPos) {
		this.investmentPos = investmentPos;
	}

	public Double getLifePos() {
		return lifePos;
	}

	public void setLifePos(Double lifePos) {
		this.lifePos = lifePos;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "quotation_id", nullable = false)
	public Quotation getQuotation() {
		return quotation;
	}

	public void setQuotation(Quotation quotation) {
		this.quotation = quotation;
	}

	public String getQuotationCreateBy() {
		return quotationCreateBy;
	}

	public void setQuotationCreateBy(String quotationCreateBy) {
		this.quotationCreateBy = quotationCreateBy;
	}

	public Date getQuotationquotationCreateDate() {
		return quotationCreateDate;
	}

	public void setQuotationquotationCreateDate(Date quotationCreateDate) {
		this.quotationCreateDate = quotationCreateDate;
	}

	public String getQuotationModifyBy() {
		return quotationModifyBy;
	}

	public void setQuotationModifyBy(String quotationModifyBy) {
		this.quotationModifyBy = quotationModifyBy;
	}

	public Date getQuotationModifyDate() {
		return quotationModifyDate;
	}

	public void setQuotationModifyDate(Date quotationModifyDate) {
		this.quotationModifyDate = quotationModifyDate;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "customer_id", nullable = false)
	public CustomerDetails getCustomerDetails() {
		return customerDetails;
	}

	public void setCustomerDetails(CustomerDetails customerDetails) {
		this.customerDetails = customerDetails;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "spouse_id", nullable = true)
	public CustomerDetails getSpouseDetails() {
		return spouseDetails;
	}

	public void setSpouseDetails(CustomerDetails spouseDetails) {
		this.spouseDetails = spouseDetails;
	}
}
