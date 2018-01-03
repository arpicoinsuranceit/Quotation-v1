package org.arpicoinsurance.groupit.main.service;

import java.util.List;

import org.arpicoinsurance.groupit.main.model.Quo_Benef_Details;
import org.arpicoinsurance.groupit.main.model.Quotation;

public interface QuotationService {
	
	boolean saveQuotation(Quo_Benef_Details qbd) throws Exception;
	
	boolean updateQuotation(Quo_Benef_Details qbd) throws Exception;
	
	boolean deleteQuotation(Integer id) throws Exception;
	
	Quotation getQuotation(Integer id) throws Exception;
	
	List <Quotation> getAllQuotation() throws Exception;
	
	List <Quotation> getQuotationByUserId(Integer id) throws Exception;

}
