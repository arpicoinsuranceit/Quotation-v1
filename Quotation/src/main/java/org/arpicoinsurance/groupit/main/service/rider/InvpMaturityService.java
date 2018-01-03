package org.arpicoinsurance.groupit.main.service;

import java.math.BigDecimal;
import java.util.Date;

public interface L3Service {
	
	BigDecimal calculateL3(int age, int term, double intrat, Date chedat, double bassum, int paytrm)throws Exception;


}
