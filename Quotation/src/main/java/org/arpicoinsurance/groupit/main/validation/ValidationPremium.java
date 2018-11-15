package org.arpicoinsurance.groupit.main.validation;

import org.springframework.stereotype.Component;

@Component
public class ValidationPremium {

	public String validateEnd(String frequency, Double premium) {
		//System.out.println(frequency + "                    " + premium);
		String val = "ok";

		String error = "<table style=\"width: 100%\"><tr><td style=\"text-align: left\">Minimum Monthly total premium </td><td> : </td><td style=\"text-align: right\"> 2,000.00 </td></tr>"
				+ "<tr><td style=\"text-align: left\">Minimum Quarterly total premium </td><td> : </td><td style=\"text-align: right\"> 6,000.00 </td></tr>"
				+ "<tr><td style=\"text-align: left\">Minimum Half Yearly total premium </td><td> : </td><td style=\"text-align: right\"> 12,000.00 </td></tr>"
				+ "<tr><td style=\"text-align: left\">Minimum Yearly premium </td><td> : </td><td style=\"text-align: right\"> 24,000.00 </td></tr></table";

		switch (frequency) {
		case "M":
			if (premium < 2000) {
				val = error;
			}
			break;
		case "Q":
			if (premium < 6000) {
				val = error;
			}
			break;
		case "H":
			if (premium < 12000) {
				val = error;
			}
			break;
		case "Y":
			if (premium < 24000) {
				val = error;
			}

			break;

		default:

			//System.out.println(frequency + "                    " + premium);
			break;
		}

		return val;
	}

	public String validateARP(String frequency, Double premium) {
		System.out.println(frequency + "                    " + premium);
		String val = "ok";

		String error = "<table style=\"width: 100%\"><tr><td style=\"text-align: left\">Minimum Monthly total premium </td><td> : </td><td style=\"text-align: right\"> 3,000.00 </td></tr>"
				+ "<tr><td style=\"text-align: left\">Minimum Quarterly total premium </td><td> : </td><td style=\"text-align: right\"> 9,000.00 </td></tr>"
				+ "<tr><td style=\"text-align: left\">Minimum Half Yearly total premium </td><td> : </td><td style=\"text-align: right\"> 18,000.00 </td></tr>"
				+ "<tr><td style=\"text-align: left\">Minimum Yearly total premium </td><td> : </td><td style=\"text-align: right\"> 36,000.00 </td></tr>"
				+ "<tr><td style=\"text-align: left\">Minimum Single total premium </td><td> : </td><td style=\"text-align: right\"> 250,000.00 </td></tr></table";

		switch (frequency) {
		case "M":
			
			if (premium < 3000) {
				val = error;
			}
			break;
		case "Q":
			
			if (premium < 9000) {
				val = error;
			}
			break;
		case "H":
			
			if (premium < 18000) {
				val = error;
			}
			break;
		case "Y":
			
			if (premium < 36000) {
				val = error;
			}
			
			break;
			
		case "S":
			
			if (premium < 250000) {
				val = error;
			}

			break;
		default:
			//System.out.println(frequency + "                    " + premium);
			break;
		}
		
		System.out.println(val);

		return val;
	}

	public String validateASFP(String frequency, Double premium) {
		//System.out.println(frequency + "                    " + premium);
		String val = "ok";

		String error = "<table style=\"width: 100%\"><tr><td style=\"text-align: left\">Minimum Monthly total premium </td><td> : </td><td style=\"text-align: right\"> 2,000.00 </td></tr>"
				+ "<tr><td style=\"text-align: left\">Minimum Quarterly total premium </td><td> : </td><td style=\"text-align: right\"> 6,000.00 </td></tr>"
				+ "<tr><td style=\"text-align: left\">Minimum Half Yearly total premium </td><td> : </td><td style=\"text-align: right\"> 12,000.00 </td></tr>"
				+ "<tr><td style=\"text-align: left\">Minimum Yearly total premium </td><td> : </td><td style=\"text-align: right\"> 24,000.00 </td></tr></table";

		switch (frequency) {
		case "M":
			if (premium < 2000) {
				val = error;
			}
			break;
		case "Q":
			if (premium < 6000) {
				val = error;
			}
			break;
		case "H":
			if (premium < 12000) {
				val = error;
			}
			break;
		case "Y":
			if (premium < 24000) {
				val = error;
			}

			break;
		default:
			//System.out.println(frequency + "                    " + premium);
			break;
		}

		return val;
	}

	public String validateInvp(String frequency, Double premium) {
		//System.out.println(frequency + "                    " + premium);
		String val = "ok";

		String error = "<table style=\"width: 100%\"><tr><td style=\"text-align: left\">Minimum Monthly total premium </td><td> : </td><td style=\"text-align: right\"> 2,000.00 </td></tr>"
				+ "<tr><td style=\"text-align: left\">Minimum Quarterly total premium </td><td> : </td><td style=\"text-align: right\"> 6,000.00 </td></tr>"
				+ "<tr><td style=\"text-align: left\">Minimum Half Yearly total premium </td><td> : </td><td style=\"text-align: right\"> 12,000.00 </td></tr>"
				+ "<tr><td style=\"text-align: left\">Minimum Yearly premium </td><td> : </td><td style=\"text-align: right\"> 24,000.00 </td></tr></table";

		switch (frequency) {
		case "M":
			if (premium < 2000) {
				val = error;
			}
			break;
		case "Q":
			if (premium < 6000) {
				val = error;
			}
			break;
		case "H":
			if (premium < 12000) {
				val = error;
			}
			break;
		case "Y":
			if (premium < 24000) {
				val = error;
			}

			break;

		default:

			//System.out.println(frequency + "                    " + premium);
			break;
		}

		return val;
	}

	public String validateAtrm(String frequency, Double premium) {
		//System.out.println(frequency + "                    " + premium);
		String val = "ok";

		String error = "<table style=\"width: 100%\"><tr><td style=\"text-align: left\">Minimum Monthly total premium </td><td> : </td><td style=\"text-align: right\"> 2,000.00 </td></tr>"
				+ "<tr><td style=\"text-align: left\">Minimum Quarterly total premium </td><td> : </td><td style=\"text-align: right\"> 6,000.00 </td></tr>"
				+ "<tr><td style=\"text-align: left\">Minimum Half Yearly total premium </td><td> : </td><td style=\"text-align: right\"> 12,000.00 </td></tr>"
				+ "<tr><td style=\"text-align: left\">Minimum Yearly premium </td><td> : </td><td style=\"text-align: right\"> 24,000.00 </td></tr></table";

		switch (frequency) {
		case "M":
			if (premium < 2000) {
				val = error;
			}
			break;
		case "Q":
			if (premium < 6000) {
				val = error;
			}
			break;
		case "H":
			if (premium < 12000) {
				val = error;
			}
			break;
		case "Y":
			if (premium < 24000) {
				val = error;
			}

			break;

		default:

			//System.out.println(frequency + "                    " + premium);
			break;
		}

		return val;
	}

	public String validateAsip(String frequency, Double premium) {
		//System.out.println(frequency + "                    " + premium);
		String val = "ok";

		String error = "<table style=\"width: 100%\"><tr><td style=\"text-align: left\">Minimum Monthly total premium </td><td> : </td><td style=\"text-align: right\"> 2,000.00 </td></tr>"
				+ "<tr><td style=\"text-align: left\">Minimum Quarterly total premium </td><td> : </td><td style=\"text-align: right\"> 6,000.00 </td></tr>"
				+ "<tr><td style=\"text-align: left\">Minimum Half Yearly total premium </td><td> : </td><td style=\"text-align: right\"> 12,000.00 </td></tr>"
				+ "<tr><td style=\"text-align: left\">Minimum Yearly premium </td><td> : </td><td style=\"text-align: right\"> 24,000.00 </td></tr></table";

		switch (frequency) {
		case "M":
			if (premium < 2000) {
				val = error;
			}
			break;
		case "Q":
			if (premium < 6000) {
				val = error;
			}
			break;
		case "H":
			if (premium < 12000) {
				val = error;
			}
			break;
		case "Y":
			if (premium < 24000) {
				val = error;
			}

			break;

		default:

			//System.out.println(frequency + "                    " + premium);
			break;
		}

		return val;
	}

	public String validateArtm(String frequency, Double premium) {
		//System.out.println(frequency + "                    " + premium);
		String val = "ok";

		String error = "<table style=\"width: 100%\"><tr><td style=\"text-align: left\">Minimum Monthly total premium </td><td> : </td><td style=\"text-align: right\"> 3,000.00 </td></tr>"
				+ "<tr><td style=\"text-align: left\">Minimum Quarterly total premium </td><td> : </td><td style=\"text-align: right\"> 9,000.00 </td></tr>"
				+ "<tr><td style=\"text-align: left\">Minimum Half Yearly total premium </td><td> : </td><td style=\"text-align: right\"> 18,000.00 </td></tr>"
				+ "<tr><td style=\"text-align: left\">Minimum Yearly total premium </td><td> : </td><td style=\"text-align: right\"> 36,000.00 </td></tr>"
				+ "<tr><td style=\"text-align: left\">Minimum Single premium </td><td> : </td><td style=\"text-align: right\"> 250,000.00 </td></tr></table";

		switch (frequency) {
		case "M":
			if (premium < 3000) {
				val = error;
			}
			break;
		case "Q":
			if (premium < 9000) {
				val = error;
			}
			break;
		case "H":
			if (premium < 18000) {
				val = error;
			}
			break;
		case "Y":
			if (premium < 36000) {
				val = error;
			}

			break;
		case "S":
			if (premium < 250000) {
				val = error;
			}

			break;

		default:

			//System.out.println(frequency + "                    " + premium);
			break;
		}

		return val;
	}

	public String validateAip(String frequency, Double premium, Integer age) {
		//System.out.println(frequency + "                    " + premium);
		String val = "ok";

		String error = "<table style=\"width: 100%\"><tr><td style=\"text-align: left\">Minimum Monthly total premium </td><td> : </td><td style=\"text-align: right\"> 3,000.00 </td></tr>"
				+ "<tr><td style=\"text-align: left\">Minimum Quarterly total premium </td><td> : </td><td style=\"text-align: right\"> 5,000.00 </td></tr>"
				+ "<tr><td style=\"text-align: left\">Minimum Half Yearly total premium </td><td> : </td><td style=\"text-align: right\"> 15,000.00 </td></tr>"
				+ "<tr><td style=\"text-align: left\">Minimum Yearly total premium </td><td> : </td><td style=\"text-align: right\"> 36,000.00 </td></tr>"
				+ "<tr><td style=\"text-align: left\">Minimum Single premium </td><td> : </td><td style=\"text-align: right\"> 50,000.00 </td></tr>"
				+ "<tr><td style=\"text-align: left\">Minimum Single premium (45+) </td><td> : </td><td style=\"text-align: right\"> 100,000.00 </td></tr></table";

		switch (frequency) {
		case "M":
			if (premium < 3000) {
				val = error;
			}
			break;
		case "Q":
			if (premium < 5000) {
				val = error;
			}
			break;
		case "H":
			if (premium < 15000) {
				val = error;
			}
			break;
		case "Y":
			if (premium < 36000) {
				val = error;
			}

			break;
		case "S":
			if (age >= 45) {
				if (premium < 100000) {
					val = error;
				}
			} else {
				if (premium < 50000) {
					val = error;
				}
			}

			break;

		default:

			//System.out.println(frequency + "                    " + premium);
			break;
		}

		return val;
	}

}
