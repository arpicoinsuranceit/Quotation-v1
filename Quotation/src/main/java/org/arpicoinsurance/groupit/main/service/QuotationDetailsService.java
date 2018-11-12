package org.arpicoinsurance.groupit.main.service;

import org.arpicoinsurance.groupit.main.helper.EditQuotation;

import org.arpicoinsurance.groupit.main.helper.QuotationReceipt;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;

public interface QuotationDetailsService {
	
	public QuotationDetails findQuotationDetails(Integer qdId)throws Exception;
	
	public QuotationReceipt findQuotationDetailsForReceipt(Integer qdId , Integer seqNo)throws Exception;

	public boolean isAvailable(Integer seqNo, Integer qId)throws Exception;

	public EditQuotation editQuotationDetails(Integer qdId)throws Exception;

	public QuotationDetails findFirstByQuotationOrderByQdIdDesc(Integer quotationId) throws Exception;
	
	public EditQuotation editQuotationDetailsView(Integer qdId)throws Exception;
	
	public boolean updateStatus(Integer seqNo, Integer qId) throws Exception;

	QuotationDetails findFirstByQuotationOrderByQdIdDesc(Integer quotationId, String type) throws Exception;

	
	public QuotationDetails findByQuotationAndSeqnum(Integer quoId,Integer seqnum) throws Exception;

	public String checkNicValidation(String nic,String gender,Integer age,Integer seqNo,Integer qId) throws Exception;

	
}
