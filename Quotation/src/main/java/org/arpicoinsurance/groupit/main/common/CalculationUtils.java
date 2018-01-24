package org.arpicoinsurance.groupit.main.common;

import java.math.BigDecimal;

public class CalculationUtils {
	
	public int getPayterm(String payFrequency) {
		switch(payFrequency) {
	        case "M" :
	        	return 12;
	        case "Q" :
	        	return 4;
	        case "H" :
	        	return 2;
	        case "Y" :
	        	return 1;
	        case "S" :
	        	return 1;
	        default :
	        	return 0;
		}
	}
	
	public double getRebate(String payFrequency) {
		switch(payFrequency) {
	        case "M" :
	        	return 0.0;
	        case "Q" :
	        	return 2.0;
	        case "H" :
	        	return 4.0;
	        case "Y" :
	        	return 6.0;
	        case "S" :
	        	return 6.0;
	        default :
	        	return 0.0;
		}
	}
	
	public double getRebate(int term, String payFrequency) {
		switch(payFrequency) {
	        case "M" :
	        	return 0.0;
	        case "Q" :
	        	return 1.0;
	        case "H" :
	        	return 2.0;
	        case "Y" :
	        	return 4.0;
	        case "S" :
	        	switch(term) {
	        		case 10 :
	        			return 10.0;
	        		case 15 :
	        			return 20.0;
	        		case 20 :
	        			return 25.0;
	        		default :
	    	        	return 0.0;
	        	}	        	
	        default :
	        	return 0;
		}
	}
	
	public double getAdminFee(String payFrequency) {
		switch(payFrequency) {
	        case "M" :
	        	return 10.0;
	        case "Q" :
	        	return 20.0;
	        case "H" :
	        	return 32.0;
	        case "Y" :
	        	return 50.0;
	        case "S" :
	        	return 450.0;	        	
	        default :
	        	return 0;
		}
	}
	
	public double getTaxAmount(double bassum) {
		return new BigDecimal(0.002).multiply(new BigDecimal(bassum)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	
	
}
