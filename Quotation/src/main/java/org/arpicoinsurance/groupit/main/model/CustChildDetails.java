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
public class CustChildDetails implements Serializable {
	
	private Integer id;
	private CustomerDetails customerDetails;
	private Child child;
	
	public CustChildDetails(){}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cust_id", nullable = false)
	public CustomerDetails getCustomer() {
		return customerDetails;
	}

	public void setCustomer(CustomerDetails customerDetails) {
		this.customerDetails = customerDetails;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "child_id", nullable = false)
	public Child getChild() {
		return child;
	}

	public void setChild(Child child) {
		this.child = child;
	}
	
	
}
