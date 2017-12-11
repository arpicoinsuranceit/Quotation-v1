package org.arpicoinsurance.groupit.main.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Region implements Serializable{
	private Integer regionId;
	private String region_Code;
	private String region_Name;
	
	private String createBy;
	private Date createdate;
	private Date lockin_date;
	
	private Zone zone;
	
	public Region() {}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

	public String getRegion_Name() {
		return region_Name;
	}

	public void setRegion_Name(String region_Name) {
		this.region_Name = region_Name;
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

	public Date getLockin_date() {
		return lockin_date;
	}

	public void setLockin_date(Date lockin_date) {
		this.lockin_date = lockin_date;
	}
	
	
	
	public String getRegion_Code() {
		return region_Code;
	}

	public void setRegion_Code(String region_Code) {
		this.region_Code = region_Code;
	}

	@ManyToOne
	public Zone getZone() {
		return zone;
	}

	public void setZone(Zone zone) {
		this.zone = zone;
	}
	
	
}
