package org.arpicoinsurance.groupit.main.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.service.ATPService;
import org.arpicoinsurance.groupit.main.service.custom.CalculateRiders;
import org.springframework.beans.factory.annotation.Autowired;

public class ATPServiceImpl implements ATPService {
	
	@Autowired
	private CalculateRiders calculateriders;

	@Override
	public BigDecimal calculateMaturity(int term, double bassum) throws Exception {

		BigDecimal maturity = null;

		switch (term) {
		case 5:
			maturity = new BigDecimal(bassum).multiply(new BigDecimal(130));
			break;
		case 6:
			maturity = new BigDecimal(bassum).multiply(new BigDecimal(135));
			break;
		case 7:
			maturity = new BigDecimal(bassum).multiply(new BigDecimal(145));
			break;
		case 8:
			maturity = new BigDecimal(bassum).multiply(new BigDecimal(155));
			break;
		case 9:
			maturity = new BigDecimal(bassum).multiply(new BigDecimal(165));
			break;
		case 10:
			maturity = new BigDecimal(bassum).multiply(new BigDecimal(175));
			break;

		default:
			break;
		}
		return maturity;
	}

	@Override
	public BigDecimal calculateNaturalDeath(double maturity, int age, double bassum) throws Exception {
		BigDecimal ndc = null;

		if (18 <= age && age <= 35) {
			ndc = (new BigDecimal(bassum).multiply(new BigDecimal(0.25))).add(new BigDecimal(maturity));
		} else if (36 <= age && age <= 45) {
			ndc = (new BigDecimal(bassum).multiply(new BigDecimal(0.20))).add(new BigDecimal(maturity));
		} else if (46 <= age && age <= 55) {
			ndc = (new BigDecimal(bassum).multiply(new BigDecimal(0.15))).add(new BigDecimal(maturity));
		} else if (56 <= age && age <= 60) {
			ndc = (new BigDecimal(bassum).multiply(new BigDecimal(0.10))).add(new BigDecimal(maturity));
		} else if (61 <= age && age <= 65) {
			ndc = (new BigDecimal(bassum).multiply(new BigDecimal(0.05))).add(new BigDecimal(maturity));
		}

		return ndc;
	}

	@Override
	public QuotationQuickCalResponse getCalcutatedAtp(QuotationCalculation calculation) throws Exception {
		CalculationUtils calculationUtils = null;
		try {

			QuotationQuickCalResponse calResp = new QuotationQuickCalResponse();
			calculationUtils = new CalculationUtils();
			/// Calculate Rebate Premium ///
			
			/// Calculate BSA Premium ///
//			BigDecimal bsaMonthly = calculateL2(quotationCalculation.get_personalInfo().getMocu(),
//					quotationCalculation.get_personalInfo().getMage(),
//					quotationCalculation.get_personalInfo().getTerm(), calculationUtils.getRebate("M"), new Date(),
//					quotationCalculation.get_personalInfo().getBsa(), calculationUtils.getPayterm("M"), calResp, false);
//
//			BigDecimal bsaYearly = bsaMonthly.multiply(new BigDecimal(12)).setScale(2);
//
//			BigDecimal bsaPremium = calculateL2(quotationCalculation.get_personalInfo().getMocu(),
//					quotationCalculation.get_personalInfo().getMage(),
//					quotationCalculation.get_personalInfo().getTerm(), rebate, new Date(),
//					quotationCalculation.get_personalInfo().getBsa(),
//					calculationUtils.getPayterm(quotationCalculation.get_personalInfo().getFrequance()), calResp, true);

//			calResp.setBasicSumAssured(bsaPremium.doubleValue());
//			calResp.setBsaYearlyPremium(bsaYearly.doubleValue());
			calResp.setAtp(true);
			
			calResp = calculateriders.getRiders(calculation, calResp);
			calResp.setAt6(calculateMaturity(calculation.get_personalInfo().getTerm(), calculation.get_personalInfo().getBsa()).doubleValue());
			calResp.setAt8(calculateNaturalDeath(calResp.getAt6(),calculation.get_personalInfo().getMage(), calculation.get_personalInfo().getBsa()).doubleValue());
			
			//calResp.setMainLifeHealthReq(healthRequirmentsService.getSumAtRiskDetailsMainLife(quotationCalculation));

//			if (calculation.get_personalInfo().getSage() != null
//					&& calculation.get_personalInfo().getSgenger() != null) {
//				calResp.setSpouseHealthReq(healthRequirmentsService.getSumAtRiskDetailsSpouse(quotationCalculation));
//			}
//			calResp.setAt6(calculateMaturity(quotationCalculation.get_personalInfo().getTerm(),
//					quotationCalculation.get_personalInfo().getBsa()).doubleValue());
//			calResp.setGuaranteed(calculateMaturity(quotationCalculation.get_personalInfo().getTerm(),
//					quotationCalculation.get_personalInfo().getBsa()).doubleValue());

			Double tot = calResp.getBasicSumAssured() + calResp.getAddBenif();
			Double adminFee = calculationUtils.getAdminFee(calculation.get_personalInfo().getFrequance());
			Double tax = calculationUtils.getTaxAmount(tot + adminFee);
			Double extraOE = adminFee + tax;
			calResp.setExtraOE(extraOE);
			calResp.setTotPremium(tot + extraOE);

			return calResp;

		} finally {
			if (calculationUtils != null) {
				calculationUtils = null;
			}
		}
	}

	@Override
	public HashMap<String, Object> saveQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation,
			Integer id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, Object> editQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation,
			Integer userId, Integer qdId, Integer type) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
