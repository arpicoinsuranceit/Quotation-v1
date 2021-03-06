package org.arpicoinsurance.groupit.main.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

@Component
public class CalculationUtils {

	public int getPayterm(String payFrequency) {
		switch (payFrequency) {
		case "M":
			return 12;
		case "Q":
			return 4;
		case "H":
			return 2;
		case "Y":
			return 1;
		case "S":
			return 1;
		default:
			return 0;
		}
	}

	public double getRebate(String payFrequency) {
		switch (payFrequency) {
		case "M":
			return 0.0;
		case "Q":
			return 2.0;
		case "H":
			return 4.0;
		case "Y":
			return 6.0;
		case "S":
			return 6.0;
		default:
			return 0.0;
		}
	}

	public double getRebate(int term, String payFrequency) {
		switch (payFrequency) {
		case "M":
			return 0.0;
		case "Q":
			return 1.0;
		case "H":
			return 2.0;
		case "Y":
			return 4.0;
		case "S":
			switch (term) {
			case 10:
				return 10.0;
			case 15:
				return 20.0;
			case 20:
				return 25.0;
			default:
				return 0.0;
			}
		default:
			return 0;
		}
	}

	public double getAdminFee(String payFrequency) {
		switch (payFrequency) {
		case "M":
			return 10.0;
		case "Q":
			return 20.0;
		case "H":
			return 32.0;
		case "Y":
			return 50.0;
		case "S":
			return 450.0;
		default:
			return 0;
		}
	}

	public double getTaxAmount(double bassum) {
		// //System.out.println(bassum + "bassum at tax");
		return new BigDecimal(0.002).multiply(new BigDecimal(bassum)).setScale(0, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
	}

	public Double getPolicyFee() {
		return 300.00;
	}

	public Double addRebatetoBSAPremium(double rebate, BigDecimal premium) throws Exception {

		BigDecimal rebateRate = (new BigDecimal(1)
				.subtract((new BigDecimal(rebate).divide(new BigDecimal(100), 6, RoundingMode.HALF_UP))));

		premium = premium.multiply(rebateRate).setScale(0, RoundingMode.HALF_UP);
		return premium.doubleValue();
	}

	public Double getFndMngRate(Double contribution, String paymod) {
		if (paymod.equalsIgnoreCase("S")) {
			if (contribution > 2000000) {
				return 0.05;
			} else if (contribution > 1000000) {
				return 0.08;
			} else {
				return 0.1;
			}
		} else {
			return 0.2;
		}

	}

	public String getPhoneNo(String phone) throws Exception {
		if (phone.length() == 9) {
			phone = "0" + phone;
			return phone;
		} else if (phone.length() == 10) {
			return phone;
		} else {
			return "Error";
		}
	}

}
