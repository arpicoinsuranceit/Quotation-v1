package org.arpicoinsurance.groupit.main.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Products implements Serializable{
	
	private Integer productId;
	private String productCode;
	private String productName;
	private Integer priductMinAge;
	private Integer priductMaxAge;
	
	private Integer active;
	
	private Date lockin_date;
	private String productCreateBy;
	private Date productCreateDate;
	private String productModifyBy;
	private Date productModifyDate;
	
	public Products() {}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getPriductMinAge() {
		return priductMinAge;
	}

	public void setPriductMinAge(Integer priductMinAge) {
		this.priductMinAge = priductMinAge;
	}

	public Integer getPriductMaxAge() {
		return priductMaxAge;
	}

	public void setPriductMaxAge(Integer priductMaxAge) {
		this.priductMaxAge = priductMaxAge;
	}

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public Date getLockin_date() {
		return lockin_date;
	}

	public void setLockin_date(Date lockin_date) {
		this.lockin_date = lockin_date;
	}

	public String getProductCreateBy() {
		return productCreateBy;
	}

	public void setProductCreateBy(String productCreateBy) {
		this.productCreateBy = productCreateBy;
	}

	public Date getProductCreateDate() {
		return productCreateDate;
	}

	public void setProductCreateDate(Date productCreateDate) {
		this.productCreateDate = productCreateDate;
	}

	public String getProductModifyBy() {
		return productModifyBy;
	}

	public void setProductModifyBy(String productModifyBy) {
		this.productModifyBy = productModifyBy;
	}

	public Date getProductModifyDate() {
		return productModifyDate;
	}

	public void setProductModifyDate(Date productModifyDate) {
		this.productModifyDate = productModifyDate;
	}
	
	
	
}
