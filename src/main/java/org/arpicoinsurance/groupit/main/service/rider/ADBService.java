package org.arpicoinsurance.groupit.main.service.rider;

import java.math.BigDecimal;

public interface ADBService {
	
	BigDecimal calculateADB(double ridsumasu, String payFrequency, double relief, double occupation_loding)throws Exception;
	
}
