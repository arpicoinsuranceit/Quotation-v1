package org.arpicoinsurance.groupit.main.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Designation implements Serializable{
	private Integer designationId;
	private String des_Code;
	private String des_Name;
	private String des_Level;
	private Integer des_Active;
	
	private String createBy;
	private Date createdate;
	
	public Designation() {}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getDesignationId() {
		return designationId;
	}

	public void setDesignationId(Integer designationId) {
		this.designationId = designationId;
	}

	public String getDes_Code() {
		return des_Code;
	}

	public void setDes_Code(String des_Code) {
		this.des_Code = des_Code;
	}

	public String getDes_Name() {
		return des_Name;
	}

	public void setDes_Name(String des_Name) {
		this.des_Name = des_Name;
	}

	public String getDes_Level() {
		return des_Level;
	}

	public void setDes_Level(String des_Level) {
		this.des_Level = des_Level;
	}

	public Integer getDes_Active() {
		return des_Active;
	}

	public void setDes_Active(Integer des_Active) {
		this.des_Active = des_Active;
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
	
	
}
