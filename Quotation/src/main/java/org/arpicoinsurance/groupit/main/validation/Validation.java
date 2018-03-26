package org.arpicoinsurance.groupit.main.validation;

import java.util.ArrayList;
import java.util.HashMap;

import org.arpicoinsurance.groupit.main.helper.Benifict;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;

public class Validation {

	private QuotationCalculation calculation;

	HashMap<String, Benifict> benefitMap = new HashMap<>();

	public Validation(QuotationCalculation calculation) {
		this.calculation = calculation;
		loadBeneficts();

	}

	public boolean InvpPostValidation(QuotationQuickCalResponse calResp) {
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
				System.out.println("mriders :"+ mRiders);
				for (Benifict benifict : mRiders) {
					String type = benifict.getType();
					System.out.println(type);
					switch (type) {
					
					case "ATPB":
						System.out.println("call ATPB");
						if (validateInvpATBP().equals(0)) {
							return "ATPB must equals to BSA or ATPB must be multi value of BSA till 10 times";
						}
						break;
					case "SFPO":
						System.out.println("call SFPO");
						if (validateInvpSFPO().equals(0)) {
							return "SFPO must be greater than or equal 250,000 and SFPO must be multi value of 50,000 and SFPO must be less than or equal to multi value of BSA till 10 times";
						}
						break;
					case "ADB":
						if (validateInvpABD().equals(0)) {
							return "ADB must equals to BSA or ADB must be multi value of BSA till 6 times and Maximum of 2,500,000";
						}
						break;
					case "TPDASB":
						if (validateInvpTPDASB().equals(0)) {
							return "TPDASB must equals to ADB";
						}
						break;
					case "TPDB":
						if (validateInvpTPDB().equals(0)) {
							return "TPDB must equals to ADB";
						}
						break;

					case "PPDB":
						if (validateInvpPPDB().equals(0)) {
							return "PPDB must equals to ADB";
						}
						break;
					case "CIB":
						if (validateInvpCIB().equals(0)) {
							return "CIB must be greater than 250,000 and less than 6,000,000 and less than sum of ATPB and BSA";
						}
						break;
					case "FEB":
						if (validateInvpFEB().equals(0)) {
							return "FEB must be greater than 25,000 and less than 75,000 and less than 0.1% of BSA";
						}
						break;
					case "MFIBD":
						if (validateInvpMIFBD().equals(0)) {
							return "MIFBD must be greater than 10,000 and less than 100,000";
						}
						break;
					case "MFIBT":
						if (validateInvpMIFBT().equals(0)) {
							return "MIFBT must be greater than 10,000 and less than 100,000";
						}
						break;
					case "MFIBDT":
						if (validateInvpMFIBDT().equals(0)) {
							return "MFIBDT must be greater than 10,000 and less than 100,000";
						}
						break;
					/*case "HRB":
						if (validateInvpHRB().equals(0)) {
							return "HRB must be equal to 100,000 , 200,000 , 300,000 , 400,000 or 500,000";
						}
						break;*/
					case "HRBI":
						if (validateInvpHRBI().equals(0)) {
							return "HRBI must be equal to 100,000 , 200,000 , 300,000 , 400,000 or 500,000";
						}
						break;
					case "HRBF":
						if (validateInvpHRBF().equals(0)) {
							return "HRBF must be equal to 100,000 , 200,000 , 300,000 , 400,000 or 500,000";
						}
						break;
					case "SUHRB":
						if (validateInvpSUHRB().equals(0)) {
							return "SUHRB must be equal to 600,000 , 800,000 or 1,000,000";
						}
						break;
					case "HB":
						if (validateInvpHB().equals(0)) {
							return "HB must be greater than 500 and less than 10,000 and multi value of 100";
						}
						break;
					case "TPDDTA":
						if (validateInvpTPDDTA().equals(0)) {
							return "TPDDTA must be equals to Loan";
						}
						break;
					case "TPDDTAPL":
						if (validateInvpTPDDTAPL().equals(0)) {
							return "TPDDTAPL must be equals to Loan";
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
							return "SCB must be greater than 250,000 and less than sum of BSA and ATPB";
						}
						break;
					case "ADBS":
						if (validateInvpADBS().equals(0)) {
							return "ADBS must equals to BSA or ADBS must be multi value of BSA till 6 times and Maximum of 2,500,000";
						}
						break;
					case "CIBS":
						if (validateInvpSCIB().equals(0)) {
							return "SCIB must be greater than 250,000 and less than 6,000,000 and less than or equal SCB";
						}
						break;
					case "FEBS":
						if (validateInvpFEBS().equals(0)) {
							return "FEBS must be greater than 25,000 and less than 75,000 and less than 0.1% of BSA";
						}
						break;
					case "HBS":
						if (validateInvpHBS().equals(0)) {
							return "HBS must be equal to HB";
						}
						break;
					/*case "HRBS":
						if (validateInvpHRBS().equals(0)) {
							return "HRBS must be equal to HRB";
						}
						break;
						*/
					case "HRBIS":
						if (validateInvpHRBIS().equals(0)) {
							return "HRBIS must be equal to HRB";
						}
						break;
					case "HRBFS":
						if (validateInvpHRBFS().equals(0)) {
							return "HRBFS must be equal to HRB";
						}
						break;
					case "PPDBS":
						if (validateInvpPPDBS().equals(0)) {
							return "PPDBS must be equals to ADBS";
						}
						break;
					case "SUHRBS":
						if (validateInvpSUHRBS().equals(0)) {
							return "SUHRBS must be equal to SUHRB";
						}
						break;
					case "TPDASBS":
						if (validateInvpTPDASBS().equals(0)) {
							return "TPDASBS must be equals to ADBS";
						}

						break;
					case "TPDBS":
						if (validateInvpTPDBS().equals(0)) {
							return "TPDBS must be equals to ADBS";
						}

						break;
					case "WPBS":

						break;

					case "JLB":
						if (validateInvpJLB().equals(0)) {
							return "JLB must be equals to Loan";
						}
						break;
						
					case "TPDDTAS":
						if (validateInvpTPDDTAS().equals(0)) {
							return "TPDDTAS must be equals to Loan";
						}
						break;
						
					case "JLBPL":
						if (validateInvpJLBPL().equals(0)) {
							return "JLBPL must be equals to Loan";
						}
						break;
						
					case "TPDDTASPL":
						if (validateInvpTPDDTASPL().equals(0)) {
							return "TPDDTASPL must be equals to Loan";
						}
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
							return "CIBC must be greater than 250,000 and less than 6,000,000 and less than sum of ATPB and BSA";
						}
						break;
					/*case "HRBC":
						if (validateInvpHRBC().equals(0)) {
							return "HRBC must be equal to HRB";
						}
						break;
						*/
					case "HRBIC":
						if (validateInvpHRBIC().equals(0)) {
							return "HRBIC must be equal to HRB";
						}
						break;
						
					case "HRBFC":
						if (validateInvpHRBFC().equals(0)) {
							return "HRBFC must be equal to HRB";
						}
						break;
					case "SUHRBC":
						if (validateInvpSUHRBC().equals(0)) {
							return "SUHRBC must be equal to SUHRB";
						}
						break;
					case "HBC":
						if (validateInvpHBC().equals(0)) {
							return "HBC must be equal to HB";
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
				&& calculation.get_personalInfo().getMage() + calculation.get_personalInfo().getTerm() <= 70
				&& calculation.get_personalInfo().getBsa() >= 250000) {
			return 1;
		}

		return 0;
	}
	
	//////////////Asfp Product Validation
	public Integer validateAsfpProd() {
	if (calculation.get_personalInfo().getTerm() >= 5 && calculation.get_personalInfo().getTerm() <= 40
			&& calculation.get_personalInfo().getMage() >= 18 && calculation.get_personalInfo().getMage() <= 60
			&& calculation.get_personalInfo().getMage() + calculation.get_personalInfo().getTerm() <= 60) {
		return 1;
	}
	
	return 0;
	}
	
	////////////// Asip Product Validation

	public Integer validateAsipProd() {
		if ((calculation.get_personalInfo().getTerm() >= 5 && calculation.get_personalInfo().getTerm() <= 11
				|| calculation.get_personalInfo().getTerm() == 15 || calculation.get_personalInfo().getTerm() == 20
				|| calculation.get_personalInfo().getTerm() == 25 || calculation.get_personalInfo().getTerm() == 30
				|| calculation.get_personalInfo().getTerm() == 35 || calculation.get_personalInfo().getTerm() == 40
				|| calculation.get_personalInfo().getTerm() == 45
				|| calculation.get_personalInfo().getTerm() == 50 ) && calculation.get_personalInfo().getMage() >= 18
						&& calculation.get_personalInfo().getMage() <= 65
						&& calculation.get_personalInfo().getMage() + calculation.get_personalInfo().getTerm() <= 70) {
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
	
	public Integer validateAsfpProdTotPremium(Double totPremium,int frequency) {
		if ((totPremium*frequency) > (1250*frequency)) {
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
	
	public Integer validateInvpSFPO() {
		if (benefitMap.containsKey("SFPO")) {
			Benifict benifict = benefitMap.get("SFPO");
			Double bsa = calculation.get_personalInfo().getBsa();
			Double rbsa = benifict.getSumAssured();
			if (rbsa >= 250000 && (rbsa%50000==0) && (rbsa<=bsa*10)) {
				return 1;
			}
		}
		return 0;

	}

	public Integer validateInvpATBP() {
		if (benefitMap.containsKey("ATPB")) {
			System.out.println("call atpb val method");
			Benifict benifict = benefitMap.get("ATPB");
			Double bsa = calculation.get_personalInfo().getBsa();
			Double rbsa = benifict.getSumAssured();
			System.out.println(bsa + " " + rbsa);
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
			if (benefitMap.containsKey("HRBI") && !benefitMap.get("HRBI").isActive() && benefitMap.containsKey("HRBF") && !benefitMap.get("HRBF").isActive() || !benefitMap.containsKey("HRBI") && !benefitMap.containsKey("HRBF")) {
				Double rbsa = benefitMap.get("SUHRB").getSumAssured();
				if (rbsa == 600000 || rbsa == 800000 || rbsa == 1000000) {
					return 1;
				}
			}

			return 0;

		}
		return 0;

	}
	
	public Integer validateInvpHRBI() {
		if (benefitMap.containsKey("HRBI")) {
			if (benefitMap.containsKey("SUHRB") && !benefitMap.get("SUHRB").isActive() && benefitMap.containsKey("HRBF") && !benefitMap.get("HRBF").isActive() || !benefitMap.containsKey("SUHRB") && !benefitMap.containsKey("HRBF")) {
				Double rbsa = benefitMap.get("HRBI").getSumAssured();
				if (rbsa == 100000 || rbsa == 200000 || rbsa == 300000 || rbsa == 400000 || rbsa == 500000) {
					return 1;
				}
			}

			return 0;

		}
		return 0;

	}
	
	public Integer validateInvpHRBF() {
		if (benefitMap.containsKey("HRBF")) {
			if (benefitMap.containsKey("SUHRB") && !benefitMap.get("SUHRB").isActive() && benefitMap.containsKey("HRBI") && !benefitMap.get("HRBI").isActive() || !benefitMap.containsKey("SUHRB") && !benefitMap.containsKey("HRBI")) {
				Double rbsa = benefitMap.get("HRBF").getSumAssured();
				if (rbsa == 100000 || rbsa == 200000 || rbsa == 300000 || rbsa == 400000 || rbsa == 500000) {
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
		System.out.println(calculation.get_product() +"]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]");
		if(calculation.get_product() != null) {
			Benifict bsas = benefitMap.get("BSAS");
			Double bsa = calculation.get_personalInfo().getBsa();
			Double scbBsa = bsas.getSumAssured();
			Double atpbBsa = 0.0;

			if (scbBsa >= 250000 && scbBsa <= (bsa + atpbBsa)) {
				return 1;
			}
		}else {
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
	
	public Integer validateInvpHRBFS() {
		if (benefitMap.containsKey("HRBFS") && benefitMap.containsKey("HRBF")) {
			Double hrbf = benefitMap.get("HRBF").getSumAssured();
			Double hrbfs = benefitMap.get("HRBFS").getSumAssured();
			//if (hrbf.equals(hrbfs)) {
				return 1;
			//}
			//return 0;
		}
		return 0;
	}
	
	public Integer validateInvpHRBIS() {
		if (benefitMap.containsKey("HRBIS") && benefitMap.containsKey("HRBI")) {
			Double hrbi = benefitMap.get("HRBI").getSumAssured();
			Double hrbis = benefitMap.get("HRBIS").getSumAssured();
			if (hrbi.equals(hrbis)) {
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
		System.out.println(calculation.get_product() +"]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]");
		if(calculation.get_product() != null) {
			Benifict cibc = benefitMap.get("CIBC");
			Double bsa = calculation.get_personalInfo().getBsa();
			Double cibcBsa = cibc.getSumAssured();
			Double atpbBsa = 0.0;

			if (cibcBsa >= 250000 && cibcBsa <= 1000000 && cibcBsa <= (bsa + atpbBsa)) {
				return 1;
			}
		}else {
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
	
	public Integer validateInvpHRBIC() {
		if (benefitMap.containsKey("HRBIC") && benefitMap.containsKey("HRBI")) {
			Double hrbi = benefitMap.get("HRBI").getSumAssured();
			Double hrbic = benefitMap.get("HRBIC").getSumAssured();
			if (hrbi.equals(hrbic)) {
				return 1;
			}
			return 0;
		}
		return 0;
	}

	public Integer validateInvpHRBFC() {
		if (benefitMap.containsKey("HRBFC") && benefitMap.containsKey("HRBF")) {
			Double hrbf = benefitMap.get("HRBF").getSumAssured();
			Double hrbfc = benefitMap.get("HRBFC").getSumAssured();
			//if (hrbf.equals(hrbfc)) {
				return 1;
			//}
			//return 0;
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
	
	public Integer validateInvpTPDDTA() {
		
		System.out.println(benefitMap.containsKey("TPDDTA"));
		
		if (benefitMap.containsKey("TPDDTA")) {
			Benifict benifict = benefitMap.get("TPDDTA");
			Double rbsa = benifict.getSumAssured();
			System.out.println(rbsa + "TPDDTA" + calculation.get_personalInfo().getBsa());
			if  (rbsa.equals(calculation.get_personalInfo().getBsa())) {
				return 1;
			}
		}
		return 0;
	}
	public Integer validateInvpJLB() {
		if (benefitMap.containsKey("JLB")) {
			Benifict benifict = benefitMap.get("JLB");
			Double rbsa = benifict.getSumAssured();
			if (rbsa.equals(calculation.get_personalInfo().getBsa())){
				return 1;
			}
		}
		return 0;
	}
	public Integer validateInvpTPDDTAS() {
		if (benefitMap.containsKey("TPDDTAS")) {
			Benifict benifict = benefitMap.get("TPDDTAS");
			Double rbsa = benifict.getSumAssured();
			if (rbsa.equals(calculation.get_personalInfo().getBsa())) {
				return 1;
			}
		}
		return 0;
	}
	
public Integer validateInvpTPDDTAPL() {
		
		System.out.println(benefitMap.containsKey("TPDDTAPL"));
		
		if (benefitMap.containsKey("TPDDTAPL")) {
			Benifict benifict = benefitMap.get("TPDDTAPL");
			Double rbsa = benifict.getSumAssured();
			if  (rbsa.equals(calculation.get_personalInfo().getBsa())) {
				return 1;
			}
		}
		return 0;
	}
	public Integer validateInvpJLBPL() {
		if (benefitMap.containsKey("JLBPL")) {
			Benifict benifict = benefitMap.get("JLBPL");
			Double rbsa = benifict.getSumAssured();
			if (rbsa.equals(calculation.get_personalInfo().getBsa())){
				return 1;
			}
		}
		return 0;
	}
	public Integer validateInvpTPDDTASPL() {
		if (benefitMap.containsKey("TPDDTASPL")) {
			Benifict benifict = benefitMap.get("TPDDTASPL");
			Double rbsa = benifict.getSumAssured();
			if (rbsa.equals(calculation.get_personalInfo().getBsa())) {
				return 1;
			}
		}
		return 0;
	}
}
