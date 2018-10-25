package org.arpicoinsurance.groupit.main.service.rider.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.dao.RateCardMFIBDTDao;
import org.arpicoinsurance.groupit.main.model.RateCardMFIBDT;
import org.arpicoinsurance.groupit.main.service.rider.MFIBDTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MFIBDTServiceImpl implements MFIBDTService {

	@Autowired
	private RateCardMFIBDTDao rateCardMFIBDTDao;

	@Override
	public BigDecimal calculateMFIBDT(Integer age, Integer term, Date chedat, Double ridsumasu, String payFrequency,
			Double relief, double occupation_loding) throws Exception {
		BigDecimal premiumMFIBDT = new BigDecimal(0);
		RateCardMFIBDT rateCardMFIBDT = rateCardMFIBDTDao
				.findByAgeAndTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term, chedat, chedat,
						chedat, chedat);
		// //System.out.println("MFIBDT ridsumasu : "+ridsumasu+" payFrequency :
		// "+payFrequency+" relief : "+relief+" Rate : "+rateCardMFIBDT.getRate());
		try {
			if (payFrequency.equalsIgnoreCase("S")) {
				// ((@rate@*@rider_sum_assured@/1000))*@relief@
				premiumMFIBDT = (new BigDecimal(rateCardMFIBDT.getRate()).multiply(new BigDecimal(ridsumasu))
						.divide(new BigDecimal(1000), 6, RoundingMode.HALF_UP)).multiply(new BigDecimal(relief))
								.setScale(0, RoundingMode.HALF_UP);
			} else {
				// ((@rate@*@rider_sum_assured@/1000)/@payment_frequency@)*@relief@
				premiumMFIBDT = ((new BigDecimal(rateCardMFIBDT.getRate()).multiply(new BigDecimal(ridsumasu))
						.divide(new BigDecimal(1000), 6, RoundingMode.HALF_UP)).divide(
								new BigDecimal(new CalculationUtils().getPayterm(payFrequency)), 10,
								RoundingMode.HALF_UP)).multiply(new BigDecimal(relief)).setScale(0,
										RoundingMode.HALF_UP);
			}
		} catch (Exception e) {
			throw new NullPointerException("MFIBDT Rate not available at Age : " + age + " and Term : " + term);
		}
		premiumMFIBDT = premiumMFIBDT.multiply(new BigDecimal(occupation_loding)).setScale(0, RoundingMode.HALF_UP);
		// //System.out.println("premiumMFIBDT : "+premiumMFIBDT.toString());
		return premiumMFIBDT;
	}

}
