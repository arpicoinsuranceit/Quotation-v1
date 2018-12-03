package org.arpicoinsurance.groupit.main.service.rider;

import java.math.BigDecimal;
import java.util.Date;

public interface ATPBService {
	
	BigDecimal calculateATPB(Integer age, Integer term, Date chedat, Double ridsumasu, String payFrequency, Double relief) throws Exception;

}
