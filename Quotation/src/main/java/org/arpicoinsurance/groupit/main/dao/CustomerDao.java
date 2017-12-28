package org.arpicoinsurance.groupit.main.dao;

import org.arpicoinsurance.groupit.main.model.Customer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CustomerDao extends CrudRepository<Customer,String> {

	@Query("from Customer where id=?1")
	Customer findOne(Integer id) throws Exception;
	
	@Modifying
	@Query("DELETE FROM Customer where id=?1")
	Integer deleteOne(Integer id) throws Exception;
}
