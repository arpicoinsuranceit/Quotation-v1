package org.arpicoinsurance.groupit.main.service.rider;

import java.math.BigDecimal;
import java.util.Date;

public interface JLBPLService {
	
	BigDecimal calculateJLBPL(int age, int term, double intrat, String sex, Date chedat, double loanamt, double occupation_loding)throws Exception;

}
