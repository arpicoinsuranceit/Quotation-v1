package org.arpicoinsurance.groupit.main.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Occupation implements Serializable{
	private Integer ocupationid;
	private String ocupationCode;
	private String ocupationName;
	
	private String ocupationCreateBy;
	private Date ocupationCreateDate;
	private String ocupationModifyBy;
	private Date ocupationModifyDate;
	
	public Occupation() {}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getOcupationid() {
		return ocupationid;
	}
	public void setOcupationid(Integer ocupationid) {
		this.ocupationid = ocupationid;
	}
	public String getOcupationCode() {
		return ocupationCode;
	}
	public void setOcupationCode(String ocupationCode) {
		this.ocupationCode = ocupationCode;
	}
	public String getOcupationName() {
		return ocupationName;
	}
	public void setOcupationName(String ocupationName) {
		this.ocupationName = ocupationName;
	}
	public String getOcupationCreateBy() {
		return ocupationCreateBy;
	}
	public void setOcupationCreateBy(String ocupationCreateBy) {
		this.ocupationCreateBy = ocupationCreateBy;
	}
	public Date getOcupationCreateDate() {
		return ocupationCreateDate;
	}
	public void setOcupationCreateDate(Date ocupationCreateDate) {
		this.ocupationCreateDate = ocupationCreateDate;
	}
	public String getOcupationModifyBy() {
		return ocupationModifyBy;
	}
	public void setOcupationModifyBy(String ocupationModifyBy) {
		this.ocupationModifyBy = ocupationModifyBy;
	}
	public Date getOcupationModifyDate() {
		return ocupationModifyDate;
	}
	public void setOcupationModifyDate(Date ocupationModifyDate) {
		this.ocupationModifyDate = ocupationModifyDate;
	}
}
