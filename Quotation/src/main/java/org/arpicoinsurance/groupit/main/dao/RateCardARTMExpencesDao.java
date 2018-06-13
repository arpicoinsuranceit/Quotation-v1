package org.arpicoinsurance.groupit.main.dao;

import java.util.Date;

import org.arpicoinsurance.groupit.main.model.RateCardARTMExpences;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RateCardARTMExpencesDao extends MongoRepository<RateCardARTMExpences, String> {
	
	RateCardARTMExpences findByPolyertoOrPolyertoLessThanAndPolyerfromOrPolyerfromGreaterThanAndPaymodAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(int polyerto1, int polyerto2, int polyerfrom1, int polyerfrom2, String paymod, Date strdat1,Date strdat2, Date enddat1,Date enddat2) throws Exception;

}
