package org.arpicoinsurance.groupit.main.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.arpicoinsurance.groupit.main.dao.RateCardASFPDao;
import org.arpicoinsurance.groupit.main.model.RateCardASFP;
import org.arpicoinsurance.groupit.main.service.ASFPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ASFPServiceImpl implements ASFPService {

	@Autowired
	private RateCardASFPDao rateCardASFPDao;
	
	@Override
	public BigDecimal calculateL10(int age, int term, double rebate, Date chedat, double msfb, int paytrm)
			throws Exception {
		System.out.println("ARP msfb : "+msfb+" age : "+age+" term : "+term+" paytrm : "+paytrm);
		BigDecimal premium = new BigDecimal(0);
		
		RateCardASFP rateCardASFP = rateCardASFPDao.findByAgeAndTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term, chedat, chedat, chedat, chedat);
		System.out.println("rateCardASFP : "+rateCardASFP.getRate());
		
		// (((@rate@-(@rate@*@rebate@/100))/1000)*@sum_assured@)/@payment_frequency@
					premium = ((((new BigDecimal(rateCardASFP.getRate()).subtract(((new BigDecimal(rateCardASFP.getRate()).multiply(new BigDecimal(rebate))).divide(new BigDecimal(100), 6, RoundingMode.HALF_UP)))).divide(new BigDecimal(1000), 6, RoundingMode.HALF_UP)).multiply(new BigDecimal(msfb))).divide(new BigDecimal(paytrm), 10, RoundingMode.HALF_UP)).setScale(0, RoundingMode.HALF_UP);
					
		System.out.println("premium : "+premium.toString());
		
		return premium;
	}

	@Override
	public BigDecimal calculateL2(int term, double msfb) throws Exception {
		BigDecimal maturity = new BigDecimal(0);
		System.out.println("term : "+term+" msfb : "+msfb);
		maturity = (new BigDecimal(term).multiply(new BigDecimal(msfb))).multiply(new BigDecimal(12)).setScale(0, RoundingMode.HALF_UP);
		System.out.println("maturity : "+maturity.toString());
		return maturity;
	}

}
