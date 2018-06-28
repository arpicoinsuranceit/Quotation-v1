package org.arpicoinsurance.groupit.main.service.rider.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.service.rider.WPBService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WPBServiceImpl implements WPBService{

	@Override
	public BigDecimal calculateWPB(QuotationQuickCalResponse calResp, Double occuLoading) throws Exception {
		BigDecimal premiumWPB = new BigDecimal(0);
//		System.out.println(calResp.getBasicSumAssured() + " WPB SSSSSSSSSSSSSSSS");
//		System.out.println(calResp.getAtpb() + " WPB SSSSSSSSSSSSSSSS");
//		System.out.println(calResp.getFeb() + " WPB SSSSSSSSSSSSSSSS");
//		System.out.println(calResp.getMifdb() + " WPB SSSSSSSSSSSSSSSS");
//		System.out.println(calResp.getMifdbt() + " WPB SSSSSSSSSSSSSSSS");
//		System.out.println(calResp.getCib() + " WPB SSSSSSSSSSSSSSSS");
//		System.out.println(calResp.getCibc() + " WPB SSSSSSSSSSSSSSSS");
		premiumWPB = premiumWPB.add(new BigDecimal(calResp.getBasicSumAssured() == null ? 0.0 : calResp.getBasicSumAssured()));
		premiumWPB = premiumWPB.add(new BigDecimal(calResp.getAtpb() == null ? 0.0 : calResp.getAtpb()));
		premiumWPB = premiumWPB.add(new BigDecimal(calResp.getFeb() == null ? 0.0 : calResp.getFeb()));
		premiumWPB = premiumWPB.add(new BigDecimal(calResp.getMifdb() == null ? 0.0 : calResp.getMifdb()));
		premiumWPB = premiumWPB.add(new BigDecimal(calResp.getMifdbt() == null ? 0.0 : calResp.getMifdbt()));
		premiumWPB = premiumWPB.add(new BigDecimal(calResp.getCib() == null ? 0.0 : calResp.getCib()));
		premiumWPB = premiumWPB.add(new BigDecimal(calResp.getCibc() == null ? 0.0 : calResp.getCibc()));
		premiumWPB = premiumWPB.multiply(new BigDecimal(0.05)).setScale(0, RoundingMode.HALF_UP);
//		System.out.println("premiumWPB : "+premiumWPB.toString());
		premiumWPB = premiumWPB.multiply(new BigDecimal(occuLoading)).setScale(0, RoundingMode.HALF_UP);
		return premiumWPB;
	}

	@Override
	public BigDecimal calculateARTMWPB(QuotationQuickCalResponse calResp, Double occuloading) throws Exception {
		// ((@Contribution@*5)/100))
		BigDecimal premiumWPB = new BigDecimal(0);
		premiumWPB = ((new BigDecimal(calResp.getBasicSumAssured()).multiply(new BigDecimal(5))).divide(new BigDecimal(100))).setScale(0, RoundingMode.HALF_UP);
		premiumWPB = premiumWPB.multiply(new BigDecimal(occuloading)).setScale(0, RoundingMode.HALF_UP);
		return premiumWPB;
	}

}
