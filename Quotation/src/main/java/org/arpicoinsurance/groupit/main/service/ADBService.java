package org.arpicoinsurance.groupit.main.service;

import java.math.BigDecimal;

public interface ADBService {
	
	BigDecimal calculateADB(double ridsumasu, String payFrequency, double relief)throws Exception;
	BigDecimal calculateADBS(double ridsumasu, String payFrequency, double relief)throws Exception;

}
