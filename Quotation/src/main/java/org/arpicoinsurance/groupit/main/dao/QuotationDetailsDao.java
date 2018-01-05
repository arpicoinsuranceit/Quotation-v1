package org.arpicoinsurance.groupit.main.dao;

import java.util.List;

import org.arpicoinsurance.groupit.main.model.QuotationDetails;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface QuotationDetailsDao extends CrudRepository<QuotationDetails,String> {

	@Query("from QuotationDetails where id=?1")
	QuotationDetails findOne(Integer id) throws Exception;
	
	@Modifying
	@Query("DELETE FROM QuotationDetails where id=?1")
	Integer deleteOne(Integer id) throws Exception;
	
	@Query("Select qd from QuotationDetails qd INNER JOIN qd.quotation q where q.id=qd.quotation and q.quotationNum=?1 order by qd.qdId desc")
	List<QuotationDetails> findByQuoNum(String id) throws Exception;
}
