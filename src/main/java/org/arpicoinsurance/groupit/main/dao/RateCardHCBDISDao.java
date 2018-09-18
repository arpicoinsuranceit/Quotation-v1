package org.arpicoinsurance.groupit.main.dao;

import java.util.Date;

import org.arpicoinsurance.groupit.main.model.RateCardHCBDIS;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RateCardHCBDISDao extends MongoRepository<RateCardHCBDIS, String> {
	RateCardHCBDIS findByAgetoOrAgetoLessThanAndAgefromOrAgefromGreaterThanAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(
			int ageto1, int ageto2, int agefrom1, int agefrom2, Date strdat1, Date strdat2, Date enddat1, Date enddat2)
			throws Exception;
}
