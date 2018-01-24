package org.arpicoinsurance.groupit.main.service;

import java.math.BigDecimal;
import java.util.Date;

public interface AIBService {
	
	BigDecimal calculateAIBMaturaty(Integer term, Double adbrat, Double fundmarat, Double fundrat, Double contribution, Date chedat, String paymod)throws Exception;
	
}
