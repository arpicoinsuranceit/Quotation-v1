package org.arpicoinsurance.groupit.main.service.rider;

import java.math.BigDecimal;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;

public interface WPBService {
	
	BigDecimal calculateWPB(QuotationQuickCalResponse calResp) throws Exception;
	
	BigDecimal calculateARTMWPB(QuotationQuickCalResponse calResp) throws Exception;
	
	
}
