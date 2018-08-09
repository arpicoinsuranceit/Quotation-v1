package org.arpicoinsurance.groupit.main.service.rider.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.dao.RateCardATFESCDao;
import org.arpicoinsurance.groupit.main.model.RateCardATFESC;
import org.arpicoinsurance.groupit.main.service.rider.FEBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FEBServiceImpl implements FEBService {

	@Autowired
	private RateCardATFESCDao rateCardATFESCDao;

	@Override
	public BigDecimal calculateFEB(Integer age, Integer term, Date chedat, Double ridsumasu, String payFrequency,
			Double relief, double occupation_loding) throws Exception {
		BigDecimal premiumFEB = new BigDecimal(0);
		RateCardATFESC rateCardATFESC = rateCardATFESCDao
				.findByAgeAndTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term, chedat, chedat,
						chedat, chedat);
		// System.out.println("FEB ridsumasu : "+ridsumasu+" payFrequency :
		// "+payFrequency+" relief : "+relief+" Rate : "+rateCardATFESC.getRate());

		try {
			if (payFrequency.equalsIgnoreCase("S")) {
				// ((@rate@*@rider_sum_assured@/1000))*@relief@

				premiumFEB = (new BigDecimal(rateCardATFESC.getRate()).multiply(new BigDecimal(ridsumasu))
						.divide(new BigDecimal(1000), 6, RoundingMode.HALF_UP)).multiply(new BigDecimal(relief))
								.setScale(0, RoundingMode.HALF_UP);

			} else {
				// ((@rate@*@rider_sum_assured@/1000)/@payment_frequency@)*@relief@
				premiumFEB = ((new BigDecimal(rateCardATFESC.getRate()).multiply(new BigDecimal(ridsumasu))
						.divide(new BigDecimal(1000), 6, RoundingMode.HALF_UP)).divide(
								new BigDecimal(new CalculationUtils().getPayterm(payFrequency)), 10,
								RoundingMode.HALF_UP)).multiply(new BigDecimal(relief)).setScale(0,
										RoundingMode.HALF_UP);
			}
		} catch (Exception e) {
			throw new NullPointerException("ATFESC Rate not found at Age : " + age + " and Term : " + term);
		}

		premiumFEB = premiumFEB.multiply(new BigDecimal(occupation_loding)).setScale(0, RoundingMode.HALF_UP);
		// System.out.println("premiumFEB : "+premiumFEB.toString());
		return premiumFEB;
	}

}
