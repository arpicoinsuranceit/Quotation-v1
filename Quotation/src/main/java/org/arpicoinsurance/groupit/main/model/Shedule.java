package org.arpicoinsurance.groupit.main.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;


public class Shedule implements Serializable{
	private Integer sheduleId;
	private Quotation quotation;
	
	public Shedule() {}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getSheduleId() {
		return sheduleId;
	}
	public void setSheduleId(Integer sheduleId) {
		this.sheduleId = sheduleId;
	}
	
	@OneToOne(cascade = CascadeType.ALL)
	public Quotation getQuotation() {
		return quotation;
	}
	public void setQuotation(Quotation quotation) {
		this.quotation = quotation;
	} 
}
