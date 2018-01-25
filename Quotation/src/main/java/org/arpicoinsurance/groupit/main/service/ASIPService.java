package org.arpicoinsurance.groupit.main.service;

import java.math.BigDecimal;
import java.util.Date;

import org.arpicoinsurance.groupit.main.helper.QuoInvpCalResp;
import org.arpicoinsurance.groupit.main.helper.QuotationInvpCalculation;

public interface ASIPService {
	
QuoInvpCalResp getCalcutatedInvp(QuotationInvpCalculation quotationInvpCalculation) throws Exception; 
	
	BigDecimal calculateL2(int term, double bassum, int paytrm)throws Exception;
	
	Double addRebatetoBSAPremium(double rebate, BigDecimal premium) throws Exception;
	
	BigDecimal calculateMaturity(int age, int term, double fundcharat, double intrat, Date chedat, double bassum, double bsapremium, int paytrm)throws Exception;

}
