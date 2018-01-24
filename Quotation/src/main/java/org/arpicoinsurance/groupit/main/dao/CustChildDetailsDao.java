package org.arpicoinsurance.groupit.main.dao;

import org.arpicoinsurance.groupit.main.model.CustChildDetails;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CustChildDetailsDao extends CrudRepository<CustChildDetails,String> {

	@Query("from CustChildDetails where id=?1")
	CustChildDetails findOne(Integer id) throws Exception;
	
	@Modifying
	@Query("DELETE FROM CustChildDetails where id=?1")
	Integer deleteOne(Integer id) throws Exception;
	
}
