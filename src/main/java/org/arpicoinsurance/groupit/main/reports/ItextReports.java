package org.arpicoinsurance.groupit.main.reports;

public interface ItextReports {
	
	byte[] createQuotationReport(Integer quoId) throws Exception;
	
}