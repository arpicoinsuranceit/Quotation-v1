package org.arpicoinsurance.groupit.main.service.rider.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.arpicoinsurance.groupit.main.service.rider.HRBFService;
import org.springframework.stereotype.Service;

@Service
public class HRBFServiceImpl implements HRBFService{

	@Override
	public BigDecimal calculateHRBF(Integer age, Integer term, String sex, Double ridsumasu, Integer adlcnt,
			Integer chlcnt, Date chedat, String payFrequency, Double relief, double occupation_loding)
			throws Exception {
		// TODO Auto-generated method stub
		return new BigDecimal(100);
	}

}
