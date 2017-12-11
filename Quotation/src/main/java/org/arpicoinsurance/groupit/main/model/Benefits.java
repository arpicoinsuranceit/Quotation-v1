package org.arpicoinsurance.groupit.main.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Benefits implements Serializable{
	
	private Integer id;
	private String benefitName;
	private Integer benefitMinAge;
	private Integer benefitMaxAge;
	
	private Date lockin_date;
	private String benefitCreateBy;
	private Date benefitCreateDate;
	private String benefitModifyBy;
	private Date benefitModifyDate;
	
	public Benefits(){}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBenefitName() {
		return benefitName;
	}

	public void setBenefitName(String benefitName) {
		this.benefitName = benefitName;
	}

	public Integer getBenefitMinAge() {
		return benefitMinAge;
	}

	public void setBenefitMinAge(Integer benefitMinAge) {
		this.benefitMinAge = benefitMinAge;
	}

	public Integer getBenefitMaxAge() {
		return benefitMaxAge;
	}

	public void setBenefitMaxAge(Integer benefitMaxAge) {
		this.benefitMaxAge = benefitMaxAge;
	}

	public Date getLockin_date() {
		return lockin_date;
	}

	public void setLockin_date(Date lockin_date) {
		this.lockin_date = lockin_date;
	}

	public String getBenefitCreateBy() {
		return benefitCreateBy;
	}

	public void setBenefitCreateBy(String benefitCreateBy) {
		this.benefitCreateBy = benefitCreateBy;
	}

	public Date getBenefitCreateDate() {
		return benefitCreateDate;
	}

	public void setBenefitCreateDate(Date benefitCreateDate) {
		this.benefitCreateDate = benefitCreateDate;
	}

	public String getBenefitModifyBy() {
		return benefitModifyBy;
	}

	public void setBenefitModifyBy(String benefitModifyBy) {
		this.benefitModifyBy = benefitModifyBy;
	}

	public Date getBenefitModifyDate() {
		return benefitModifyDate;
	}

	public void setBenefitModifyDate(Date benefitModifyDate) {
		this.benefitModifyDate = benefitModifyDate;
	}
}
