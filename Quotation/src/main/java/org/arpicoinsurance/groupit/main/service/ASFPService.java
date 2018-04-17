package org.arpicoinsurance.groupit.main.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;

public interface ASFPService {
	
	BigDecimal calculateL10(int ocu,int age, int term, double rebate, Date chedat, double msfb, int paytrm, QuotationQuickCalResponse calResp)throws Exception;
	
	BigDecimal calculateL2(int term, double msfb)throws Exception;

	QuotationQuickCalResponse getCalcutatedAsfp(QuotationCalculation calculation)throws Exception;

	HashMap<String, Object> saveQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation, Integer id)throws Exception;
	
	HashMap<String, Object> editQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation,Integer userId,Integer qdId)throws Exception;

}
