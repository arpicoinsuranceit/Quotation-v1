package org.arpicoinsurance.groupit.main.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.dao.RateCardAIPDao;
import org.arpicoinsurance.groupit.main.helper.QuoCalResp;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.model.RateCardAIP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AIPServiceImpl implements org.arpicoinsurance.groupit.main.service.AIPService
{
  @Autowired
  private RateCardAIPDao rateCardAIPDao;
  
  public AIPServiceImpl() {}
  
  public QuoCalResp getCalcutatedAip(QuotationCalculation quotationCalculation) throws Exception
  {
    return null;
  }
  
  public BigDecimal calculateAIPMaturaty(Integer term, Double adbrat, Double fundmarat, Double intrat, Double contribution, Date chedat, String paymod, boolean schedule)
    throws Exception
  {
    CalculationUtils calculationUtils = new CalculationUtils();
    BigDecimal maturity = new BigDecimal(0);
    BigDecimal open_fund = new BigDecimal("0");
    BigDecimal fund_amount = new BigDecimal("0");
    BigDecimal balance_bfi = new BigDecimal("0");
    BigDecimal interest_annum = new BigDecimal("0");
    BigDecimal balance_bmf = new BigDecimal("0");
    BigDecimal mgt_fees = new BigDecimal("0");
    BigDecimal close_bal = new BigDecimal("0");
    BigDecimal premium = new BigDecimal(contribution.doubleValue());
    BigDecimal total_amount = new BigDecimal("0");
    BigDecimal cum_premium = new BigDecimal("0");
    BigDecimal management_fee = new BigDecimal(fundmarat.doubleValue());
    BigDecimal interest_rate = new BigDecimal(intrat.doubleValue());
    BigDecimal adb_rate = new BigDecimal(adbrat.doubleValue());
    System.out.println(" term : " + term + " adbrat : " + adbrat + " fundmarat : " + fundmarat + " intrat : " + intrat + " paymod : " + paymod);
    for (int i = 1; i <= term.intValue(); i++) {
      int polyear;
      if (i > 3) {
        polyear = 4;
      } else {
        polyear = i;
      }
      
      RateCardAIP rateCardAIP = null;
      
      if (paymod.equalsIgnoreCase("S")) {
        rateCardAIP = rateCardAIPDao.findByTermtoOrTermtoLessThanAndTermfromOrTermfromGreaterThanAndPaymodAndStrdatLessThanOrStrdat(term.intValue(), term.intValue(), term.intValue(), term.intValue(), paymod, chedat, chedat);
      } else {
        rateCardAIP = rateCardAIPDao.findByTermtoOrTermtoLessThanAndTermfromOrTermfromGreaterThanAndPaymodAndPolyearAndStrdatLessThanOrStrdat(term.intValue(), term.intValue(), term.intValue(), term.intValue(), paymod, polyear, chedat, chedat);
      }
      
      System.out.println(" term : " + term + " polyear : " + polyear + " Rate : " + rateCardAIP.getRate());
      BigDecimal fund_rate = new BigDecimal(rateCardAIP.getRate().doubleValue());
      
      for (int j = 1; j <= calculationUtils.getPayterm(paymod); j++) {
        if (schedule) {
          System.out.println("polyer : " + i + " polmth : " + j + " opnfun : " + open_fund.toString());
        }
        
        if ((paymod.equalsIgnoreCase("S")) && (i > 1)) {
          fund_amount = new BigDecimal("0");
          cum_premium = new BigDecimal("0");
        } else {
          fund_amount = premium.multiply(fund_rate.divide(new BigDecimal("100"))).setScale(6, 4);
          cum_premium = cum_premium.add(premium);
        }
        
        balance_bfi = fund_amount.add(open_fund);
        
        double interest = Math.pow(1.0D + interest_rate.divide(new BigDecimal("100")).doubleValue(), 12.0D / calculationUtils.getPayterm(paymod) / 12.0D) - 1.0D;
        
        interest_annum = balance_bfi.multiply(new BigDecimal(interest)).setScale(6, 4);
        
        balance_bmf = balance_bfi.add(interest_annum).setScale(6, 4);
        
        mgt_fees = balance_bmf.multiply(management_fee.divide(new BigDecimal("100"))).setScale(6, 4);
        close_bal = balance_bmf.subtract(mgt_fees).setScale(6, 4);
        
        open_fund = new BigDecimal(close_bal.toPlainString()).setScale(6, 4);
        
        if (schedule) {
          System.out.println("cumcon : " + cum_premium.toString() + " fndamt : " + fund_amount.toString() + " fndbfi : " + balance_bfi.toString() + 
            " intanm : " + interest_annum.toString() + " fndbmf : " + balance_bmf.toString() + " mgtfee : " + mgt_fees.toString() + 
            " fndclo : " + close_bal.toString() + " fndclo : " + close_bal.toString());
          
          if (close_bal.compareTo(cum_premium) == -1) {
            System.out.println("adbcov : " + cum_premium.multiply(adb_rate).setScale(2, 4).toPlainString());
          } else {
            System.out.println("adbcov : " + close_bal.multiply(adb_rate).setScale(2, 4).toPlainString());
          }
        }
        
        total_amount = new BigDecimal(close_bal.toPlainString()).setScale(2, 4);
      }
    }
    




    System.out.println("maturity " + intrat + " : " + total_amount.toString());
    maturity = total_amount;
    return maturity;
  }
}