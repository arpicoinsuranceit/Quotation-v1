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
public class SheduleDetails implements Serializable {
	private Integer sheDetailId;
	private Shedule shedule;
	private Integer policyYear;
	private Integer outYear;
	private Double outSum;
	private Double lorned;
	private Double premiumRate;
	private Double premium;
	
	public SheduleDetails() {}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getSheDetailId() {
		return sheDetailId;
	}

	public void setSheDetailId(Integer sheDetailId) {
		this.sheDetailId = sheDetailId;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "shedule_id", nullable = false)
	public Shedule getSheduleId() {
		return shedule;
	}

	public void setSheduleId(Shedule shedule) {
		this.shedule = shedule;
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
