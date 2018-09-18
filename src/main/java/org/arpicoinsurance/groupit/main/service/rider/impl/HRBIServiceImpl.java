package org.arpicoinsurance.groupit.main.service.rider.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.dao.RateCardHCBDISDao;
import org.arpicoinsurance.groupit.main.dao.RateCardHRBIDao;
import org.arpicoinsurance.groupit.main.model.RateCardHCBDIS;
import org.arpicoinsurance.groupit.main.model.RateCardHRBI;
import org.arpicoinsurance.groupit.main.service.rider.HRBIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HRBIServiceImpl implements HRBIService {

	@Autowired
	private RateCardHRBIDao rateCardHRBIDao;

	@Autowired
	private RateCardHCBDISDao rateCardHCBDISDao;

	@Override
	public BigDecimal calculateHRBI(Integer age, Integer term, String sex, Double ridsumasu, Date chedat,
			String payFrequency, Double relief, double occupation_loding) throws Exception {
/*
		 System.out.println(age + "###############" + term+ "###############" + sex+
		 "###############" + ridsumasu+ "###############" + payFrequency+
		 "###############" + relief+ "###############" + occupation_loding);*/
		BigDecimal premiumHRBI = new BigDecimal(0);

		RateCardHRBI rateCardHRBI = rateCardHRBIDao
				.findByAgeAndTermAndSumasuAndSexAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term,
						ridsumasu, sex, chedat, chedat, chedat, chedat);
		RateCardHCBDIS rateCardHCBDIS = rateCardHCBDISDao
				.findByAgetoOrAgetoLessThanAndAgefromOrAgefromGreaterThanAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(
						age, age, age, age, chedat, chedat, chedat, chedat);
		// System.out.println("Rate : "+rateCardHRBI.getRate());
		// System.out.println("Rate : "+rateCardHCBDIS.getRate());
		// System.out.println("HRB age : "+age+" sex : "+sex+" ridsumasu : "+ridsumasu+"
		// term : "+term+" payFrequency : "+payFrequency+" relief : "+relief+" Rate :
		// "+rateCardHRBI.getRate());
		try {
			if (payFrequency.equalsIgnoreCase("S")) {
				// ((@rate@) *@relief@)
				premiumHRBI = new BigDecimal(rateCardHRBI.getRate()).multiply(new BigDecimal(relief));
			} else {
				// ((@rate@/@payment_frequency@) *@relief@)
				premiumHRBI = (new BigDecimal(rateCardHRBI.getRate()).divide(
						new BigDecimal(new CalculationUtils().getPayterm(payFrequency)), 6, RoundingMode.HALF_UP))
								.multiply(new BigDecimal(relief));
			}
		} catch (Exception e) {
			throw new NullPointerException("HCBI Rate not found at Age : " + age + ", Term : " + term
					+ ", Rider Sumassured : " + ridsumasu + " and Sex : " + sex);
		}

		// Added for HCB Discount
		try {
			premiumHRBI = premiumHRBI.multiply(new BigDecimal(rateCardHCBDIS.getRate())).setScale(0,
					RoundingMode.HALF_UP);
		} catch (Exception e) {
			throw new NullPointerException("HCBI Discount Rate not found at Age : " + age);
		}
		premiumHRBI = premiumHRBI.multiply(new BigDecimal(occupation_loding)).setScale(0, RoundingMode.HALF_UP);
		// System.out.println("premiumHRBI : "+premiumHRBI.toString());
		return premiumHRBI;
	}

}
