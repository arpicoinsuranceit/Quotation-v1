package org.arpicoinsurance.groupit.main.dao;

import java.util.Date;

import org.arpicoinsurance.groupit.main.model.RateCardJLB;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RateCardJLBDao extends MongoRepository<RateCardJLB, String> {
	
	RateCardJLB findByAgeAndTermAndSexAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(int age, int term, String sex, Date strdat1,Date strdat2, Date enddat1,Date enddat2) throws Exception;

	RateCardJLB findFirstByOrderByTermDesc() throws Exception;
}
