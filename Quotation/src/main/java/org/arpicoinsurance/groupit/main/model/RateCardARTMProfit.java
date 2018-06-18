package org.arpicoinsurance.groupit.main.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "inratecardartmprof")
public class RateCardARTMProfit {
	
	@Id
    private String id;
    private Integer polyerto;
    private Integer polyerfrom;
    private String paymod;
    private Double rate;
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
	public Integer getPolyerto() {
		return polyerto;
	}
	public void setPolyerto(Integer polyerto) {
		this.polyerto = polyerto;
	}
	public Integer getPolyerfrom() {
		return polyerfrom;
	}
	public void setPolyerfrom(Integer polyerfrom) {
		this.polyerfrom = polyerfrom;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
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
	public String getPaymod() {
		return paymod;
	}
	public void setPaymod(String paymod) {
		this.paymod = paymod;
	}

}
