package org.arpicoinsurance.groupit.main.dao;

import org.arpicoinsurance.groupit.main.model.CustomerDetails;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CustomerDetailsDao extends CrudRepository<CustomerDetails,String> {

	@Query("from CustomerDetails where id=?1")
	CustomerDetails findOne(Integer id) throws Exception;
	
	@Modifying
	@Query("DELETE FROM CustomerDetails where id=?1")
	Integer deleteOne(Integer id) throws Exception;
}
