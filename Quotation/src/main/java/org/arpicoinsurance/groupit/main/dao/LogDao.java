package org.arpicoinsurance.groupit.main.dao;

import org.arpicoinsurance.groupit.main.model.Logs;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface LogDao extends CrudRepository<Logs,String> {

	@Query("from Logs where id=?1")
	Logs findOne(Integer id) throws Exception;
	
	@Modifying
	@Query("DELETE FROM Logs where id=?1")
	Integer deleteOne(Integer id) throws Exception;
}
