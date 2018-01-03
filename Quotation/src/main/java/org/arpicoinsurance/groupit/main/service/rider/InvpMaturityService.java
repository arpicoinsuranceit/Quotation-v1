package org.arpicoinsurance.groupit.main.service.rider;

import java.math.BigDecimal;
import java.util.Date;

public interface InvpMaturityService {
	
	BigDecimal calculateInvpMaturaty(Integer age, Integer term, Double intrat, Date chedat, Double bassum, Integer paytrm)throws Exception;


}
