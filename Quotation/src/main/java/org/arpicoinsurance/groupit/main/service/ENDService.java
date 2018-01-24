package org.arpicoinsurance.groupit.main.service;

import java.math.BigDecimal;
import java.util.Date;

public interface ENDService {
	
	BigDecimal calculateL2(int age, int term, double rebate, Date chedat, double bassum, int paytrm)throws Exception;
	
	BigDecimal calculateMaturity(int term, double bassum)throws Exception;

}
