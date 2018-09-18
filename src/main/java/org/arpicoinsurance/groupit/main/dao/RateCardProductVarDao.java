package org.arpicoinsurance.groupit.main.dao;

import java.util.Date;

import org.arpicoinsurance.groupit.main.model.RateCardProductVar;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RateCardProductVarDao extends MongoRepository<RateCardProductVar, String> {
	
	RateCardProductVar findByPrdcodAndPracodAndPramodAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(String prdcod, String pracod, String pramod, Date strdat1, Date strdat2, Date enddat1, Date enddat2) throws Exception;

}
