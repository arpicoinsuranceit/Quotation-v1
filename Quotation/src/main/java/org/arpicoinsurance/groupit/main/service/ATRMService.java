package org.arpicoinsurance.groupit.main.service;

import java.math.BigDecimal;
import java.util.Date;
import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;

public interface ATRMService {

	BigDecimal calculateL2(int ocu,int age, int term, double rebate, Date chedat, double bassum, int paytrm)throws Exception;
	
	QuotationQuickCalResponse getCalcutatedAtrm(QuotationCalculation calculation)throws Exception;
	
	String saveQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation,Integer id) throws Exception;
	
}
