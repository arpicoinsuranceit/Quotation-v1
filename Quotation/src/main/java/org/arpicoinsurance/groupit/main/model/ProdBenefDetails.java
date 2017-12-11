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
public class ProdBenefDetails implements Serializable{
	
	private Integer id;
	private Products products;
	private Benefits benefits;
	
	public ProdBenefDetails() {
		// TODO Auto-generated constructor stub
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "product_id", nullable = false)
	public Products getProducts() {
		return products;
	}

	public void setProducts(Products products) {
		this.products = products;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "benefit_id", nullable = false)
	public Benefits getBenefits() {
		return benefits;
	}

	public void setBenefits(Benefits benefits) {
		this.benefits = benefits;
	}
	
	
	
	

}
