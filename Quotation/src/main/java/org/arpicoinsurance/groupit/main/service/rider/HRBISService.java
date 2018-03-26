package org.arpicoinsurance.groupit.main.service.rider;

import java.math.BigDecimal;
import java.util.Date;

public interface HRBISService {

	BigDecimal calculateHRBCIS (Integer age, Integer term, String sex, Double ridsumasu, Integer adlcnt, Integer chlcnt, Date chedat, String payFrequency, Double relief, double occupation_loding) throws Exception;
}
