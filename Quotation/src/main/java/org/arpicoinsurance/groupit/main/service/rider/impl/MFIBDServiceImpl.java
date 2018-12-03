package org.arpicoinsurance.groupit.main.service.rider.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.dao.RateCardMFIBDDao;
import org.arpicoinsurance.groupit.main.model.RateCardMFIBD;
import org.arpicoinsurance.groupit.main.service.rider.MFIBDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MFIBDServiceImpl implements MFIBDService {

	@Autowired
	private RateCardMFIBDDao rateCardMFIBDDao;

	@Override
	public BigDecimal calculateMFIBD(Integer age, Integer term, Date chedat, Double ridsumasu, String payFrequency,
			Double relief) throws Exception {
		BigDecimal premiumMFIBD = new BigDecimal(0);
		RateCardMFIBD rateCardMFIBD = rateCardMFIBDDao
				.findByAgeAndTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term, chedat, chedat,
						chedat, chedat);
		// //System.out.println("MFIBD ridsumasu : "+ridsumasu+" payFrequency :
		// "+payFrequency+" relief : "+relief+" Rate : "+rateCardMFIBD.getRate());
		try {
			if (payFrequency.equalsIgnoreCase("S")) {
				// ((@rate@*@rider_sum_assured@/1000))*@relief@
				premiumMFIBD = (new BigDecimal(rateCardMFIBD.getRate()).multiply(new BigDecimal(ridsumasu))
						.divide(new BigDecimal(1000), 6, RoundingMode.HALF_UP)).multiply(new BigDecimal(relief))
								.setScale(0, RoundingMode.HALF_UP);
			} else {
				// ((@rate@*@rider_sum_assured@/1000)/@payment_frequency@)*@relief@
				premiumMFIBD = ((new BigDecimal(rateCardMFIBD.getRate()).multiply(new BigDecimal(ridsumasu))
						.divide(new BigDecimal(1000), 6, RoundingMode.HALF_UP)).divide(
								new BigDecimal(new CalculationUtils().getPayterm(payFrequency)), 10,
								RoundingMode.HALF_UP)).multiply(new BigDecimal(relief)).setScale(0,
										RoundingMode.HALF_UP);
			}
		} catch (Exception e) {
			throw new NullPointerException("MIDBD Rate not found at Age : " + age + "Term : " + term);
		}
		//premiumMFIBD = premiumMFIBD.multiply(new BigDecimal(occupation_loding)).setScale(0, RoundingMode.HALF_UP);
		// //System.out.println("premiumMFIBD : "+premiumMFIBD.toString());
		return premiumMFIBD;
	}

}
