package org.arpicoinsurance.groupit.main.service;

import java.math.BigDecimal;
import java.util.Date;

import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;

public interface ARPService {
	
	BigDecimal calculateL2(int age, int term, String rlfterm, double rebate, Date chedat, double bassum, String payFrequency)throws Exception;
	
	BigDecimal calculateMaturity(int term, double bassum)throws Exception;
	
	QuotationQuickCalResponse getCalcutatedArp ( QuotationCalculation calculation) throws Exception;
	
	String saveQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation,Integer id) throws Exception;

	String editQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation, Integer userId, Integer qdId) throws Exception;

}
