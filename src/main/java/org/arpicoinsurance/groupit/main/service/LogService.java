package org.arpicoinsurance.groupit.main.service;

import org.arpicoinsurance.groupit.main.model.Logs;


public interface LogService {

	boolean saveLog(Logs logs) throws Exception;
	
}
