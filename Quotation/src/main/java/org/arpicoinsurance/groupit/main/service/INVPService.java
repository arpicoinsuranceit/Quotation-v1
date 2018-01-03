package org.arpicoinsurance.groupit.main.service;

import java.math.BigDecimal;
import java.util.Date;

public interface INVPService {
	
	BigDecimal calculateL2(int age, int term, double intrat, Date chedat, double bassum, int paytrm)throws Exception;
	
	Double addRebatetoBSAPremium(double rebate, BigDecimal premium);
	
	BigDecimal calculateMaturity(int age, int term, double intrat, Date chedat, double bassum, int paytrm)throws Exception;

}
