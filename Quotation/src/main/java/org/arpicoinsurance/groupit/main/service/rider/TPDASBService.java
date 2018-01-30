package org.arpicoinsurance.groupit.main.service.rider;

import java.math.BigDecimal;
import java.util.Date;

public interface TPDASBService { 
	
	BigDecimal calculateTPDASB(int age, Date chedat, double ridsumasu, String payFrequency, double relief, double occupation_loding)throws Exception;
	


}
