package org.arpicoinsurance.groupit.main.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.arpicoinsurance.groupit.main.helper.BenefictHistory;
import org.arpicoinsurance.groupit.main.helper.Benifict;
import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.springframework.stereotype.Component;

@Component
public class HealthValidation {

	public String validateHealthEndArpAtrmAtrmAsfp(List<BenefictHistory> benefictHistories,
			QuotationQuickCalResponse calresp, InvpSaveQuotation _invpSaveQuotation) {

		boolean inSuhrb = false;
		boolean inHrb = false;

		Double suhrbSumAssured = 0.0;
		Double hrbSumAssured = 0.0;

		for (Benifict detail : _invpSaveQuotation.get_riderDetails().get_mRiders()) {
			switch (detail.getType()) {
			case "HRB":

				break;

			default:
				break;
			}
		}

		String prods[] = { "ARP", "END", "ASFP", "INVP", "ATRM", "ASIP", "ARTM" };

		List<String> pprNumList = new ArrayList<>();
		List<String> prodList = Arrays.asList(prods);

		Double totalPrm = calresp.getTotPremium();

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
						totalPrm += e.getTotPremium();
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
		
		return null;

	}

	public String validateHealthInvpAsip(List<BenefictHistory> benefictHistories, QuotationCalculation calculation,
			InvpSaveQuotation _invpSaveQuotation) {
		return null;
	}

}
