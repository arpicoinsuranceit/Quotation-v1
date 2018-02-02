package org.arpicoinsurance.groupit.main.service.rider;

import java.math.BigDecimal;
import org.arpicoinsurance.groupit.main.helper.QuoAtrmCalResp;
import org.arpicoinsurance.groupit.main.helper.QuoEndCalResp;
import org.arpicoinsurance.groupit.main.helper.QuoInvpCalResp;

public interface WPBSService {

	BigDecimal calculateWPBS(QuoInvpCalResp calResp) throws Exception;
	
	BigDecimal calculateWPBS(QuoEndCalResp calResp) throws Exception;
	
	BigDecimal calculateWPBS(QuoAtrmCalResp calResp) throws Exception;
	
}
