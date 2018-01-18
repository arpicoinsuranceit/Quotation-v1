package org.arpicoinsurance.groupit.main.model;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;

@Entity
public class Quotation implements Serializable{
	private Integer id;
	private String status;
	private Products products;
	private CustomerDetails customerDetails;
	private CustomerDetails spouseDetails;
	private Users user;
	
	public Quotation(){}

	@TableGenerator(name="tbl", initialValue= 20000)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator="tbl")
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

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "product_id", nullable = false)
	public Products getProducts() {
		return products;
	}

	public void setProducts(Products products) {
		this.products = products;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "customer_id", nullable = false)
	public CustomerDetails getCustomerDetails() {
		return customerDetails;
	}

	public void setCustomerDetails(CustomerDetails customerDetails) {
		this.customerDetails = customerDetails;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", nullable = false)
	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "spouse_id", nullable = true)
	public CustomerDetails getSpouseDetails() {
		return spouseDetails;
	}

	public void setSpouseDetails(CustomerDetails spouseDetails) {
		this.spouseDetails = spouseDetails;
	}


}
