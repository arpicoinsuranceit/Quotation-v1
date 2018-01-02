package org.arpicoinsurance.groupit.main.service;

import java.math.BigDecimal;
import java.util.Date;

public interface ATPBService {
	
	BigDecimal calculateATPB(int age, int term, Date chedat, double ridsumasu, String payFrequency, double relief)throws Exception;

}
