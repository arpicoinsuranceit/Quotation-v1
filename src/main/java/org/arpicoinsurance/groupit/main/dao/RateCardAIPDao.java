package org.arpicoinsurance.groupit.main.dao;

import java.util.Date;

import org.arpicoinsurance.groupit.main.model.RateCardAIP;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface RateCardAIPDao extends MongoRepository<RateCardAIP, String> {
	
	@Query("{$and : [{ $or: [ { 'termto' : { $lt: ?0 } }, { 'termto' : ?1 } ]} , {$or: [ { 'termfrom' : { $gt: ?2 } }, { 'termfrom' : ?3 } ] }], 'paymod' : ?4, 'polyear' : ?5 , $or:[{'strdat' :{$lt: ?6} }, {'strdat' : ?7}]}")
	RateCardAIP findByTermtoOrTermtoLessThanAndTermfromOrTermfromGreaterThanAndPaymodAndPolyearAndStrdatLessThanOrStrdat(int termto1, int termto2, int termfrom1, int termfrom2, String paymod, int polyear, Date strdat1,Date strdat2) throws Exception;
	
	@Query("{$and : [{ $or: [ { 'termto' : { $lt: ?0 } }, { 'termto' : ?1 } ]} , {$or: [ { 'termfrom' : { $gt: ?2 } }, { 'termfrom' : ?3 } ] }], 'paymod' : ?4, $or:[{'strdat' :{$lt: ?5} }, {'strdat' : ?6}]}")
	RateCardAIP findByTermtoOrTermtoLessThanAndTermfromOrTermfromGreaterThanAndPaymodAndStrdatLessThanOrStrdat(int termto1, int termto2, int termfrom1, int termfrom2, String paymod, Date strdat1,Date strdat2) throws Exception;

}
