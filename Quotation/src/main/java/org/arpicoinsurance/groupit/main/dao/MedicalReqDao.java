package org.arpicoinsurance.groupit.main.dao;

import org.arpicoinsurance.groupit.main.model.MedicalReq;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MedicalReqDao extends CrudRepository<MedicalReq,String> {

	@Query("from MedicalReq where id=?1")
	MedicalReq findOne(Integer id) throws Exception;
	
	@Modifying
	@Query("DELETE FROM MedicalReq where id=?1")
	Integer deleteOne(Integer id) throws Exception;
	
	MedicalReq findOneByMedCode(String medCode) throws Exception;
}
