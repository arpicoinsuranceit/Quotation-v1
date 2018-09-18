package org.arpicoinsurance.groupit.main.dao;

import java.util.Date;

import org.arpicoinsurance.groupit.main.model.RateCardHRBF;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RateCardHRBFDao extends MongoRepository<RateCardHRBF, String> {
	
	RateCardHRBF findByAgeAndTermAndChlcntAndSumasuAndAdlcntAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(int age, int term, int chlcnt, double sumasu, int adlcnt, Date strdat1,Date strdat2, Date enddat1,Date enddat2) throws Exception;
	
	RateCardHRBF findFirstByOrderByTermDesc() throws Exception;

}
