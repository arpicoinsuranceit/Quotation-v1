package org.arpicoinsurance.groupit.main.service.impl;

import org.arpicoinsurance.groupit.main.dao.RateCardINVPDao;
import org.arpicoinsurance.groupit.main.service.L2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class L2ServiceImpl implements L2Service{

	@Autowired
	private RateCardINVPDao rateCardINVPDao;
	
	@Override
	public Double calculateL2() throws Exception {
		return null;
	}

}
