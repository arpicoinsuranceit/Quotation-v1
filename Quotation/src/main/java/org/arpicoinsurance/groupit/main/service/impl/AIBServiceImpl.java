package org.arpicoinsurance.groupit.main.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.dao.RateCardAIBDao;
import org.arpicoinsurance.groupit.main.model.RateCardAIB;
import org.arpicoinsurance.groupit.main.service.AIBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AIBServiceImpl implements AIBService {

	@Autowired
	private RateCardAIBDao rateCardAIBDao;
	
	@Override
	public BigDecimal calculateAIBMaturaty(Integer term, Double adbrat, Double fundmarat, Double fundrat,
			Double contribution, Date chedat, String paymod) throws Exception {
		CalculationUtils calculationUtils = new CalculationUtils();
		BigDecimal maturity  = new BigDecimal(0);
		BigDecimal open_fund = new BigDecimal("0");
        BigDecimal fund_amount = new BigDecimal("0");
        BigDecimal balance_bfi = new BigDecimal("0");
        BigDecimal interest_annum = new BigDecimal("0");
        BigDecimal balance_bmf = new BigDecimal("0");
        BigDecimal mgt_fees = new BigDecimal("0");
        BigDecimal close_bal = new BigDecimal("0");
        BigDecimal premium = new BigDecimal(contribution);
        BigDecimal total_amount = new BigDecimal("0");
        BigDecimal cum_premium = new BigDecimal("0");        
        BigDecimal management_fee = new BigDecimal(fundmarat);
        BigDecimal fund_rate = new BigDecimal(fundrat);
        RateCardAIB rateCardAIB = rateCardAIBDao.findByTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(term, chedat, chedat, chedat, chedat);
		BigDecimal interest_rate = new BigDecimal(rateCardAIB.getRate());
		System.out.println("term : "+term+" adbrat : "+adbrat+" fundmarat : "+fundmarat+" fundrat : "+fundrat+" intrat : "+interest_rate+" paymod : "+paymod);
		for (int i = 1; i <= term; i++) {
			for (int j = 1; j <= calculationUtils.getPayterm(paymod); j++) {
				
				if(paymod.equalsIgnoreCase("S") && i > 1){
					fund_amount = new BigDecimal("0");
                    cum_premium = new BigDecimal("0");
				}else{
					fund_amount = premium.multiply(fund_rate.divide(new BigDecimal("100"))).setScale(6, BigDecimal.ROUND_HALF_UP);
	                cum_premium = cum_premium.add(premium);
				}

                balance_bfi = fund_amount.add(open_fund);

                double interest = Math.pow((1 + interest_rate.divide(new BigDecimal("100")).doubleValue()), ((12.00 / calculationUtils.getPayterm(paymod)) / 12.00)) - 1;

                interest_annum = balance_bfi.multiply(new BigDecimal(interest)).setScale(6, BigDecimal.ROUND_HALF_UP);

                balance_bmf = balance_bfi.add(interest_annum).setScale(6, BigDecimal.ROUND_HALF_UP);

                mgt_fees = balance_bmf.multiply(management_fee.divide(new BigDecimal("100"))).setScale(6, BigDecimal.ROUND_HALF_UP);
                close_bal = balance_bmf.subtract(mgt_fees).setScale(6, BigDecimal.ROUND_HALF_UP);

                open_fund = new BigDecimal(close_bal.toPlainString()).setScale(6, BigDecimal.ROUND_HALF_UP);
                                
                total_amount = new BigDecimal(close_bal.toPlainString()).setScale(2, BigDecimal.ROUND_HALF_UP);
                
			}
			
		}
		
		System.out.println("maturity : "+total_amount.toString());
		maturity = total_amount;
		return maturity;
	}

}
