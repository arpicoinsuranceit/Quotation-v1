package org.arpicoinsurance.groupit.main.service.rider.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.dao.RateCardHRBFDao;
import org.arpicoinsurance.groupit.main.dao.RateCardHRBIDao;
import org.arpicoinsurance.groupit.main.model.RateCardHRBF;
import org.arpicoinsurance.groupit.main.model.RateCardHRBI;
import org.arpicoinsurance.groupit.main.service.rider.HRBFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HRBFServiceImpl implements HRBFService{

	@Autowired
	private RateCardHRBFDao rateCardHRBFDao;
	
	@Override
	public BigDecimal calculateHRBF(Integer age, Integer term, Double ridsumasu, Integer adlcnt,
			Integer chlcnt, Date chedat, String payFrequency, Double relief, double occupation_loding)
			throws Exception {
		
		System.out.println(adlcnt +"##########"+ chlcnt+"##########"+ term+"##########"+ age+"##########"+ ridsumasu+"##########"+ chlcnt);
		BigDecimal premiumHRBF = new BigDecimal(0);
		
		RateCardHRBF rateCardHRBF= rateCardHRBFDao.findByAgeAndTermAndChlcntAndSumasuAndAdlcntAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term, chlcnt, ridsumasu, adlcnt, chedat, chedat, chedat, chedat);
		System.out.println("Rate : "+rateCardHRBF.getRate());
		System.out.println("HRB age : "+age+" ridsumasu : "+ridsumasu+" term : "+term+" payFrequency : "+payFrequency+" relief : "+relief+" Rate : "+rateCardHRBF.getRate());
		if(payFrequency.equalsIgnoreCase("S")){
			// ((@rate@) *@relief@)
			premiumHRBF = new BigDecimal(rateCardHRBF.getRate()).multiply(new BigDecimal(relief)).setScale(0, RoundingMode.HALF_UP);
		}else {
			// ((@rate@/@payment_frequency@) *@relief@)
			premiumHRBF = (new BigDecimal(rateCardHRBF.getRate()).divide(new BigDecimal(new CalculationUtils().getPayterm(payFrequency)), 6, RoundingMode.HALF_UP)).multiply(new BigDecimal(relief)).setScale(0, RoundingMode.HALF_UP); 
		}
		
		premiumHRBF = premiumHRBF.multiply(new BigDecimal(occupation_loding)).setScale(0, RoundingMode.HALF_UP);
		System.out.println("premiumHRBI : "+premiumHRBF.toString());
		return premiumHRBF;
	}

}
