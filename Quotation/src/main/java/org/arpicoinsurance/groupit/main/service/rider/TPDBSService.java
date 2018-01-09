package org.arpicoinsurance.groupit.main.service.rider;

import java.math.BigDecimal;

public interface TPDBSService {
	BigDecimal calculateTPDBS(double ridsumasu, String payFrequency, double relief) throws Exception;
}
