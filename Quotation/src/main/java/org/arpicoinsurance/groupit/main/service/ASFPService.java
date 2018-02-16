package org.arpicoinsurance.groupit.main.service;

import java.math.BigDecimal;
import java.util.Date;

import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;

public interface ASFPService {
	
	BigDecimal calculateL10(int ocu,int age, int term, double rebate, Date chedat, double msfb, int paytrm)throws Exception;
	
	BigDecimal calculateL2(int term, double msfb)throws Exception;

	QuotationQuickCalResponse getCalcutatedAsfp(QuotationCalculation calculation)throws Exception;

	String saveQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation, Integer id)throws Exception;
	
	String editQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation,Integer userId,Integer qdId)throws Exception;

}
