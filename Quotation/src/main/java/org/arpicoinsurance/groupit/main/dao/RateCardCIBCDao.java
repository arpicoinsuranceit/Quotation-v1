package org.arpicoinsurance.groupit.main.dao;

import java.util.Date;

import org.arpicoinsurance.groupit.main.model.RateCardCIBC;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RateCardCIBCDao extends MongoRepository<RateCardCIBC, String>{
	
	RateCardCIBC findByAgeAndTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(int age, int term, Date strdat1,Date strdat2, Date enddat1,Date enddat2) throws Exception;

}
