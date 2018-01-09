package org.arpicoinsurance.groupit.main.service.rider.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.service.rider.ADBService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ADBServiceImpl implements ADBService{

	@Override
	public BigDecimal calculateADB(double ridsumasu, String payFrequency, double relief)
			throws Exception {
		System.out.println("ADB ridsumasu : "+ridsumasu+" payFrequency : "+payFrequency+" relief : "+relief);
		BigDecimal premiumADB = new BigDecimal(0);
		if(payFrequency.equalsIgnoreCase("S")){
			//(((1*@rider_sum_assured@)/1000))*@relief@
			premiumADB = (new BigDecimal(ridsumasu).divide(new BigDecimal(1000), 6, RoundingMode.HALF_UP)).multiply(new BigDecimal(relief)).setScale(0, RoundingMode.HALF_UP);
		}else {
			// (((1*@rider_sum_assured@)/1000)/@payment_frequency@)*@relief@
			premiumADB = ((new BigDecimal(ridsumasu).divide(new BigDecimal(1000), 6, RoundingMode.HALF_UP)).divide(new BigDecimal(new CalculationUtils().getPayterm(payFrequency)), 10, RoundingMode.HALF_UP)).multiply(new BigDecimal(relief)).setScale(0, RoundingMode.HALF_UP);  
		}
		System.out.println("premiumADB : "+premiumADB.toString());
		return premiumADB;
		
	}

	

	

}
