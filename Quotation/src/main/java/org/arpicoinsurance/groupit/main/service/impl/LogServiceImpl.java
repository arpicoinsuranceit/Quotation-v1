package org.arpicoinsurance.groupit.main.service.impl;

import java.util.ArrayList;
import java.util.List;

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

	@Override
	public boolean updateLog(Logs logs) throws Exception {
		if(logDao.save(logs)!=null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteLog(Integer id) throws Exception {
		if(logDao.deleteOne(id)!=null) {
			return true;
		}
		return false;
	}

	@Override
	public Logs getLog(Integer id) throws Exception {
		return logDao.findOne(id);
	}

	@Override
	public List<Logs> getAllLogs() throws Exception {
		List<Logs> logs = new ArrayList<>();
		logDao.findAll().forEach(logs::add);
		return logs;
	}

	
}
