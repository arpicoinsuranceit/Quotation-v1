package org.arpicoinsurance.groupit.main.service.rider.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.service.rider.PPDBService;

public class PPDBServiceImpl implements PPDBService{

	@Override
	public BigDecimal calculatePPDB(double ridsumasu, String payFrequency, double relief) throws Exception {
		System.out.println("ridsumasu : "+ridsumasu+" payFrequency : "+payFrequency+" relief : "+relief);
		BigDecimal premiumPPDB = new BigDecimal(0);
		if(payFrequency.equalsIgnoreCase("S")){
			//(((1*@rider_sum_assured@)/1000))*@relief@
			premiumPPDB = (new BigDecimal(ridsumasu).divide(new BigDecimal(1000), 6, RoundingMode.HALF_UP)).multiply(new BigDecimal(relief)).setScale(0, RoundingMode.HALF_UP);
		}else {
			// (((1*@rider_sum_assured@)/1000)/@payment_frequency@)*@relief@
			premiumPPDB = ((new BigDecimal(ridsumasu).divide(new BigDecimal(1000), 6, RoundingMode.HALF_UP)).divide(new BigDecimal(new CalculationUtils().getPayterm(payFrequency)), 10, RoundingMode.HALF_UP)).multiply(new BigDecimal(relief)).setScale(0, RoundingMode.HALF_UP);  
		}
		return premiumPPDB;
	}

	@Override
	public BigDecimal calculatePPDBS(double ridsumasu, String payFrequency, double relief) throws Exception {
		System.out.println("ridsumasu : "+ridsumasu+" payFrequency : "+payFrequency+" relief : "+relief);
		BigDecimal premiumPPDBS = new BigDecimal(0);
		if(payFrequency.equalsIgnoreCase("S")){
			//(((1*@rider_sum_assured@)/1000))*@relief@
			premiumPPDBS = (new BigDecimal(ridsumasu).divide(new BigDecimal(1000), 6, RoundingMode.HALF_UP)).multiply(new BigDecimal(relief)).setScale(0, RoundingMode.HALF_UP);
		}else {
			// (((1*@rider_sum_assured@)/1000)/@payment_frequency@)*@relief@
			premiumPPDBS = ((new BigDecimal(ridsumasu).divide(new BigDecimal(1000), 6, RoundingMode.HALF_UP)).divide(new BigDecimal(new CalculationUtils().getPayterm(payFrequency)), 10, RoundingMode.HALF_UP)).multiply(new BigDecimal(relief)).setScale(0, RoundingMode.HALF_UP);  
		}
		return premiumPPDBS;
	}

	

}