package org.arpicoinsurance.groupit.main.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;

public interface ENDService {
	
	BigDecimal calculateL2(int ocu,int age, int term, double rebate, Date chedat, double bassum, int paytrm, QuotationQuickCalResponse calResp, boolean isAddOccuLoading)throws Exception;
	
	BigDecimal calculateMaturity(int term, double bassum)throws Exception;

	QuotationQuickCalResponse getCalcutatedEnd(QuotationCalculation calculation)throws Exception;
	
	HashMap<String, Object> saveQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation,Integer id) throws Exception;

	HashMap<String, Object> editQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation, Integer userId,
			Integer qdId)throws Exception;

}
