package org.arpicoinsurance.groupit.main.dao;

import java.util.List;

import org.arpicoinsurance.groupit.main.model.PreviousPassword;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PreviousPasswordDao extends CrudRepository<PreviousPassword,String> {

	@Query("from PreviousPassword where login_login_id=?1")
	PreviousPassword findOne(Integer id) throws Exception;
	
	@Modifying
	@Query("DELETE FROM PreviousPassword where id=?1")
	Integer deleteOne(Integer id) throws Exception;
	
	@Query("from PreviousPassword where login_login_id=?1 order by disable_date desc")
	List<PreviousPassword> findRecentPsw(Integer id) throws Exception;
}
