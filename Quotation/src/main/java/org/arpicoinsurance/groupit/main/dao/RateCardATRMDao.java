package org.arpicoinsurance.groupit.main.dao;

import java.util.Date;

import org.arpicoinsurance.groupit.main.model.RateCardATRM;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RateCardATRMDao extends MongoRepository<RateCardATRM, String> {
	
	RateCardATRM findByAgeAndTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(int age, int term, Date strdat1,Date strdat2, Date enddat1,Date enddat2) throws Exception;

	RateCardATRM findFirstByOrderByTermDesc() throws Exception;
}
