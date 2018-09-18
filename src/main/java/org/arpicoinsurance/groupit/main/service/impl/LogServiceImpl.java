package org.arpicoinsurance.groupit.main.service.impl;

import org.arpicoinsurance.groupit.main.dao.LogDao;
import org.arpicoinsurance.groupit.main.model.Logs;
import org.arpicoinsurance.groupit.main.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LogServiceImpl implements LogService{

	@Autowired
	private LogDao logDao;
	
	@Override
	public boolean saveLog(Logs logs) throws Exception {
		if(logDao.save(logs)!=null) {
			return true;
		}
		return false;
	}

}
