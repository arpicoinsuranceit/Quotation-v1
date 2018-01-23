package org.arpicoinsurance.groupit.main.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.arpicoinsurance.groupit.main.dao.RateCardATRMDao;
import org.arpicoinsurance.groupit.main.model.RateCardATRM;
import org.arpicoinsurance.groupit.main.service.ATRMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ATRMServiceImpl implements ATRMService {

	@Autowired
	private RateCardATRMDao rateCardATRMDao;
	
	@Override
	public BigDecimal calculateL2(int age, int term, double rebate, Date chedat, double bassum,
			int paytrm) throws Exception {
		System.out.println("ARP bassum : "+bassum+" age : "+age+" term : "+term+" paytrm : "+paytrm);
		BigDecimal premium = new BigDecimal(0);
		
		RateCardATRM rateCardATRM = rateCardATRMDao.findByAgeAndTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term, chedat, chedat, chedat, chedat);
		System.out.println("rateCardATRM : "+rateCardATRM.getRate());
		
		// (((@rate@-(@rate@*@rebate@/100))/1000)*@sum_assured@)/@payment_frequency@
		premium = ((((new BigDecimal(rateCardATRM.getRate()).subtract(((new BigDecimal(rateCardATRM.getRate()).multiply(new BigDecimal(rebate))).divide(new BigDecimal(100), 6, RoundingMode.HALF_UP)))).divide(new BigDecimal(1000), 6, RoundingMode.HALF_UP)).multiply(new BigDecimal(bassum))).divide(new BigDecimal(paytrm), 10, RoundingMode.HALF_UP)).setScale(0, RoundingMode.HALF_UP);
					
		System.out.println("premium : "+premium.toString());
		return premium;
	}

}
