package org.arpicoinsurance.groupit.main.service.custom;

import java.math.BigDecimal;

import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.model.Benefits;
import org.arpicoinsurance.groupit.main.model.Occupation;

public interface OccupationLoadingService {
	
	BigDecimal calculateOccupationLoading(boolean addOccuLoad, Double premium, Double bsa, Occupation occupation, Benefits benefictId, QuotationQuickCalResponse calResp) throws Exception;
	
}
