package org.arpicoinsurance.groupit.main.service;

import org.arpicoinsurance.groupit.main.helper.EditQuotation;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;

public interface QuotationDetailsService {
	
	public QuotationDetails findQuotationDetails(Integer qdId)throws Exception;
	
	public EditQuotation editQuotationDetails(Integer qdId)throws Exception;
	
	public EditQuotation editQuotationDetailsView(Integer qdId)throws Exception;
	
	QuotationDetails findFirstByQuotationOrderByQdIdDesc(Integer quotationId) throws Exception;
	
}
