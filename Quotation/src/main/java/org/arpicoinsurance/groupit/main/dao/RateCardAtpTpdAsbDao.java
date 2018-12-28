package org.arpicoinsurance.groupit.main.dao;

import java.util.Date;

import org.arpicoinsurance.groupit.main.model.RateCardAtpTpdAsb;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RateCardAtpTpdAsbDao extends MongoRepository<RateCardAtpTpdAsb, String>{

	RateCardAtpTpdAsb findByTermAndAgeAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(Integer term, Integer age, Date strdat1, Date strdat2, Date enddat1, Date enddat2);
	
}
