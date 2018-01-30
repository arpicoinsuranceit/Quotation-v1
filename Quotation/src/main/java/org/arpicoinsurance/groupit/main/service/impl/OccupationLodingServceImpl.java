package org.arpicoinsurance.groupit.main.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.arpicoinsurance.groupit.main.dao.OccupationDao;
import org.arpicoinsurance.groupit.main.dao.OccupationLodingDao;
import org.arpicoinsurance.groupit.main.model.Occupation;
import org.arpicoinsurance.groupit.main.model.OcupationLoading;
import org.arpicoinsurance.groupit.main.service.OccupationLodingServce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OccupationLodingServceImpl implements OccupationLodingServce {

	@Autowired
	private OccupationDao occupationDao;
	
	@Autowired
	private OccupationLodingDao occupationLodingDao;
	
	@Override
	public Map<String, Double> getOccupationLoding(Integer occupation_id) throws Exception {
		Map<String, Double> oculoding = new HashMap<String, Double>();
		Occupation occupation = occupationDao.findByOcupationid(occupation_id);
		List<OcupationLoading> occupationLodings = occupationLodingDao.findByOccupation(occupation);
		for (OcupationLoading ocupationLoading : occupationLodings) {
			oculoding.put(ocupationLoading.getBenefits().getRiderCode(), ocupationLoading.getValue());
		}
		return oculoding;
	}

}
