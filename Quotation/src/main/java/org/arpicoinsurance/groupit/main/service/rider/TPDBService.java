package org.arpicoinsurance.groupit.main.service.rider;

import java.math.BigDecimal;

public interface TPDBService {
	BigDecimal calculateTPDB(double ridsumasu, String payFrequency, double relief) throws Exception;
}
