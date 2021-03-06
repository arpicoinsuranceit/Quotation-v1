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
public class MedicalDetails implements Serializable{

	private Integer id;
	private String status;
	private String custStatus;
	
	private MedicalReq medicalReq;
	private QuotationDetails quotationDetails;
	
	private String medDetailsCreateBy;
	private Date medDetailsCreatedate;
	private String medDetailsModifyBy;
	private Date medDetailsModifydate;
	
	public MedicalDetails() {}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getCustStatus() {
		return custStatus;
	}

	public void setCustStatus(String custStatus) {
		this.custStatus = custStatus;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "medicalReq_id", nullable = false)
	public MedicalReq getMedicalReq() {
		return medicalReq;
	}
	public void setMedicalReq(MedicalReq medicalReq) {
		this.medicalReq = medicalReq;
	}
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "quotationDetail_id", nullable = false)
	public QuotationDetails getQuotationDetails() {
		return quotationDetails;
	}

	public void setQuotationDetails(QuotationDetails quotationDetails) {
		this.quotationDetails = quotationDetails;
	}

	public String getMedDetailsCreateBy() {
		return medDetailsCreateBy;
	}
	public void setMedDetailsCreateBy(String medDetailsCreateBy) {
		this.medDetailsCreateBy = medDetailsCreateBy;
	}
	public Date getMedDetailsCreatedate() {
		return medDetailsCreatedate;
	}
	public void setMedDetailsCreatedate(Date medDetailsCreatedate) {
		this.medDetailsCreatedate = medDetailsCreatedate;
	}
	public String getMedDetailsModifyBy() {
		return medDetailsModifyBy;
	}
	public void setMedDetailsModifyBy(String medDetailsModifyBy) {
		this.medDetailsModifyBy = medDetailsModifyBy;
	}
	public Date getMedDetailsModifydate() {
		return medDetailsModifydate;
	}
	public void setMedDetailsModifydate(Date medDetailsModifydate) {
		this.medDetailsModifydate = medDetailsModifydate;
	}
}
