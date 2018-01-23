package org.arpicoinsurance.groupit.main.dao;

import java.util.Date;

import org.arpicoinsurance.groupit.main.model.RateCardASIP;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RateCardASIPDao extends MongoRepository<RateCardASIP, String> {

	RateCardASIP findByAgeAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(int age, Date strdat1,Date strdat2, Date enddat1,Date enddat2) throws Exception;
	
}
