package org.arpicoinsurance.groupit.main.dao;

import org.arpicoinsurance.groupit.main.model.Users;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UsersDao extends CrudRepository<Users,String> {

	@Query("from Users where id=?1")
	Users findOne(Integer id) throws Exception;
	
	@Modifying
	@Query("DELETE FROM Users where id=?1")
	Integer deleteOne(Integer id) throws Exception;
}
