package org.arpicoinsurance.groupit.main.service.rider.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.dao.RateCardMFIBTDao;
import org.arpicoinsurance.groupit.main.model.RateCardMFIBT;
import org.arpicoinsurance.groupit.main.service.rider.MFIBTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MFIBTServiceImpl implements MFIBTService{

	@Autowired
	private RateCardMFIBTDao rateCardMFIBTDao;
	
	@Override
	public BigDecimal calculateMFIBT(Integer age, Integer term, Date chedat, Double ridsumasu, String payFrequency,
			Double relief, double occupation_loding) throws Exception {
		BigDecimal premiumMFIBT = new BigDecimal(0);
		RateCardMFIBT rateCardMFIBT = rateCardMFIBTDao.findByAgeAndTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term, chedat, chedat, chedat, chedat);
//		System.out.println("MFIBT ridsumasu : "+ridsumasu+" payFrequency : "+payFrequency+" relief : "+relief+" Rate : "+rateCardMFIBT.getRate());
		try{if(payFrequency.equalsIgnoreCase("S")){
			// ((@rate@*@rider_sum_assured@/1000))*@relief@
			premiumMFIBT = (new BigDecimal(rateCardMFIBT.getRate()).multiply(new BigDecimal(ridsumasu)).divide(new BigDecimal(1000), 6, RoundingMode.HALF_UP)).multiply(new BigDecimal(relief)).setScale(0, RoundingMode.HALF_UP);		
		}else {
			// ((@rate@*@rider_sum_assured@/1000)/@payment_frequency@)*@relief@
			premiumMFIBT = ((new BigDecimal(rateCardMFIBT.getRate()).multiply(new BigDecimal(ridsumasu)).divide(new BigDecimal(1000), 6, RoundingMode.HALF_UP)).divide(new BigDecimal(new CalculationUtils().getPayterm(payFrequency)), 10, RoundingMode.HALF_UP)).multiply(new BigDecimal(relief)).setScale(0, RoundingMode.HALF_UP);  
		}} catch (Exception e) {
			throw new NullPointerException("MFIBT Rate not found at Age : " + age + " and Term : " + term);
		}
		premiumMFIBT = premiumMFIBT.multiply(new BigDecimal(occupation_loding)).setScale(0, RoundingMode.HALF_UP);
//		System.out.println("premiumMFIBT : "+premiumMFIBT.toString());
		return premiumMFIBT;
	}

}
