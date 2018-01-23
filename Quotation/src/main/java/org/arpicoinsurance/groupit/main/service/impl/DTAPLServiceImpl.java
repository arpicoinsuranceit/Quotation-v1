package org.arpicoinsurance.groupit.main.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.arpicoinsurance.groupit.main.dao.RateCardDTADao;
import org.arpicoinsurance.groupit.main.model.RateCardDTA;
import org.arpicoinsurance.groupit.main.service.DTAPLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DTAPLServiceImpl implements DTAPLService {

	@Autowired
	private RateCardDTADao rateCardDTADao;
	
	@Override
	public BigDecimal calculateL2(int age, int term, double intrat, String sex, Date chedat, double loanamt)
			throws Exception {
		System.out.println("age : "+age+" term : "+term+" intrat : "+intrat+" sex : "+sex+" loanamt : "+loanamt);
		// TODO Auto-generated method stub
		BigDecimal amount = new BigDecimal(loanamt);
		BigDecimal total_premium = new BigDecimal(0);
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

            // (@loan_reduction@*@rate@/1000)+((@loan_reduction@*@rate@/1000)*0.2)
            BigDecimal premium = ((reduction.multiply(new BigDecimal(rateCardDTA.getRate()))).divide(new BigDecimal(1000), 8, BigDecimal.ROUND_HALF_UP)).add((((reduction.multiply(new BigDecimal(rateCardDTA.getRate()))).divide(new BigDecimal(1000), 8, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(0.2)))).setScale(0, RoundingMode.HALF_UP);
            
            total_premium = total_premium.add(premium);

            System.out.println("polyer : "+ String.valueOf(i));
            System.out.println("outyer : "+ String.valueOf(term - (i - 1)));
            System.out.println("outsum : "+ amount.toPlainString());
            System.out.println("lonred : "+ reduction.toPlainString());
            System.out.println("prmrat : "+ rateCardDTA.getRate());
            System.out.println("premum : "+ premium.toPlainString());

            amount = outstanding;

        }
		
		System.out.println("total_premium : "+total_premium.toString());
		return total_premium;
	}

}