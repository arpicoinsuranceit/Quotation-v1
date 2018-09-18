package org.arpicoinsurance.groupit.main.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "inratecardhrb")
public class RateCardHRB {
	
	@Id
    private String id;
    private Integer ageto;
    private Integer agefrom;
    private String sex;
    private Double sumasu;
    private Integer adlcnt;
    private Integer chlcnt;
    private Double rate;
    private Date strdat;
    private Date enddat;
    private String creaby;
    private Date creadt;
    
	public RateCardHRB() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getAgeto() {
		return ageto;
	}
	public void setAgeto(Integer ageto) {
		this.ageto = ageto;
	}
	public Integer getAgefrom() {
		return agefrom;
	}
	public void setAgefrom(Integer agefrom) {
		this.agefrom = agefrom;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Double getSumasu() {
		return sumasu;
	}
	public void setSumasu(Double sumasu) {
		this.sumasu = sumasu;
	}
	public Integer getAdlcnt() {
		return adlcnt;
	}
	public void setAdlcnt(Integer adlcnt) {
		this.adlcnt = adlcnt;
	}
	public Integer getChlcnt() {
		return chlcnt;
	}
	public void setChlcnt(Integer chlcnt) {
		this.chlcnt = chlcnt;
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
