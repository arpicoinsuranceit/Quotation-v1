package org.arpicoinsurance.groupit.main.dao;

import java.util.Date;
import java.util.List;

import org.arpicoinsurance.groupit.main.model.RateCardSUHRB;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface RateCardSUHRBDao extends MongoRepository<RateCardSUHRB, String>{
	
	RateCardSUHRB findByAgetoOrAgetoLessThanAndAgefromOrAgefromGreaterThanAndSexAndTermAndSumasuAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(int ageto1, int ageto2, int agefrom1, int agefrom2, String sex, int term, double sumasu, Date strdat1,Date strdat2, Date enddat1,Date enddat2) throws Exception;
	
	@Query("{$and : [{ $or: [ { 'ageto' : { $lt: ?0 } }, { 'ageto' : ?1 } ]} , {$or: [ { 'agefrom' : { $gt: ?2 } }, { 'agefrom' : ?3 } ] }], 'sex' : ?4, 'term' : ?5 ,'sumasu' : ?6 , $or:[{'strdat' :{$lt: ?7} }, {'strdat' : ?8}]}")
	RateCardSUHRB findByAgetoOrAgetoLessThanAndAgefromOrAgefromGreaterThanAndSexAndTermAndSumasuAndStrdatLessThanOrStrdat(int ageto1, int ageto2, int agefrom1, int agefrom2, String sex, int term, double sumasu, Date strdat1,Date strdat2) throws Exception;

}
