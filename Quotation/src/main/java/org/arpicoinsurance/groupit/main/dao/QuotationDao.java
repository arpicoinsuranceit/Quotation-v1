package org.arpicoinsurance.groupit.main.dao;

import org.arpicoinsurance.groupit.main.model.Quotation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface QuotationDao extends CrudRepository<Quotation,String> {

	@Query("from Quotation where id=?1")
	Quotation findOne(Integer id) throws Exception;
	
	@Modifying
	@Query("DELETE FROM Quotation where id=?1")
	Integer deleteOne(Integer id) throws Exception;
}
