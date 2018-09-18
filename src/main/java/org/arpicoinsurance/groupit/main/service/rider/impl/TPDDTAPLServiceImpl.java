package org.arpicoinsurance.groupit.main.service.rider.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.arpicoinsurance.groupit.main.dao.RateCardTPDDTADao;
import org.arpicoinsurance.groupit.main.model.RateCardTPDDTA;
import org.arpicoinsurance.groupit.main.service.rider.TPDDTAPLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TPDDTAPLServiceImpl implements TPDDTAPLService {

	@Autowired
	private RateCardTPDDTADao rateCardTPDDTADao;

	@Override
	public BigDecimal calculateTPDDTAPL(int age, int term, double intrat, String sex, Date chedat, double loanamt,
			double occupation_loding) throws Exception {
		// System.out.println("age : "+age+" term : "+term+" intrat : "+intrat+" sex :
		// "+sex+" loanamt : "+loanamt);
		// TODO Auto-generated method stub
		BigDecimal amount = new BigDecimal(loanamt);
		BigDecimal premiumTPDDTAPL = new BigDecimal(0);
		for (int i = 1; i <= term; ++i) {

			RateCardTPDDTA rateCardTPDDTA = rateCardTPDDTADao
					.findByAgeAndTermAndSexAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, i, sex, chedat,
							chedat, chedat, chedat);
			/*
			 * System.out.println("rateCardTPDDTA : "+ rateCardTPDDTA.getRate());
			 * System.out.println("age : "+ age); System.out.println("term : "+ i);
			 */

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
			// (@loan_reduction@*@rate@/1000)*1.2
			try {
				premium = ((reduction.multiply(new BigDecimal(rateCardTPDDTA.getRate()))).divide(new BigDecimal(1000),
						8, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(1.2)).setScale(0, RoundingMode.HALF_UP);
			} catch (Exception e) {
				throw new NullPointerException(
						"TPDDTAPL Rate not found at Age : " + age + ", Term : " + term + " and Sex : " + sex);
			}
			premiumTPDDTAPL = premiumTPDDTAPL.add(premium);

			/*
			 * System.out.println("polyer : "+ String.valueOf(i));
			 * System.out.println("outyer : "+ String.valueOf(term - (i - 1)));
			 * System.out.println("outsum : "+ amount.toPlainString());
			 * System.out.println("lonred : "+ reduction.toPlainString());
			 * System.out.println("prmrat : "+ rateCardTPDDTAPL.getRate());
			 * System.out.println("premum : "+ premium.toPlainString());
			 */
			amount = outstanding;

		}
		premiumTPDDTAPL = premiumTPDDTAPL.multiply(new BigDecimal(occupation_loding)).setScale(0, RoundingMode.HALF_UP);
		// System.out.println("premiumTPDDTAPL : "+premiumTPDDTAPL.toString());
		return premiumTPDDTAPL;
	}

}
