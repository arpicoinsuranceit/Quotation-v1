package org.arpicoinsurance.groupit.main.service;

import java.math.BigDecimal;
import java.util.Date;

import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;

public interface ASIPService {

	QuotationQuickCalResponse getCalcutatedASIP(QuotationCalculation quotationCalculation) throws Exception;

	BigDecimal calculateL2(int ocu, int term, double bassum, int paytrm) throws Exception;


	BigDecimal calculateMaturity(int age, int term, double fundcharat, double intrat, Date chedat, double bassum,
			double bsapremium, int paytrm) throws Exception;
	
	String saveQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation,Integer id) throws Exception;

	String editQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation,Integer userId,Integer qdId) throws Exception;

}
