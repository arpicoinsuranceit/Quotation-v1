package org.arpicoinsurance.groupit.main.service.rider;

import java.math.BigDecimal;
import java.util.Date;

public interface TPDDTAPLService {
	
	BigDecimal calculateTPDDTAPL(int age, int term, double intrat, String sex, Date chedat, double loanamt, double occupation_lodings)throws Exception;

}
