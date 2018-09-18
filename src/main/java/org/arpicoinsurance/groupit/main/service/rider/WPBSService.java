package org.arpicoinsurance.groupit.main.service.rider;

import java.math.BigDecimal;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;

public interface WPBSService {

	BigDecimal calculateWPBS(QuotationQuickCalResponse calResp, Double Occuloading) throws Exception;
	
	
	
	
}
