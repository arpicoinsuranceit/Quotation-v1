package org.arpicoinsurance.groupit.main.dao;

import java.util.List;
import org.arpicoinsurance.groupit.main.model.Child;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ChildDao extends CrudRepository<Child,String> {

	@Query("from Child where id=?1")
	Child findOne(Integer id) throws Exception;
	
	@Modifying
	@Query("DELETE FROM Child where id=?1")
	Integer deleteOne(Integer id) throws Exception;
	
	
}
