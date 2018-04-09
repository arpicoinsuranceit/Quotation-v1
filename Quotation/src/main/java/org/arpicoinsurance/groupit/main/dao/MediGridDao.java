package org.arpicoinsurance.groupit.main.dao;

import java.util.List;

import org.arpicoinsurance.groupit.main.model.MediTestGrid;
import org.springframework.data.repository.CrudRepository;

public interface MediGridDao extends CrudRepository<MediTestGrid, Integer>{

	 List<MediTestGrid> findOneByAgeFromGreaterThanEqualAndAgeToLessThanEqualAndSumAssuredFromGreaterThanEqualAndSumAssuredToLessThanEqual(Integer ageFrom, Integer ageTo, Double sumAssuredFrom, Double sumAssuredTo);
}
