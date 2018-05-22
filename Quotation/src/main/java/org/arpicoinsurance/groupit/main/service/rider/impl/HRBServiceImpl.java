package org.arpicoinsurance.groupit.main.service.rider.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.dao.RateCardHRBDao;
import org.arpicoinsurance.groupit.main.model.RateCardHRB;
import org.arpicoinsurance.groupit.main.service.rider.HRBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class HRBServiceImpl implements HRBService{

	@Autowired
	private RateCardHRBDao rateCardHRBDao;
	
	@Override
	public BigDecimal calculateHRB(Integer age, String sex, Double ridsumasu, Integer adlcnt, Integer chlcnt,
			Date chedat, String payFrequency, Double relief, double occupation_loding) throws Exception {
		BigDecimal premiumHRB = new BigDecimal(0);
//		System.out.println(age);
//		System.out.println(sex);
//		System.out.println(ridsumasu);
//		System.out.println(adlcnt);
//		System.out.println(chlcnt);
		
		
		RateCardHRB rateCardHRB = rateCardHRBDao.findByAgetoOrAgetoLessThanAndAgefromOrAgefromGreaterThanAndSexAndSumasuAndAdlcntAndChlcntAndStrdatLessThanOrStrdat(age, age, age, age, sex, ridsumasu, adlcnt, chlcnt, chedat, chedat);
//		System.out.println("Rate : "+rateCardHRB.getRate());
//		System.out.println("HRB age : "+age+" sex : "+sex+" ridsumasu : "+ridsumasu+" adlcnt : "+adlcnt+" chlcnt : "+chlcnt+" payFrequency : "+payFrequency+" relief : "+relief+" Rate : "+rateCardHRB.getRate());
		if(payFrequency.equalsIgnoreCase("S")){
			// ((@rate@) *@relief@)
			premiumHRB = new BigDecimal(rateCardHRB.getRate()).multiply(new BigDecimal(relief)).setScale(0, RoundingMode.HALF_UP);
		}else {
			// ((@rate@/@payment_frequency@) *@relief@)
			premiumHRB = (new BigDecimal(rateCardHRB.getRate()).divide(new BigDecimal(new CalculationUtils().getPayterm(payFrequency)), 6, RoundingMode.HALF_UP)).multiply(new BigDecimal(relief)).setScale(0, RoundingMode.HALF_UP); 
		}
		
		premiumHRB = premiumHRB.multiply(new BigDecimal(occupation_loding)).setScale(0, RoundingMode.HALF_UP);
//		System.out.println("premiumHRB : "+premiumHRB.toString());
		return premiumHRB;
	}


}
