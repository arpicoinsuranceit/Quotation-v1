package org.arpicoinsurance.groupit.main.dao;

import org.arpicoinsurance.groupit.main.model.Login;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface LoginDao extends CrudRepository<Login,String> {

	@Query("from Login where id=?1")
	Login findOne(Integer id) throws Exception;
	
	@Modifying
	@Query("DELETE FROM Login where id=?1")
	Integer deleteOne(Integer id) throws Exception;
	
	@Query("from Login where user_name=?1 and password=?2")
	Login findByUserNameAndPsw(String userName,String Password) throws Exception;
	
	@Query("select datediff(curdate(),modifydate) from Login where login_id=?1")
	Integer findDaysToNextPsw(Integer id) throws Exception;
}
