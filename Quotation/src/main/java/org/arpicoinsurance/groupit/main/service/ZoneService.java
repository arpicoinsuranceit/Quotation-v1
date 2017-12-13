package org.arpicoinsurance.groupit.main.service;

import java.util.List;

import org.arpicoinsurance.groupit.main.model.Rms_Sbu;
import org.arpicoinsurance.groupit.main.model.Zone;


public interface ZoneService {

	boolean saveZone(Zone zone) throws Exception;
	
	boolean updateZone(Zone zone) throws Exception;
	
	boolean deleteZone(Integer id) throws Exception;
	
	Zone getZone(Integer id) throws Exception;
	
	List <Zone> getAllZone() throws Exception;
	
}
