package org.arpicoinsurance.groupit.main.validation;

import java.util.ArrayList;
import java.util.HashMap;

import org.arpicoinsurance.groupit.main.helper.Benifict;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;

public class ValidationInvp {

	private QuotationCalculation calculation;

	HashMap<String, Benifict> benefitMap = new HashMap<>();

	public ValidationInvp(QuotationCalculation calculation) {
		this.calculation = calculation;
		loadBeneficts();

	}

	public String validateBenifict() {
		if (calculation.get_riderDetails() != null) {
			if (calculation.get_riderDetails().get_mRiders() != null
					|| calculation.get_riderDetails().get_mRiders().size() > 0) {
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
							return "SUHRB";
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

	public Integer validateInvpProd() {
		System.out.println(calculation.get_personalInfo().getTerm());
		System.out.println(calculation.get_personalInfo().getMage());
		System.out.println(calculation.get_personalInfo().getBsa());
		if (calculation.get_personalInfo().getTerm() >= 5 && calculation.get_personalInfo().getTerm() <= 30
				&& calculation.get_personalInfo().getMage() >= 18 && calculation.get_personalInfo().getMage() <= 65
				&& calculation.get_personalInfo().getBsa() >= 250000) {
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
			if (benefitMap.containsKey("HRB") && !benefitMap.get("HRB").isActive()
					|| !benefitMap.containsKey("HRB")) {
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
			if (rbsa >= 500 && rbsa <= 10000 && rbsa%100==0) {
				return 1;
			}
		}
		return 0;

	}

}
