package org.arpicoinsurance.groupit.main.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Nominee {

	private Integer id;
	private String nomineeName;
	private Integer age;
	private Date nomineeDob;
	private String relation;
	
	private QuotationDetails quotationDetails;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNomineeName() {
		return nomineeName;
	}

	public void setNomineeName(String nomineeName) {
		this.nomineeName = nomineeName;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Date getNomineeDob() {
		return nomineeDob;
	}

	public void setNomineeDob(Date nomineeDob) {
		this.nomineeDob = nomineeDob;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "quotationDetail_id", nullable = false)
	public QuotationDetails getQuotationDetails() {
		return quotationDetails;
	}

	public void setQuotationDetails(QuotationDetails quotationDetails) {
		this.quotationDetails = quotationDetails;
	}
	
	
}
