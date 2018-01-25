package org.arpicoinsurance.groupit.main.service.rider;

import java.math.BigDecimal;

import org.arpicoinsurance.groupit.main.helper.QuoInvpCalResp;

public interface WPBService {
	
	BigDecimal calculateWPB(QuoInvpCalResp calResp) throws Exception;
	
}
