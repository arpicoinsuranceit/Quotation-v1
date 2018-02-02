package org.arpicoinsurance.groupit.main.validation;

import java.util.ArrayList;
import java.util.HashMap;

import org.arpicoinsurance.groupit.main.helper.Benifict;
import org.arpicoinsurance.groupit.main.helper.QuoInvpCalResp;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;

public class Validation {

	private QuotationCalculation calculation;

	HashMap<String, Benifict> benefitMap = new HashMap<>();

	public Validation(QuotationCalculation calculation) {
		this.calculation = calculation;
		loadBeneficts();

	}

	public boolean InvpPostValidation(QuoInvpCalResp calResp) {
		if (calResp.getBasicSumAssured() < 1250) {
			return false;
		} else {
			return true;
		}
	}

	public String validateBenifict() {
		if (calculation.get_riderDetails() != null) {
			if (calculation.get_riderDetails().get_mRiders() != null
					&& calculation.get_riderDetails().get_mRiders().size() > 0) {
				ArrayList<Benifict> mRiders = calculation.get_riderDetails().get_mRiders();

				for (Benifict benifict : mRiders) {
					String type = benifict.getType();
					switch (type) {
					case "ATPB":
						if (validateInvpATBP().equals(0)) {
							return "ATPB";
						}
						break;
					case "ADB":
						if (validateInvpABD().equals(0)) {
							return "ADB";
						}
						break;
					case "TPDASB":
						if (validateInvpTPDASB().equals(0)) {
							return "TPDASB";
						}
						break;
					case "TPDB":
						if (validateInvpTPDB().equals(0)) {
							return "TPDB";
						}
						break;

					case "PPDB":
						if (validateInvpPPDB().equals(0)) {
							return "PPDB";
						}
						break;
					case "CIB":
						if (validateInvpCIB().equals(0)) {
							return "CIB";
						}
						break;
					case "FEB":
						if (validateInvpFEB().equals(0)) {
							return "FEB";
						}
						break;
					case "MFIBD":
						if (validateInvpMIFBD().equals(0)) {
							return "MIFBD";
						}
						break;
					case "MFIBT":
						if (validateInvpMIFBT().equals(0)) {
							return "MIFBT";
						}
						break;
					case "MFIBDT":
						if (validateInvpMFIBDT().equals(0)) {
							return "MFIBDT";
						}
						break;
					case "HRB":
						if (validateInvpHRB().equals(0)) {
							return "HRB";
						}
						break;
					case "SUHRB":
						if (validateInvpSUHRB().equals(0)) {
							return "SUHRB";
						}
						break;
					case "HB":
						if (validateInvpHB().equals(0)) {
							return "HB";
						}
						break;

					default:
						break;
					}
				}
			}
			if (calculation.get_personalInfo().getSage() != null && calculation.get_personalInfo().getSgenger() != null
					&& calculation.get_personalInfo().getSocu() != null
					&& calculation.get_riderDetails().get_sRiders() != null
					&& calculation.get_riderDetails().get_sRiders().size() > 0) {

				ArrayList<Benifict> sRiders = calculation.get_riderDetails().get_sRiders();

				for (Benifict benifict : sRiders) {
					String type = benifict.getType();
					switch (type) {
					case "BSAS":
						if (validateInvpSCB().equals(0)) {
							return "SCB";
						}
						break;
					case "ADBS":
						if (validateInvpADBS().equals(0)) {
							return "ADBS";
						}
						break;
					case "CIBS":
						if (validateInvpSCIB().equals(0)) {
							return "CIBS";
						}
						break;
					case "FEBS":
						if (validateInvpFEBS().equals(0)) {
							return "FEBS";
						}
						break;
					case "HBS":
						if (validateInvpHBS().equals(0)) {
							return "HBS";
						}
						break;
					case "HRBS":
						if (validateInvpHRBS().equals(0)) {
							return "HRBS";
						}
						break;
					case "PPDBS":
						if (validateInvpPPDBS().equals(0)) {
							return "PPDBS";
						}
						break;
					case "SUHRBS":
						if (validateInvpSUHRBS().equals(0)) {
							return "SUHRBS";
						}
						break;
					case "TPDASBS":
						if (validateInvpTPDASBS().equals(0)) {
							return "TPDASBS";
						}

						break;
					case "TPDBS":
						if (validateInvpTPDBS().equals(0)) {
							return "TPDBS";
						}

						break;
					case "WPBS":

						break;

					default:
						break;
					}
				}
			}

			if (calculation.get_personalInfo().getChildrens() != null
					&& calculation.get_personalInfo().getChildrens().size() > 0
					&& calculation.get_riderDetails().get_cRiders() != null
					&& calculation.get_riderDetails().get_cRiders().size() > 0) {
				ArrayList<Benifict> cRiders = calculation.get_riderDetails().get_cRiders();

				for (Benifict benifict : cRiders) {
					String type = benifict.getType();
					switch (type) {
					case "CIBC":
						if (validateInvpCIBC().equals(0)) {
							System.out.println("Error CIBC");
							return "CIBC";
						}
						break;
					case "HRBC":
						if (validateInvpHRBC().equals(0)) {
							return "HRBC";
						}
						break;
					case "SUHRBC":
						if (validateInvpSUHRBC().equals(0)) {
							return "SUHRBC";
						}
						break;
					case "HBC":
						if (validateInvpHBC().equals(0)) {
							return "HBC";
						}
						break;
					default:
						break;
					}
				}
			}

		}

		return "No";

	}

	private void loadBeneficts() {
		if (calculation.get_riderDetails() != null) {
			if (calculation.get_riderDetails().get_mRiders() != null
					&& calculation.get_riderDetails().get_mRiders().size() > 0) {
				ArrayList<Benifict> mRiders = calculation.get_riderDetails().get_mRiders();
				for (Benifict benifict : mRiders) {
					benefitMap.put(benifict.getType(), benifict);
				}
			}

			if (calculation.get_riderDetails().get_sRiders() != null
					&& calculation.get_riderDetails().get_sRiders().size() > 0) {
				ArrayList<Benifict> sRiders = calculation.get_riderDetails().get_sRiders();
				for (Benifict benifict : sRiders) {
					benefitMap.put(benifict.getType(), benifict);
				}
			}

			if (calculation.get_riderDetails().get_cRiders() != null
					&& calculation.get_riderDetails().get_cRiders().size() > 0) {
				ArrayList<Benifict> cRiders = calculation.get_riderDetails().get_cRiders();
				for (Benifict benifict : cRiders) {
					benefitMap.put(benifict.getType(), benifict);
				}
			}
		}
	}

	////////////// Invp Product Validation
	public Integer validateInvpEndProd() {
		if (calculation.get_personalInfo().getTerm() >= 5 && calculation.get_personalInfo().getTerm() <= 30
				&& calculation.get_personalInfo().getMage() >= 18 && calculation.get_personalInfo().getMage() <= 65
				&& calculation.get_personalInfo().getMage() + calculation.get_personalInfo().getTerm() <= 65
				&& calculation.get_personalInfo().getBsa() >= 250000) {
			return 1;
		}

		return 0;
	}

	////////////// Asip Product Validation
	public Integer validateAsipProd() {
		if (calculation.get_personalInfo().getTerm() >= 5 && calculation.get_personalInfo().getTerm() <= 11
				|| calculation.get_personalInfo().getTerm() == 15 || calculation.get_personalInfo().getTerm() == 20
				|| calculation.get_personalInfo().getTerm() == 25 || calculation.get_personalInfo().getTerm() == 30
				|| calculation.get_personalInfo().getTerm() == 35 || calculation.get_personalInfo().getTerm() == 40
				|| calculation.get_personalInfo().getTerm() == 45
				|| calculation.get_personalInfo().getTerm() == 50 && calculation.get_personalInfo().getMage() >= 18
						&& calculation.get_personalInfo().getMage() <= 65
						&& calculation.get_personalInfo().getMage() + calculation.get_personalInfo().getTerm() <= 65) {
			return 1;
		}

		return 0;
	}

	////////////// Arp Product Validation
	public Integer validateArpProd() {

		if (calculation.get_personalInfo().getPayingterm().equals("5")
				&& !calculation.get_personalInfo().getFrequance().equals("S")
				|| calculation.get_personalInfo().getPayingterm().equals("10")
						&& !calculation.get_personalInfo().getFrequance().equals("S")
				|| calculation.get_personalInfo().getPayingterm().equals("S")
						&& calculation.get_personalInfo().getFrequance().equals("S")

						&& calculation.get_personalInfo().getMage() >= 18
						&& calculation.get_personalInfo().getMage() <= 65
						&& calculation.get_personalInfo().getMage() + calculation.get_personalInfo().getTerm() <= 65
						&& calculation.get_personalInfo().getBsa() >= 250000
						) {
			return 1;
		}

		return 0;
	}

	public Integer validateInvpProdTotPremium(Double totPremium) {
		if (totPremium >= 1250) {
			return 1;
		}
		return 0;
	}

	// ----------------------- Mainlife Validations before Calculation
	// -----------------
	public Integer validateInvpABD() {
		if (benefitMap.containsKey("ADB")) {
			Benifict benifict = benefitMap.get("ADB");
			Double bsa = calculation.get_personalInfo().getBsa();
			Double rbsa = benifict.getSumAssured();
			if ((rbsa >= bsa && rbsa <= bsa * 6 && rbsa % bsa == 0 && rbsa <= 2500000)
					|| (bsa >= 2500000 && rbsa <= 2500000)) {
				return 1;
			}
		}
		return 0;

	}

	public Integer validateInvpATBP() {
		if (benefitMap.containsKey("ATPB")) {
			Benifict benifict = benefitMap.get("ATPB");
			Double bsa = calculation.get_personalInfo().getBsa();
			Double rbsa = benifict.getSumAssured();
			if (rbsa >= bsa && rbsa <= bsa * 10 && rbsa % bsa == 0) {
				return 1;
			}
			return 0;

		}
		return 0;

	}

	public Integer validateInvpTPDASB() {
		if (benefitMap.containsKey("TPDASB") && benefitMap.containsKey("ADB")) {
			Benifict tpdasb = benefitMap.get("TPDASB");
			Benifict adb = benefitMap.get("ADB");
			if (benefitMap.containsKey("TPDB") && !benefitMap.get("TPDB").isActive()
					|| !benefitMap.containsKey("TPDB")) {
				if (adb.isActive() && tpdasb.getSumAssured().equals(adb.getSumAssured())) {
					return 1;
				}
			}

			return 0;

		}
		return 0;

	}

	public Integer validateInvpTPDB() {
		if (benefitMap.containsKey("TPDB") && benefitMap.containsKey("ADB")) {
			Benifict tpdb = benefitMap.get("TPDB");
			Benifict adb = benefitMap.get("ADB");
			if (benefitMap.containsKey("TPDASB") && !benefitMap.get("TPDASB").isActive()
					|| !benefitMap.containsKey("TPDASB")) {
				if (adb.isActive() && tpdb.getSumAssured().equals(adb.getSumAssured())) {
					return 1;
				}
			}

			return 0;

		}
		return 0;

	}

	public Integer validateInvpPPDB() {
		if (benefitMap.containsKey("PPDB") && benefitMap.containsKey("ADB")) {
			Benifict ppdb = benefitMap.get("PPDB");
			Benifict adb = benefitMap.get("ADB");
			if (adb.isActive() && ppdb.getSumAssured().equals(adb.getSumAssured())) {
				return 1;
			}

			return 0;

		}
		return 0;

	}

	public Integer validateInvpCIB() {
		if (benefitMap.containsKey("CIB")) {
			Double atpb = 0.0;
			if (benefitMap.containsKey("ATPB")) {
				atpb = benefitMap.get("ATPB").getSumAssured();
			}
			Double bsa = calculation.get_personalInfo().getBsa();
			Double cib = benefitMap.get("CIB").getSumAssured();

			if (cib <= (atpb + bsa) && cib <= 6000000 && cib >= 250000) {
				return 1;
			}

			return 0;

		}
		return 0;

	}

	public Integer validateInvpFEB() {
		if (benefitMap.containsKey("FEB")) {
			Benifict benifict = benefitMap.get("FEB");
			Double bsa = calculation.get_personalInfo().getBsa();
			Double rbsa = benifict.getSumAssured();
			if (rbsa >= 25000 && rbsa <= 75000 && rbsa <= (bsa * 0.1)) {
				return 1;
			}
		}
		return 0;

	}

	public Integer validateInvpMIFBD() {
		if (benefitMap.containsKey("MFIBD")) {
			Benifict benifict = benefitMap.get("MFIBD");
			Double rbsa = benifict.getSumAssured();
			if (rbsa >= 10000 && rbsa <= 100000) {
				return 1;
			}
		}
		return 0;

	}

	public Integer validateInvpMIFBT() {
		if (benefitMap.containsKey("MFIBT")) {
			Benifict benifict = benefitMap.get("MFIBT");
			Double rbsa = benifict.getSumAssured();
			if (rbsa >= 10000 && rbsa <= 100000) {
				return 1;
			}
		}
		return 0;

	}

	public Integer validateInvpMFIBDT() {
		if (benefitMap.containsKey("MFIBDT")) {
			Benifict benifict = benefitMap.get("MFIBDT");
			Double rbsa = benifict.getSumAssured();
			if (rbsa >= 10000 && rbsa <= 100000) {
				return 1;
			}
		}
		return 0;

	}

	public Integer validateInvpHRB() {
		if (benefitMap.containsKey("HRB")) {
			if (benefitMap.containsKey("SUHRB") && !benefitMap.get("SUHRB").isActive()
					|| !benefitMap.containsKey("SUHRB")) {
				Double rbsa = benefitMap.get("HRB").getSumAssured();
				if (rbsa == 100000 || rbsa == 200000 || rbsa == 300000 || rbsa == 400000 || rbsa == 500000) {
					return 1;
				}
			}

			return 0;

		}
		return 0;

	}

	public Integer validateInvpSUHRB() {
		if (benefitMap.containsKey("SUHRB")) {
			if (benefitMap.containsKey("HRB") && !benefitMap.get("HRB").isActive() || !benefitMap.containsKey("HRB")) {
				Double rbsa = benefitMap.get("SUHRB").getSumAssured();
				if (rbsa == 600000 || rbsa == 800000 || rbsa == 1000000) {
					return 1;
				}
			}

			return 0;

		}
		return 0;

	}

	public Integer validateInvpHB() {
		if (benefitMap.containsKey("HB")) {
			Benifict benifict = benefitMap.get("HB");
			Double rbsa = benifict.getSumAssured();
			if (rbsa >= 500 && rbsa <= 10000 && rbsa % 100 == 0) {
				return 1;
			}
		}
		return 0;

	}

	// ----------------------- End of Mainlife Validations before Calculation
	// -----------------
	// ----------------------- Spouse Validations Before Calculate
	// ----------------------------
	public Integer validateInvpSCB() {
		if (benefitMap.containsKey("ATPB") && benefitMap.containsKey("BSAS")) {
			Benifict bsas = benefitMap.get("BSAS");
			Benifict atpb = benefitMap.get("ATPB");
			Double bsa = calculation.get_personalInfo().getBsa();
			Double scbBsa = bsas.getSumAssured();
			Double atpbBsa = atpb.getSumAssured();

			if (scbBsa >= 250000 && scbBsa <= (bsa + atpbBsa)) {
				return 1;
			}
		}
		return 0;
	}

	public Integer validateInvpADBS() {
		if (benefitMap.containsKey("ADBS") && benefitMap.containsKey("BSAS")) {
			Benifict bsas = benefitMap.get("BSAS");
			Benifict benifict = benefitMap.get("ADBS");
			Double scb = bsas.getSumAssured();
			Double rbsa = benifict.getSumAssured();

			if ((rbsa >= scb && rbsa <= scb * 6 && rbsa % scb == 0 && rbsa <= 2500000)
					|| (scb >= 2500000 && rbsa <= 2500000)) {
				return 1;
			}
		}
		return 0;
	}

	public Integer validateInvpTPDASBS() {
		if (benefitMap.containsKey("TPDASBS") && benefitMap.containsKey("ADBS")) {
			Benifict tpdasbs = benefitMap.get("TPDASBS");
			Benifict adbs = benefitMap.get("ADBS");
			if (benefitMap.containsKey("TPDBS") && !benefitMap.get("TPDBS").isActive()
					|| !benefitMap.containsKey("TPDBS")) {
				if (adbs.isActive() && tpdasbs.getSumAssured().equals(adbs.getSumAssured())) {
					return 1;
				}
			}

			return 0;

		}
		return 0;

	}

	public Integer validateInvpTPDBS() {
		if (benefitMap.containsKey("TPDBS") && benefitMap.containsKey("ADBS")) {
			Benifict tpdbs = benefitMap.get("TPDBS");
			Benifict adbs = benefitMap.get("ADBS");
			if (benefitMap.containsKey("TPDASBS") && !benefitMap.get("TPDASBS").isActive()
					|| !benefitMap.containsKey("TPDASBS")) {
				if (adbs.isActive() && tpdbs.getSumAssured().equals(adbs.getSumAssured())) {
					return 1;
				}
			}

			return 0;

		}
		return 0;

	}

	public Integer validateInvpPPDBS() {
		if (benefitMap.containsKey("PPDBS") && benefitMap.containsKey("ADBS")) {
			Benifict ppdbs = benefitMap.get("PPDBS");
			Benifict adbs = benefitMap.get("ADBS");
			if (adbs.isActive() && ppdbs.getSumAssured().equals(adbs.getSumAssured())) {
				return 1;
			}

			return 0;

		}
		return 0;

	}

	public Integer validateInvpSCIB() {
		if (benefitMap.containsKey("CIBS") && benefitMap.containsKey("BSAS")) {
			Double scb = benefitMap.get("BSAS").getSumAssured();
			Double cib = benefitMap.get("CIBS").getSumAssured();

			if (cib <= scb && cib <= 6000000 && cib >= 250000) {
				return 1;
			}

			return 0;

		}
		return 0;

	}

	public Integer validateInvpFEBS() {
		if (benefitMap.containsKey("FEBS")) {
			Benifict benifict = benefitMap.get("FEBS");
			Double bsa = calculation.get_personalInfo().getBsa();
			Double rbsa = benifict.getSumAssured();
			if (rbsa >= 25000 && rbsa <= 75000 && rbsa <= (bsa * 0.1)) {
				return 1;
			}
		}
		return 0;

	}

	public Integer validateInvpHRBS() {
		if (benefitMap.containsKey("HRBS") && benefitMap.containsKey("HRB")) {
			Double hrb = benefitMap.get("HRB").getSumAssured();
			Double hrbs = benefitMap.get("HRBS").getSumAssured();
			if (hrb.equals(hrbs)) {
				return 1;
			}
			return 0;
		}
		return 0;
	}

	public Integer validateInvpSUHRBS() {
		if (benefitMap.containsKey("SUHRBS") && benefitMap.containsKey("SUHRB")) {
			Double suhrb = benefitMap.get("SUHRB").getSumAssured();
			Double suhrbs = benefitMap.get("SUHRBS").getSumAssured();
			if (suhrb.equals(suhrbs)) {
				return 1;
			}
			return 0;
		}
		return 0;
	}

	public Integer validateInvpHBS() {
		if (benefitMap.containsKey("HBS") && benefitMap.containsKey("HB")) {
			Double hb = benefitMap.get("HB").getSumAssured();
			Double hbs = benefitMap.get("HBS").getSumAssured();

			if (hbs >= 500 && hbs <= 10000 && hbs.equals(hb) && hbs % 100 == 0) {
				return 1;
			}
			return 0;
		}
		return 0;
	}

	// ----------------------- End of Spouse Validations before Calculation
	// -----------------

	// ----------------------- Children Validations Before Calculate
	// ----------------------------

	public Integer validateInvpCIBC() {
		System.out.println("Calle CIBC");
		if (benefitMap.containsKey("ATPB") && benefitMap.containsKey("CIBC")) {
			Benifict cibc = benefitMap.get("CIBC");
			Benifict atpb = benefitMap.get("ATPB");
			Double bsa = calculation.get_personalInfo().getBsa();
			Double cibcBsa = cibc.getSumAssured();
			Double atpbBsa = atpb.getSumAssured();

			if (cibcBsa >= 250000 && cibcBsa <= 1000000 && cibcBsa <= (bsa + atpbBsa)) {
				return 1;
			}
		}
		return 0;
	}

	public Integer validateInvpHRBC() {
		if (benefitMap.containsKey("HRBC") && benefitMap.containsKey("HRB")) {
			Double hrb = benefitMap.get("HRB").getSumAssured();
			Double hrbc = benefitMap.get("HRBC").getSumAssured();
			if (hrb.equals(hrbc)) {
				return 1;
			}
			return 0;
		}
		return 0;
	}

	public Integer validateInvpSUHRBC() {
		if (benefitMap.containsKey("SUHRBC") && benefitMap.containsKey("SUHRB")) {
			Double suhrb = benefitMap.get("SUHRB").getSumAssured();
			Double suhrbc = benefitMap.get("SUHRBC").getSumAssured();
			if (suhrb.equals(suhrbc)) {
				return 1;
			}
			return 0;
		}
		return 0;
	}

	public Integer validateInvpHBC() {
		if (benefitMap.containsKey("HBC") && benefitMap.containsKey("HB")) {
			Double hb = benefitMap.get("HB").getSumAssured();
			Double hbc = benefitMap.get("HBC").getSumAssured();

			if (hbc >= 500 && hbc <= 10000 && hbc.equals(hb) && hbc % 100 == 0) {
				return 1;
			}
			return 0;
		}
		return 0;
	}
}