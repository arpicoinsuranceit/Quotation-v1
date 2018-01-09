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
public class Quo_Benef_Child_Details implements Serializable{
	private Integer quo_Benef_ChildId;
	private CustChildDetails child;
	private Quo_Benef_Details quoBenefDetails;
	
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
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "quoBenfDetails_id", nullable = false)
	public Quo_Benef_Details getQuoBenefDetails() {
		return quoBenefDetails;
	}
	public void setQuoBenefDetails(Quo_Benef_Details quoBenefDetails) {
		this.quoBenefDetails = quoBenefDetails;
	}
	
	
	
}
