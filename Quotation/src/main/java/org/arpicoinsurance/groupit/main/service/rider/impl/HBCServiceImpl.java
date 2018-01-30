package org.arpicoinsurance.groupit.main.service.rider.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.dao.RateCardHBCDao;
import org.arpicoinsurance.groupit.main.model.RateCardHBC;
import org.arpicoinsurance.groupit.main.service.rider.HBCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class HBCServiceImpl implements HBCService{

	@Autowired
	private RateCardHBCDao rateCardHBCDao;

	@Override
	public BigDecimal calculateHBC(Integer term, Date chedat, Double ridsumasu, String payFrequency,
			Double relief, double occupation_loding) throws Exception {
		BigDecimal premiumHBC = new BigDecimal(0);
		RateCardHBC rateCardHBC = rateCardHBCDao.findByTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(term, chedat, chedat, chedat, chedat);
		System.out.println("HBC ridsumasu : "+ridsumasu+" payFrequency : "+payFrequency+" relief : "+relief+" Rate : "+rateCardHBC.getRate());
		if(payFrequency.equalsIgnoreCase("S")){
			// ((@rate@*@rider_sum_assured@/100))*@relief@
			premiumHBC = (new BigDecimal(rateCardHBC.getRate()).multiply(new BigDecimal(ridsumasu)).divide(new BigDecimal(100), 6, RoundingMode.HALF_UP)).multiply(new BigDecimal(relief)).setScale(0, RoundingMode.HALF_UP);		
		}else {
			// ((@rate@*@rider_sum_assured@/100)/@payment_frequency@)*@relief@
			premiumHBC = ((new BigDecimal(rateCardHBC.getRate()).multiply(new BigDecimal(ridsumasu)).divide(new BigDecimal(100), 6, RoundingMode.HALF_UP)).divide(new BigDecimal(new CalculationUtils().getPayterm(payFrequency)), 10, RoundingMode.HALF_UP)).multiply(new BigDecimal(relief)).setScale(0, RoundingMode.HALF_UP);  
		}
		
		premiumHBC = premiumHBC.multiply(new BigDecimal(occupation_loding)).setScale(0, RoundingMode.HALF_UP);
		System.out.println("premiumHBC : "+premiumHBC.toString());
		return premiumHBC;
	}

}
