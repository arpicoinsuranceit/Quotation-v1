package org.arpicoinsurance.groupit.main.service;

import java.math.BigDecimal;
import java.util.Date;

import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuoInvpCalResp;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;

public interface ARPService {
	
	BigDecimal calculateL2(int age, int term, String rlfterm, double rebate, Date chedat, double bassum, String payFrequency)throws Exception;
	
	BigDecimal calculateMaturity(int term, double bassum)throws Exception;
	
	QuoInvpCalResp getCalcutatedArp ( QuotationCalculation calculation) throws Exception;
	
	String saveQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation,Integer id) throws Exception;

}
