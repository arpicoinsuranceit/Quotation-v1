package org.arpicoinsurance.groupit.main.service.rider;

import java.math.BigDecimal;

import org.arpicoinsurance.groupit.main.helper.QuoCalResp;

public interface WPBService {
	
	BigDecimal calculateWPB(QuoCalResp calResp) throws Exception;
	
}
