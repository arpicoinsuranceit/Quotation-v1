package org.arpicoinsurance.groupit.main.dao;

import java.util.List;

import org.arpicoinsurance.groupit.main.model.Benefits;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface BenefitsDao extends CrudRepository<Benefits,String> {

	@Query("from Benefits where id=?1")
	Benefits findOne(Integer id) throws Exception;
	
	@Modifying
	@Query("DELETE FROM Benefits where id=?1")
	Integer deleteOne(Integer id) throws Exception;
	
	Benefits findByRiderCode(String riderCode) throws Exception;

	List<Benefits> findAllByActive(Integer active);
	
}
