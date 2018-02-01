package org.arpicoinsurance.groupit.main.service;

import java.math.BigDecimal;
import java.util.Date;

import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuoEndCalResp;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;

public interface ENDService {
	
	BigDecimal calculateL2(int ocu,int age, int term, double rebate, Date chedat, double bassum, int paytrm)throws Exception;
	
	BigDecimal calculateMaturity(int term, double bassum)throws Exception;

	QuoEndCalResp getCalcutatedEnd(QuotationCalculation calculation)throws Exception;
	
	String saveQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation,Integer id) throws Exception;

}
