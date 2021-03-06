package org.arpicoinsurance.groupit.main.dao;

import java.util.List;

import org.arpicoinsurance.groupit.main.model.MedicalDetails;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MedicalDetailsDao extends CrudRepository<MedicalDetails,String> {

	@Query("from MedicalDetails where id=?1")
	MedicalDetails findOne(Integer id) throws Exception;
	
	@Modifying
	@Query("DELETE FROM MedicalDetails where id=?1")
	Integer deleteOne(Integer id) throws Exception;
	
	public List<MedicalDetails> findByQuotationDetails(QuotationDetails quotationDetails)throws Exception;

}
