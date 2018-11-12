package org.arpicoinsurance.groupit.main.dao;

import java.util.List;

import org.arpicoinsurance.groupit.main.model.QuotationDetails;
import org.arpicoinsurance.groupit.main.model.Surrendervals;
import org.springframework.data.repository.CrudRepository;

public interface SurrendervalDao extends CrudRepository<Surrendervals, Integer>{

	List<Surrendervals> findByQuotationDetails(QuotationDetails quotationDetails) throws Exception;
	
}
