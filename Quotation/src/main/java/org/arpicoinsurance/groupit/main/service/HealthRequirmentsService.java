package org.arpicoinsurance.groupit.main.service;

import java.util.HashMap;
import java.util.List;

import org.arpicoinsurance.groupit.main.helper.MediTestReceiptHelper;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;

public interface HealthRequirmentsService {

	HashMap<String, Object> getSumAtRiskDetailsMainLife(QuotationCalculation calculation);
	HashMap<String, Object> getSumAtRiskDetailsSpouse(QuotationCalculation calculation);
	
	List<MediTestReceiptHelper> getMediTestByQuoDetails(Integer QuId) throws Exception;

}
