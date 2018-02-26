package org.arpicoinsurance.groupit.main.service;

import java.util.List;
import org.arpicoinsurance.groupit.main.helper.QuotationView;
import org.arpicoinsurance.groupit.main.helper.ViewQuotation;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Details;
import org.arpicoinsurance.groupit.main.model.Quotation;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;

public interface Quo_Benef_DetailsService {
	boolean saveQuo_Benef_Details(Quo_Benef_Details qbd) throws Exception;
	
	boolean updateQuo_Benef_Details(Quo_Benef_Details qbd) throws Exception;
	
	boolean deleteQuo_Benef_Details(Integer id) throws Exception;
	
	Quo_Benef_Details getQuo_Benef_Details(Integer id) throws Exception;
	
	List <Quo_Benef_Details> getAllQuo_Benef_Details() throws Exception;
	
	List <QuotationDetails> getQuo_Benef_DetailsByQuoDetailId(Quotation quotation) throws Exception;
	
	List <QuotationView> getQuo_Benef_DetailsByQuoDetailId(Integer id) throws Exception;
	
	List <Quo_Benef_Details> findByQuotationDetails(QuotationDetails quotation) throws Exception;

	List<ViewQuotation> getQuotationDetails(Integer quoId)throws Exception;
	
	QuotationView getQuo_Benef_DetailByQuoDetailId(QuotationDetails quotationDetails) throws Exception;
	
	QuotationDetails getQuo_Benef_DetailByQuoDetailId(Quotation quotation) throws Exception;
}
