package org.arpicoinsurance.groupit.main.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.arpicoinsurance.groupit.main.dao.RateCardINVPDao;
import org.arpicoinsurance.groupit.main.model.RateCardINVP;
import org.arpicoinsurance.groupit.main.service.INVPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class INVEPServiceImpl implements INVPService{

	@Autowired
	private RateCardINVPDao rateCardINVPDao;
	
	@Override
	public BigDecimal calculateL2(int age, int term, double intrat, Date chedat, double bassum, int paytrm) throws Exception {
		BigDecimal premium = new BigDecimal(0);
		RateCardINVP rateCardINVP = rateCardINVPDao.findByAgeAndTermAndIntratAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term, intrat, chedat, chedat, chedat, chedat);
		System.out.println("age : "+age+" term : "+term+" intrat : "+intrat+" paytrm : "+paytrm+" Sumasu : "+rateCardINVP.getSumasu()+" SumRate : "+rateCardINVP.getSumasu()+" Rate : "+rateCardINVP.getRate());
		premium = ((new BigDecimal(1000).divide(new BigDecimal(rateCardINVP.getSumasu()),20,RoundingMode.HALF_UP)).multiply(new BigDecimal(bassum))).divide(new BigDecimal(paytrm), 4, RoundingMode.UP);
		return premium;
	}

	@Override
	public Double addRebatetoBSAPremium(double rebate, BigDecimal premium) {
		System.out.println("rebate : "+rebate);
		BigDecimal rebateRate = new BigDecimal(1).subtract((new BigDecimal(rebate).divide(new BigDecimal(100), 6 ,RoundingMode.HALF_UP)));
		System.out.println("rebateRate : "+rebateRate.doubleValue());
		premium = premium.multiply(rebateRate).setScale(0, RoundingMode.HALF_UP);
		return premium.doubleValue();
	}

	@Override
	public BigDecimal calculateMaturity(int age, int term, double intrat, Date chedat, double bassum, int paytrm)
			throws Exception {
		// (@rate@/@sum_assured_rate@)*@sum_assured@
				BigDecimal maturity  = new BigDecimal(0);
				RateCardINVP rateCardINVP = rateCardINVPDao.findByAgeAndTermAndIntratAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term, intrat, chedat, chedat, chedat, chedat);
				System.out.println("age : "+age+" term : "+term+" intrat : "+intrat+" paytrm : "+paytrm+" Sumasu : "+rateCardINVP.getSumasu()+" SumRate : "+rateCardINVP.getSumasu()+" Rate : "+rateCardINVP.getRate());
				maturity = (new BigDecimal(rateCardINVP.getRate()).divide(new BigDecimal(rateCardINVP.getSumasu()),20,RoundingMode.HALF_UP)).multiply(new BigDecimal(bassum)).setScale(0, RoundingMode.HALF_UP);
				return maturity;
			
	}
	
	

}
