package org.arpicoinsurance.groupit.main.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MedicalReq implements Serializable{

	private Integer id;
	private String medCode;
	private String medName;
	private String catCode;
	private String medCreateBy;
	private Date medCreatedate;
	private String medModifyBy;
	private Date medModifydate;
	
	public MedicalReq() {}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMedCode() {
		return medCode;
	}
	public void setMedCode(String medCode) {
		this.medCode = medCode;
	}
	public String getMedName() {
		return medName;
	}
	public void setMedName(String medName) {
		this.medName = medName;
	}
	public String getCatCode() {
		return catCode;
	}

	public void setCatCode(String catCode) {
		this.catCode = catCode;
	}

	public String getMedCreateBy() {
		return medCreateBy;
	}
	public void setMedCreateBy(String medCreateBy) {
		this.medCreateBy = medCreateBy;
	}
	public Date getMedCreatedate() {
		return medCreatedate;
	}
	public void setMedCreatedate(Date medCreatedate) {
		this.medCreatedate = medCreatedate;
	}
	public String getMedModifyBy() {
		return medModifyBy;
	}
	public void setMedModifyBy(String medModifyBy) {
		this.medModifyBy = medModifyBy;
	}
	public Date getMedModifydate() {
		return medModifydate;
	}
	public void setMedModifydate(Date medModifydate) {
		this.medModifydate = medModifydate;
	}
}
