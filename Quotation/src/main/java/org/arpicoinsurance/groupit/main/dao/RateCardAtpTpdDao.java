package org.arpicoinsurance.groupit.main.dao;

import java.util.Date;

import org.arpicoinsurance.groupit.main.model.RateCardAtpTpd;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RateCardAtpTpdDao extends MongoRepository<RateCardAtpTpd, String>{

	RateCardAtpTpd findByTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(Integer term, Date strdat1, Date strdat2, Date enddat1, Date enddat2);
}
