package org.arpicoinsurance.groupit.main.service.rider;

import java.math.BigDecimal;
import java.util.Date;

public interface HRBFService {

	BigDecimal calculateHRBF(Integer age, Integer term, Double ridsumasu, Integer adlcnt, Integer chlcnt, Date chedat, String payFrequency, Double relief) throws Exception;
	
	
}
