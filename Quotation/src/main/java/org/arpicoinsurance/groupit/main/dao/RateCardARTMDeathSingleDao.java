package org.arpicoinsurance.groupit.main.dao;

import java.util.Date;

import org.arpicoinsurance.groupit.main.model.RateCardARTMDeath;
import org.arpicoinsurance.groupit.main.model.RateCardARTMDeathSingle;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RateCardARTMDeathSingleDao extends MongoRepository<RateCardARTMDeathSingle, String> {
	
	RateCardARTMDeathSingle findByAgeAndTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(int age, int term, Date strdat1,Date strdat2, Date enddat1,Date enddat2) throws Exception;

	RateCardARTMDeathSingle findFirstByOrderByTermDesc();

}
