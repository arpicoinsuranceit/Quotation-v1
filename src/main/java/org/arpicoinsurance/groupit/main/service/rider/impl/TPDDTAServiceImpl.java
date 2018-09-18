package org.arpicoinsurance.groupit.main.service.rider.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.arpicoinsurance.groupit.main.dao.RateCardTPDDTADao;
import org.arpicoinsurance.groupit.main.model.RateCardTPDDTA;
import org.arpicoinsurance.groupit.main.service.rider.TPDDTAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TPDDTAServiceImpl implements TPDDTAService {

	@Autowired
	private RateCardTPDDTADao rateCardTPDDTADao;

	@Override
	public BigDecimal calculateTPDDTA(int age, int term, double intrat, String sex, Date chedat, double loanamt,
			double occupation_loding) throws Exception {
		// System.out.println("age : "+age+" term : "+term+" intrat : "+intrat+" sex :
		// "+sex+" loanamt : "+loanamt);
		// TODO Auto-generated method stub
		BigDecimal amount = new BigDecimal(loanamt);
		BigDecimal premiumTPDDTA = new BigDecimal(0);
		for (int i = 1; i <= term; ++i) {

			RateCardTPDDTA rateCardTPDDTA = rateCardTPDDTADao
					.findByAgeAndTermAndSexAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, i, sex, chedat,
							chedat, chedat, chedat);
			// System.out.println("rateCardTPDDTA : "+ rateCardTPDDTA.getRate());

			// annuity for term
			double annuity = 1 + (intrat / 100);
			annuity = Math.pow(annuity, ((term - (i - 1)) * -1));
			annuity = 1 - annuity;
			annuity /= (intrat / 100);

			// annuity for term -1
			double annuity2 = 1 + (intrat / 100);
			annuity2 = Math.pow(annuity2, ((term - i) * -1));
			annuity2 = 1 - annuity2;
			annuity2 /= (intrat / 100);

			BigDecimal outstanding = amount.multiply(new BigDecimal(annuity2 / annuity)).setScale(8,
					RoundingMode.HALF_UP);

			BigDecimal reduction = amount.subtract(outstanding).setScale(8, RoundingMode.HALF_UP);
			BigDecimal premium = null;
			// @loan_reduction@*@rate@/1000
			try {
				premium = (reduction.multiply(new BigDecimal(rateCardTPDDTA.getRate()))).divide(new BigDecimal(1000), 0,
						BigDecimal.ROUND_HALF_UP);
			} catch (Exception e) {
				throw new NullPointerException(
						"TPDDTA Rate not found at Age : " + age + ", Term : " + term + " and Sex : " + sex);
			}
			premiumTPDDTA = premiumTPDDTA.add(premium);

			/*
			 * System.out.println("polyer : "+ String.valueOf(i));
			 * System.out.println("outyer : "+ String.valueOf(term - (i - 1)));
			 * System.out.println("outsum : "+ amount.toPlainString());
			 * System.out.println("lonred : "+ reduction.toPlainString());
			 * System.out.println("prmrat : "+ rateCardTPDDTA.getRate());
			 * System.out.println("premum : "+ premium.toPlainString());
			 */
			amount = outstanding;

		}
		premiumTPDDTA = premiumTPDDTA.multiply(new BigDecimal(occupation_loding)).setScale(0, RoundingMode.HALF_UP);
		// System.out.println("premiumTPDDTA : "+premiumTPDDTA.toString());
		return premiumTPDDTA;
	}

}
