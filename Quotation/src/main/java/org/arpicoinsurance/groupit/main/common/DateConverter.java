package org.arpicoinsurance.groupit.main.common;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateConverter {
	
	public Date stringToDate (String stdate) {
		System.out.println(stdate+"=========================================");
		LocalDate localDate = LocalDate.parse(stdate);
		Date date = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());;
		return date;
	}
}
