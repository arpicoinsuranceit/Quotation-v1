package org.arpicoinsurance.groupit.main.service.rider.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.arpicoinsurance.groupit.main.helper.QuoInvpCalResp;
import org.arpicoinsurance.groupit.main.service.rider.WPBSService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WPBSServiceImpl implements WPBSService {

	@Override
	public BigDecimal calculateWPBS(QuoInvpCalResp calResp) throws Exception {
		BigDecimal premiumWPBS = new BigDecimal(0);
		premiumWPBS = premiumWPBS.add(new BigDecimal(calResp.getBsas() == null ? 0.0 : calResp.getBsas()));
		premiumWPBS = premiumWPBS.add(new BigDecimal(calResp.getFebs() == null ? 0.0 : calResp.getFebs()));
		premiumWPBS = premiumWPBS.add(new BigDecimal(calResp.getCibs() == null ? 0.0 : calResp.getCibs()));
		premiumWPBS = premiumWPBS.multiply(new BigDecimal(0.05)).setScale(0, RoundingMode.HALF_UP);
		System.out.println("premiumWPBS : "+premiumWPBS.toString());
		return premiumWPBS;
	}


}
