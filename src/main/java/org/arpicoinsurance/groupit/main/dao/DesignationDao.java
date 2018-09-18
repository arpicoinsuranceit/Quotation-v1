package org.arpicoinsurance.groupit.main.dao;

import org.arpicoinsurance.groupit.main.model.Designation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface DesignationDao extends CrudRepository<Designation,String> {

	@Query("from Designation where id=?1")
	Designation findOne(Integer id) throws Exception;
	
	@Modifying
	@Query("DELETE FROM Designation where id=?1")
	Integer deleteOne(Integer id) throws Exception;
}
