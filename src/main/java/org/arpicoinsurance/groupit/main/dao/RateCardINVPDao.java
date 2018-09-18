package org.arpicoinsurance.groupit.main.dao;

import java.util.Date;
import org.arpicoinsurance.groupit.main.model.RateCardINVP;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RateCardINVPDao extends MongoRepository<RateCardINVP,String>{

	RateCardINVP findByAgeAndTermAndIntratAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(int age, int term, double intrat, Date strdat1,Date strdat2, Date enddat1,Date enddat2) throws Exception;

}
