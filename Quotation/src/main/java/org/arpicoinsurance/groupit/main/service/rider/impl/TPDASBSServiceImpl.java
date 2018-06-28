package org.arpicoinsurance.groupit.main.service.rider.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.dao.RateCardTPDASBDao;
import org.arpicoinsurance.groupit.main.model.RateCardTPDASB;
import org.arpicoinsurance.groupit.main.service.rider.TPDASBSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
public class TPDASBSServiceImpl implements TPDASBSService {

	@Autowired
	private RateCardTPDASBDao cardTPDASBDao;
	
	@Override
	public BigDecimal calculateTPDASBS(int age, int term, Date chedat, double ridsumasu, String payFrequency, double relief, double occupation_loding)
			throws Exception {
		BigDecimal premiumTPDASBS = new BigDecimal(0);
		RateCardTPDASB rateCardTPDASB = cardTPDASBDao.findByAgeAndTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term, chedat, chedat, chedat, chedat);
//		System.out.println("TPDASBS ridsumasu : "+ridsumasu+" payFrequency : "+payFrequency+" relief : "+relief+" Rate : "+rateCardTPDASB.getRate());
		if(payFrequency.equalsIgnoreCase("S")){
			// ((@rate@*@rider_sum_assured@/1000))*@relief@
			premiumTPDASBS = ((new BigDecimal(rateCardTPDASB.getRate()).multiply(new BigDecimal(ridsumasu))).divide(new BigDecimal(1000), 6, RoundingMode.HALF_UP)).multiply(new BigDecimal(relief)).setScale(0, RoundingMode.HALF_UP);		
		}else {
			// ((@rate@*@rider_sum_assured@/1000)/@payment_frequency@)*@relief@
			premiumTPDASBS = (((new BigDecimal(rateCardTPDASB.getRate()).multiply(new BigDecimal(ridsumasu))).divide(new BigDecimal(1000), 6, RoundingMode.HALF_UP)).divide(new BigDecimal(new CalculationUtils().getPayterm(payFrequency)), 10, RoundingMode.HALF_UP)).multiply(new BigDecimal(relief)).setScale(0, RoundingMode.HALF_UP);  
		}
		premiumTPDASBS = premiumTPDASBS.multiply(new BigDecimal(occupation_loding)).setScale(0, RoundingMode.HALF_UP);
//		System.out.println("premiumTPDASBS : "+premiumTPDASBS.toString());
		return premiumTPDASBS;
	}

}
