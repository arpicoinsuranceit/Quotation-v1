package org.arpicoinsurance.groupit.main.dao;

import java.util.Date;

import org.arpicoinsurance.groupit.main.model.RateCardASIPFund;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RateCardASIPFundDao extends MongoRepository<RateCardASIPFund, String> {
	
	RateCardASIPFund findByTermAndPolyearAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(int term, int polyear, Date strdat1,Date strdat2, Date enddat1,Date enddat2) throws Exception;

}
