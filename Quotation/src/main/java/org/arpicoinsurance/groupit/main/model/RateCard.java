package org.arpicoinsurance.groupit.main.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "inratecareds")
public class RateCard {
	@Id
    private String id;
	private String rcdcod;
	private Integer term;
	private Double rate;
	private Date strdat;
	private String creaby;
	private Date creadt;
	private Boolean status;
	private Double ageto;
	private Double intrat;
	private Double sumasu;
	
	public RateCard() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRcdcod() {
		return rcdcod;
	}
	public void setRcdcod(String rcdcod) {
		this.rcdcod = rcdcod;
	}
	public Integer getTerm() {
		return term;
	}
	public void setTerm(Integer term) {
		this.term = term;
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
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public Double getAgeto() {
		return ageto;
	}
	public void setAgeto(Double ageto) {
		this.ageto = ageto;
	}
	public Double getIntrat() {
		return intrat;
	}
	public void setIntrat(Double intrat) {
		this.intrat = intrat;
	}
	public Double getSumasu() {
		return sumasu;
	}
	public void setSumasu(Double sumasu) {
		this.sumasu = sumasu;
	}
	
}
