package org.arpicoinsurance.groupit.main.service;

import java.math.BigDecimal;
import java.util.Date;

import org.arpicoinsurance.groupit.main.helper.QuoCalResp;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;

public interface AIPService {

	QuoCalResp getCalcutatedAip(QuotationCalculation quotationCalculation) throws Exception; 
	
	BigDecimal calculateAIPMaturaty(Integer term, Double adbrat, Double fundmarat, Double intrat, Double contribution, Date chedat, String paymod, boolean schedule)throws Exception;
}

