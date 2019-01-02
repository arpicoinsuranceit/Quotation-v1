package org.arpicoinsurance.groupit.main.dao;

import java.util.Date;

import org.arpicoinsurance.groupit.main.model.RateCardAtpMc;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RateCardAtpMcDao extends MongoRepository< RateCardAtpMc, String>{

	RateCardAtpMc findByTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(Integer term, Date strdat1, Date strdat2, Date enddat1, Date enddat2);
}
