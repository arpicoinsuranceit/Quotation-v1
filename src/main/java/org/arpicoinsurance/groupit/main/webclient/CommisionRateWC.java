package org.arpicoinsurance.groupit.main.webclient;

import java.util.HashMap;

import org.arpicoinsurance.groupit.main.helper.CommisionRatePara;

public interface CommisionRateWC {
	
	HashMap<String, Double> getCommisionRate(CommisionRatePara commisionRatePara) throws Exception;

}
