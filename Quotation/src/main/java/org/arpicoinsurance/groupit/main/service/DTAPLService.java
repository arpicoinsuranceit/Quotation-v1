package org.arpicoinsurance.groupit.main.service;

import java.util.Date;
import java.util.HashMap;

import org.arpicoinsurance.groupit.main.helper.DTAHelper;
import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;

public interface DTAPLService {

	DTAHelper calculateL2(int ocu, int age, int term, double intrat, String sex, Date chedat, double loanamt, QuotationQuickCalResponse calResp, boolean isAddOccuLoading) throws Exception;

	QuotationQuickCalResponse getCalcutatedDta(QuotationCalculation quotationCalculation) throws Exception;

	HashMap<String, Object> saveQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation, Integer id)
			throws Exception;

	HashMap<String, Object> editQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation, Integer userId,
			Integer qdId, Integer type) throws Exception;
}
