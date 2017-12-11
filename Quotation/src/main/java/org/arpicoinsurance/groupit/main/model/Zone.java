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
public class Zone implements Serializable{
	
	private Integer zoneId;
	private String zone_Code;
	private String zone_Name;
	
	private String createBy;
	private Date createdate;
	private Date lockin_date;
	
	private Rms_Sbu rms_Sbu;
	
	public Zone() {}
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getZoneId() {
		return zoneId;
	}

	public void setZoneId(Integer zoneId) {
		this.zoneId = zoneId;
	}

	public String getZone_Code() {
		return zone_Code;
	}

	public void setZone_Code(String zone_Code) {
		this.zone_Code = zone_Code;
	}

	public String getZone_Name() {
		return zone_Name;
	}

	public void setZone_Name(String zone_Name) {
		this.zone_Name = zone_Name;
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

	@ManyToOne(cascade=CascadeType.ALL)
	@Column(nullable=false)
	public Rms_Sbu getRms_Sbu() {
		return rms_Sbu;
	}

	public void setRms_Sbu(Rms_Sbu rms_Sbu) {
		this.rms_Sbu = rms_Sbu;
	}
	
	

}
