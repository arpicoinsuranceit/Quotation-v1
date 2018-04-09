package org.arpicoinsurance.groupit.main.service.rider.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import javax.transaction.Transactional;

import org.arpicoinsurance.groupit.main.helper.Benifict;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.service.HealthRequirmentsService;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class HealthRequirmentsDetailsServiceImpl implements HealthRequirmentsService {

	@Override
	public HashMap<String, Object> getSumAtRiskDetails(QuotationCalculation calculation, Double previous,
			String custCode) {
		String product = calculation.get_product();

		Double riskCurrent = 0.0;

		switch (product) {
		case "AIB":
			riskCurrent = 0.0;
			break;
		case "AIP":
			riskCurrent = 0.0;
			break;
		case "ARP":
			riskCurrent = calculateRickArpAsipAtrmE(calculation);
			break;
		case "ASFP":
			riskCurrent = calculation.get_personalInfo().getBsa();
			break;
		case "ASIP":
			riskCurrent = calculateRickArpAsipAtrmE(calculation);
			break;
		case "ATRM":
			riskCurrent = calculateRickArpAsipAtrmE(calculation);
			break;
		case "DTA":
			riskCurrent = calculation.get_personalInfo().getBsa();
			break;
		case "DTAPL":
			riskCurrent = calculation.get_personalInfo().getBsa();
			break;
		case "END1":
			riskCurrent = calculateRickArpAsipAtrmE(calculation);
			break;
		case "INVP":
			riskCurrent = calculateRickArpAsipAtrmE(calculation);
			break;

		default:
			break;
		}

		return null;
	}

	private Double calculateRickArpAsipAtrmE(QuotationCalculation calculation) {
		BigDecimal sumAtRisk = new BigDecimal(calculation.get_personalInfo().getBsa());

		ArrayList<Benifict> benifictListM = calculation.get_riderDetails().get_mRiders();
		ArrayList<Benifict> benifictListS = calculation.get_riderDetails().get_sRiders();

		for (Benifict benifict : benifictListM) {

			String type = benifict.getType();

			switch (type) {
			case "ATPB":
				sumAtRisk.add(new BigDecimal(benifict.getSumAssured()));
				break;
			case "FEB":
				sumAtRisk.add(new BigDecimal(benifict.getSumAssured()));
				break;
			case "CIB":
				sumAtRisk.add(new BigDecimal(benifict.getSumAssured()).multiply(new BigDecimal(0.5)));
				break;
			case "MFIBD":
				sumAtRisk.add(new BigDecimal(benifict.getSumAssured()));
				break;
			case "MFIBDT":
				sumAtRisk.add(new BigDecimal(benifict.getSumAssured()));
				break;
			default:
				break;
			}

		}

		for (Benifict benifict : benifictListS) {

			String type = benifict.getType();
			
			switch (type) {
			case "BSAS":
				sumAtRisk.add(new BigDecimal(benifict.getSumAssured()));
				break;
			case "SCB":
				sumAtRisk.add(new BigDecimal(benifict.getSumAssured()));
				break;
			case "FEBS":
				sumAtRisk.add(new BigDecimal(benifict.getSumAssured()));
				break;
			case "CIBS":
				sumAtRisk.add(new BigDecimal(benifict.getSumAssured()).multiply(new BigDecimal(0.5)));
				break;
			case "SCIB":
				sumAtRisk.add(new BigDecimal(benifict.getSumAssured()).multiply(new BigDecimal(0.5)));
				break;
			default:
				break;
			}

		}

		return sumAtRisk.add(new BigDecimal(calculation.get_personalInfo().getBsa())).doubleValue();
	}

}
