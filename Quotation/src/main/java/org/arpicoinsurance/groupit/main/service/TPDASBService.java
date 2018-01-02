package org.arpicoinsurance.groupit.main.service;

import java.math.BigDecimal;
import java.util.Date;

public interface TPDASBService { 
	
	BigDecimal calculateTPDASB(int age, Date chedat, double ridsumasu, String payFrequency, double relief)throws Exception;
	
	BigDecimal calculateTPDASBS(int age, Date chedat, double ridsumasu, String payFrequency, double relief)throws Exception;

}
