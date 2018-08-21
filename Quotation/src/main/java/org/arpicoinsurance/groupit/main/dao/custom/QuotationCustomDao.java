package org.arpicoinsurance.groupit.main.dao.custom;

import java.util.List;

import org.arpicoinsurance.groupit.main.helper.QuotationSearch;

public interface QuotationCustomDao {
	List<QuotationSearch> getQuotation (String id) throws Exception;

	List<QuotationSearch> getQuotationForReceipt(String id) throws Exception;
}