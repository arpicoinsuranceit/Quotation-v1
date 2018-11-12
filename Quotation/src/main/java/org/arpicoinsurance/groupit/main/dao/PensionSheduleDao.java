package org.arpicoinsurance.groupit.main.dao;

import java.util.List;

import org.arpicoinsurance.groupit.main.model.PensionShedule;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;
import org.springframework.data.repository.CrudRepository;

public interface PensionSheduleDao extends CrudRepository<PensionShedule, Integer>{
	
	List<PensionShedule> findByQuotationDetails(QuotationDetails quotationDetails)throws Exception;

}
