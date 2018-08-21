package org.arpicoinsurance.groupit.main.service;

import java.util.List;

import org.arpicoinsurance.groupit.main.helper.QuotationSearch;

public interface BranchUnderwriteService {
	
	List<QuotationSearch> getQuotationToUnderwrite (String token) throws Exception;

}
