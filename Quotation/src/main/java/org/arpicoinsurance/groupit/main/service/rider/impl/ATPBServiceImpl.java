package org.arpicoinsurance.groupit.main.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.dao.RateCardATPBDao;
import org.arpicoinsurance.groupit.main.dao.RateCardINVPDao;
import org.arpicoinsurance.groupit.main.model.RateCardATPB;
import org.arpicoinsurance.groupit.main.service.ATPBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ATPBServiceImpl implements ATPBService{

	@Autowired
	private RateCardATPBDao rateCardATPBDao;
	
	@Override
	public BigDecimal calculateATPB(int age, int term, Date chedat, double ridsumasu, String payFrequency, double relief) throws Exception {
		BigDecimal premiumATPB = new BigDecimal(0);
		RateCardATPB rateCardATPB = rateCardATPBDao.findByAgeAndTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term, chedat, chedat, chedat, chedat);
		System.out.println("ridsumasu : "+ridsumasu+" payFrequency : "+payFrequency+" relief : "+relief+" Rate : "+rateCardATPB.getRate());
		if(payFrequency.equalsIgnoreCase("S")){
			// ((@rate@*@rider_sum_assured@/1000))*@relief@
			premiumATPB = (new BigDecimal(rateCardATPB.getRate()).multiply(new BigDecimal(ridsumasu)).divide(new BigDecimal(1000), 6, RoundingMode.HALF_UP)).multiply(new BigDecimal(relief)).setScale(0, RoundingMode.HALF_UP);		
		}else {
			// ((@rate@*@rider_sum_assured@/1000)/@payment_frequency@)*@relief@
			premiumATPB = ((new BigDecimal(rateCardATPB.getRate()).multiply(new BigDecimal(ridsumasu)).divide(new BigDecimal(1000), 6, RoundingMode.HALF_UP)).divide(new BigDecimal(CalculationUtils.getPayterm(payFrequency)), 10, RoundingMode.HALF_UP)).multiply(new BigDecimal(relief)).setScale(0, RoundingMode.HALF_UP);  
		}
		return premiumATPB;
	}

}
