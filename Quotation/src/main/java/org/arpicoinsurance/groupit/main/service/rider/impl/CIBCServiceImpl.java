package org.arpicoinsurance.groupit.main.service.rider.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.dao.RateCardCIBCDao;
import org.arpicoinsurance.groupit.main.model.RateCardCIBC;
import org.arpicoinsurance.groupit.main.service.rider.CIBCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CIBCServiceImpl implements CIBCService{

	@Autowired
	private RateCardCIBCDao rateCardCIBCDao;
	
	@Override
	public BigDecimal calculateCIBC(Integer age, Integer term, Date chedat, Double ridsumasu, String payFrequency,
			Double relief) throws Exception {
		// TODO Auto-generated method stub
		BigDecimal premiumCIBC = new BigDecimal(0);
		RateCardCIBC rateCardCIBC = rateCardCIBCDao.findByAgeAndTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term, chedat, chedat, chedat, chedat);
		System.out.println("CIBC ridsumasu : "+ridsumasu+" payFrequency : "+payFrequency+" relief : "+relief+" Rate : "+rateCardCIBC.getRate());
		if(payFrequency.equalsIgnoreCase("S")){
			// ((@rate@*@rider_sum_assured@/1000))*@relief@
			premiumCIBC = (new BigDecimal(rateCardCIBC.getRate()).multiply(new BigDecimal(ridsumasu)).divide(new BigDecimal(1000), 6, RoundingMode.HALF_UP)).multiply(new BigDecimal(relief)).setScale(0, RoundingMode.HALF_UP);		
		}else {
			// ((@rate@*@rider_sum_assured@/1000)/@payment_frequency@)*@relief@
			premiumCIBC = ((new BigDecimal(rateCardCIBC.getRate()).multiply(new BigDecimal(ridsumasu)).divide(new BigDecimal(1000), 6, RoundingMode.HALF_UP)).divide(new BigDecimal(new CalculationUtils().getPayterm(payFrequency)), 10, RoundingMode.HALF_UP)).multiply(new BigDecimal(relief)).setScale(0, RoundingMode.HALF_UP);  
		}
		System.out.println("premiumCIBC : "+premiumCIBC.toString());
		return premiumCIBC;
	}


}
