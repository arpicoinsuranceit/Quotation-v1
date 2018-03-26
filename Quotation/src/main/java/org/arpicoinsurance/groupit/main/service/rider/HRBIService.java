package org.arpicoinsurance.groupit.main.service.rider;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Service;

public interface HRBIService {

	BigDecimal calculateHRBCI (Integer age, Integer term, String sex, Double ridsumasu, Integer adlcnt, Integer chlcnt, Date chedat, String payFrequency, Double relief, double occupation_loding) throws Exception;
}
