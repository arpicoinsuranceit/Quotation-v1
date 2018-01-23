package org.arpicoinsurance.groupit.main.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.dao.RateCardENDDao;
import org.arpicoinsurance.groupit.main.dao.RateCardARPDao;
import org.arpicoinsurance.groupit.main.model.RateCardEND;
import org.arpicoinsurance.groupit.main.model.RateCardARP;
import org.arpicoinsurance.groupit.main.service.ARPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ARPServiceImpl implements ARPService {
	
	@Autowired
	private RateCardENDDao rateCardENDDao;
	
	@Autowired
	private RateCardARPDao rateCardARPDao;

	@Override
	public BigDecimal calculateL2(int age, int term, String rlfterm, double rebate, Date chedat, double bassum, String payFrequency)
			throws Exception {
		System.out.println("ARP bassum : "+bassum+" age : "+age+" term : "+term+" payFrequency : "+payFrequency+" rlfterm : "+rlfterm);
		BigDecimal premium = new BigDecimal(0);
		
		RateCardEND rateCardEND = rateCardENDDao.findByAgeAndTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term, chedat, chedat, chedat, chedat);
		System.out.println("rateCardARP : "+rateCardEND.getRate());
		RateCardARP rateCardARP = rateCardARPDao.findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term, rlfterm, chedat, chedat, chedat, chedat);
		System.out.println("rateCardARPRelief : "+rateCardARP.getRate());
		
		if(payFrequency.equalsIgnoreCase("S")){
			// ((((@rate@-(@rate@*@rebate@/100))/1000)*@sum_assured@)) *@relief@			
			premium = (((new BigDecimal(rateCardEND.getRate()).subtract(((new BigDecimal(rateCardEND.getRate()).multiply(new BigDecimal(rebate))).divide(new BigDecimal(100), 6, RoundingMode.HALF_UP)))).divide(new BigDecimal(1000), 6, RoundingMode.HALF_UP)).multiply(new BigDecimal(bassum))).multiply(new BigDecimal(rateCardARP.getRate())).setScale(0, RoundingMode.HALF_UP);
		}else {
			// ((((@rate@-(@rate@*@rebate@/100))/1000)*@sum_assured@)/@payment_frequency@) *@relief@
			premium = ((((new BigDecimal(rateCardEND.getRate()).subtract(((new BigDecimal(rateCardEND.getRate()).multiply(new BigDecimal(rebate))).divide(new BigDecimal(100), 6, RoundingMode.HALF_UP)))).divide(new BigDecimal(1000), 6, RoundingMode.HALF_UP)).multiply(new BigDecimal(bassum))).divide(new BigDecimal(new CalculationUtils().getPayterm(payFrequency)), 10, RoundingMode.HALF_UP)).multiply(new BigDecimal(rateCardARP.getRate())).setScale(0, RoundingMode.HALF_UP);
		}
		System.out.println("premium : "+premium.toString());
		return premium;
	}

	@Override
	public BigDecimal calculateMaturity(int term, double bassum)
			throws Exception {
		// @sum_assured@ + ((@sum_assured@*0.025)*@term@)
		BigDecimal maturity = new BigDecimal(0);
		System.out.println("term : "+term+" bassum : "+bassum);
		maturity = (new BigDecimal(bassum).add(((new BigDecimal(bassum).multiply(new BigDecimal(0.025))).multiply(new BigDecimal(term))))).setScale(0, RoundingMode.HALF_UP);
		System.out.println("maturity : "+maturity.toString());
		return maturity;
	}

}
