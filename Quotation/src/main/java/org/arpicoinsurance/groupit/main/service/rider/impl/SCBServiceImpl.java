package org.arpicoinsurance.groupit.main.service.rider.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.dao.RateCardATFESCDao;
import org.arpicoinsurance.groupit.main.model.RateCardATFESC;
import org.arpicoinsurance.groupit.main.service.rider.SCBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SCBServiceImpl implements SCBService {

	@Autowired
	private RateCardATFESCDao rateCardATFESCDao;

	@Override
	public BigDecimal calculateSCB(Integer age, Integer term, Date chedat, Double ridsumasu, String payFrequency,
			Double relief, double occupation_loding) throws Exception {

		// //System.out.println(age);
		// //System.out.println(term);
		// //System.out.println(chedat);
		// //System.out.println(ridsumasu);
		// //System.out.println(payFrequency);
		// //System.out.println(relief);
		// //System.out.println(occupation_loding);
		//

		BigDecimal premiumSCB = new BigDecimal(0);
		RateCardATFESC rateCardATFESC = rateCardATFESCDao
				.findByAgeAndTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term, chedat, chedat,
						chedat, chedat);
		// //System.out.println("SCB ridsumasu : "+ridsumasu+" payFrequency :
		// "+payFrequency+" relief : "+relief+" Rate : "+rateCardATFESC.getRate());
		try {
			if (payFrequency.equalsIgnoreCase("S")) {
				// ((@rate@*@rider_sum_assured@/1000))*@relief@
				premiumSCB = (new BigDecimal(rateCardATFESC.getRate()).multiply(new BigDecimal(ridsumasu))
						.divide(new BigDecimal(1000), 6, RoundingMode.HALF_UP)).multiply(new BigDecimal(relief))
								.setScale(0, RoundingMode.HALF_UP);
			} else {
				// ((@rate@*@rider_sum_assured@/1000)/@payment_frequency@)*@relief@
				premiumSCB = ((new BigDecimal(rateCardATFESC.getRate()).multiply(new BigDecimal(ridsumasu))
						.divide(new BigDecimal(1000), 6, RoundingMode.HALF_UP)).divide(
								new BigDecimal(new CalculationUtils().getPayterm(payFrequency)), 10,
								RoundingMode.HALF_UP)).multiply(new BigDecimal(relief)).setScale(0,
										RoundingMode.HALF_UP);
			}
		} catch (Exception e) {
			throw new NullPointerException("SCB Rate not found at Age : " + age + " and Term : " + term);
		}
		premiumSCB = premiumSCB.multiply(new BigDecimal(occupation_loding)).setScale(0, RoundingMode.HALF_UP);
		// //System.out.println("premiumSCB : "+premiumSCB.toString());
		return premiumSCB;
	}

}
