package org.arpicoinsurance.groupit.main.dao;

import java.util.List;

import org.arpicoinsurance.groupit.main.model.Quo_Benef_Details;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;
import org.springframework.data.repository.CrudRepository;

public interface Quo_Benef_DetailsDao extends CrudRepository<Quo_Benef_Details, Integer>{
	
	public List<Quo_Benef_Details> findByQuotationDetails(QuotationDetails quotation)throws Exception;
}
