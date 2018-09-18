package org.arpicoinsurance.groupit.main.dao;

import org.arpicoinsurance.groupit.main.model.Zone;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ZoneDao extends CrudRepository<Zone,String> {

	@Query("from Zone where id=?1")
	Zone findOne(Integer id) throws Exception;
	
	@Modifying
	@Query("DELETE FROM Zone where id=?1")
	Integer deleteOne(Integer id) throws Exception;
}
