package org.arpicoinsurance.groupit.main.dao;

import org.arpicoinsurance.groupit.main.model.QuotationDetails;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface QuotationDetailsDao extends CrudRepository<QuotationDetails,String> {

	@Query("from QuotationDetails where id=?1")
	QuotationDetails findOne(Integer id) throws Exception;
	
	@Modifying
	@Query("DELETE FROM QuotationDetails where id=?1")
	Integer deleteOne(Integer id) throws Exception;
}
