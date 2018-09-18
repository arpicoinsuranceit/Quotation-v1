package org.arpicoinsurance.groupit.main.dao;

import java.util.Date;

import org.arpicoinsurance.groupit.main.model.RateCardDTA;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RateCardDTADao extends MongoRepository<RateCardDTA, String> {
	
	RateCardDTA findByAgeAndTermAndSexAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(int age, int term, String sex, Date strdat1,Date strdat2, Date enddat1,Date enddat2) throws Exception;

}
