package org.arpicoinsurance.groupit.main.service.custom;

import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.model.Occupation;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;

public interface CalculateRiders {
	
	QuotationQuickCalResponse getRiders(QuotationCalculation quotationCalculation, QuotationQuickCalResponse calResp) throws Exception;
	
	QuotationQuickCalResponse calculateMainlifeRiders(Integer age, String type, Integer payTerm, Double bsa, String gender, String frequance, Integer ocu, QuotationQuickCalResponse calResp,
		 Integer adultCount, Integer childCount, Double inrate, String productCode, Occupation occupation) throws Exception;
	
	QuotationQuickCalResponse calculateBenifPremium(String type, Double ridsumasu, String gender, Integer age, String payFrequency,
			Integer term, Integer occupation_id, QuotationQuickCalResponse calResp, Integer adultCount, Integer childCount, Double loan, Double inRate,
			String productCode, Occupation occupation)
			throws Exception ;
	
	
}
