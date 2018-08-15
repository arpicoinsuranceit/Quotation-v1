package org.arpicoinsurance.groupit.main.service;

import org.arpicoinsurance.groupit.main.helper.EditQuotation;

import org.arpicoinsurance.groupit.main.helper.QuotationReceipt;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;

public interface QuotationDetailsService {

public QuotationDetails findQuotationDetails(Integer qdId)throws Exception;
	
	public EditQuotation editQuotationDetails(Integer qdId)throws Exception;

	public QuotationDetails findFirstByQuotationOrderByQdIdDesc(Integer quotationId) throws Exception;
	
	public QuotationReceipt findQuotationDetailsForReceipt(Integer qdId)throws Exception;

	public boolean isAvailable(Integer qdId, Integer qId)throws Exception;

	public EditQuotation editQuotationDetailsView(Integer qdId)throws Exception;

	public boolean updateStatus(Integer qdId) throws Exception;
	
	public QuotationDetails findByQuotationAndSeqnum(Integer quoId,Integer seqnum) throws Exception;
}
