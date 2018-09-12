package org.arpicoinsurance.groupit.main.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;

public interface ATRMService {

	BigDecimal calculateL2(int ocu,int age, int term, double rebate, Date chedat, double bassum, int paytrm, QuotationQuickCalResponse calResp, boolean isAddOccuLoading)throws Exception;
	
	QuotationQuickCalResponse getCalcutatedAtrm(QuotationCalculation calculation)throws Exception;
	
	HashMap<String, Object> saveQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation,Integer id) throws Exception;

	HashMap<String, Object> editQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation, Integer userId,
			Integer qdId, Integer type) throws Exception;
	
}
