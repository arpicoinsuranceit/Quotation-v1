package org.arpicoinsurance.groupit.main.dao;

import java.util.Date;

import javax.transaction.Transactional;

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
	
	@Modifying
	@Query("Update Login set last_log=?1 where id=?2")
	@Transactional
	Integer updateOne(Date date,Integer id) throws Exception;
	
	@Modifying
	@Query("Update Login set fail_count=?1 where id=?2")
	@Transactional
	Integer updateFailCount(Integer failCount,Integer id) throws Exception;
	
	@Modifying
	@Query("Update Login set locks=?1 where id=?2")
	@Transactional
	Integer updateOne(Integer locks,Integer id) throws Exception;
	
	@Query("from Login where user_name=?1")
	Login findByUserName(String userName) throws Exception;
}
