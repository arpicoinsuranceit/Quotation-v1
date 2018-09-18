package org.arpicoinsurance.groupit.main.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MediTestGrid {
	private Integer id;
	private Double sumAssuredFrom;
	private Double sumAssuredTo;
	private Integer ageFrom;
	private Integer ageTo;
	private String tests;
	private String mediGrade;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Double getSumAssuredFrom() {
		return sumAssuredFrom;
	}
	public void setSumAssuredFrom(Double sumAssuredFrom) {
		this.sumAssuredFrom = sumAssuredFrom;
	}
	public Double getSumAssuredTo() {
		return sumAssuredTo;
	}
	public void setSumAssuredTo(Double sumAssuredTo) {
		this.sumAssuredTo = sumAssuredTo;
	}
	public Integer getAgeFrom() {
		return ageFrom;
	}
	public void setAgeFrom(Integer ageFrom) {
		this.ageFrom = ageFrom;
	}
	public Integer getAgeTo() {
		return ageTo;
	}
	public void setAgeTo(Integer ageTo) {
		this.ageTo = ageTo;
	}
	public String getTests() {
		return tests;
	}
	public void setTests(String tests) {
		this.tests = tests;
	}
	public String getMediGrade() {
		return mediGrade;
	}
	public void setMediGrade(String mediGrade) {
		this.mediGrade = mediGrade;
	}
}
