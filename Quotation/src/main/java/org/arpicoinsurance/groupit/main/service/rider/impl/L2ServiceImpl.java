package org.arpicoinsurance.groupit.main.service.rider.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.dao.RateCardARTMDeathDao;
import org.arpicoinsurance.groupit.main.dao.RateCardARTMDeathSingleDao;
import org.arpicoinsurance.groupit.main.model.RateCardARTMDeath;
import org.arpicoinsurance.groupit.main.model.RateCardARTMDeathSingle;
import org.arpicoinsurance.groupit.main.service.rider.L2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class L2ServiceImpl implements L2Service {

	@Autowired
	private RateCardARTMDeathDao rateCardARTMDeathDao;
	
	@Autowired
	private RateCardARTMDeathSingleDao rateCardARTMDeathSingleDao;

	@Override
	public BigDecimal calculateL2(double ridsumasu, int term, int age, String payFrequency, double occupation_loding)
			throws Exception {
		//System.out.println(" ////////////////////// artm " + age + " " + term);
		Date chedat = new Date();
		BigDecimal premiumL2 = new BigDecimal(0);

		// System.out.println(rateCardARTMDeath.getRate() + " //////////////////////
		// artm");
		try {
			if (payFrequency.equalsIgnoreCase("S")) {
				RateCardARTMDeathSingle rateCardARTMDeathSingle = rateCardARTMDeathSingleDao
						.findByAgeAndTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term, chedat, chedat,
								chedat, chedat);
				//System.out.println(rateCardARTMDeathSingle.getRate() + " artm");
				// ((@rider_sum_assured@)/1000)*@rate@)
				premiumL2 = (new BigDecimal(ridsumasu).divide(new BigDecimal(1000), 6,RoundingMode.HALF_UP)).multiply(new BigDecimal(rateCardARTMDeathSingle.getRate()));
			} else {
				RateCardARTMDeath rateCardARTMDeath = rateCardARTMDeathDao
						.findByAgeAndTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term, chedat, chedat,
								chedat, chedat);
				// ((@rate@/@payment_frequency@) *(@rider_sum_assured@)/1000))
				premiumL2 = (new BigDecimal(rateCardARTMDeath.getRate()).divide(
						new BigDecimal(new CalculationUtils().getPayterm(payFrequency)), 6, RoundingMode.HALF_UP))
								.multiply((new BigDecimal(ridsumasu).divide(new BigDecimal(1000), 6,
										RoundingMode.HALF_UP)))
								.setScale(4, BigDecimal.ROUND_HALF_UP);
			}
		} catch (Exception e) {
			throw new NullPointerException("ARTM L2 Rate not found at Age : " + age + " and Term : " + term);
		}
		premiumL2 = premiumL2.multiply(new BigDecimal(occupation_loding)).setScale(0, BigDecimal.ROUND_HALF_UP);
		return premiumL2;
	}

}
