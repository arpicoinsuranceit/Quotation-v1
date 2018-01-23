package org.arpicoinsurance.groupit.main.dao;

import java.util.List;

import org.arpicoinsurance.groupit.main.model.QuotationDetails;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface QuotationDetailsDao extends CrudRepository<QuotationDetails,String> {

	QuotationDetails findOneByQdId(Integer id) throws Exception;
	
	@Modifying
	Integer deleteOneByQdId(Integer id) throws Exception;
	
	@Query("Select qd from QuotationDetails qd INNER JOIN qd.quotation q where q.id=qd.quotation and q.id=?1 order by qd.qdId desc")
	List<QuotationDetails> findByQuoNum(Integer id) throws Exception;
}
