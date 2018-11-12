package org.arpicoinsurance.groupit.main.dao;

import java.util.ArrayList;

import org.arpicoinsurance.groupit.main.model.Occupation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface OccupationDao extends CrudRepository<Occupation,String> {

	@Query("from Occupation")
	ArrayList<Occupation> getAll() throws Exception;

	//@Query("from Occupation where id=?1")
	Occupation findByOcupationid(Integer id) throws Exception;
	
	@Modifying
	@Query("DELETE FROM Occupation where id=?1")
	Integer deleteOne(Integer id) throws Exception;
	
	Occupation findByOcupationCode(String ocupationCode) throws Exception;
	
	
}
