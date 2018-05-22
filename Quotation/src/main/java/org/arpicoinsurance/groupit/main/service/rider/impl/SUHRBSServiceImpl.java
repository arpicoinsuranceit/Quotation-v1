package org.arpicoinsurance.groupit.main.service.rider.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.dao.RateCardSUHRBDao;
import org.arpicoinsurance.groupit.main.model.RateCardSUHRB;
import org.arpicoinsurance.groupit.main.service.rider.SUHRBSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SUHRBSServiceImpl implements SUHRBSService{

	@Autowired
	private RateCardSUHRBDao rateCardSUHRBDao;
	
	@Override
	public BigDecimal calculateSUHRBS(Integer age, String sex, Integer term, Double ridsumasu, Date chedat,
			String payFrequency, Double relief, double occupation_loding) throws Exception {
		BigDecimal premiumSUHRBS = new BigDecimal(0);
		RateCardSUHRB rateCardSUHRB = rateCardSUHRBDao.findByAgetoOrAgetoLessThanAndAgefromOrAgefromGreaterThanAndSexAndTermAndSumasuAndStrdatLessThanOrStrdat(age, age, age, age, sex, term, ridsumasu, chedat, chedat);
//		System.out.println("SUHRBS age : "+age);
//		System.out.println("sex : "+sex);
//		System.out.println("ridsumasu : "+ridsumasu);
//		System.out.println("term : "+term);
//		System.out.println("payFrequency : "+payFrequency);
//		System.out.println("relief : "+relief);
//		System.out.println("Rate : "+rateCardSUHRB.getRate());
//		
		if(payFrequency.equalsIgnoreCase("S")){
			// ((@rate@) *@relief@)
			premiumSUHRBS = new BigDecimal(rateCardSUHRB.getRate()).multiply(new BigDecimal(relief)).setScale(0, RoundingMode.HALF_UP);
		}else {
			// ((@rate@/@payment_frequency@) *@relief@)
			premiumSUHRBS = (new BigDecimal(rateCardSUHRB.getRate()).divide(new BigDecimal(new CalculationUtils().getPayterm(payFrequency)), 6, RoundingMode.HALF_UP)).multiply(new BigDecimal(relief)).setScale(0, RoundingMode.HALF_UP); 
		}
		premiumSUHRBS = premiumSUHRBS.multiply(new BigDecimal(occupation_loding)).setScale(0, RoundingMode.HALF_UP);
//		System.out.println("premiumSUHRBS : "+premiumSUHRBS.toString());
		return premiumSUHRBS;
	}

}
