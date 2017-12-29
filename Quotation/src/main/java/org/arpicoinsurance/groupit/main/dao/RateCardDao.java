package org.arpicoinsurance.groupit.main.dao;

import org.arpicoinsurance.groupit.main.model.RateCard;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RateCardDao extends MongoRepository<RateCard, String>{

     RateCard findByRcdcodAndAgetoAndTerm(String rcdcod, Double ageto,
Integer term);

}