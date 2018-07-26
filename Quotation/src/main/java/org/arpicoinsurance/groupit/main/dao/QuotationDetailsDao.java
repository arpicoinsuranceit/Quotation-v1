package org.arpicoinsurance.groupit.main.dao;

import java.util.List;
import org.arpicoinsurance.groupit.main.model.Quotation;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

public interface QuotationDetailsDao extends CrudRepository<QuotationDetails,String> {

	//QuotationDetails findOneByQdId(Integer id) throws Exception;
	
	@Modifying
	Integer deleteOneByQdId(Integer id) throws Exception;
	
	List<QuotationDetails> findByQuotationOrderByQdIdDesc(Quotation quotation) throws Exception;
	
	QuotationDetails findFirstByQuotationOrderByQdIdDesc(Quotation quotation) throws Exception;
	
	QuotationDetails findByQdId(Integer qdId) throws Exception;
	
	Integer countByQuotation(Quotation quotation)throws Exception;
	
}
