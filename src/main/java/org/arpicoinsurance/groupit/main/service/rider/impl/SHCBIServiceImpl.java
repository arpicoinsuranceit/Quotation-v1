package org.arpicoinsurance.groupit.main.service.rider.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.dao.RateCardSUHRBDao;
import org.arpicoinsurance.groupit.main.model.RateCardSUHRB;
import org.arpicoinsurance.groupit.main.service.rider.SHCBIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SHCBIServiceImpl implements SHCBIService {

	@Autowired
	private RateCardSUHRBDao rateCardSUHRBDao;

	@Override
	public BigDecimal calculateSHCBI(Integer age, String sex, Integer term, Double ridsumasu, Date chedat,
			String payFrequency, Double relief, double occupation_loding) throws Exception {
		BigDecimal premiumSUHRB = new BigDecimal(0);
		RateCardSUHRB rateCardSUHRB = rateCardSUHRBDao
				.findByAgetoOrAgetoLessThanAndAgefromOrAgefromGreaterThanAndSexAndTermAndSumasuAndStrdatLessThanOrStrdat(
						age, age, age, age, sex, term, ridsumasu, chedat, chedat);

		// System.out.println("Rate : "+rateCardSUHRB.getRate());
		// System.out.println("Rate : "+rateCardHCBDIS.getRate());
		// System.out.println("SUHRB age : "+age+" sex : "+sex+" ridsumasu :
		// "+ridsumasu+" term : "+term+" payFrequency : "+payFrequency+" relief :
		// "+relief+" Rate : "+rateCardSUHRB.getRate());
		try {
			if (payFrequency.equalsIgnoreCase("S")) {
				// ((@rate@) *@relief@)
				premiumSUHRB = new BigDecimal(rateCardSUHRB.getRate()).multiply(new BigDecimal(relief)).setScale(0,
						RoundingMode.HALF_UP);
			} else {
				// ((@rate@/@payment_frequency@) *@relief@)
				premiumSUHRB = (new BigDecimal(rateCardSUHRB.getRate()).divide(
						new BigDecimal(new CalculationUtils().getPayterm(payFrequency)), 6, RoundingMode.HALF_UP))
								.multiply(new BigDecimal(relief)).setScale(0, RoundingMode.HALF_UP);
			}
		} catch (Exception e) {
			throw new NullPointerException("SHCBI Rate Not found at Age : " + age + ", Sex : " + sex + ", Term : "
					+ term + " and Rider Sumassured : " + ridsumasu);
		}

		premiumSUHRB = premiumSUHRB.multiply(new BigDecimal(occupation_loding)).setScale(0, RoundingMode.HALF_UP);
		// System.out.println("premiumSUHRB : "+premiumSUHRB.toString());
		return premiumSUHRB;
	}

}
