package org.arpicoinsurance.groupit.main.dao;

import org.arpicoinsurance.groupit.main.model.Branch;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface BranchDao extends CrudRepository<Branch,String> {

	@Query("from Branch where id=?1")
	Branch findOne(Integer id) throws Exception;
	
	@Modifying
	@Query("DELETE FROM Branch where id=?1")
	Integer deleteOne(Integer id) throws Exception;
}
