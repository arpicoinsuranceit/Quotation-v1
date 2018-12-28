package org.arpicoinsurance.groupit.main.dao;

import java.util.Date;

import org.arpicoinsurance.groupit.main.model.RateCardAtpAdbPpd;
import org.arpicoinsurance.groupit.main.model.RateCardAtpAtpb;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RateCardAtpAtpbDao extends MongoRepository< RateCardAtpAtpb, String>{

	RateCardAtpAtpb findByTermAndAgeAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(Integer term, Integer age, Date strdat1, Date strdat2, Date enddat1, Date enddat2);
}
