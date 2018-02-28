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
public class UserProfilePicture {

	private Integer id;
	private String url;
	private Users users;
	private boolean approve;
	private String status;
	private Date uploadDate;
	private Date approvedDate;
	private Users approvedBy;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@ManyToOne(cascade =CascadeType.ALL)
	@JoinColumn(name = "user_id")
	public Users getUsers() {
		return users;
	}
	public void setUsers(Users users) {
		this.users = users;
	}
	public boolean isApprove() {
		return approve;
	}
	public void setApprove(boolean approve) {
		this.approve = approve;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	public Date getApprovedDate() {
		return approvedDate;
	}
	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}
	
	@ManyToOne(cascade =CascadeType.ALL)
	@JoinColumn(name = "approve_id")
	public Users getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(Users approvedBy) {
		this.approvedBy = approvedBy;
	}
	
	
}
