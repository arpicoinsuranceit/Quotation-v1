package org.arpicoinsurance.groupit.main.service;

import java.math.BigDecimal;
import java.util.Date;

import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuoInvpCalResp;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;

public interface ASIPService {

	QuoInvpCalResp getCalcutatedASIP(QuotationCalculation quotationCalculation) throws Exception;

	BigDecimal calculateL2(int ocu, int term, double bassum, int paytrm) throws Exception;

	Double addRebatetoBSAPremium(double rebate, BigDecimal premium) throws Exception;

	BigDecimal calculateMaturity(int age, int term, double fundcharat, double intrat, Date chedat, double bassum,
			double bsapremium, int paytrm) throws Exception;
	
	String saveQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation,Integer id) throws Exception;


}
