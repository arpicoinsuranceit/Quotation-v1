package org.arpicoinsurance.groupit.main.service.rider.impl;

import org.arpicoinsurance.groupit.main.dao.RateCardINVPDao;
import org.arpicoinsurance.groupit.main.service.rider.BSAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BSAServiceImpl implements BSAService{

	@Autowired
	private RateCardINVPDao rateCardINVPDao;
	
	@Override
	public Double calculateL2() throws Exception {
		return null;
	}

}
