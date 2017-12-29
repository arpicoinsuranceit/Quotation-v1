package org.arpicoinsurance.groupit.main.service.impl;

import java.util.List;

import org.arpicoinsurance.groupit.main.dao.OccupationDao;
import org.arpicoinsurance.groupit.main.model.Occupation;
import org.arpicoinsurance.groupit.main.service.OccupationServce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OccupationServiceImpl implements OccupationServce{

	@Autowired
	private OccupationDao occupationDao;
	
	@Override
	public List<Occupation> getAllOccupations() throws Exception {
		return occupationDao.getAll();
	}

}
