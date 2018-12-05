package org.arpicoinsurance.groupit.main.service.rider.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.arpicoinsurance.groupit.main.dao.RateCardTPDDTASDao;
import org.arpicoinsurance.groupit.main.model.RateCardTPDDTAS;
import org.arpicoinsurance.groupit.main.service.rider.TPDDTASPLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TPDDTASPLServiceImpl implements TPDDTASPLService {

	@Autowired
	private RateCardTPDDTASDao rateCardTPDDTASDao;

	@Override
	public BigDecimal calculateTPDDTASPL(int age, int term, double intrat, String sex, Date chedat, double loanamt) throws Exception {
		// //System.out.println("age : "+age+" term : "+term+" intrat : "+intrat+" sex :
		// "+sex+" loanamt : "+loanamt);
		// TODO Auto-generated method stub
		BigDecimal amount = new BigDecimal(loanamt);
		BigDecimal premiumTPDDTASPL = new BigDecimal(0);
		for (int i = 1; i <= term; ++i) {

			RateCardTPDDTAS rateCardTPDDTAS = rateCardTPDDTASDao
					.findByAgeAndTermAndSexAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, i, sex, chedat,
							chedat, chedat, chedat);
			// //System.out.println("rateCardTPDDTAS : "+ rateCardTPDDTAS.getRate());

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
			// ((@loan_reduction@*@rate@/1000)*1.2)*0.85
			try {
				premium = (((reduction.multiply(new BigDecimal(rateCardTPDDTAS.getRate()))).divide(new BigDecimal(1000),
						8, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(1.2))).multiply(new BigDecimal(0.85))
								.setScale(0, RoundingMode.HALF_UP);
			} catch (Exception e) {
				throw new NullPointerException(
						"TPDDTAS Rate not found at Age : " + age + ", Sex : " + sex + " and Term : " + term);
			}

			premiumTPDDTASPL = premiumTPDDTASPL.add(premium);

			/*
			 * //System.out.println("polyer : "+ String.valueOf(i));
			 * //System.out.println("outyer : "+ String.valueOf(term - (i - 1)));
			 * //System.out.println("outsum : "+ amount.toPlainString());
			 * //System.out.println("lonred : "+ reduction.toPlainString());
			 * //System.out.println("prmrat : "+ rateCardTPDDTAS.getRate());
			 * //System.out.println("premum : "+ premium.toPlainString());
			 */
			amount = outstanding;

		}
		//premiumTPDDTASPL = premiumTPDDTASPL.multiply(new BigDecimal(occupation_loding)).setScale(0,
		//		RoundingMode.HALF_UP);
		// //System.out.println("premiumTPDDTASPL : "+premiumTPDDTASPL.toString());
		return premiumTPDDTASPL;
	}

}
