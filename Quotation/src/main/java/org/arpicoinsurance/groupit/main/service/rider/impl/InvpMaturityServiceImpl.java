package org.arpicoinsurance.groupit.main.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.arpicoinsurance.groupit.main.dao.RateCardINVPDao;
import org.arpicoinsurance.groupit.main.model.RateCardINVP;
import org.arpicoinsurance.groupit.main.service.L3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class L3ServiceImpl implements L3Service{

	@Autowired
	private RateCardINVPDao rateCardINVPDao;
	
	@Override
	public BigDecimal calculateL3(int age, int term, double intrat, Date chedat, double bassum, int paytrm) throws Exception {
		// (@rate@/@sum_assured_rate@)*@sum_assured@
		BigDecimal maturity  = new BigDecimal(0);
		RateCardINVP rateCardINVP = rateCardINVPDao.findByAgeAndTermAndIntratAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term, intrat, chedat, chedat, chedat, chedat);
		System.out.println("age : "+age+" term : "+term+" intrat : "+intrat+" paytrm : "+paytrm+" Sumasu : "+rateCardINVP.getSumasu()+" SumRate : "+rateCardINVP.getSumasu()+" Rate : "+rateCardINVP.getRate());
		maturity = (new BigDecimal(rateCardINVP.getRate()).divide(new BigDecimal(rateCardINVP.getSumasu()),20,RoundingMode.HALF_UP)).multiply(new BigDecimal(bassum)).setScale(0, RoundingMode.HALF_UP);
		return maturity;
	}

}
