package org.arpicoinsurance.groupit.main.service.rider;

import java.math.BigDecimal;
import java.util.Date;

public interface SUHRBSService {
	BigDecimal calculateSUHRBS(Integer age, String sex, Integer term, Double ridsumasu, Date chedat, String payFrequency, Double relief, double occupation_loding) throws Exception;
}
