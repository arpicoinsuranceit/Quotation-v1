package org.arpicoinsurance.groupit.main.dao;

import java.util.Date;

import org.arpicoinsurance.groupit.main.model.RateCardARP;
import org.arpicoinsurance.groupit.main.model.RateCardARTMDeath;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RateCardARTMDeathDao extends MongoRepository<RateCardARTMDeath, String>{
	
	RateCardARTMDeath findByAgeAndTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(int age, int term, Date strdat1,Date strdat2, Date enddat1,Date enddat2) throws Exception;

	RateCardARTMDeath findFirstByOrderByTermDesc();

}
