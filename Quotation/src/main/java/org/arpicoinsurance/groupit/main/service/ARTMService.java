package org.arpicoinsurance.groupit.main.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.model.PensionShedule;

public interface ARTMService {
	
	/*AIPCalResp calculateARTMMaturaty(Plan plan, Double intrat, boolean shedule, boolean isAddOccuLoading)throws Exception;
	
	HashMap<String, Object> saveQuotation(InvpSavePersonalInfo _invpSaveQuotation, Integer id) throws Exception;

	HashMap<String, Object> editQuotation(InvpSavePersonalInfo _invpSaveQuotation, Integer userId, Integer qdId) throws Exception;
	*/
	
	BigDecimal pensionPremium(QuotationCalculation calculation, String reprat, Double closingFundAmount)throws Exception;
	
	BigDecimal calculateMaturity(boolean printShedule, QuotationQuickCalResponse calResp, QuotationCalculation calculation, String divRate, List<PensionShedule> pensionShedules, Integer level)throws Exception;


	QuotationQuickCalResponse getCalcutatedARTM(QuotationCalculation calculation, boolean printShedule)throws Exception;
	
	HashMap<String, Object> saveQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation,Integer id) throws Exception;

	HashMap<String, Object> editQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation, Integer userId,
			Integer qdId)throws Exception;
	
	
}

