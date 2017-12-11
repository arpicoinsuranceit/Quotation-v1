package org.arpicoinsurance.groupit.main.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Users implements Serializable{
	private Integer userId;
    private String user_Code;
    private String user_EmpNo;
    private String user_Title;
    private String user_Name;
    private String user_address;
    private String user_Nic;
    private String user_Passport;
    private String user_Email;
    private String user_Tele;
    private String user_Mobile;
    private Integer user_Active;
    
    private String createBy;
	private Date createdate;
	private String modifyBy;
	private Date modifydate;
	private Date lockin_date;
	
	private Designation designation;
	
	public Users() {}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUser_Code() {
		return user_Code;
	}

	public void setUser_Code(String user_Code) {
		this.user_Code = user_Code;
	}

	public String getUser_EmpNo() {
		return user_EmpNo;
	}

	public void setUser_EmpNo(String user_EmpNo) {
		this.user_EmpNo = user_EmpNo;
	}

	public String getUser_Title() {
		return user_Title;
	}

	public void setUser_Title(String user_Title) {
		this.user_Title = user_Title;
	}

	public String getUser_Name() {
		return user_Name;
	}

	public void setUser_Name(String user_Name) {
		this.user_Name = user_Name;
	}

	public String getUser_address() {
		return user_address;
	}

	public void setUser_address(String user_address) {
		this.user_address = user_address;
	}

	public String getUser_Nic() {
		return user_Nic;
	}

	public void setUser_Nic(String user_Nic) {
		this.user_Nic = user_Nic;
	}

	public String getUser_Passport() {
		return user_Passport;
	}

	public void setUser_Passport(String user_Passport) {
		this.user_Passport = user_Passport;
	}

	public String getUser_Email() {
		return user_Email;
	}

	public void setUser_Email(String user_Email) {
		this.user_Email = user_Email;
	}

	public String getUser_Tele() {
		return user_Tele;
	}

	public void setUser_Tele(String user_Tele) {
		this.user_Tele = user_Tele;
	}

	public String getUser_Mobile() {
		return user_Mobile;
	}

	public void setUser_Mobile(String user_Mobile) {
		this.user_Mobile = user_Mobile;
	}

	public Integer getUser_Active() {
		return user_Active;
	}

	public void setUser_Active(Integer user_Active) {
		this.user_Active = user_Active;
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

	@ManyToOne(cascade=CascadeType.ALL)
	@Column(nullable=false)
	public Designation getDesignation() {
		return designation;
	}

	public void setDesignation(Designation designation) {
		this.designation = designation;
	}
	
	
    
    
}
