package org.arpicoinsurance.groupit.main.service.rider.impl;

import java.math.BigDecimal;
import java.util.Date;

import javax.transaction.Transactional;

import org.arpicoinsurance.groupit.main.service.rider.SHCBFService;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class SHCBFServiceImpl implements SHCBFService{

	@Override
	public BigDecimal calculateSHCBF(Integer age, Integer term, Double ridsumasu, Integer adlcnt, Integer chlcnt,
			Date chedat, String payFrequency, Double relief, double occupation_loding) throws Exception {
		
		return new BigDecimal(100);
	}

}
