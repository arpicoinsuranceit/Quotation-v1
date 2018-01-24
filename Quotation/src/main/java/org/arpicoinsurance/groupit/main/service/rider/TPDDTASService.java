package org.arpicoinsurance.groupit.main.service.rider;

import java.math.BigDecimal;
import java.util.Date;

public interface TPDDTASService {
	
	BigDecimal calculateTPDDTAS(int age, int term, double intrat, String sex, Date chedat, double loanamt)throws Exception;

}
