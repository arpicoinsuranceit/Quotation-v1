package org.arpicoinsurance.groupit.main.dao;

import java.util.Date;

import org.arpicoinsurance.groupit.main.model.RateCardTPDDTA;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RateCardTPDDTADao extends MongoRepository<RateCardTPDDTA, String> {
	
	RateCardTPDDTA findByAgeAndTermAndSexAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(int age, int term, String sex, Date strdat1,Date strdat2, Date enddat1,Date enddat2) throws Exception;

	RateCardTPDDTA findFirstByOrderByTermDesc() throws Exception;
}
