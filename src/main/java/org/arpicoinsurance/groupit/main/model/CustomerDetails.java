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
public class CustomerDetails implements Serializable{

	private Integer custDetailId;
	private String custTitle;
	private String custName;
	private String custNic;
	private String custAddress;
	private Date custDob;
	private String custGender;
	private String custCivilStatus;
	private String custTel;
	private String custTelx;
	private String custEmail;
	
	private Customer customer;
	private Occupation occupation;
	
	private Date lockin_date;
	private String custCreateBy;
	private Date custCreateDate;
	private String custModifyBy;
	private Date custModifyDate;
	
	public CustomerDetails() {}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getCustDetailId() {
		return custDetailId;
	}

	public void setCustDetailId(Integer custDetailId) {
		this.custDetailId = custDetailId;
	}

	public String getCustTitle() {
		return custTitle;
	}
	
	public void setCustTitle(String custTitle) {
		this.custTitle = custTitle;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getCustNic() {
		return custNic;
	}

	public void setCustNic(String custNic) {
		this.custNic = custNic;
	}

	public String getCustAddress() {
		return custAddress;
	}

	public void setCustAddress(String custAddress) {
		this.custAddress = custAddress;
	}

	public Date getCustDob() {
		return custDob;
	}

	public void setCustDob(Date custDob) {
		this.custDob = custDob;
	}

	public String getCustGender() {
		return custGender;
	}

	public void setCustGender(String custGender) {
		this.custGender = custGender;
	}

	public String getCustCivilStatus() {
		return custCivilStatus;
	}

	public void setCustCivilStatus(String custCivilStatus) {
		this.custCivilStatus = custCivilStatus;
	}

	public String getCustTel() {
		return custTel;
	}

	public void setCustTel(String custTel) {
		this.custTel = custTel;
	}

	public String getCustTelx() {
		return custTelx;
	}

	public void setCustTelx(String custTelx) {
		this.custTelx = custTelx;
	}

	public String getCustEmail() {
		return custEmail;
	}

	public void setCustEmail(String custEmail) {
		this.custEmail = custEmail;
	}

	public Date getLockin_date() {
		return lockin_date;
	}

	public void setLockin_date(Date lockin_date) {
		this.lockin_date = lockin_date;
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
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cust_id", nullable = false)
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ocupation_id", nullable = false)
	public Occupation getOccupation() {
		return occupation;
	}

	public void setOccupation(Occupation occupation) {
		this.occupation = occupation;
	}
	
}
