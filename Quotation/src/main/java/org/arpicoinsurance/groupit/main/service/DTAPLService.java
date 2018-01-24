package org.arpicoinsurance.groupit.main.service;

import java.math.BigDecimal;
import java.util.Date;

public interface DTAPLService {
	
	BigDecimal calculateL2(int age, int term, double intrat, String sex, Date chedat, double loanamt)throws Exception;
	
}
