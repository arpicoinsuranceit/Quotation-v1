package org.arpicoinsurance.groupit.main.service.rider.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.dao.RateCardHBDao;
import org.arpicoinsurance.groupit.main.model.RateCardHB;
import org.arpicoinsurance.groupit.main.service.rider.HBSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class HBSServiceImpl implements HBSService{

	@Autowired
	private RateCardHBDao rateCardHBDao;

	@Override
	public BigDecimal calculateHBS(Integer age, Integer term, Date chedat, Double ridsumasu, String payFrequency,
			Double relief, double occupation_loding) throws Exception {
		BigDecimal premiumHBS = new BigDecimal(0);
		RateCardHB rateCardHB = rateCardHBDao.findByAgeAndTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term, chedat, chedat, chedat, chedat);
		System.out.println("HBS ridsumasu : "+ridsumasu+" payFrequency : "+payFrequency+" relief : "+relief+" Rate : "+rateCardHB.getRate());
		if(payFrequency.equalsIgnoreCase("S")){
			// ((@rate@*@rider_sum_assured@/100))*@relief@
			premiumHBS = (new BigDecimal(rateCardHB.getRate()).multiply(new BigDecimal(ridsumasu)).divide(new BigDecimal(100), 6, RoundingMode.HALF_UP)).multiply(new BigDecimal(relief)).setScale(0, RoundingMode.HALF_UP);		
		}else {
			// ((@rate@*@rider_sum_assured@/100)/@payment_frequency@)*@relief@
			premiumHBS = ((new BigDecimal(rateCardHB.getRate()).multiply(new BigDecimal(ridsumasu)).divide(new BigDecimal(100), 6, RoundingMode.HALF_UP)).divide(new BigDecimal(new CalculationUtils().getPayterm(payFrequency)), 10, RoundingMode.HALF_UP)).multiply(new BigDecimal(relief)).setScale(0, RoundingMode.HALF_UP);  
		}
		premiumHBS = premiumHBS.multiply(new BigDecimal(occupation_loding)).setScale(0, RoundingMode.HALF_UP);
		System.out.println("premiumHBS : "+premiumHBS.toString());
		return premiumHBS;
	}

}
