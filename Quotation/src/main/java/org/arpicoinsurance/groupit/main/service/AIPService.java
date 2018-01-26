package org.arpicoinsurance.groupit.main.service;

import java.math.BigDecimal;
import java.util.Date;

import org.arpicoinsurance.groupit.main.helper.AIPCalResp;
import org.arpicoinsurance.groupit.main.helper.QuoInvpCalResp;
import org.arpicoinsurance.groupit.main.helper.QuotationInvpCalculation;

public interface AIPService {

	QuoInvpCalResp getCalcutatedAip(QuotationInvpCalculation quotationInvpCalculation) throws Exception; 
	
	AIPCalResp calculateAIPMaturaty(Integer term, Double adbrat, Double fundmarat, Double intrat, Double contribution, Date chedat, String paymod, boolean schedule)throws Exception;
}

