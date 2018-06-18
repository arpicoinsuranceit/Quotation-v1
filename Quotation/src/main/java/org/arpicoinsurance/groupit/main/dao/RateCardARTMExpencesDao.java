package org.arpicoinsurance.groupit.main.dao;

import java.util.Date;

import org.arpicoinsurance.groupit.main.model.RateCardARTMExpences;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface RateCardARTMExpencesDao extends MongoRepository<RateCardARTMExpences, String> {
	
	@Query("{$and : [{ $or: [ { 'polyerto' : { $lt: ?0 } }, { 'polyerto' : ?1 } ]} , {$or: [ { 'polyerfrom' : { $gt: ?2 } }, { 'polyerfrom' : ?3 } ] }], 'paymod' : ?4, $or:[{'strdat' :{$lt: ?5} }, {'strdat' : ?6}]}")
	RateCardARTMExpences findByPolyertoOrPolyertoLessThanAndPolyerfromOrPolyerfromGreaterThanAndPaymodAndStrdatLessThanOrStrdat(int polyerto1, int polyerto2, int polyerfrom1, int polyerfrom2, String paymod, Date strdat1,Date strdat2) throws Exception;
	
	
	RateCardARTMExpences findByPaymodAndPolyertoLessThanOrPolyertoAndPolyerfromGreaterThanOrPolyerfromAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(String paymod, int polyerto1, int polyerto2, int polyerfrom1, int polyerfrom2, Date strdat1,Date strdat2, Date enddat1,Date enddat2) throws Exception;

}