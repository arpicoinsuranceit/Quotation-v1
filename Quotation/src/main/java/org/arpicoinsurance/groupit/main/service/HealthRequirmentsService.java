package org.arpicoinsurance.groupit.main.service;

import java.util.HashMap;

import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;

public interface HealthRequirmentsService {

	HashMap<String, Object> getSumAtRiskDetailsMainLife(QuotationCalculation calculation);
	HashMap<String, Object> getSumAtRiskDetailsSpouse(QuotationCalculation calculation);

}
