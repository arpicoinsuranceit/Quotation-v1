package org.arpicoinsurance.groupit.main.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.arpicoinsurance.groupit.main.dao.Rms_SbuDao;
import org.arpicoinsurance.groupit.main.model.Rms_Sbu;
import org.arpicoinsurance.groupit.main.service.Rms_SbuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class Rms_SbuImpl implements Rms_SbuService{

	@Autowired
	private Rms_SbuDao sbudao;
	
	@Override
	public boolean saveSbu(Rms_Sbu rms_Sbu) throws Exception {
		if(sbudao.save(rms_Sbu)!=null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean updateSbu(Rms_Sbu rms_Sbu) throws Exception {
		if(sbudao.save(rms_Sbu)!=null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteSbu(Integer id) throws Exception {
		if(sbudao.deleteOne(id)!=null) {
			return true;
		}
		return false;
	}

	@Override
	public Rms_Sbu getSbu(Integer id) throws Exception {
		return sbudao.findOne(id);
	}

	@Override
	public List<Rms_Sbu> getAllSbu() throws Exception {
		List<Rms_Sbu> sbus = new ArrayList<>();
		sbudao.findAll().forEach(sbus::add);
		return sbus;
	}

	
}
