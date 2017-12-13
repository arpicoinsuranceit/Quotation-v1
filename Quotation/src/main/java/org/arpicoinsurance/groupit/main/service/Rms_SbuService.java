package org.arpicoinsurance.groupit.main.service;

import java.util.List;

import org.arpicoinsurance.groupit.main.model.Rms_Sbu;


public interface Rms_SbuService {

	boolean saveSbu(Rms_Sbu rms_Sbu) throws Exception;
	boolean updateSbu(Rms_Sbu rms_Sbu) throws Exception;
	boolean deleteSbu(Integer id) throws Exception;
	Rms_Sbu getSbu(Integer id) throws Exception;
	List <Rms_Sbu> getAllSbu() throws Exception;
	
}
