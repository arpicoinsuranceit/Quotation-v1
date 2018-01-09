package org.arpicoinsurance.groupit.main.service.rider;

import java.math.BigDecimal;

import org.arpicoinsurance.groupit.main.helper.QuoCalResp;

public interface WPBSService {

	BigDecimal calculateWPBS(QuoCalResp calResp) throws Exception;
	
}
