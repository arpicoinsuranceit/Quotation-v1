package org.arpicoinsurance.groupit.main.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.helper.SurrenderValHelper;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;

public interface ARPService {
	
	BigDecimal calculateL2(int ocu, int age, int term, String rlfterm, double rebate, Date chedat, double bassum, String payFrequency, QuotationQuickCalResponse calResp, boolean isAddOccuLoading)throws Exception;
	
	BigDecimal calculateMaturity(int term, double bassum) throws Exception;
	
	List<SurrenderValHelper> calculateSurrendervals(int age, int term, String rlfterm, double bassum, String payFrequency, double total_premium) throws Exception;
	
	QuotationQuickCalResponse getCalcutatedArp (QuotationCalculation calculation) throws Exception;
	
	HashMap<String, Object> saveQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation,Integer id) throws Exception;

	HashMap<String, Object> editQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation, Integer userId, Integer qdId) throws Exception;

}
