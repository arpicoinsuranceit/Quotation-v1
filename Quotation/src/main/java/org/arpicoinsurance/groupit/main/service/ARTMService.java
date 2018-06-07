package org.arpicoinsurance.groupit.main.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import org.arpicoinsurance.groupit.main.helper.AIPCalResp;
import org.arpicoinsurance.groupit.main.helper.InvpSavePersonalInfo;
import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.Plan;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;

public interface ARTMService {
	
	/*AIPCalResp calculateARTMMaturaty(Plan plan, Double intrat, boolean shedule, boolean isAddOccuLoading)throws Exception;
	
	HashMap<String, Object> saveQuotation(InvpSavePersonalInfo _invpSaveQuotation, Integer id) throws Exception;

	HashMap<String, Object> editQuotation(InvpSavePersonalInfo _invpSaveQuotation, Integer userId, Integer qdId) throws Exception;
	*/
	
	BigDecimal pensionPremium()throws Exception;
	
	BigDecimal calculateMaturity(boolean printShedule, QuotationQuickCalResponse calResp)throws Exception;

	QuotationQuickCalResponse getCalcutatedARTM(QuotationCalculation calculation, boolean printShedule)throws Exception;
	
	HashMap<String, Object> saveQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation,Integer id) throws Exception;

	HashMap<String, Object> editQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation, Integer userId,
			Integer qdId)throws Exception;
	
	
}

