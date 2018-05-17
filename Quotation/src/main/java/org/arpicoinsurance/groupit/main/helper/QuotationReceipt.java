package org.arpicoinsurance.groupit.main.helper;

public class QuotationReceipt {
	
	private Integer QuotationId;
	private Integer QuotationDetailId;
	private String customerName;
	private String CustTitle;
	private String agentCode;
	
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
}
