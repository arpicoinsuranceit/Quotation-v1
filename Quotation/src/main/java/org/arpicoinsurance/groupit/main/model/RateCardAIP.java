package org.arpicoinsurance.groupit.main.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "inratecardaip")
public class RateCardAIP {
	
	@Id
    private String id;
    private Integer termto;
    private Integer termfrom;
    private String paymod;
    private Integer polyear;
    private Double rate;
    private Date strdat;
    private Date enddat;
    private String creaby;
    private Date creadt;
    
	public RateCardAIP() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getTermto() {
		return termto;
	}
	public void setTermto(Integer termto) {
		this.termto = termto;
	}
	public Integer getTermfrom() {
		return termfrom;
	}
	public void setTermfrom(Integer termfrom) {
		this.termfrom = termfrom;
	}
	public String getPaymod() {
		return paymod;
	}
	public void setPaymod(String paymod) {
		this.paymod = paymod;
	}
	public Integer getPolyear() {
		return polyear;
	}
	public void setPolyear(Integer polyear) {
		this.polyear = polyear;
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
    
}
