package org.arpicoinsurance.groupit.main.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "inproductvar")
public class RateCardProductVar {
	
	@Id
    private String id;
	private String prdcod;
	private String pracod;
	private String pradec;
	private String pramod;
	private Integer intval;
    private Double dobval;
    private String strval;
    private Date strdat;
    private Date enddat;
    private String creaby;
    private Date creadt;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPrdcod() {
		return prdcod;
	}
	public void setPrdcod(String prdcod) {
		this.prdcod = prdcod;
	}
	public String getPracod() {
		return pracod;
	}
	public void setPracod(String pracod) {
		this.pracod = pracod;
	}
	public String getPradec() {
		return pradec;
	}
	public void setPradec(String pradec) {
		this.pradec = pradec;
	}
	public String getPramod() {
		return pramod;
	}
	public void setPramod(String pramod) {
		this.pramod = pramod;
	}
	public Integer getIntval() {
		return intval;
	}
	public void setIntval(Integer intval) {
		this.intval = intval;
	}
	public Double getDobval() {
		return dobval;
	}
	public void setDobval(Double dobval) {
		this.dobval = dobval;
	}
	public String getStrval() {
		return strval;
	}
	public void setStrval(String strval) {
		this.strval = strval;
	}
	public Date getStrdat() {
		return strdat;
	}
	public void setStrdat(Date strdat) {
		this.strdat = strdat;
	}
	public Date getEnddat() {
		return enddat;
	}
	public void setEnddat(Date enddat) {
		this.enddat = enddat;
	}
	public String getCreaby() {
		return creaby;
	}
	public void setCreaby(String creaby) {
		this.creaby = creaby;
	}
	public Date getCreadt() {
		return creadt;
	}
	public void setCreadt(Date creadt) {
		this.creadt = creadt;
	}

}
