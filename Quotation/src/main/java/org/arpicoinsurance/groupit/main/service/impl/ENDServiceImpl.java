package org.arpicoinsurance.groupit.main.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.arpicoinsurance.groupit.main.dao.RateCardENDDao;
import org.arpicoinsurance.groupit.main.model.RateCardEND;
import org.arpicoinsurance.groupit.main.service.ENDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ENDServiceImpl implements ENDService {

	@Autowired
	private RateCardENDDao rateCardENDDao;
	
	@Override
	public BigDecimal calculateL2(int age, int term, double rebate, Date chedat, double bassum, int paytrm)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println("END bassum : "+bassum+" age : "+age+" term : "+term+" paytrm : "+paytrm);
		BigDecimal premium = new BigDecimal(0);
		
		RateCardEND rateCardEND = rateCardENDDao.findByAgeAndTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term, chedat, chedat, chedat, chedat);
		System.out.println("rateCardEND : "+rateCardEND.getRate());
		
		// (((@rate@-(@rate@*@rebate@/100))/1000)*@sum_assured@)/@payment_frequency@
		premium = ((((new BigDecimal(rateCardEND.getRate()).subtract(((new BigDecimal(rateCardEND.getRate()).multiply(new BigDecimal(rebate))).divide(new BigDecimal(100), 6, RoundingMode.HALF_UP)))).divide(new BigDecimal(1000), 6, RoundingMode.HALF_UP)).multiply(new BigDecimal(bassum))).divide(new BigDecimal(paytrm), 10, RoundingMode.HALF_UP)).setScale(0, RoundingMode.HALF_UP);
					
		System.out.println("premium : "+premium.toString());
		return premium;
	}

	@Override
	public BigDecimal calculateMaturity(int term, double bassum) throws Exception {
		// @sum_assured@ + ((@sum_assured@*0.025)*@term@)
		BigDecimal maturity = new BigDecimal(0);
		System.out.println("term : "+term+" bassum : "+bassum);
		maturity = (new BigDecimal(bassum).add(((new BigDecimal(bassum).multiply(new BigDecimal(0.025))).multiply(new BigDecimal(term))))).setScale(0, RoundingMode.HALF_UP);
		System.out.println("maturity : "+maturity.toString());
		return maturity;
	}

}
