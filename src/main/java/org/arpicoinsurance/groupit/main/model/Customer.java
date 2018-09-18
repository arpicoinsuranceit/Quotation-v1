package org.arpicoinsurance.groupit.main.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Customer implements Serializable{
	private Integer customerID;
	private String custCode;
	private String custName;
	
	private String custCreateBy;
	private Date custCreateDate;
	private String custModifyBy;
	private Date custModifyDate;
	
	public Customer() {}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getCustomerID() {
		return customerID;
	}
	public void setCustomerID(Integer customerID) {
		this.customerID = customerID;
	}
	public String getCustCode() {
		return custCode;
	}
	public void setCustCode(String custCode) {
		this.custCode = custCode;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getCustCreateBy() {
		return custCreateBy;
	}
	public void setCustCreateBy(String custCreateBy) {
		this.custCreateBy = custCreateBy;
	}
	public Date getCustCreateDate() {
		return custCreateDate;
	}
	public void setCustCreateDate(Date custCreateDate) {
		this.custCreateDate = custCreateDate;
	}
	public String getCustModifyBy() {
		return custModifyBy;
	}
	public void setCustModifyBy(String custModifyBy) {
		this.custModifyBy = custModifyBy;
	}
	public Date getCustModifyDate() {
		return custModifyDate;
	}
	public void setCustModifyDate(Date custModifyDate) {
		this.custModifyDate = custModifyDate;
	}
}
