package org.arpicoinsurance.groupit.main.validation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.arpicoinsurance.groupit.main.helper.BenefictHistory;
import org.arpicoinsurance.groupit.main.helper.Benifict;
import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.springframework.stereotype.Component;

@Component
public class HealthValidation {

	public String validateHealthEndArpAtrmAtrmAsfp(List<BenefictHistory> benefictHistories,
			QuotationQuickCalResponse calresp, InvpSaveQuotation _invpSaveQuotation) {

		System.out.println("validateHealthEndArpAtrmAtrmAsfp");
		
		System.out.println(calresp.getTotPremium());
		
		System.out.println(benefictHistories.size());

		benefictHistories.forEach(System.out::println);

		String prods[] = { "ARP", "END1", "ASFP", "INVP", "ATRM", "ASIP", "ARTM" };

		List<String> pprNumList = new ArrayList<>();
		List<String> prodList = Arrays.asList(prods);

		Double totalPrm = 0.0;

		System.out.println("_invpSaveQuotation.get_personalInfo().get_plan().get_frequance() : "
				+ _invpSaveQuotation.get_personalInfo().get_plan().get_frequance());

		switch (_invpSaveQuotation.get_personalInfo().get_plan().get_frequance()) {
		case "Monthly":
			totalPrm = calresp.getTotPremium() * 12;
			break;
		case "M":
			totalPrm = calresp.getTotPremium() * 12;
			break;
		case "Quartaly":
			totalPrm = calresp.getTotPremium() * 4;
			break;
		case "Q":
			totalPrm = calresp.getTotPremium() * 4;
			break;
		case "Half Yearly":
			totalPrm = calresp.getTotPremium() * 2;
			break;
		case "H":
			totalPrm = calresp.getTotPremium() * 2;
			break;
		case "Yearly":
			totalPrm = calresp.getTotPremium();
			break;
		case "Y":
			totalPrm = calresp.getTotPremium();
			break;
		default:
			break;
		}

		System.out.println("totalPrm : " + totalPrm);

		Double suhcb = 0.0;
		Double hcb = 0.0;

		for (BenefictHistory e : benefictHistories) {

			if (prodList.contains(e.getPrdcod())) {
				if (e.getType().equals("M")) {
					switch (e.getRiderCode()) {

					case "HCBF":
						hcb = e.getSumAssuredTot();
						break;

					case "HCBI":
						hcb = e.getSumAssuredTot();
						break;

					case "HRB":
						hcb = e.getSumAssuredTot();
						break;

					case "SHCBI":
						suhcb = e.getSumAssuredTot();
						break;

					case "SUHRB":
						suhcb = e.getSumAssuredTot();
						break;

					default:
						break;
					}

					if (!pprNumList.contains(e.getPprnum())) {
						pprNumList.add(e.getPprnum());

						switch (e.getFrequance()) {
						case "M":
							totalPrm = totalPrm + e.getTotPremium() * 12;
							break;
						case "Q":
							totalPrm = totalPrm + e.getTotPremium() * 4;
							break;
						case "H":
							totalPrm = totalPrm + e.getTotPremium() * 2;
							break;
						case "Y":
							totalPrm = totalPrm + e.getTotPremium();
							break;
						default:
							break;
						}
					}
				}

				if (e.getType().equals("S")) {
					switch (e.getRiderCode()) {

					case "HCBFS":
						hcb = e.getSumAssuredTot();
						break;

					case "HCBIS":
						hcb = e.getSumAssuredTot();
						break;

					case "HRBS":
						hcb = e.getSumAssuredTot();
						break;

					case "SHCBIS":
						suhcb = e.getSumAssuredTot();
						break;

					case "SUHRBS":
						suhcb = e.getSumAssuredTot();
						break;

					default:
						break;
					}
				}
			}
		}
		System.out.println("totalPrm : " + totalPrm);
		System.out.println("hcb  : " + hcb);
		System.out.println("suhcb  : " + suhcb);
		if (suhcb > 0) {
			if (calresp.getHrbf() != null && calresp.getHrbf() > 0) {
				return "HCBF can't get Because previous policy has SUHCB";
			}

			if (calresp.getHrbi() != null && calresp.getHrbi() > 0) {
				return "HCBI can't get Because previous policy has SUHCB";
			}
		}

		if (hcb > 0) {
			if (calresp.getSuhrb() != null && calresp.getSuhrb() > 0) {
				return "SUHBC can't get Because previous policy has HCB";
			}
		}

		if (calresp.getHrbi() > 0 || calresp.getHrbf() > 0) {
			Double cover = 0.0;
			for (Benifict benifict : _invpSaveQuotation.get_riderDetails().get_mRiders()) {
				if (benifict.getType().equalsIgnoreCase("HRB") || benifict.getType().equalsIgnoreCase("HRBI")
						|| benifict.getType().equalsIgnoreCase("HRBF") || benifict.getType().equalsIgnoreCase("HCBI")
						|| benifict.getType().equalsIgnoreCase("HCBF")) {
					cover += benifict.getSumAssured();
				}
			}

			System.out.println("cover :  " + cover);

			if ((cover + hcb) > 500000) {
				return "Max HCB is " + (500000 - (hcb)) + " because previous policies already have Rs." + hcb + ".";
			}

			System.out.println(cover == 100000 || cover == 200000 || cover == 300000);
			System.out.println((totalPrm / 12) > 3000);

			if (((cover + hcb) == 100000 || (cover + hcb) == 200000 || (cover + hcb) == 300000) && (totalPrm / 12) < 3000) {
				return "Minmum Total Premium of All Policies Must be 3000";
			}
			if (((cover + hcb) == 400000) && (totalPrm / 12) < 4000) {
				return "Minmum Total Premium of All Policies Must be 4000";
			}
			if (((cover + hcb) == 500000) && (totalPrm / 12) < 5000) {
				return "Minmum Total Premium of All Policies Must be 5000";
			}
		}

		if (calresp.getSuhrb() > 0 && suhcb > 0) {
			return "Can't get suhcb because previous policies already have Rs." + suhcb + ".";
		}

		if (calresp.getSuhrb() > 0) {
			Double cover = 0.0;

			for (Benifict benifict : _invpSaveQuotation.get_riderDetails().get_mRiders()) {
				if (benifict.getType().equalsIgnoreCase("SUHRB") || benifict.getType().equalsIgnoreCase("SHCBI")
						|| benifict.getType().equalsIgnoreCase("SHCBF")) {
					cover += benifict.getSumAssured();
				}
			}

			if ((cover == 600000) && (totalPrm / 12) < 6000) {
				return "Minmum Total Premium of All Policies Must be 6000";
			}
			if ((cover == 800000) && (totalPrm / 12) < 7500) {
				return "Minmum Total Premium of All Policies Must be 7500";
			}
			if ((cover == 1000000) && (totalPrm / 12) < 10000) {
				return "Minmum Total Premium of All Policies Must be 10000";
			}
		}

		return "ok";

	}

	public String validateHealthInvpAsip(List<BenefictHistory> benefictHistories, QuotationQuickCalResponse calresp,
			InvpSaveQuotation _invpSaveQuotation) {

		String prods[] = { "ARP", "END1", "ASFP", "INVP", "ATRM", "ASIP", "ARTM" };

		List<String> pprNumList = new ArrayList<>();
		List<String> prodList = Arrays.asList(prods);

		Double totalPrm = 0.0;

		switch (_invpSaveQuotation.get_personalInfo().get_plan().get_frequance()) {
		case "Monthly":
			totalPrm = calresp.getTotPremium() * 12;
			break;
		case "M":
			totalPrm = calresp.getTotPremium() * 12;
			break;
		case "Quartaly":
			totalPrm = calresp.getTotPremium() * 4;
			break;
		case "Q":
			totalPrm = calresp.getTotPremium() * 4;
			break;
		case "Half Yearly":
			totalPrm = calresp.getTotPremium() * 2;
			break;
		case "H":
			totalPrm = calresp.getTotPremium() * 2;
			break;
		case "Yearly":
			totalPrm = calresp.getTotPremium();
			break;
		case "Y":
			totalPrm = calresp.getTotPremium();
			break;
		default:
			break;
		}

		System.out.println("totalPrm : " + totalPrm);

		Double suhcb = 0.0;
		Double hcb = 0.0;

		for (BenefictHistory e : benefictHistories) {

			if (prodList.contains(e.getPrdcod())) {
				if (e.getType().equals("M")) {
					switch (e.getRiderCode()) {

					case "HCBF":
						hcb = e.getSumAssuredTot();
						break;

					case "HCBI":
						hcb = e.getSumAssuredTot();
						break;

					case "HRB":
						hcb = e.getSumAssuredTot();
						break;

					case "SHCBI":
						suhcb = e.getSumAssuredTot();
						break;

					case "SUHRB":
						suhcb = e.getSumAssuredTot();
						break;

					default:
						break;
					}

					if (!pprNumList.contains(e.getPprnum())) {
						pprNumList.add(e.getPprnum());

						switch (e.getFrequance()) {
						case "M":
							totalPrm = totalPrm + e.getTotPremium() * 12;
							break;
						case "Q":
							totalPrm = totalPrm + e.getTotPremium() * 4;
							break;
						case "H":
							totalPrm = totalPrm + e.getTotPremium() * 2;
							break;
						case "Y":
							totalPrm = totalPrm + e.getTotPremium();
							break;
						default:
							break;
						}
					}
				}

				if (e.getType().equals("S")) {
					switch (e.getRiderCode()) {

					case "HCBFS":
						hcb = e.getSumAssuredTot();
						break;

					case "HCBIS":
						hcb = e.getSumAssuredTot();
						break;

					case "HRBS":
						hcb = e.getSumAssuredTot();
						break;

					case "SHCBIS":
						suhcb = e.getSumAssuredTot();
						break;

					case "SUHRBS":
						suhcb = e.getSumAssuredTot();
						break;

					default:
						break;
					}
				}
			}
		}
		if (suhcb > 0) {
			if (calresp.getHrbf() != null && calresp.getHrbf() > 0) {
				return "HCBF can't get Because previous policy has SUHCB";
			}

			if (calresp.getHrbi() != null && calresp.getHrbi() > 0) {
				return "HCBI can't get Because previous policy has SUHCB";
			}
		}

		if (hcb > 0) {
			if (calresp.getSuhrb() != null && calresp.getSuhrb() > 0) {
				return "SUHBC can't get Because previous policy has HCB";
			}
		}

		if (calresp.getHrbi() > 0 || calresp.getHrbf() > 0) {
			Double cover = 0.0;
			for (Benifict benifict : _invpSaveQuotation.get_riderDetails().get_mRiders()) {
				if (benifict.getType().equalsIgnoreCase("HRB") || benifict.getType().equalsIgnoreCase("HRBI")
						|| benifict.getType().equalsIgnoreCase("HRBF") || benifict.getType().equalsIgnoreCase("HCBI")
						|| benifict.getType().equalsIgnoreCase("HCBF")) {
					cover += benifict.getSumAssured();
				}
			}

			System.out.println("cover :  " + cover);

			if ((cover + hcb) > 500000) {
				return "Max HCB is " + (500000 - (hcb)) + " because previous policies already have Rs." + hcb + ".";
			}


			if (((cover + hcb) == 100000) && (totalPrm / 12) < 3000) {
				return "Minmum Total Premium of All Policies Must be 3,000";
			}
			if (((cover + hcb) == 200000) && (totalPrm / 12) < 5000) {
				return "Minmum Total Premium of All Policies Must be 5,000";
			}
			if (((cover + hcb) == 300000) && (totalPrm / 12) < 7500) {
				return "Minmum Total Premium of All Policies Must be 7,500";
			}
			if (((cover + hcb) == 400000) && (totalPrm / 12) < 10000) {
				return "Minmum Total Premium of All Policies Must be 10,000";
			}
			if (((cover + hcb) == 500000) && (totalPrm / 12) < 12500) {
				return "Minmum Total Premium of All Policies Must be 12,500";
			}
		}

		if (calresp.getSuhrb() > 0 && suhcb > 0) {
			return "Can't get suhcb because previous policies already have Rs." + suhcb + ".";
		}

		if (calresp.getSuhrb() > 0) {
			Double cover = 0.0;

			for (Benifict benifict : _invpSaveQuotation.get_riderDetails().get_mRiders()) {
				if (benifict.getType().equalsIgnoreCase("SUHRB") || benifict.getType().equalsIgnoreCase("SHCBI")
						|| benifict.getType().equalsIgnoreCase("SHCBF")) {
					cover += benifict.getSumAssured();
				}
			}

			if ((cover == 600000) && (totalPrm / 12) < 15000) {
				return "Minmum Total Premium of All Policies Must be 15,000";
			}
			if ((cover == 800000) && (totalPrm / 12) < 17500) {
				return "Minmum Total Premium of All Policies Must be 17,500";
			}
			if ((cover == 1000000) && (totalPrm / 12) < 20000) {
				return "Minmum Total Premium of All Policies Must be 20,000";
			}
		}

		return "ok";

	}

	public String validateHealthEndArpAtrmAtrmAsfp(QuotationQuickCalResponse calResp,
			InvpSaveQuotation _invpSaveQuotation) {

		Double suhcb = 0.0;
		Double hcb = 0.0;

		boolean isHcb = false;
		boolean isShcb = false;
		

		for (Benifict benifict : _invpSaveQuotation.get_riderDetails().get_mRiders()) {
			if (benifict.getType().equalsIgnoreCase("HRB") || benifict.getType().equalsIgnoreCase("HRBI")
					|| benifict.getType().equalsIgnoreCase("HRBF") || benifict.getType().equalsIgnoreCase("HCBI")
					|| benifict.getType().equalsIgnoreCase("HCBF")) {
				hcb += benifict.getSumAssured();

				isHcb = true;
			}

			if (benifict.getType().equalsIgnoreCase("SUHRB") || benifict.getType().equalsIgnoreCase("SHCBI")
					|| benifict.getType().equalsIgnoreCase("SHCBF")) {
				
				suhcb += benifict.getSumAssured();

				isShcb = true;
			}

		}
		
		

		String resp = "fail";
		
		if((isHcb && !isShcb) || (!isHcb && isShcb) || (!isHcb && !isShcb)) {
			resp = "ok";
		}
		
		System.out.println(_invpSaveQuotation.get_personalInfo().get_plan().get_frequance());
		
		switch (_invpSaveQuotation.get_personalInfo().get_plan().get_frequance()) {

		case "Monthly":
			if (isHcb) {
				resp = getWithoutNICEndArtmArpAsfpAtrm(calResp.getTotPremium(), hcb, true);
			}

			if (isShcb) {
				resp = getWithoutNICEndArtmArpAsfpAtrm(calResp.getTotPremium(), suhcb, false);
			}
			break;
		case "Quartaly":
			if (isHcb) {
				resp = getWithoutNICEndArtmArpAsfpAtrm(
						new BigDecimal(calResp.getTotPremium()).divide(new BigDecimal(3)).doubleValue(), hcb, true);
			}

			if (isShcb) {
				resp = getWithoutNICEndArtmArpAsfpAtrm(
						new BigDecimal(calResp.getTotPremium()).divide(new BigDecimal(3)).doubleValue(), suhcb, false);
			}
			break;
		case "Half Yearly":
			if (isHcb) {
				resp = getWithoutNICEndArtmArpAsfpAtrm(
						new BigDecimal(calResp.getTotPremium()).divide(new BigDecimal(6)).doubleValue(), hcb, true);
			}

			if (isShcb) {
				resp = getWithoutNICEndArtmArpAsfpAtrm(
						new BigDecimal(calResp.getTotPremium()).divide(new BigDecimal(6)).doubleValue(), suhcb, false);
			}
			break;
		case "Yearly":
			if (isHcb) {
				resp = getWithoutNICEndArtmArpAsfpAtrm(
						new BigDecimal(calResp.getTotPremium()).divide(new BigDecimal(12)).doubleValue(), hcb, true);
			}

			if (isShcb) {
				resp = getWithoutNICEndArtmArpAsfpAtrm(
						new BigDecimal(calResp.getTotPremium()).divide(new BigDecimal(12)).doubleValue(), suhcb, false);
			}
			break;
		default:
			break;
		}

		return resp;
	}

	private String getWithoutNICEndArtmArpAsfpAtrm(Double totPremium, Double sumasu, boolean isHCB) {

		if (isHCB) {
			if (sumasu == 100000 || sumasu == 200000 || sumasu == 300000) {
				if (totPremium < 3000) {
					return "Premium Must be 3,000 for get HCB 100,000, 200,000, 300,000";
				}
			}
			if (sumasu == 400000) {
				if (totPremium < 4000) {
					return "Premium Must be 4,000 for get HCB 400,000";
				}
			}
			if (sumasu == 500000) {
				if (totPremium < 5000) {
					return "Premium Must be 5,000 for get HCB 500,000";
				}
			}

			return "ok";
		} else {

			if (sumasu == 600000) {
				if (totPremium < 6000) {
					return "Premium Must be 6,000 for get SHCB 600,000";
				}
			}
			if (sumasu == 800000) {
				if (totPremium < 7500) {
					return "Premium Must be 7,500 for get SHCB 800,000";
				}
			}
			if (sumasu == 1000000) {
				if (totPremium < 10000) {
					return "Premium Must be 10,000 for get SHCB 1000,000";
				}
			}

			return "ok";

		}

	}
	
	
	
	public String validateHealthInvpAsfp(QuotationQuickCalResponse calResp,
			InvpSaveQuotation _invpSaveQuotation) {

		Double suhcb = 0.0;
		Double hcb = 0.0;

		boolean isHcb = false;
		boolean isShcb = false;

		for (Benifict benifict : _invpSaveQuotation.get_riderDetails().get_mRiders()) {
			if (benifict.getType().equalsIgnoreCase("HRB") || benifict.getType().equalsIgnoreCase("HRBI")
					|| benifict.getType().equalsIgnoreCase("HRBF") || benifict.getType().equalsIgnoreCase("HCBI")
					|| benifict.getType().equalsIgnoreCase("HCBF")) {
				hcb += benifict.getSumAssured();

				isHcb = true;
			}

			if (benifict.getType().equalsIgnoreCase("SUHRB") || benifict.getType().equalsIgnoreCase("SHCBI")
					|| benifict.getType().equalsIgnoreCase("SHCBF")) {
				
				suhcb += benifict.getSumAssured();

				isShcb = true;
			}

		}

		String resp = "fail";
		
		
		if((isHcb && !isShcb) || (!isHcb && isShcb) || (!isHcb && !isShcb)) {
			resp = "ok";
		}
		
		System.out.println(_invpSaveQuotation.get_personalInfo().get_plan().get_frequance());
		
		switch (_invpSaveQuotation.get_personalInfo().get_plan().get_frequance()) {
		
		case "Monthly":
			if (isHcb) {
				resp = getWithoutNICInvpAsfp(calResp.getTotPremium(), hcb, true);
			}

			if (isShcb) {
				resp = getWithoutNICInvpAsfp(calResp.getTotPremium(), suhcb, false);
			}
			break;
		case "Quartaly":
			if (isHcb) {
				resp = getWithoutNICInvpAsfp(
						new BigDecimal(calResp.getTotPremium()).divide(new BigDecimal(3)).doubleValue(), hcb, true);
			}

			if (isShcb) {
				resp = getWithoutNICInvpAsfp(
						new BigDecimal(calResp.getTotPremium()).divide(new BigDecimal(3)).doubleValue(), suhcb, false);
			}
			break;
		case "Half Yearly":
			if (isHcb) {
				resp = getWithoutNICInvpAsfp(
						new BigDecimal(calResp.getTotPremium()).divide(new BigDecimal(6)).doubleValue(), hcb, true);
			}

			if (isShcb) {
				resp = getWithoutNICInvpAsfp(
						new BigDecimal(calResp.getTotPremium()).divide(new BigDecimal(6)).doubleValue(), suhcb, false);
			}
			break;
		case "Yearly":
			if (isHcb) {
				resp = getWithoutNICInvpAsfp(
						new BigDecimal(calResp.getTotPremium()).divide(new BigDecimal(12)).doubleValue(), hcb, true);
			}

			if (isShcb) {
				resp = getWithoutNICInvpAsfp(
						new BigDecimal(calResp.getTotPremium()).divide(new BigDecimal(12)).doubleValue(), suhcb, false);
			}
			break;
		default:
			break;
		}

		return resp;
	}

	private String getWithoutNICInvpAsfp(Double totPremium, Double sumasu, boolean isHCB) {
		
		System.out.println(totPremium);
		System.out.println(sumasu);

		if (isHCB) {
			if (sumasu == 100000) {
				if (totPremium < 3000) {
					return "Premium Must be 3,000 for get HCB 100,000";
				}
			}
			if (sumasu == 200000) {
				if (totPremium < 5000) {
					return "Premium Must be 5,000 for get HCB 200,000";
				}
			}
			if (sumasu == 300000) {
				if (totPremium < 7500) {
					return "Premium Must be 7,500 for get HCB 300,000";
				}
			}
			if (sumasu == 400000) {
				if (totPremium < 10000) {
					return "Premium Must be 10,000 for get HCB 400,000";
				}
			}
			if (sumasu == 500000) {
				if (totPremium < 12500) {
					return "Premium Must be 12,500 for get HCB 500,000";
				}
			}

			return "ok";
		} else {

			if (sumasu == 600000) {
				if (totPremium < 15000) {
					return "Premium Must be 10,000 for get SHCB 600,000";
				}
			}
			if (sumasu == 800000) {
				if (totPremium < 17500) {
					return "Premium Must be 17,500 for get SHCB 800,000";
				}
			}
			if (sumasu == 1000000) {
				if (totPremium < 20000) {
					return "Premium Must be 20,000 for get SHCB 1000,000";
				}
			}

			return "ok";

		}

	}

}
