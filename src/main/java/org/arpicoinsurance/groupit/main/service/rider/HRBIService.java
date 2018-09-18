package org.arpicoinsurance.groupit.main.service.rider;

import java.math.BigDecimal;
import java.util.Date;


public interface HRBIService {

	BigDecimal calculateHRBI (Integer age, Integer term, String sex, Double ridsumasu, Date chedat, String payFrequency, Double relief, double occupation_loding) throws Exception;
}
