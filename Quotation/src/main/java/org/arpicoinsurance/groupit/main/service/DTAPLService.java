package org.arpicoinsurance.groupit.main.service;

import java.util.Date;

import org.arpicoinsurance.groupit.main.helper.DTAHelper;
import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;

public interface DTAPLService {

	DTAHelper calculateL2(int age, int term, double intrat, String sex, Date chedat, double loanamt) throws Exception;

	QuotationQuickCalResponse getCalcutatedDta(QuotationCalculation quotationCalculation) throws Exception;

	String saveQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation, Integer id)
			throws Exception;
}
