package org.arpicoinsurance.groupit.main.service;

import java.math.BigDecimal;
import java.util.Date;

import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;

public interface INVPService {
	
	QuotationQuickCalResponse getCalcutatedInvp(QuotationCalculation quotationCalculation) throws Exception; 
	
	BigDecimal calculateL2(int ocu,int age, int term, double intrat, Date chedat, double bassum, int paytrm)throws Exception;
	
	BigDecimal calculateMaturity(int age, int term, double intrat, Date chedat, double bassum, int paytrm)throws Exception;
	
	BigDecimal getInvestLifePremium(int age, int term, Date chedat, double bassum, double premium, int paytrm)throws Exception;
	
	

	String saveQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation,Integer id) throws Exception;
	
	String editQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation,Integer userId,Integer qdId) throws Exception;

	
}
