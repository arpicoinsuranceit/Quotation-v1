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
public class OcupationLoading implements Serializable {

	private Integer ocuLoadingId;
	private Double value;
	private Benefits benefits;
	private Occupation occupation;
	private Double subRate;
	private Double emRate;
	private Double rateMil;

	private String ocuLoadingCreateBy;
	private Date ocuLoadingCreateDate;
	private String ocuLoadingModifyBy;
	private Date ocuLoadingModifyDate;

	public OcupationLoading() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getOcuLoadingId() {
		return ocuLoadingId;
	}

	public void setOcuLoadingId(Integer ocuLoadingId) {
		this.ocuLoadingId = ocuLoadingId;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "benifit_id", nullable = false)
	public Benefits getBenefits() {
		return benefits;
	}

	public void setBenefits(Benefits benefits) {
		this.benefits = benefits;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ocupation_id", nullable = false)
	public Occupation getOccupation() {
		return occupation;
	}

	public void setOccupation(Occupation occupation) {
		this.occupation = occupation;
	}

	public Double getSubRate() {
		return subRate;
	}

	public void setSubRate(Double subRate) {
		this.subRate = subRate;
	}

	public Double getEmRate() {
		return emRate;
	}

	public void setEmRate(Double emRate) {
		this.emRate = emRate;
	}

	public Double getRateMil() {
		return rateMil;
	}

	public void setRateMil(Double rateMil) {
		this.rateMil = rateMil;
	}

	public String getOcuLoadingCreateBy() {
		return ocuLoadingCreateBy;
	}

	public void setOcuLoadingCreateBy(String ocuLoadingCreateBy) {
		this.ocuLoadingCreateBy = ocuLoadingCreateBy;
	}

	public Date getOcuLoadingCreateDate() {
		return ocuLoadingCreateDate;
	}

	public void setOcuLoadingCreateDate(Date ocuLoadingCreateDate) {
		this.ocuLoadingCreateDate = ocuLoadingCreateDate;
	}

	public String getOcuLoadingModifyBy() {
		return ocuLoadingModifyBy;
	}

	public void setOcuLoadingModifyBy(String ocuLoadingModifyBy) {
		this.ocuLoadingModifyBy = ocuLoadingModifyBy;
	}

	public Date getOcuLoadingModifyDate() {
		return ocuLoadingModifyDate;
	}

	public void setOcuLoadingModifyDate(Date ocuLoadingModifyDate) {
		this.ocuLoadingModifyDate = ocuLoadingModifyDate;
	}

	@Override
	public String toString() {
		return "OcupationLoading [ocuLoadingId=" + ocuLoadingId + ", value=" + value + ", benefits=" + benefits
				+ ", occupation=" + occupation + ", subRate=" + subRate + ", emRate=" + emRate + ", rateMil=" + rateMil
				+ ", ocuLoadingCreateBy=" + ocuLoadingCreateBy + ", ocuLoadingCreateDate=" + ocuLoadingCreateDate
				+ ", ocuLoadingModifyBy=" + ocuLoadingModifyBy + ", ocuLoadingModifyDate=" + ocuLoadingModifyDate + "]";
	}

}
