package org.arpicoinsurance.groupit.main.service;

import java.util.List;
import org.arpicoinsurance.groupit.main.model.Logs;


public interface LogService {

	boolean saveLog(Logs logs) throws Exception;
	
	boolean updateLog(Logs logs) throws Exception;
	
	boolean deleteLog(Integer id) throws Exception;
	
	Logs getLog(Integer id) throws Exception;
	
	List <Logs> getAllLogs() throws Exception;
	
}
