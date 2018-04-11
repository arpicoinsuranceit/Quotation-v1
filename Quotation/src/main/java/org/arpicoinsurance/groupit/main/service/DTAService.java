package org.arpicoinsurance.groupit.main.service;

import java.util.Date;

import org.arpicoinsurance.groupit.main.helper.DTAHelper;
import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;

public interface DTAService {
	
	DTAHelper calculateL2(int ocu, int age, int term, double intrat, String sex, Date chedat, double loanamt, QuotationQuickCalResponse calResp)throws Exception;

	QuotationQuickCalResponse getCalcutatedDta(QuotationCalculation quotationCalculation) throws Exception; 
	
	String saveQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation,Integer id) throws Exception;

	String editQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation, Integer userId,
			Integer qdId) throws Exception;

}
