package org.arpicoinsurance.groupit.main.service;

import org.arpicoinsurance.groupit.main.model.QuotationDetails;

public interface QuotationDetailsService {
	
	public QuotationDetails findQuotationDetails(Integer qdId)throws Exception;
	
	public QuotationDetails editQuotationDetails(Integer qdId)throws Exception;	
	
}
