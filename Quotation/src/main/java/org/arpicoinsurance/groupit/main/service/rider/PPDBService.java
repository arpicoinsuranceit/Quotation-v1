package org.arpicoinsurance.groupit.main.service.rider;

import java.math.BigDecimal;

public interface PPDBService {
	BigDecimal calculatePPDB(double ridsumasu, String payFrequency, double relief)throws Exception;
	BigDecimal calculatePPDBS(double ridsumasu, String payFrequency, double relief)throws Exception;
}
