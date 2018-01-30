package org.arpicoinsurance.groupit.main.dao;

import java.util.List;

import org.arpicoinsurance.groupit.main.model.Occupation;
import org.arpicoinsurance.groupit.main.model.OcupationLoading;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface OccupationLodingDao extends CrudRepository<OcupationLoading,String> {

	@Query("from OcupationLoading where id=?1")
	OcupationLoading findOne(Integer id) throws Exception;
	
	@Modifying
	@Query("DELETE FROM OcupationLoading where id=?1")
	Integer deleteOne(Integer id) throws Exception;
	
	//@Query("from OcupationLoading where ocupation_id=?1")
	List<OcupationLoading> findByOccupation(Occupation occupation) throws Exception;
}
