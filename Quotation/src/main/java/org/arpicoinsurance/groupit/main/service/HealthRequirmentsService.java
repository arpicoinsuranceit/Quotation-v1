package org.arpicoinsurance.groupit.main.service;

import java.util.HashMap;

import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;

public interface HealthRequirmentsService {

	HashMap<String, Object> getSumAtRiskDetails(QuotationCalculation calculation, Double previous, String custCode);

}
