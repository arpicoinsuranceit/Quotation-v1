package org.arpicoinsurance.groupit.main.service.rider.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.dao.RateCardSUHRBDao;
import org.arpicoinsurance.groupit.main.model.RateCardSUHRB;
import org.arpicoinsurance.groupit.main.service.rider.SUHRBCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SUHRBCServiceImpl implements SUHRBCService {

	@Autowired
	private RateCardSUHRBDao rateCardSUHRBDao;

	@Override
	public BigDecimal calculateSUHRBC(Integer age, String sex, Integer term, Double ridsumasu, Date chedat,
			String payFrequency, Double relief) throws Exception {
		BigDecimal premiumSUHRBC = new BigDecimal(0);
		// System.out.println(age);
		// System.out.println(sex);
		// System.out.println(term);
		// System.out.println(ridsumasu);
		// System.out.println(payFrequency);

		RateCardSUHRB rateCardSUHRB = rateCardSUHRBDao
				.findByAgetoOrAgetoLessThanAndAgefromOrAgefromGreaterThanAndSexAndTermAndSumasuAndStrdatLessThanOrStrdat(
						age, age, age, age, sex, term, ridsumasu, chedat, chedat);

		// System.out.println("Rate : "+rateCardHRB.getRate());
		// System.out.println("Rate : "+rateCardHCBDIS.getRate());
		// System.out.println("SUHRBC age : "+age+" sex : "+sex+" ridsumasu :
		// "+ridsumasu+" term : "+term+" payFrequency : "+payFrequency+" relief :
		// "+relief+" Rate : "+rateCardSUHRB.getRate());
		try {
			if (payFrequency.equalsIgnoreCase("S")) {
				// ((@rate@) *@relief@)
				premiumSUHRBC = new BigDecimal(rateCardSUHRB.getRate()).multiply(new BigDecimal(relief)).setScale(0,
						RoundingMode.HALF_UP);
			} else {
				// ((@rate@/@payment_frequency@) *@relief@)
				premiumSUHRBC = (new BigDecimal(rateCardSUHRB.getRate()).divide(
						new BigDecimal(new CalculationUtils().getPayterm(payFrequency)), 6, RoundingMode.HALF_UP))
								.multiply(new BigDecimal(relief)).setScale(0, RoundingMode.HALF_UP);
			}
		} catch (Exception e) {
			throw new NullPointerException("SHCBIC Rate not found at Age : " + age + ", Sex : " + sex + ", Term : "
					+ term + ", Rider Sumassured : " + ridsumasu);
		}

		// System.out.println("premiumSUHRBC : "+premiumSUHRBC.toString());
		return premiumSUHRBC;
	}

}
