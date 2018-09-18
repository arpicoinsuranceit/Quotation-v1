package org.arpicoinsurance.groupit.main.service.rider;

import java.math.BigDecimal;

public interface L2Service {
	BigDecimal calculateL2(double ridsumasu, int term, int age, String payFrequency, double occupation_loding)throws Exception;
}
