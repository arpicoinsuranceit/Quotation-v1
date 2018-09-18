package org.arpicoinsurance.groupit.main.dao;

import java.util.Date;

import org.arpicoinsurance.groupit.main.model.RateCardARP;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RateCardARPDao extends MongoRepository<RateCardARP, String> {
	
	RateCardARP findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(int age, int term, String rlfterm, Date strdat1,Date strdat2, Date enddat1,Date enddat2) throws Exception;

}
