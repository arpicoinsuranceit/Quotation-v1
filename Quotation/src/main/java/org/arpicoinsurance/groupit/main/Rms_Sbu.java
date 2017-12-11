package org.arpicoinsurance.groupit.main;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;

@Entity
public class Rms_Sbu implements Serializable{
	private Integer sbuId;
	private String sbu_Code;
	private String sbu_Name;
	private String sbu_S_Name;
	private String address;
	private String city;
	private String tel;
	private String telx;
	private String fax;
	private String tax_no;
	private String email;
	private Integer active;
	
	private String createBy;
	private Date createdate;
	private String modifyBy;
	private Date modifydate;
	private Date lockin_date;
	
	public Rms_Sbu() {}
	
	public Integer getSbuId() {
		return sbuId;
	}

	public void setSbuId(Integer sbuId) {
		this.sbuId = sbuId;
	}

	public String getSbu_Code() {
		return sbu_Code;
	}

	public void setSbu_Code(String sbu_Code) {
		this.sbu_Code = sbu_Code;
	}

	public String getSbu_Name() {
		return sbu_Name;
	}

	public void setSbu_Name(String sbu_Name) {
		this.sbu_Name = sbu_Name;
	}

	public String getSbu_S_Name() {
		return sbu_S_Name;
	}

	public void setSbu_S_Name(String sbu_S_Name) {
		this.sbu_S_Name = sbu_S_Name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getTelx() {
		return telx;
	}

	public void setTelx(String telx) {
		this.telx = telx;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getTax_no() {
		return tax_no;
	}

	public void setTax_no(String tax_no) {
		this.tax_no = tax_no;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
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

	public String getModifyBy() {
		return modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	public Date getModifydate() {
		return modifydate;
	}

	public void setModifydate(Date modifydate) {
		this.modifydate = modifydate;
	}

	public Date getLockin_date() {
		return lockin_date;
	}

	public void setLockin_date(Date lockin_date) {
		this.lockin_date = lockin_date;
	}
}
