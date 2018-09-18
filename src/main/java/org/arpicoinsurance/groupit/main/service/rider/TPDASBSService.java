package org.arpicoinsurance.groupit.main.service.rider;

import java.math.BigDecimal;
import java.util.Date;

public interface TPDASBSService {
	BigDecimal calculateTPDASBS(int age, int term, Date chedat, double ridsumasu, String payFrequency, double relief, double occupation_loding)throws Exception;
}
