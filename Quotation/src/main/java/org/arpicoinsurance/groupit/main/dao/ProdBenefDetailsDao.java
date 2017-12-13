package org.arpicoinsurance.groupit.main.dao;

import org.arpicoinsurance.groupit.main.model.ProdBenefDetails;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ProdBenefDetailsDao extends CrudRepository<ProdBenefDetails,String> {

	@Query("from ProdBenefDetails where id=?1")
	ProdBenefDetails findOne(Integer id) throws Exception;
	
	@Modifying
	@Query("DELETE FROM ProdBenefDetails where id=?1")
	Integer deleteOne(Integer id) throws Exception;
}
