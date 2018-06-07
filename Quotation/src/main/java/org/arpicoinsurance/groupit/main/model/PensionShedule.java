package org.arpicoinsurance.groupit.main.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class PensionShedule implements Serializable{
	
	private Integer sheduleId;
	
	private Integer policyYear;
	private Integer month;
	private Integer age;
	private Double contribution;
	private Double commisionPayable;
	private Double Expenses;
	private Double deathBenefit; 
	private Double waiverOfPremium;
	private Double contingencyProfitLoading;
	private Double amountCreditedToFund;
	private Double fundBalanceBeforeInterest;
	private Double interestPerAnnumForPolicyHolder;
	private Double closingFundBalanace;
	
	private QuotationDetails quotationDetails;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getSheduleId() {
		return sheduleId;
	}

	public void setSheduleId(Integer sheduleId) {
		this.sheduleId = sheduleId;
	}

	public Integer getPolicyYear() {
		return policyYear;
	}

	public void setPolicyYear(Integer policyYear) {
		this.policyYear = policyYear;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Double getContribution() {
		return contribution;
	}

	public void setContribution(Double contribution) {
		this.contribution = contribution;
	}

	public Double getCommisionPayable() {
		return commisionPayable;
	}

	public void setCommisionPayable(Double commisionPayable) {
		this.commisionPayable = commisionPayable;
	}

	public Double getExpenses() {
		return Expenses;
	}

	public void setExpenses(Double expenses) {
		Expenses = expenses;
	}

	public Double getDeathBenefit() {
		return deathBenefit;
	}

	public void setDeathBenefit(Double deathBenefit) {
		this.deathBenefit = deathBenefit;
	}

	public Double getWaiverOfPremium() {
		return waiverOfPremium;
	}

	public void setWaiverOfPremium(Double waiverOfPremium) {
		this.waiverOfPremium = waiverOfPremium;
	}

	public Double getContingencyProfitLoading() {
		return contingencyProfitLoading;
	}

	public void setContingencyProfitLoading(Double contingencyProfitLoading) {
		this.contingencyProfitLoading = contingencyProfitLoading;
	}

	public Double getAmountCreditedToFund() {
		return amountCreditedToFund;
	}

	public void setAmountCreditedToFund(Double amountCreditedToFund) {
		this.amountCreditedToFund = amountCreditedToFund;
	}

	public Double getFundBalanceBeforeInterest() {
		return fundBalanceBeforeInterest;
	}

	public void setFundBalanceBeforeInterest(Double fundBalanceBeforeInterest) {
		this.fundBalanceBeforeInterest = fundBalanceBeforeInterest;
	}

	public Double getInterestPerAnnumForPolicyHolder() {
		return interestPerAnnumForPolicyHolder;
	}

	public void setInterestPerAnnumForPolicyHolder(Double interestPerAnnumForPolicyHolder) {
		this.interestPerAnnumForPolicyHolder = interestPerAnnumForPolicyHolder;
	}

	public Double getClosingFundBalanace() {
		return closingFundBalanace;
	}

	public void setClosingFundBalanace(Double closingFundBalanace) {
		this.closingFundBalanace = closingFundBalanace;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "quotationDetail_id", nullable = false)
	public QuotationDetails getQuotationDetails() {
		return quotationDetails;
	}

	public void setQuotationDetails(QuotationDetails quotationDetails) {
		this.quotationDetails = quotationDetails;
	}
	
	
	
	
}
