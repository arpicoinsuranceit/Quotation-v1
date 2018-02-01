package org.arpicoinsurance.groupit.main.dao;

import org.arpicoinsurance.groupit.main.model.Quotation;
import org.springframework.data.repository.CrudRepository;

public interface QuotationDao extends CrudRepository<Quotation, Integer>{
	
	Quotation findById(Integer id) throws Exception;

}
