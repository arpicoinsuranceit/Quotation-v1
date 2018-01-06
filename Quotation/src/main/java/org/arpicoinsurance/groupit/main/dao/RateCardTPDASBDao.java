package org.arpicoinsurance.groupit.main.dao;

import java.util.Date;

import org.arpicoinsurance.groupit.main.model.RateCardTPDASB;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RateCardTPDASBDao extends MongoRepository<RateCardTPDASB,String>{

	RateCardTPDASB findByAgeAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(int age, Date strdat1,Date strdat2, Date enddat1,Date enddat2) throws Exception;

}
