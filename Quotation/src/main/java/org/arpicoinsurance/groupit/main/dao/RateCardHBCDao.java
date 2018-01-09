package org.arpicoinsurance.groupit.main.dao;

import java.util.Date;

import org.arpicoinsurance.groupit.main.model.RateCardHBC;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RateCardHBCDao extends MongoRepository<RateCardHBC, String>{
	
	RateCardHBC findByTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(int term, Date strdat1,Date strdat2, Date enddat1,Date enddat2) throws Exception;

}
