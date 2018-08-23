package org.arpicoinsurance.groupit.main.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.helper.Benifict;
import org.arpicoinsurance.groupit.main.helper.Children;
import org.arpicoinsurance.groupit.main.helper.InvpSavePersonalInfo;
import org.arpicoinsurance.groupit.main.helper.MainLife;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.helper.Spouse;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;

public class Validation {

	private CalculationUtils calculationUtils = new CalculationUtils();

	private QuotationCalculation calculation;

	HashMap<String, Benifict> benefitMap = new HashMap<>();

	public Validation() {

	}

	public Validation(QuotationCalculation calculation) {
		this.calculation = calculation;
		loadBeneficts();

	}

	public String saveEditValidations(InvpSavePersonalInfo InvpSavePersonalInfo) {

		MainLife life = InvpSavePersonalInfo.get_mainlife();

		if (life != null) {
			if (life.get_mName() == null || !(life.get_mName().length() > 0)) {
				return "Main Life Name can't be null";
			}
			if (life.get_mDob() == null || !(life.get_mDob().length() > 0)) {
				return "Main Life DOB can't be null";
			}
			if (life.get_mMobile() == null || !(life.get_mMobile().length() > 0)) {
				return "Main Life Mobile can't be null";
			}
			if (life.get_mOccupation() == null || !(life.get_mOccupation().length() > 0)) {
				return "Main Life Occupation can't be null";
			}
			if (life.get_mGender() == null || !(life.get_mGender().length() > 0)) {
				return "Main Life Gender can't be null";
			}
		} else {
			return "Main Life Can't be null";
		}

		Spouse spouse = InvpSavePersonalInfo.get_spouse();
		if (spouse != null) {
			if (spouse.is_sActive()) {
				if (spouse.get_sDob() == null || !(spouse.get_sDob().length() > 0)) {
					return "Spouse DOB can't be null";
				}
				if (spouse.get_sName() == null || !(spouse.get_sName().length() > 0)) {
					return "Spouse Name can't be null";
				}
				if (spouse.get_sGender() == null || !(spouse.get_sGender().length() > 0)) {
					return "Spouse Gender can't be null";
				}
				if (spouse.get_sOccupation() == null || !(spouse.get_sOccupation().length() > 0)) {
					return "Spouse Occupation can't be null";
				}
			}
		}

		List<Children> childrens = InvpSavePersonalInfo.get_childrenList();

		if (childrens != null && childrens.size() > 0) {
			for (Children children : childrens) {
				if (children.is_cActive()) {
					if (children.get_cName() == null || !(children.get_cName().length() > 0)) {
						return "Children name can't be null";
					}
					if(children.get_cDob() == null || !(children.get_cDob().length() > 0 )) {
						return "Children DOB can't be null";
					}
				}
			}	
		}

		return "ok";
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
				// System.out.println("mriders :" + mRiders);
				for (Benifict benifict : mRiders) {
					String type = benifict.getType();
					// System.out.println(type);
					switch (type) {

					case "ATPB":
						// System.out.println("call ATPB");
						if (calculation.get_product().equals("ASFP")) {
							if (validateASFPATBP().equals(0)) {
								return "ATPB must be greater than or equal 500000 and ATPB must be less than or equal (500000 x 10) and ATPB mod 25000 equal 0";
							}
						} else {
							if (validateInvpATBP().equals(0)) {
								return "ATPB must be greater than or equal BSA and ATPB must be less than or equal (BSA x 10) and ATPB mod 25000 equal 0";
							}
						}
						break;
					case "L2":
						// System.out.println("call ATPB");
						if (calculation.get_product().equals("ARTM")) {
							if (validateARTML2().equals(0)) {
								return "L2 must be Greater than or Equal 100,000 and Less than or Equal 1,000,000 and L2 mod 25000 must 0";
							}
						}
						break;
					case "SFPO":
						// System.out.println("call SFPO");
						if (validateInvpSFPO().equals(0)) {
							return "SFPO must be greater than or equal 250,000 and SFPO must be multi value of 25,000";
						}
						if (validateInvpSFPO().equals(2)) {
							return "Max Age is 60 for get SFPO";
						}
						break;
					case "ADB":
						if (calculation.get_product().equals("ASFP")) {
							if (validateASFPABD().equals(0)) {
								return "ADB must be greater than or equal 500000 and ADB must be less than or equal (500000 x 6) and ADB mod 25000 equal 0";
							}
						} else {
							if (validateInvpABD().equals(0)) {
								return "ADB must be greater than or equal BSA and ADB must be less than or equal (BSA x 6) and ADB mod 25000 equal 0 Max value must be 25,000,000";
							}
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

						// System.out.println(calculation.get_product());

						/////// for END
						
						/*if (calculation.get_product().equals("END1")) {
							if (validateCIBEND().equals(0)) {
								return "CIB must be greater than 100,000 and less than 6,000,000 and less than sum of ATPB and BSA and CIB mod 25000 must 0";
							}
						} else */if (calculation.get_product().equals("ARTM")) {
							if (validateCIBARTM().equals(0)) {
								return "Internal Error";
							}
							if (validateCIBARTM().equals(2)) {
								return "Please get L2 for get This Benefict";
							}
							if (validateCIBARTM().equals(3)) {
								return "CIB mest Greater than or Equal to 100,000 and Less than or Equal to 10 times of L2 and Less than or Equal 6,000,000 and CIB mod 100000 must 0";
							}
						} else {
							if (validateInvpCIB().equals(0)) {
								return "CIB must be greater than 250,000 and less than 6,000,000 and less than sum of ATPB and BSA and CIB mod 25000 must 0";
							}
						}
						break;
					case "FEB":
						/////// for END
						if (calculation.get_product().equals("END1")) {
							if (validateENDFEB().equals(0)) {
								return "FEB must be greater than or equal 10,000 and less than or equal 75,000 and less than or equal 10% of BSA";
							}
						} else {
							if (validateInvpFEB().equals(0)) {
								return "FEB must be greater than or equal 25,000 and less than or equal 75,000 and less than or equal 10% of BSA";
							}
						}

						break;
					case "MFIBD":
						if (validateInvpMIFBD().equals(0)) {
							return "MIFBD mod 1000 must be 0 and greater than 10,000 and less than 100,000 and Less than Yearly Premium";
						}
						break;
					case "MFIBT":
						if (validateInvpMIFBT().equals(0)) {
							return "MIFBT mod 1000 must be 0 and greater than 10,000 and less than 100,000 and Less than Yearly Premium";
						}
						break;
					case "MFIBDT":
						if (validateInvpMFIBDT().equals(0)) {
							return "MFIBDT mod 1000 must be 0 and greater than 10,000 and less than 100,000 and Less than Yearly Premium";
						}
						break;
					/*
					 * case "HRB": if (validateInvpHRB().equals(0)) { return
					 * "HRB must be equal to 100,000 , 200,000 , 300,000 , 400,000 or 500,000"; }
					 * break;
					 */
					case "HRBI":
						if (validateInvpHRBI().equals(0)) {
							return "HRBI must be equal to 100,000 , 200,000 , 300,000 , 400,000 or 500,000";
						}
						break;
					case "HRBF":
						if (validateInvpHRBF().equals(2)) {
							return "You must add spouse or child for get HRBF";
						}
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
							return "HB must be greater than 500 and less than 15,000 and multi value of 100 and Less than 20% of Yearly Premium";
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
							return "ADBS must be greater than or equal SCB and ADBS must be less than or equal (SCB x 6) and ADBS mod 25000 equal 0 Max value must be 25,000,000 and Less than ADB";
						}
						break;
					case "CIBS":
						if (validateInvpSCIB().equals(2)) {
							return "Please Select SCB before get SCIB";
						}
						//////// validation END
						/*if (calculation.get_product().equals("END1")) {
							if (validateInvpSCIBEND().equals(0)) {
								return "SCIB must be greater than or equal 100,000 and less than or equal 6,000,000 and SCIB nod 25000 must 0";
							}
						} else {*/
							if (validateInvpSCIB().equals(0)) {
								return "SCIB must be greater than or equal 250,000 and less than or equal 6,000,000 and SCIB nod 25000 must 0";
							}
						/*}*/
						break;
					case "FEBS":
						//////// validation END
						if (calculation.get_product().equals("END1")) {
							if (validateENDFEBS().equals(0)) {
								return "FEBS must be greater than or equal 10,000 and less than or equal 75,000 and less than or equal 10% of BSA less than FEB";
							}
						} else {
							if (validateInvpFEBS().equals(0)) {
								return "FEBS must be greater than or equal 25,000 and less than or equal 75,000 and less than or equal 10% of BSA less than FEB";
							}
						}
						break;
					case "HBS":
						if (validateInvpHBS().equals(0)) {
							return "HBS must be equal to HB";
						}
						break;
					/*
					 * case "HRBS": if (validateInvpHRBS().equals(0)) { return
					 * "HRBS must be equal to HRB"; } break;
					 */
					case "HRBIS":
						if (validateInvpHRBIS().equals(0)) {
							return "HRBIS must be equal to HRBS";
						}
						break;
					case "HRBFS":
						if (validateInvpHRBFS().equals(0)) {
							return "HRBFS must be equal to HRBF";
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
						/*if (calculation.get_product().equals("END1")) {
							if (validateENDCIBC().equals(0)) {
								return "CIBC must be greater than or equal 100,000 and less than or equal 1,000,000 and CIBC mod 25000 must 0";
							}
						} else {*/
							if (validateInvpCIBC().equals(0)) {
								return "CIBC must be greater than or equal 250,000 and less than or equal 1,000,000 and CIBC mod 25000 must 0";
							}
						/*}*/
						break;
					/*
					 * case "HRBC": if (validateInvpHRBC().equals(0)) { return
					 * "HRBC must be equal to HRB"; } break;
					 */
					case "HRBIC":
						if (validateInvpHRBIC().equals(0)) {
							return "HRBIC must be equal to HRBC";
						}
						break;

					case "HRBFC":
						if (validateInvpHRBFC().equals(0)) {
							return "HRBFC must be equal to HRBF";
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
		if (calculation.get_personalInfo().getTerm() >= 5 && calculation.get_personalInfo().getTerm() <= 45
				&& calculation.get_personalInfo().getMage() >= 18 && calculation.get_personalInfo().getMage() <= 65
				&& calculation.get_personalInfo().getMage() + calculation.get_personalInfo().getTerm() <= 70
				&& calculation.get_personalInfo().getBsa() >= 250000) {
			return 1;
		}

		return 0;
	}

	////////////// Asfp Product Validation
	public Integer validateAsfpProd() {
		if (calculation.get_personalInfo().getTerm() >= 5 && calculation.get_personalInfo().getTerm() <= 40
				&& calculation.get_personalInfo().getMage() >= 18 && calculation.get_personalInfo().getMage() <= 70
				&& calculation.get_personalInfo().getMage() + calculation.get_personalInfo().getTerm() <= 70) {
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
				|| calculation.get_personalInfo().getTerm() == 45 || calculation.get_personalInfo().getTerm() == 50)
				&& calculation.get_personalInfo().getMage() >= 18 && calculation.get_personalInfo().getMage() <= 65
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
						&& calculation.get_personalInfo().getMage() + calculation.get_personalInfo().getTerm() <= 70
						&& calculation.get_personalInfo().getBsa() >= 100000) {
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

	public Integer validateAsfpProdTotPremium(Double totPremium, int frequency) {
		if ((totPremium * frequency) > (1250 * frequency)) {
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
			if ((rbsa >= bsa && rbsa <= bsa * 6 && rbsa % 25000 == 0 && rbsa <= 25000000)
					|| (bsa >= 25000000 && rbsa <= 25000000)) {

				return 1;
			}
		}
		return 0;

	}

	public Integer validateASFPABD() {
		if (benefitMap.containsKey("ADB")) {
			Benifict benifict = benefitMap.get("ADB");
			// Double bsa = calculation.get_personalInfo().getBsa();
			Double rbsa = benifict.getSumAssured();
			if ((rbsa >= 500000 && rbsa <= 3000000 && rbsa % 25000 == 0)) {

				return 1;
			}
		}
		return 0;

	}

	public Integer validateInvpSFPO() {
		if (benefitMap.containsKey("SFPO")) {
			if (calculation.get_personalInfo().getMage() > 60) {
				return 2;
			}
			Benifict benifict = benefitMap.get("SFPO");
			Double bsa = calculation.get_personalInfo().getBsa();
			Double rbsa = benifict.getSumAssured();
			if (rbsa >= 250000 && (rbsa % 25000 == 0)) {
				return 1;
			}
		}
		return 0;

	}

	public Integer validateASFPATBP() {
		if (benefitMap.containsKey("ATPB")) {
			Benifict benifict = benefitMap.get("ATPB");
			Double rbsa = benifict.getSumAssured();
			if (rbsa >= 500000 && rbsa <= 5000000 && rbsa % 25000 == 0) {
				return 1;
			}
			return 0;

		}
		return 0;
	}

	public Integer validateInvpATBP() {
		if (benefitMap.containsKey("ATPB")) {
			// System.out.println("call atpb val method");
			Benifict benifict = benefitMap.get("ATPB");
			Double bsa = calculation.get_personalInfo().getBsa();
			Double rbsa = benifict.getSumAssured();
			if (rbsa >= bsa && rbsa <= bsa * 10 && rbsa % 25000 == 0) {
				return 1;
			}
			return 0;
		}
		return 0;
	}

	public Integer validateARTML2() {
		if (benefitMap.containsKey("L2")) {
			Benifict benifict = benefitMap.get("L2");
			if (benifict.getSumAssured() >= 100000 && benifict.getSumAssured() <= 1000000 && benifict.getSumAssured() % 25000 == 0) {
				return 1;
			}
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

			if (cib <= (atpb + bsa) && cib <= 6000000 && cib >= 250000 && cib % 25000 == 0) {
				return 1;
			}

			return 0;

		}
		return 0;

	}

	/////// for END
	public Integer validateCIBEND() {
		if (benefitMap.containsKey("CIB")) {
			Double atpb = 0.0;
			if (benefitMap.containsKey("ATPB")) {
				atpb = benefitMap.get("ATPB").getSumAssured();
			}
			Double bsa = calculation.get_personalInfo().getBsa();
			Double cib = benefitMap.get("CIB").getSumAssured();

			if (cib <= (atpb + bsa) && cib <= 6000000 && cib >= 100000 && cib % 25000 == 0) {
				return 1;
			}

			return 0;

		}
		return 0;

	}

	/////// for ARTM
	public Integer validateCIBARTM() {
		if (benefitMap.containsKey("CIB")) {
			if (benefitMap.containsKey("L2")) {
				Double cib = benefitMap.get("CIB").getSumAssured();
				Double l2 = benefitMap.get("L2").getSumAssured();
				if (cib <= l2 * 10 && cib >= 100000 && cib <= 6000000 && cib % 100000 == 0) {
					return 1;
				}
				return 3;

			} else {
				return 2;
			}

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

	/////// for END
	public Integer validateENDFEB() {
		if (benefitMap.containsKey("FEB")) {
			Benifict benifict = benefitMap.get("FEB");
			Double bsa = calculation.get_personalInfo().getBsa();
			Double rbsa = benifict.getSumAssured();
			if (rbsa >= 10000 && rbsa <= 75000 && rbsa <= (bsa * 0.1)) {
				return 1;
			}
		}
		return 0;

	}

	public Integer validateInvpMIFBD() {
		if (benefitMap.containsKey("MFIBD") && (benefitMap.containsKey("MFIBT") && !benefitMap.get("MFIBT").isActive()
				&& benefitMap.containsKey("MFIBDT") && !benefitMap.get("MFIBDT").isActive()
				|| !benefitMap.containsKey("MFIBT") && !benefitMap.containsKey("MFIBDT"))) {
			Benifict benifict = benefitMap.get("MFIBD");
			Double rbsa = benifict.getSumAssured();
			if (rbsa >= 10000 && rbsa <= 100000 && rbsa % 1000 == 0) {
				return 1;
			}
		}
		return 0;

	}

	public Integer validateInvpMIFBT() {
		if (benefitMap.containsKey("MFIBT") && (benefitMap.containsKey("MFIBDT") && !benefitMap.get("MFIBDT").isActive()
				&& benefitMap.containsKey("MFIBD") && !benefitMap.get("MFIBD").isActive()
				|| !benefitMap.containsKey("MFIBDT") && !benefitMap.containsKey("MFIBD"))) {
			Benifict benifict = benefitMap.get("MFIBT");
			Double rbsa = benifict.getSumAssured();
			if (rbsa >= 10000 && rbsa <= 100000 && rbsa % 1000 == 0) {
				return 1;
			}
		}
		return 0;

	}

	public Integer validateInvpMFIBDT() {
		if (benefitMap.containsKey("MFIBDT") && (benefitMap.containsKey("MFIBT") && !benefitMap.get("MFIBT").isActive()
				&& benefitMap.containsKey("MFIBD") && !benefitMap.get("MFIBD").isActive()
				|| !benefitMap.containsKey("MFIBT") && !benefitMap.containsKey("MFIBD"))) {
			Benifict benifict = benefitMap.get("MFIBDT");
			Double rbsa = benifict.getSumAssured();
			if (rbsa >= 10000 && rbsa <= 100000 && rbsa % 1000 == 0) {
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
			if (benefitMap.containsKey("HRBI") && !benefitMap.get("HRBI").isActive() && benefitMap.containsKey("HRBF")
					&& !benefitMap.get("HRBF").isActive()
					|| !benefitMap.containsKey("HRBI") && !benefitMap.containsKey("HRBF")) {
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
			if (benefitMap.containsKey("SUHRB") && !benefitMap.get("SUHRB").isActive() && benefitMap.containsKey("HRBF")
					&& !benefitMap.get("HRBF").isActive()
					|| !benefitMap.containsKey("SUHRB") && !benefitMap.containsKey("HRBF")) {
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
		if (calculation.get_personalInfo().getSage() == null && calculation.get_personalInfo().getChildrens() != null
				&& calculation.get_personalInfo().getChildrens().size() == 0) {
			return 2;
		}
		if (benefitMap.containsKey("HRBF")) {
			if (benefitMap.containsKey("SUHRB") && !benefitMap.get("SUHRB").isActive() && benefitMap.containsKey("HRBI")
					&& !benefitMap.get("HRBI").isActive()
					|| !benefitMap.containsKey("SUHRB") && !benefitMap.containsKey("HRBI")) {
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
			if (rbsa >= 500 && rbsa <= 15000 && rbsa % 100 == 0) {
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
		// System.out.println(calculation.get_product() +
		// "]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]");
		if (calculation.get_product() != null) {
			Benifict bsas = benefitMap.get("BSAS");
			Double bsa = calculation.get_personalInfo().getBsa();
			Double scbBsa = bsas.getSumAssured();
			Double atpbBsa = 0.0;
			if (benefitMap.containsKey("ATPB")) {
				atpbBsa = benefitMap.get("ATPB").getSumAssured();
			}

			if (scbBsa >= 250000 && scbBsa <= (bsa + atpbBsa)) {
				return 1;
			}
		} else {
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
		if (benefitMap.containsKey("ADBS") && benefitMap.containsKey("BSAS") && benefitMap.containsKey("ADB")) {
			Benifict bsas = benefitMap.get("BSAS");
			Benifict benifict = benefitMap.get("ADBS");
			Double scb = bsas.getSumAssured();
			Double rbsa = benifict.getSumAssured();
			Double adb = benefitMap.get("ADB").getSumAssured();

			if ((rbsa >= scb && rbsa <= scb * 6 && rbsa % 25000 == 0 && rbsa <= 25000000 && adb >= rbsa)
					|| (scb >= 25000000 && rbsa <= 25000000 && adb >= rbsa)) {
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
		// System.out.println(benefitMap.get("CIBS").getSumAssured() +
		// "aaaaaaaaaaaaaaaaaa");

		if (benefitMap.get("BSAS") == null) {
			return 2;
		}

		if (benefitMap.containsKey("CIBS") && benefitMap.containsKey("BSAS")) {
			// System.out.println("called");
			Double scb = benefitMap.get("BSAS").getSumAssured();
			Double cib = benefitMap.get("CIBS").getSumAssured();

			// System.out.println(scb);
			// System.out.println(cib);
			if (cib <= scb && cib <= 6000000 && cib >= 250000 && cib % 25000 == 0) {
				return 1;
			}

			return 0;

		}
		return 0;

	}

	//////// validation END
	public Integer validateInvpSCIBEND() {

		/// System.out.println(benefitMap.get("CIBS").getSumAssured() +
		/// "aaaaaaaaaaaaaaaaaa");

		if (benefitMap.get("BSAS") == null) {
			return 2;
		}

		if (benefitMap.containsKey("CIBS") && benefitMap.containsKey("BSAS")) {
			System.out.println("called");
			Double scb = benefitMap.get("BSAS").getSumAssured();
			Double cib = benefitMap.get("CIBS").getSumAssured();

			// System.out.println(scb);
			// System.out.println(cib);

			if (cib <= scb && cib <= 6000000 && cib >= 100000 && cib % 25000 == 0) {
				return 1;
			}

			return 0;

		}
		return 0;

	}

	public Integer validateInvpFEBS() {
		if (benefitMap.containsKey("FEBS") && benefitMap.containsKey("FEB")) {
			Benifict benifict = benefitMap.get("FEBS");
			Double bsa = calculation.get_personalInfo().getBsa();
			Double rbsa = benifict.getSumAssured();
			Double feb = benefitMap.get("FEB").getSumAssured();
			if (rbsa >= 25000 && rbsa <= 75000 && rbsa <= (bsa * 0.1) && feb >= rbsa) {
				return 1;
			}
		}
		return 0;

	}

	//////// validation END
	public Integer validateENDFEBS() {
		if (benefitMap.containsKey("FEBS") && benefitMap.containsKey("FEB")) {
			Benifict benifict = benefitMap.get("FEBS");
			Double bsa = calculation.get_personalInfo().getBsa();
			Double rbsa = benifict.getSumAssured();
			Double feb = benefitMap.get("FEB").getSumAssured();
			if (rbsa >= 10000 && rbsa <= 75000 && rbsa <= (bsa * 0.1) && feb >= rbsa) {
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
			// Double hrbf = benefitMap.get("HRBF").getSumAssured();
			// Double hrbfs = benefitMap.get("HRBFS").getSumAssured();
			// if (hrbf.equals(hrbfs)) {
			return 1;
			// }
			// return 0;
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

			if (hbs >= 500 && hbs <= 15000 && hbs.equals(hb) && hbs % 100 == 0) {
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
		// System.out.println(calculation.get_product() +
		// "]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]");
		if (calculation.get_product() != null) {
			Benifict cibc = benefitMap.get("CIBC");
			// Double bsa = calculation.get_personalInfo().getBsa();
			Double cibcBsa = cibc.getSumAssured();
			// Double atpbBsa = 0.0;

			if (cibcBsa >= 250000 && cibcBsa <= 1000000 && cibcBsa % 25000 == 0) {
				return 1;
			}
		} else {
			if (benefitMap.containsKey("ATPB") && benefitMap.containsKey("CIBC")) {
				Benifict cibc = benefitMap.get("CIBC");
				// Benifict atpb = benefitMap.get("ATPB");
				// Double bsa = calculation.get_personalInfo().getBsa();
				Double cibcBsa = cibc.getSumAssured();
				// Double atpbBsa = atpb.getSumAssured();

				if (cibcBsa >= 250000 && cibcBsa <= 1000000 && cibcBsa % 25000 == 0) {
					return 1;
				}
			}
		}

		return 0;
	}

	public Integer validateENDCIBC() {

		// System.out.println(calculation.get_product() +
		// "]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]");

		if (calculation.get_product() != null) {
			Benifict cibc = benefitMap.get("CIBC");
			// Double bsa = calculation.get_personalInfo().getBsa();
			Double cibcBsa = cibc.getSumAssured();
			// Double atpbBsa = 0.0;

			if (cibcBsa >= 100000 && cibcBsa <= 1000000 && cibcBsa % 25000 == 0) {
				return 1;
			}
		} else {
			if (benefitMap.containsKey("ATPB") && benefitMap.containsKey("CIBC")) {
				Benifict cibc = benefitMap.get("CIBC");
				// Benifict atpb = benefitMap.get("ATPB");
				// Double bsa = calculation.get_personalInfo().getBsa();
				Double cibcBsa = cibc.getSumAssured();
				// Double atpbBsa = atpb.getSumAssured();

				if (cibcBsa >= 100000 && cibcBsa <= 1000000 && cibcBsa % 25000 == 0) {
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
			// Double hrbf = benefitMap.get("HRBF").getSumAssured();
			// Double hrbfc = benefitMap.get("HRBFC").getSumAssured();
			// if (hrbf.equals(hrbfc)) {
			return 1;
			// }
			// return 0;
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

			if (hbc >= 500 && hbc <= 15000 && hbc.equals(hb) && hbc % 100 == 0) {
				return 1;
			}
			return 0;
		}
		return 0;
	}

	public Integer validateInvpTPDDTA() {

		// System.out.println(benefitMap.containsKey("TPDDTA"));

		if (benefitMap.containsKey("TPDDTA")) {
			Benifict benifict = benefitMap.get("TPDDTA");
			Double rbsa = benifict.getSumAssured();
			// System.out.println(rbsa + "TPDDTA" +
			// calculation.get_personalInfo().getBsa());
			if (rbsa.equals(calculation.get_personalInfo().getBsa())) {
				return 1;
			}
		}
		return 0;
	}

	public Integer validateInvpJLB() {
		if (benefitMap.containsKey("JLB")) {
			Benifict benifict = benefitMap.get("JLB");
			Double rbsa = benifict.getSumAssured();
			if (rbsa.equals(calculation.get_personalInfo().getBsa())) {
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

		// System.out.println(benefitMap.containsKey("TPDDTAPL"));

		if (benefitMap.containsKey("TPDDTAPL")) {
			Benifict benifict = benefitMap.get("TPDDTAPL");
			Double rbsa = benifict.getSumAssured();
			if (rbsa.equals(calculation.get_personalInfo().getBsa())) {
				return 1;
			}
		}
		return 0;
	}

	public Integer validateInvpJLBPL() {
		if (benefitMap.containsKey("JLBPL")) {
			Benifict benifict = benefitMap.get("JLBPL");
			Double rbsa = benifict.getSumAssured();
			if (rbsa.equals(calculation.get_personalInfo().getBsa())) {
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

	public String validateArtm(QuotationCalculation calculation) {

		if (!benefitMap.containsKey("L2")) {
			return "L2 is required";
		}
		if (calculation.get_personalInfo().getRetAge() >= 40 && calculation.get_personalInfo().getRetAge() <= 65) {

			System.out.println(calculationUtils.getPayterm(calculation.get_personalInfo().getFrequance())
					* calculation.get_personalInfo().getBsa());
			if (calculationUtils.getPayterm(calculation.get_personalInfo().getFrequance())
					* calculation.get_personalInfo().getBsa() >= 36000) {
				if ((calculation.get_personalInfo().getFrequance().equalsIgnoreCase("S")
						&& calculation.get_personalInfo().getBsa() < 250000)) {
					return "Contribution must be greater than or equal 250000";
				}

				if (calculation.get_personalInfo().getPensionPaingTerm() == 10
						|| calculation.get_personalInfo().getPensionPaingTerm() == 15
						|| calculation.get_personalInfo().getPensionPaingTerm() == 20) {
					Integer paingTerm = Integer.parseInt(calculation.get_personalInfo().getPayingterm());
					if (paingTerm >= 10 && paingTerm <= 47
							|| calculation.get_personalInfo().getFrequance().equals("S")) {
						if (paingTerm <= (calculation.get_personalInfo().getRetAge()
								- calculation.get_personalInfo().getMage())
								|| calculation.get_personalInfo().getFrequance().equals("S")) {

							return "ok";

						} else {
							return "Pension Paying Max Term must " + (calculation.get_personalInfo().getRetAge()
									- calculation.get_personalInfo().getMage());
						}
					} else {
						return "Pension Paying Term must between 10 and 47";
					}
				} else {
					return "Pension Pension Paying Term must be 10, 15 or 20";
				}

			} else {
				return "Yearly Contribution  must be greater than or equal 36000";
			}

		} else {
			return "Retirement Age must between 60 and 40";
		}

	}

	public String validateAIP(Integer age, String frequance, Double contribution, Integer term) {

		if (frequance.equals("S")) {
			if (age <= 70) {
				if (term >= 5) {
					if (age + term <= 75) {
						if (age > 45) {
							if (contribution >= 100000) {
								return "ok";
							} else {
								return "Contribution Must be Minimum 100000";
							}
						} else {
							if (contribution >= 50000) {
								return "ok";
							} else {
								return "Contribution Must be Minimum 50000";
							}
						}
					} else {
						return "Age + Term must be Less Than or Equal 75";
					}
				} else {
					return "Term must be Less Than or Equal 5";
				}

			} else {
				return "Age must be Less Than or Equal 70";
			}
		} else {

			if (age <= 65) {
				if (age + term <= 75) {
					if (term >= 10) {
						if (contribution >= 3000) {
							return "ok";
						} else {
							return "Contribution Must be Minimum 3000";
						}

					} else {
						return "Term must be Less Than or Equal 10";
					}
				} else {
					return "Age + Term must be Less Than or Equal 75";
				}
			} else {
				return "Age must be Less Than or Equal 65";
			}

		}

	}

}
