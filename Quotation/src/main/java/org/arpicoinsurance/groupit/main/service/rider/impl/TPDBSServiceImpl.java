package org.arpicoinsurance.groupit.main.service.rider.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.service.rider.TPDBSService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TPDBSServiceImpl implements TPDBSService {

	@Override
	public BigDecimal calculateTPDBS(double ridsumasu, String payFrequency, double relief, double occupation_loding)
			throws Exception {
		// TODO Auto-generated method stub
		// //System.out.println("TPDBS ridsumasu : "+ridsumasu+" payFrequency :
		// "+payFrequency+" relief : "+relief);
		BigDecimal premiumTPDBS = new BigDecimal(0);
		if (payFrequency.equalsIgnoreCase("S")) {
			// (((0.5*@rider_sum_assured@)/1000))*@relief@
			premiumTPDBS = ((new BigDecimal(0.5).multiply(new BigDecimal(ridsumasu))).divide(new BigDecimal(1000), 6,
					RoundingMode.HALF_UP)).multiply(new BigDecimal(relief)).setScale(0, RoundingMode.HALF_UP);
		} else {
			// (((0.5*@rider_sum_assured@)/1000)/@payment_frequency@)*@relief@
			premiumTPDBS = (((new BigDecimal(0.5).multiply(new BigDecimal(ridsumasu))).divide(new BigDecimal(1000), 6,
					RoundingMode.HALF_UP)).divide(new BigDecimal(new CalculationUtils().getPayterm(payFrequency)), 10,
							RoundingMode.HALF_UP)).multiply(new BigDecimal(relief)).setScale(0, RoundingMode.HALF_UP);
		}
		premiumTPDBS = premiumTPDBS.multiply(new BigDecimal(occupation_loding)).setScale(0, RoundingMode.HALF_UP);
		// //System.out.println("premiumTPDBS : "+premiumTPDBS.toString());
		return premiumTPDBS;
	}

}
