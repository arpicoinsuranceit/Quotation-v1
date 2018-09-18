package org.arpicoinsurance.groupit.main.dao;

import java.util.Date;

import org.arpicoinsurance.groupit.main.model.RateCardMFIBDT;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RateCardMFIBDTDao extends MongoRepository<RateCardMFIBDT, String>{
	
	RateCardMFIBDT findByAgeAndTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(int age, int term, Date strdat1,Date strdat2, Date enddat1,Date enddat2) throws Exception;

	RateCardMFIBDT findFirstByOrderByTermDesc() throws Exception;
}
