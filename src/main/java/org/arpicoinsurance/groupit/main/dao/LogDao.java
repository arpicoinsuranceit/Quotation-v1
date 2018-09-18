package org.arpicoinsurance.groupit.main.dao;

import org.arpicoinsurance.groupit.main.model.Logs;
import org.springframework.data.repository.CrudRepository;

public interface LogDao extends CrudRepository<Logs,Integer> {
	
}
