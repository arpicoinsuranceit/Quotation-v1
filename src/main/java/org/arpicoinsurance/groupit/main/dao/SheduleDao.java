package org.arpicoinsurance.groupit.main.dao;

import java.util.List;

import org.arpicoinsurance.groupit.main.model.QuotationDetails;
import org.arpicoinsurance.groupit.main.model.Shedule;
import org.springframework.data.repository.CrudRepository;

public interface SheduleDao extends CrudRepository<Shedule, Integer> {
	
	public List<Shedule> findByQuotationDetails(QuotationDetails quotationDetails)throws Exception;

}
