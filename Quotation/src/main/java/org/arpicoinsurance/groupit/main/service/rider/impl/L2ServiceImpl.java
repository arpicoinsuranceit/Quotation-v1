package org.arpicoinsurance.groupit.main.service.rider.impl;

import java.math.BigDecimal;

import org.arpicoinsurance.groupit.main.service.rider.L2Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class L2ServiceImpl implements L2Service{

	@Override
	public BigDecimal calculateL2(double ridsumasu, int term, int age, String payFrequency, double occupation_loding)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
