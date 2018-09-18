package org.arpicoinsurance.groupit.main.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
public class Branch implements Serializable{
	
	private Integer branchId;
	private String branch_Code;
	private String branch_Name;
	private String branch_S_Name;
	private String branch_Tele;
	private String branch_WebSite;
	private String branch_Email;
	private String branch_Fax;
	
	private String createBy;
	private Date createdate;
	private String modifyBy;
	private Date modifydate;
	private Date lockin_date;
	
	private Region region;
	
	public Branch() {}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getBranchId() {
		return branchId;
	}

	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}

	public String getBranch_Code() {
		return branch_Code;
	}

	public void setBranch_Code(String branch_Code) {
		this.branch_Code = branch_Code;
	}

	public String getBranch_Name() {
		return branch_Name;
	}

	public void setBranch_Name(String branch_Name) {
		this.branch_Name = branch_Name;
	}

	public String getBranch_S_Name() {
		return branch_S_Name;
	}

	public void setBranch_S_Name(String branch_S_Name) {
		this.branch_S_Name = branch_S_Name;
	}

	public String getBranch_Tele() {
		return branch_Tele;
	}

	public void setBranch_Tele(String branch_Tele) {
		this.branch_Tele = branch_Tele;
	}

	public String getBranch_WebSite() {
		return branch_WebSite;
	}

	public void setBranch_WebSite(String branch_WebSite) {
		this.branch_WebSite = branch_WebSite;
	}

	public String getBranch_Email() {
		return branch_Email;
	}

	public void setBranch_Email(String branch_Email) {
		this.branch_Email = branch_Email;
	}

	public String getBranch_Fax() {
		return branch_Fax;
	}

	public void setBranch_Fax(String branch_Fax) {
		this.branch_Fax = branch_Fax;
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

	@ManyToOne
	@JoinColumn(nullable = false)
	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}
	
	

}
