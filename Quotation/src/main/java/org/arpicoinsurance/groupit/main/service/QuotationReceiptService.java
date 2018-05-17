package org.arpicoinsurance.groupit.main.service;

import java.util.List;

import org.arpicoinsurance.groupit.main.helper.QuotationSearch;

public interface QuotationReceiptService {
	
	public List<QuotationSearch> searchQuotation(String id) throws Exception;
	
}
