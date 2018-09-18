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
public class Surrendervals implements Serializable{

	private Integer id;
	private Integer padtrm;
	private Double isumas;
	private Double paidup;
	private Double surrnd;
	private Double mature;
	private Double prmpyr;
	private Double prmpad;
	private Integer polyer;
	
	private QuotationDetails quotationDetails;
	
	private String createBy;
	private Date createdate;
	
	public Surrendervals() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPadtrm() {
		return padtrm;
	}

	public void setPadtrm(Integer padtrm) {
		this.padtrm = padtrm;
	}

	public Double getIsumas() {
		return isumas;
	}

	public void setIsumas(Double isumas) {
		this.isumas = isumas;
	}

	public Double getPaidup() {
		return paidup;
	}

	public void setPaidup(Double paidup) {
		this.paidup = paidup;
	}

	public Double getSurrnd() {
		return surrnd;
	}

	public void setSurrnd(Double surrnd) {
		this.surrnd = surrnd;
	}

	public Double getMature() {
		return mature;
	}

	public void setMature(Double mature) {
		this.mature = mature;
	}

	public Double getPrmpyr() {
		return prmpyr;
	}

	public void setPrmpyr(Double prmpyr) {
		this.prmpyr = prmpyr;
	}

	public Double getPrmpad() {
		return prmpad;
	}

	public void setPrmpad(Double prmpad) {
		this.prmpad = prmpad;
	}

	public Integer getPolyer() {
		return polyer;
	}

	public void setPolyer(Integer polyer) {
		this.polyer = polyer;
	}
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "quotationDetail_id", nullable = false)
	public QuotationDetails getQuotationDetails() {
		return quotationDetails;
	}

	public void setQuotationDetails(QuotationDetails quotationDetails) {
		this.quotationDetails = quotationDetails;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	
	
}
