package org.arpicoinsurance.groupit.main.service.rider.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.dao.RateCardATFESCDao;
import org.arpicoinsurance.groupit.main.dao.RateCardINVPDao;
import org.arpicoinsurance.groupit.main.model.RateCardATFESC;
import org.arpicoinsurance.groupit.main.service.rider.ATPBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ATPBServiceImpl implements ATPBService{

	@Autowired
	private RateCardATFESCDao rateCardATFESCDao;
	
	@Override
	public BigDecimal calculateATPB(Integer age, Integer term, Date chedat, Double ridsumasu, String payFrequency, Double relief) throws Exception {
		BigDecimal premiumATPB = new BigDecimal(0);
		RateCardATFESC rateCardATFESC = rateCardATFESCDao.findByAgeAndTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term, chedat, chedat, chedat, chedat);
		System.out.println("ATPB ridsumasu : "+ridsumasu+" payFrequency : "+payFrequency+" relief : "+relief+" Rate : "+rateCardATFESC.getRate());
		if(payFrequency.equalsIgnoreCase("S")){
			// ((@rate@*@rider_sum_assured@/1000))*@relief@
			premiumATPB = (new BigDecimal(rateCardATFESC.getRate()).multiply(new BigDecimal(ridsumasu)).divide(new BigDecimal(1000), 6, RoundingMode.HALF_UP)).multiply(new BigDecimal(relief)).setScale(0, RoundingMode.HALF_UP);		
		}else {
			// ((@rate@*@rider_sum_assured@/1000)/@payment_frequency@)*@relief@
			premiumATPB = ((new BigDecimal(rateCardATFESC.getRate()).multiply(new BigDecimal(ridsumasu)).divide(new BigDecimal(1000), 6, RoundingMode.HALF_UP)).divide(new BigDecimal(new CalculationUtils().getPayterm(payFrequency)), 10, RoundingMode.HALF_UP)).multiply(new BigDecimal(relief)).setScale(0, RoundingMode.HALF_UP);  
		}
		System.out.println("premiumATPB : "+premiumATPB.toString());
		return premiumATPB;
	}

}
