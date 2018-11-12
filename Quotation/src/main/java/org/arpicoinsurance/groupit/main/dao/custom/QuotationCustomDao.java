package org.arpicoinsurance.groupit.main.dao.custom;

import java.util.List;

import org.arpicoinsurance.groupit.main.helper.QuotationSearch;
import org.arpicoinsurance.groupit.main.helper.QuotationSearchProp;

public interface QuotationCustomDao {
	List<QuotationSearch> getQuotation (String id) throws Exception;

	List<QuotationSearch> getQuotationForReceipt(String id, String branches) throws Exception;

	List<QuotationSearchProp> getQuotationProp (String id) throws Exception;
	
	List<QuotationSearch> getQuotationToUnderwrite (String status,Integer branchId) throws Exception;

	List<QuotationSearch> getQuotationForReceipt(String id)throws Exception; 

}
