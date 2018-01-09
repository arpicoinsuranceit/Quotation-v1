package org.arpicoinsurance.groupit.main.service.rider;

import java.math.BigDecimal;
import java.util.Date;

public interface SUHRBService {
	BigDecimal calculateSUHRB(Integer age, String sex, Integer term, Double ridsumasu, Date chedat, String payFrequency, Double relief) throws Exception;
}
