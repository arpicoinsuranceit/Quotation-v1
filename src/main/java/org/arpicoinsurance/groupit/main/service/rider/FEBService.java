package org.arpicoinsurance.groupit.main.service.rider;

import java.math.BigDecimal;
import java.util.Date;

public interface FEBService {
	
	BigDecimal calculateFEB(Integer age, Integer term, Date chedat, Double ridsumasu, String payFrequency, Double relief, double occupation_loding)throws Exception;

}
