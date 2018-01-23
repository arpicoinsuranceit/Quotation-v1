package org.arpicoinsurance.groupit.main.service;

import java.math.BigDecimal;
import java.util.Date;

public interface ARPService {
	
	BigDecimal calculateL2(int age, int term, String rlfterm, double rebate, Date chedat, double bassum, String payFrequency)throws Exception;
	
	BigDecimal calculateMaturity(int term, double bassum)throws Exception;

}
