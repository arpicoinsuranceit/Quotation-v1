package org.arpicoinsurance.groupit.main.dao;

import org.arpicoinsurance.groupit.main.model.Rms_Sbu;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface Rms_SbuDao extends CrudRepository<Rms_Sbu,String> {

	@Query("from Rms_Sbu where id=?1")
	Rms_Sbu findOne(Integer id) throws Exception;
	
	@Modifying
	@Query("DELETE FROM Rms_Sbu where id=?1")
	Integer deleteOne(Integer id) throws Exception;
}
