package org.arpicoinsurance.groupit.main.service;

import java.util.List;

import org.arpicoinsurance.groupit.main.helper.QuotationSearch;
import org.arpicoinsurance.groupit.main.helper.QuotationSearchProp;

public interface QuotationReceiptService {
	
	public List<QuotationSearch> searchQuotation(String id) throws Exception;
	
	public List<QuotationSearchProp> searchQuotationProp(String id) throws Exception;
	
}
