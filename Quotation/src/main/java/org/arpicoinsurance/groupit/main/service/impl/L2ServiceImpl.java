package org.arpicoinsurance.groupit.main.service.impl;

import java.math.BigDecimal;

import org.arpicoinsurance.groupit.main.dao.RateCardINVPDao;
import org.arpicoinsurance.groupit.main.model.RateCardINVP;
import org.arpicoinsurance.groupit.main.service.L2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class L2ServiceImpl implements L2Service{

	@Autowired
	private RateCardINVPDao rateCardINVPDao;
	
	@Override
	public Double calculateL2() throws Exception {
		// (((1000/@sum_assured_rate@)*@sum_assured@)/@payment_frequency@)*(1-(@rebate@/100))
		//BigDecimal premium = new BigDecimal(0);
		//RateCardINVP rateCardINVP = rateCardINVPDao.findByAgeAndTermAndIntratAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term, intrat, strdat1, strdat2, enddat1, enddat2)
		//		premium = new BigDecimal(1000).divide(new BigDecimal(rateCardINVP.getSumasu()))
		return null;
	}

}
