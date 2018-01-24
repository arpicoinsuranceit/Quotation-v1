package org.arpicoinsurance.groupit.main.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Quo_Benef_Child_Details implements Serializable{
	private Integer quo_Benef_ChildId;
	private CustChildDetails child;
	private Integer term;
	private Double premium;
	private Quo_Benef_Details quo_Benef_Details;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getQuo_Benef_ChildId() {
		return quo_Benef_ChildId;
	}
	public void setQuo_Benef_ChildId(Integer quo_Benef_ChildId) {
		this.quo_Benef_ChildId = quo_Benef_ChildId;
	}
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "child_id", nullable = false)
	public CustChildDetails getCustChildDetails() {
		return child;
	}
	public void setCustChildDetails(CustChildDetails child) {
		this.child = child;
	}

	public Integer getTerm() {
		return term;
	}
	public void setTerm(Integer term) {
		this.term = term;
	}
	public Double getPremium() {
		return premium;
	}
	public void setPremium(Double premium) {
		this.premium = premium;
	}
	

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "quo_benf_details_id", nullable = false)
	public Quo_Benef_Details getQuo_Benef_Details() {
		return quo_Benef_Details;
	}
	
	public void setQuo_Benef_Details(Quo_Benef_Details quo_Benef_Details) {
		this.quo_Benef_Details = quo_Benef_Details;
	}
	
	
	
}
