package org.arpicoinsurance.groupit.main.dao;

import java.util.Date;

import org.arpicoinsurance.groupit.main.model.RateCardARTMProfit;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RateCardARTMProfitDao extends MongoRepository<RateCardARTMProfit, String> {
	
	RateCardARTMProfit findByPolyertoOrPolyertoLessThanAndPolyerfromOrPolyerfromGreaterThanAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(int polyerto1, int polyerto2, int polyerfrom1, int polyerfrom2, Date strdat1,Date strdat2, Date enddat1,Date enddat2) throws Exception;

}
