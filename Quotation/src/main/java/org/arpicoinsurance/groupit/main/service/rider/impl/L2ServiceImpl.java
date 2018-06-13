package org.arpicoinsurance.groupit.main.service.rider.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.arpicoinsurance.groupit.main.dao.RateCardARTMDeathDao;
import org.arpicoinsurance.groupit.main.dao.RateCardARTMExpencesDao;
import org.arpicoinsurance.groupit.main.model.RateCardARTMDeath;
import org.arpicoinsurance.groupit.main.service.rider.L2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class L2ServiceImpl implements L2Service {

	@Autowired
	private RateCardARTMDeathDao rateCardARTMDeathDao;

	@Override
	public BigDecimal calculateL2(double ridsumasu, int term, int age, String payFrequency, double occupation_loding)
			throws Exception {
		Date chedat = new Date();
		RateCardARTMDeath rateCardARTMDeath = rateCardARTMDeathDao
				.findByAgeAndTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term, chedat, chedat,
						chedat, chedat);
		BigDecimal premiumL2 = new BigDecimal(0);

		// ((@rate@/12) *(@rider_sum_assured@)/1000))
		premiumL2 = (new BigDecimal(rateCardARTMDeath.getRate()).divide(new BigDecimal(12)))
				.multiply((new BigDecimal(ridsumasu).divide(new BigDecimal(1000))))
				.setScale(4, BigDecimal.ROUND_HALF_UP);
	
		premiumL2 = premiumL2.multiply(new BigDecimal(occupation_loding)).setScale(0, BigDecimal.ROUND_HALF_UP);
		return premiumL2;
	}

}
