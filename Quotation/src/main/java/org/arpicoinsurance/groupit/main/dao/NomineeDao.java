package org.arpicoinsurance.groupit.main.dao;

import java.util.List;

import org.arpicoinsurance.groupit.main.model.Nominee;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;
import org.springframework.data.repository.CrudRepository;

public interface NomineeDao extends CrudRepository<Nominee, Integer>{

	List<Nominee> findByQuotationDetails(QuotationDetails details);

}
