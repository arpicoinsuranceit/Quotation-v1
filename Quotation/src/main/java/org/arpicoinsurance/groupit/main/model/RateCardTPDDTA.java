package org.arpicoinsurance.groupit.main.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "inratecarddtatpd")
public class RateCardTPDDTA {
	
	@Id
    private String id;
    private Integer age;
    private Integer term;
    private String sex;
    private Double rate;
    private Date strdat;
    private Date enddat;
    private String creaby;
    private Date creadt;
    
	public RateCardTPDDTA() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getTerm() {
		return term;
	}

	public void setTerm(Integer term) {
		this.term = term;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
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
