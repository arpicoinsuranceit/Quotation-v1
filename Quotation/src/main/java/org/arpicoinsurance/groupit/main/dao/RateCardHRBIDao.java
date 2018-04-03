package org.arpicoinsurance.groupit.main.dao;

import java.util.Date;

import org.arpicoinsurance.groupit.main.model.RateCardHRBI;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RateCardHRBIDao extends MongoRepository<RateCardHRBI, String> {
	
	RateCardHRBI findByAgeAndTermAndSumasuAndSexAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(int age, int term, double sumasu, String sex, Date strdat1,Date strdat2, Date enddat1,Date enddat2) throws Exception;

}
