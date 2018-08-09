package org.arpicoinsurance.groupit.main.service.rider.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.dao.RateCardSFPODao;
import org.arpicoinsurance.groupit.main.model.RateCardSFPO;
import org.arpicoinsurance.groupit.main.service.rider.SFPOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SFPOServiceImpl implements SFPOService {

	@Autowired
	private RateCardSFPODao rateCardSFPODao;

	@Override
	public BigDecimal calculateSFPO(Integer age, Integer term, Date chedat, Double ridsumasu, String payFrequency,
			Double relief, double occupation_loding) throws Exception {
		// TODO Auto-generated method stub
		BigDecimal premiumSFPO = new BigDecimal(0);
		RateCardSFPO rateCardSFPO = rateCardSFPODao
				.findByAgeAndTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term, chedat, chedat,
						chedat, chedat);
		// System.out.println("SFPO ridsumasu : "+ridsumasu+" payFrequency :
		// "+payFrequency+" relief : "+relief+" Rate : "+rateCardSFPO.getRate());
		try {
			if (payFrequency.equalsIgnoreCase("S")) {
				// ((@rate@*@rider_sum_assured@/1000))*@relief@
				premiumSFPO = (new BigDecimal(rateCardSFPO.getRate()).multiply(new BigDecimal(ridsumasu))
						.divide(new BigDecimal(1000), 6, RoundingMode.HALF_UP)).multiply(new BigDecimal(relief))
								.setScale(0, RoundingMode.HALF_UP);
			} else {
				// ((@rate@*@rider_sum_assured@/1000)/@payment_frequency@)*@relief@
				premiumSFPO = ((new BigDecimal(rateCardSFPO.getRate()).multiply(new BigDecimal(ridsumasu))
						.divide(new BigDecimal(1000), 6, RoundingMode.HALF_UP)).divide(
								new BigDecimal(new CalculationUtils().getPayterm(payFrequency)), 10,
								RoundingMode.HALF_UP)).multiply(new BigDecimal(relief)).setScale(0,
										RoundingMode.HALF_UP);
			}
		} catch (Exception e) {
			throw new NullPointerException("SFPO Rate not found at Age : " + age + " and Term : " + term);
		}
		premiumSFPO = premiumSFPO.multiply(new BigDecimal(occupation_loding)).setScale(0, RoundingMode.HALF_UP);
		// System.out.println("premiumSFPO : "+premiumSFPO.toString());
		return premiumSFPO;
	}

}
