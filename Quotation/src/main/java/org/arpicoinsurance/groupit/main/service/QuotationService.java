package org.arpicoinsurance.groupit.main.service;

import java.util.ArrayList;
import java.util.List;

import org.arpicoinsurance.groupit.main.helper.QuoDetails;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Details;
import org.arpicoinsurance.groupit.main.model.Quotation;
import org.arpicoinsurance.groupit.main.model.Users;

public interface QuotationService {
	
	boolean saveQuotation(Quo_Benef_Details qbd) throws Exception;
	
	boolean updateQuotation(Quo_Benef_Details qbd) throws Exception;
	
	boolean deleteQuotation(Integer id) throws Exception;
	
	Quotation getQuotation(Integer id) throws Exception;
	
	List <Quotation> getAllQuotation() throws Exception;
	
	List <Quotation> getQuotationByUserId(Users users,String status) throws Exception;

	ArrayList<QuoDetails> getQuotationDetails(Integer id) throws Exception;

}
