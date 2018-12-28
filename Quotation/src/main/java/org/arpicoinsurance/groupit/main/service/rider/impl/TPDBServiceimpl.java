package org.arpicoinsurance.groupit.main.service.rider.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.service.rider.TPDBService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TPDBServiceimpl implements TPDBService {

	@Override
	public BigDecimal calculateTPDB(double ridsumasu, String payFrequency, double relief, Double atpRate)
			throws Exception {
		// TODO Auto-generated method stub
		// //System.out.println("TPDB ridsumasu : "+ridsumasu+" payFrequency :
		// "+payFrequency+" relief : "+relief);
		BigDecimal premiumTPDB = new BigDecimal(0);
		if (payFrequency.equalsIgnoreCase("S")) {
			// (((0.5*@rider_sum_assured@)/1000))*@relief@
			premiumTPDB = ((new BigDecimal(0.5).multiply(new BigDecimal(ridsumasu))).divide(new BigDecimal(1000), 6,
					RoundingMode.HALF_UP)).multiply(new BigDecimal(relief)).setScale(0, RoundingMode.HALF_UP);
		} else {
			// (((0.5*@rider_sum_assured@)/1000)/@payment_frequency@)*@relief@
			premiumTPDB = (((new BigDecimal(0.5).multiply(new BigDecimal(ridsumasu))).divide(new BigDecimal(1000), 6,
					RoundingMode.HALF_UP)).divide(new BigDecimal(new CalculationUtils().getPayterm(payFrequency)), 10,
							RoundingMode.HALF_UP)).multiply(new BigDecimal(relief)).setScale(0, RoundingMode.HALF_UP);
		}
		
		// ATP Rate
		premiumTPDB = premiumTPDB.multiply(new BigDecimal(atpRate)).setScale(0, RoundingMode.HALF_UP);;
		//premiumTPDB = premiumTPDB.multiply(new BigDecimal(occupation_loding)).setScale(0, RoundingMode.HALF_UP);
		// //System.out.println("premiumTPDB : "+premiumTPDB.toString());
		return premiumTPDB;
	}

}
