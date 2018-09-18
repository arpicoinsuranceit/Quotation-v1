package org.arpicoinsurance.groupit.main.dao;

import java.util.Date;
import org.arpicoinsurance.groupit.main.model.RateCardHRB;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface RateCardHRBDao extends MongoRepository<RateCardHRB, String>{

	@Query("{$and : [{ $or: [ { 'ageto' : { $lt: ?0 } }, { 'ageto' : ?1 } ]} , {$or: [ { 'agefrom' : { $gt: ?2 } }, { 'agefrom' : ?3 } ] }], 'sex' : ?4, 'sumasu' : ?5 , 'adlcnt' : ?6, 'chlcnt' : ?7, $or:[{'strdat' :{$gt: ?10} }, {'strdat' : ?11}],$or:[{'enddat' :{$gt: ?10} }, {'enddat' : ?11}]}")
	RateCardHRB findByAgetoOrAgetoLessThanAndAgefromOrAgefromGreaterThanAndSexAndSumasuAndAdlcntAndChlcntAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(int ageto1, int ageto2, int agefrom1, int agefrom2, String sex, double sumasu, int adlcnt, int chlcnt, Date strdat1,Date strdat2, Date enddat1,Date enddat2) throws Exception;
	
	@Query("{$and : [{ $or: [ { 'ageto' : { $lt: ?0 } }, { 'ageto' : ?1 } ]} , {$or: [ { 'agefrom' : { $gt: ?2 } }, { 'agefrom' : ?3 } ] }], 'sex' : ?4, 'sumasu' : ?5 , 'adlcnt' : ?6, 'chlcnt' : ?7, $or:[{'strdat' :{$lt: ?8} }, {'strdat' : ?9}]}")
	RateCardHRB findByAgetoOrAgetoLessThanAndAgefromOrAgefromGreaterThanAndSexAndSumasuAndAdlcntAndChlcntAndStrdatLessThanOrStrdat(int ageto1, int ageto2, int agefrom1, int agefrom2, String sex, double sumasu, int adlcnt, int chlcnt, Date strdat1,Date strdat2) throws Exception;
	
	
}
