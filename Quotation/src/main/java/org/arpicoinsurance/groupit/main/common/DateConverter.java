package org.arpicoinsurance.groupit.main.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {
	
	public Date stringToDate (String stdate) {
		//System.out.println(stdate+"=========================================");
		Date initDate=null;
		try {
			initDate = new SimpleDateFormat("dd-MM-yyyy").parse(stdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//LocalDate localDate = LocalDate.parse(stdate);
		//Date date = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());;
		return initDate;
	}
}
