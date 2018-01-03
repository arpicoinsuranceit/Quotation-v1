package org.arpicoinsurance.groupit.main.dao;

import java.util.Date;

import org.arpicoinsurance.groupit.main.model.RateCardATPB;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RateCardATPBDao extends MongoRepository<RateCardATPB, String>{
	
	RateCardATPB findByAgeAndTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(int age, int term, Date strdat1,Date strdat2, Date enddat1,Date enddat2);

}
