package org.arpicoinsurance.groupit.main.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.arpicoinsurance.groupit.main.dao.ZoneDao;
import org.arpicoinsurance.groupit.main.model.Zone;
import org.arpicoinsurance.groupit.main.service.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ZoneServiceImpl implements ZoneService{

	@Autowired
	private ZoneDao zoneDao;
	
	@Override
	public boolean saveZone(Zone zone) throws Exception {
		if(zoneDao.save(zone)!=null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean updateZone(Zone zone) throws Exception {
		if(zoneDao.save(zone)!=null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteZone(Integer id) throws Exception {
		if(zoneDao.deleteOne(id)!=null) {
			return true;
		}
		return false;
	}

	@Override
	public Zone getZone(Integer id) throws Exception {
		return zoneDao.findOne(id);
	}

	@Override
	public List<Zone> getAllZone() throws Exception {
		List<Zone> zones = new ArrayList<>();
		zoneDao.findAll().forEach(zones::add);
		return zones;
	}

	
}
