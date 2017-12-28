package org.arpicoinsurance.groupit.main.dao;

import org.arpicoinsurance.groupit.main.model.Region;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RegionDao extends CrudRepository<Region, String>{
	
	@Query("from Region where id=?1")
	Region findOne(Integer id) throws Exception;
	
	@Modifying
	@Query("DELETE FROM Region where id=?1")
	Integer deleteOne(Integer id) throws Exception;

}
