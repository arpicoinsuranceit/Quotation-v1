package org.arpicoinsurance.groupit.main.dao;

import java.util.Date;

import org.arpicoinsurance.groupit.main.model.RateCardTPDDTAS;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RateCardTPDDTASDao extends MongoRepository<RateCardTPDDTAS, String> {

	RateCardTPDDTAS findByAgeAndTermAndSexAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(int age, int term, String sex, Date strdat1,Date strdat2, Date enddat1,Date enddat2) throws Exception;
	
	RateCardTPDDTAS findFirstByOrderByTermDesc() throws Exception;
}
