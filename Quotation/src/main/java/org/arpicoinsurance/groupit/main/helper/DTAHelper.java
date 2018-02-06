package org.arpicoinsurance.groupit.main.helper;

import java.math.BigDecimal;
import java.util.ArrayList;

public class DTAHelper {
	private BigDecimal bsa;
	private ArrayList<DTAShedule> dtaSheduleList;
	
	public BigDecimal getBsa() {
		return bsa;
	}
	public void setBsa(BigDecimal bsa) {
		this.bsa = bsa;
	}
	public ArrayList<DTAShedule> getDtaSheduleList() {
		return dtaSheduleList;
	}
	public void setDtaSheduleList(ArrayList<DTAShedule> dtaSheduleList) {
		this.dtaSheduleList = dtaSheduleList;
	}
}
