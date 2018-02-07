package org.arpicoinsurance.groupit.main.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Shedule implements Serializable{

	private Integer sheduleId;
	private QuotationDetails quotationDetails;
	
	private Integer policyYear;
	private Integer outYear;
	private Double outSum;
	private Double lorned;
	private Double premiumRate;
	private Double premium;

	
	public Shedule() {}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getSheduleId() {
		return sheduleId;
	}
	public void setSheduleId(Integer sheduleId) {
		this.sheduleId = sheduleId;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "quotationDetail_id", nullable = false)
	public QuotationDetails getQuotationDetails() {
		return quotationDetails;
	}

	public void setQuotationDetails(QuotationDetails quotationDetails) {
		this.quotationDetails = quotationDetails;
	}

	public Integer getPolicyYear() {
		return policyYear;
	}

	public void setPolicyYear(Integer policyYear) {
		this.policyYear = policyYear;
	}

	public Integer getOutYear() {
		return outYear;
	}

	public void setOutYear(Integer outYear) {
		this.outYear = outYear;
	}

	public Double getOutSum() {
		return outSum;
	}

	public void setOutSum(Double outSum) {
		this.outSum = outSum;
	}

	public Double getLorned() {
		return lorned;
	}

	public void setLorned(Double lorned) {
		this.lorned = lorned;
	}

	public Double getPremiumRate() {
		return premiumRate;
	}

	public void setPremiumRate(Double premiumRate) {
		this.premiumRate = premiumRate;
	}

	public Double getPremium() {
		return premium;
	}

	public void setPremium(Double premium) {
		this.premium = premium;
	}
	
	
}
