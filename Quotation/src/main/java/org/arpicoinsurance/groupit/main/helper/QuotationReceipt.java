package org.arpicoinsurance.groupit.main.helper;

public class QuotationReceipt {
	
	private Integer QuotationId;
	private Integer QuotationDetailId;
	private String customerName;
	private String CustTitle;
	private String agentCode;
	private String productCode; 
	private String productName; 
	private String branchCode;
	private Double premium;
	private Double polfeePremium;
	
	public Integer getQuotationId() {
		return QuotationId;
	}
	public void setQuotationId(Integer quotationId) {
		QuotationId = quotationId;
	}
	public Integer getQuotationDetailId() {
		return QuotationDetailId;
	}
	public void setQuotationDetailId(Integer quotationDetailId) {
		QuotationDetailId = quotationDetailId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustTitle() {
		return CustTitle;
	}
	public void setCustTitle(String custTitle) {
		CustTitle = custTitle;
	}
	public String getAgentCode() {
		return agentCode;
	}
	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public Double getPremium() {
		return premium;
	}
	public void setPremium(Double premium) {
		this.premium = premium;
	}
	public Double getPolfeePremium() {
		return polfeePremium;
	}
	public void setPolfeePremium(Double polfeePremium) {
		this.polfeePremium = polfeePremium;
	}
}
