package org.arpicoinsurance.groupit.main.service.rider.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.dao.RateCardCIBDao;
import org.arpicoinsurance.groupit.main.model.RateCardCIB;
import org.arpicoinsurance.groupit.main.service.rider.SCIBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SCIBServiceImpl implements SCIBService {

	@Autowired
	private RateCardCIBDao rateCardCIBDao;

	@Override
	public BigDecimal calculateSCIB(Integer age, Integer term, Date chedat, Double ridsumasu, String payFrequency,
			Double relief, double occupation_loding) throws Exception {
		// TODO Auto-generated method stub
		BigDecimal premiumSCIB = new BigDecimal(0);
		RateCardCIB rateCardCIB = rateCardCIBDao.findByAgeAndTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(
				age, term, chedat, chedat, chedat, chedat);
		// System.out.println("SCIB ridsumasu : "+ridsumasu+" payFrequency :
		// "+payFrequency+" relief : "+relief+" Rate : "+rateCardCIB.getRate());
		try {
			if (payFrequency.equalsIgnoreCase("S")) {
				// ((@rate@*@rider_sum_assured@/1000))*@relief@
				premiumSCIB = (new BigDecimal(rateCardCIB.getRate()).multiply(new BigDecimal(ridsumasu))
						.divide(new BigDecimal(1000), 6, RoundingMode.HALF_UP)).multiply(new BigDecimal(relief))
								.setScale(0, RoundingMode.HALF_UP);
			} else {
				// ((@rate@*@rider_sum_assured@/1000)/@payment_frequency@)*@relief@
				premiumSCIB = ((new BigDecimal(rateCardCIB.getRate()).multiply(new BigDecimal(ridsumasu))
						.divide(new BigDecimal(1000), 6, RoundingMode.HALF_UP)).divide(
								new BigDecimal(new CalculationUtils().getPayterm(payFrequency)), 10,
								RoundingMode.HALF_UP)).multiply(new BigDecimal(relief)).setScale(0,
										RoundingMode.HALF_UP);
			}
		} catch (Exception e) {
			throw new NullPointerException("SCIB Rate not found at Age : " + age + " and Term : " + term);
		}
		premiumSCIB = premiumSCIB.multiply(new BigDecimal(occupation_loding)).setScale(0, RoundingMode.HALF_UP);
		// System.out.println("premiumSCIB : "+premiumSCIB.toString());
		return premiumSCIB;
	}

}
