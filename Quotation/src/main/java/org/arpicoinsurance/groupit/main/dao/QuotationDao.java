package org.arpicoinsurance.groupit.main.dao;

import java.util.List;

import org.arpicoinsurance.groupit.main.model.Quotation;
import org.arpicoinsurance.groupit.main.model.Users;
import org.springframework.data.repository.CrudRepository;

public interface QuotationDao extends CrudRepository<Quotation, Integer>{
	
	Quotation findById(Integer id) throws Exception;


	List<Quotation> findByUserAndStatusOrderByIdDesc(Users user, String status)throws Exception;
	

	List<Quotation> findByUserAndStatusInOrderByIdDesc(Users user, List<String> status)throws Exception;

	List<Quotation> findByStatusInOrderByIdDesc(List<String> status)throws Exception;
	

}
