package org.arpicoinsurance.groupit.main.service;

import java.math.BigDecimal;
import java.util.Date;

public interface ASFPService {
	
	BigDecimal calculateL10(int age, int term, double rebate, Date chedat, double msfb, int paytrm)throws Exception;
	
	BigDecimal calculateL2(int term, double msfb)throws Exception;

}
