package org.arpicoinsurance.groupit.main.service.rider;

import java.math.BigDecimal;
import java.util.Date;

public interface HRBICService {

	BigDecimal calculateHRBCIC (Integer age, Integer term, String sex, Double ridsumasu, Integer adlcnt, Integer chlcnt, Date chedat, String payFrequency, Double relief, double occupation_loding) throws Exception;
	
}
