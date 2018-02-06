package org.arpicoinsurance.groupit.main.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.dao.RateCardDTADao;
import org.arpicoinsurance.groupit.main.helper.DTAHelper;
import org.arpicoinsurance.groupit.main.helper.DTAShedule;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.model.RateCardDTA;
import org.arpicoinsurance.groupit.main.service.DTAService;
import org.arpicoinsurance.groupit.main.service.custom.CalculateRiders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DTAServiceImpl implements DTAService {

	@Autowired
	private RateCardDTADao rateCardDTADao;
	
	@Autowired
	private CalculateRiders calculateriders;
	
	@Override
	public DTAHelper calculateL2(int age, int term, double intrat, String sex, Date chedat, double loanamt)
			throws Exception {
		DTAHelper dtaHelper=new DTAHelper();
		
		System.out.println("age : "+age+" term : "+term+" intrat : "+intrat+" sex : "+sex+" loanamt : "+loanamt);
		// TODO Auto-generated method stub
		BigDecimal amount = new BigDecimal(loanamt);
		BigDecimal total_premium = new BigDecimal(0);
		ArrayList<DTAShedule> dtaSheduleList = new ArrayList<>();
		for (int i = 1; i <= term; ++i) {

			RateCardDTA rateCardDTA=rateCardDTADao.findByAgeAndTermAndSexAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, i, sex, chedat, chedat, chedat, chedat);
			
            //annuity for term
            double annuity = 1 + (intrat / 100);
            annuity = Math.pow(annuity, ((term - (i - 1)) * -1));
            annuity = 1 - annuity;
            annuity /= (intrat / 100);

            //annuity for term -1
            double annuity2 = 1 + (intrat / 100);
            annuity2 = Math.pow(annuity2, ((term - i) * -1));
            annuity2 = 1 - annuity2;
            annuity2 /= (intrat / 100);
           
            BigDecimal outstanding = amount.multiply(new BigDecimal(annuity2 / annuity)).setScale(8, RoundingMode.HALF_UP);

            BigDecimal reduction = amount.subtract(outstanding).setScale(8, RoundingMode.HALF_UP);

            // @loan_reduction@*@rate@/1000
            BigDecimal premium = (reduction.multiply(new BigDecimal(rateCardDTA.getRate()))).divide(new BigDecimal(1000), 0, BigDecimal.ROUND_HALF_UP);

            DTAShedule shedule=new DTAShedule();
            
            shedule.setLonred(reduction.doubleValue());
            shedule.setOutsum(amount.doubleValue());
            shedule.setOutyer(term - (i - 1));
            shedule.setPolYear(i);
            shedule.setPremum(premium.doubleValue());
            shedule.setPrmrat(rateCardDTA.getRate());
            
            dtaSheduleList.add(shedule);
            
            total_premium = total_premium.add(premium);

            System.out.println("polyer : "+ String.valueOf(i));
            System.out.println("outyer : "+ String.valueOf(term - (i - 1)));
            System.out.println("outsum : "+ amount.toPlainString());
            System.out.println("lonred : "+ reduction.toPlainString());
            System.out.println("prmrat : "+ rateCardDTA.getRate());
            System.out.println("premum : "+ premium.toPlainString());

            amount = outstanding;

        }
		dtaHelper.setBsa(total_premium);
		
		System.out.println("total_premium : "+total_premium.toString());
		return dtaHelper;
	}

	@Override
	public QuotationQuickCalResponse getCalcutatedDta(QuotationCalculation quotationCalculation) throws Exception {
		CalculationUtils calculationUtils = null;
		try {

			QuotationQuickCalResponse calResp = new QuotationQuickCalResponse();
			calculationUtils = new CalculationUtils();
			
			
			Double rebate = calculationUtils.getRebate(quotationCalculation.get_personalInfo().getTerm(),
					quotationCalculation.get_personalInfo().getFrequance());
			
			DTAHelper dtaHelper = calculateL2(quotationCalculation.get_personalInfo().getMage(),
					quotationCalculation.get_personalInfo().getTerm(), 
					quotationCalculation.get_personalInfo().getIntrate(),
					quotationCalculation.get_personalInfo().getMgenger(),
					new Date(), quotationCalculation.get_personalInfo().getBsa());
			
			BigDecimal bsaPremium = dtaHelper.getBsa();
			calResp.setDtaShedules(dtaHelper.getDtaSheduleList());
			
			calResp = calculateriders.getRiders(quotationCalculation, calResp);
			calResp.setBasicSumAssured(calculationUtils.addRebatetoBSAPremium(rebate, bsaPremium));
			
			
			Double tot = calResp.getBasicSumAssured() + calResp.getAddBenif();
			Double adminFee = calculationUtils.getAdminFee(quotationCalculation.get_personalInfo().getFrequance());
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

}
