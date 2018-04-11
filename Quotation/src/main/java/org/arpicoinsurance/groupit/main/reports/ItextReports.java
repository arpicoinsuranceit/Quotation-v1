package org.arpicoinsurance.groupit.main.reports;

import java.util.HashMap;

public interface ItextReports {
	
	HashMap<String , Object> createQuotationReport(Integer quoId) throws Exception;
	
}