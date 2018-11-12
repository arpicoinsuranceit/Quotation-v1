package org.arpicoinsurance.groupit.main.service;

import java.util.List;

import org.arpicoinsurance.groupit.main.model.Occupation;

public interface OccupationServce {
	List<Occupation> getAllOccupations() throws Exception;
	
	Occupation getOccupationByCode(String code)throws Exception;
}
