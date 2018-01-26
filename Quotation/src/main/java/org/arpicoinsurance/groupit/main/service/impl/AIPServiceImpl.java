package org.arpicoinsurance.groupit.main.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.dao.RateCardAIPDao;
import org.arpicoinsurance.groupit.main.helper.AIPCalResp;
import org.arpicoinsurance.groupit.main.helper.AipCalShedule;
import org.arpicoinsurance.groupit.main.helper.QuoInvpCalResp;
import org.arpicoinsurance.groupit.main.helper.QuotationInvpCalculation;
import org.arpicoinsurance.groupit.main.model.RateCardAIP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AIPServiceImpl implements org.arpicoinsurance.groupit.main.service.AIPService {
	@Autowired
	private RateCardAIPDao rateCardAIPDao;

	public AIPServiceImpl() {
	}

	public AIPCalResp calculateAIPMaturaty(Integer term, Double adbrat, Double fundmarat, Double intrat,
			Double contribution, Date chedat, String paymod, boolean schedule) throws Exception {

		AIPCalResp aipCalResp = null;
		ArrayList< AipCalShedule > aipCalShedules=null;
		try {
			aipCalResp = new AIPCalResp();

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
			System.out.println(" term : " + term + " adbrat : " + adbrat + " fundmarat : " + fundmarat + " intrat : "
					+ intrat + " paymod : " + paymod);
			aipCalShedules = new ArrayList<>();
			for (int i = 1; i <= term.intValue(); i++) {
				int polyear;
				if (i > 3) {
					polyear = 4;
				} else {
					polyear = i;
				}

				RateCardAIP rateCardAIP = null;

				if (paymod.equalsIgnoreCase("S")) {
					rateCardAIP = rateCardAIPDao
							.findByTermtoOrTermtoLessThanAndTermfromOrTermfromGreaterThanAndPaymodAndStrdatLessThanOrStrdat(
									term.intValue(), term.intValue(), term.intValue(), term.intValue(), paymod, chedat,
									chedat);
				} else {
					rateCardAIP = rateCardAIPDao
							.findByTermtoOrTermtoLessThanAndTermfromOrTermfromGreaterThanAndPaymodAndPolyearAndStrdatLessThanOrStrdat(
									term.intValue(), term.intValue(), term.intValue(), term.intValue(), paymod, polyear,
									chedat, chedat);
				}

				System.out.println(" term : " + term + " polyear : " + polyear + " Rate : " + rateCardAIP.getRate());
				BigDecimal fund_rate = new BigDecimal(rateCardAIP.getRate().doubleValue());

				for (int j = 1; j <= calculationUtils.getPayterm(paymod); j++) {
					AipCalShedule aipCalShedule= new AipCalShedule();
					if (schedule) {
						System.out.println("polyer : " + i + " polmth : " + j + " opnfun : " + open_fund.toString());
						aipCalShedule.setPolicyYear(i);
						aipCalShedule.setPolicyMonth(j);
						aipCalShedule.setOpeningFee(open_fund.doubleValue());
					}

					if ((paymod.equalsIgnoreCase("S")) && (i > 1)) {
						fund_amount = new BigDecimal("0");
						cum_premium = new BigDecimal("0");
					} else {
						fund_amount = premium.multiply(fund_rate.divide(new BigDecimal("100"))).setScale(6, 4);
						cum_premium = cum_premium.add(premium);
					}

					balance_bfi = fund_amount.add(open_fund);

					double interest = Math.pow(1.0D + interest_rate.divide(new BigDecimal("100")).doubleValue(),
							12.0D / calculationUtils.getPayterm(paymod) / 12.0D) - 1.0D;

					interest_annum = balance_bfi.multiply(new BigDecimal(interest)).setScale(6, 4);

					balance_bmf = balance_bfi.add(interest_annum).setScale(6, 4);

					mgt_fees = balance_bmf.multiply(management_fee.divide(new BigDecimal("100"))).setScale(6, 4);
					close_bal = balance_bmf.subtract(mgt_fees).setScale(6, 4);

					open_fund = new BigDecimal(close_bal.toPlainString()).setScale(6, 4);

					if (schedule) {
						
						System.out.println("cumcon : " + cum_premium.toString() + " fndamt : " + fund_amount.toString()
								+ " fndbfi : " + balance_bfi.toString() + " intanm : " + interest_annum.toString()
								+ " fndbmf : " + balance_bmf.toString() + " mgtfee : " + mgt_fees.toString()
								+ " fndclo : " + close_bal.toString() + " fndclo : " + close_bal.toString());

						aipCalShedule.setComCon(cum_premium.doubleValue());
						aipCalShedule.setFundAmt(fund_amount.doubleValue());
						aipCalShedule.setFndBfi(balance_bfi.doubleValue());
						aipCalShedule.setIntAmt(interest_annum.doubleValue());
						aipCalShedule.setFndBmf(balance_bmf.doubleValue());
						aipCalShedule.setMgtFee(mgt_fees.doubleValue());
						aipCalShedule.setFndClo(close_bal.doubleValue());
						
						if (close_bal.compareTo(cum_premium) == -1) {
							System.out.println(
									"adbcov : " + cum_premium.multiply(adb_rate).setScale(2, 4).toPlainString());
							
							aipCalShedule.setAdbCov(cum_premium.multiply(adb_rate).setScale(2, 4).doubleValue());
						} else {
							System.out
									.println("adbcov : " + close_bal.multiply(adb_rate).setScale(2, 4).toPlainString());
							
							aipCalShedule.setAdbCov(close_bal.multiply(adb_rate).setScale(2, 4).doubleValue());
						}
						aipCalShedules.add(aipCalShedule);
					}

					total_amount = new BigDecimal(close_bal.toPlainString()).setScale(2, 4);
					
				}
				
				
			}

			System.out.println("maturity " + intrat + " : " + total_amount.toString());
			
			maturity = total_amount;
			
			Double adminFee=calculationUtils.getAdminFee(paymod);
			Double tax = calculationUtils.getTaxAmount(adminFee+maturity.doubleValue());
			
			
			
			
			aipCalResp.setMaturaty(maturity.doubleValue());
			aipCalResp.setAipCalShedules(aipCalShedules);
			aipCalResp.setExtraOe(adminFee+tax);
			return aipCalResp;
		} finally {
			if (aipCalResp != null) {
				aipCalResp = null;
			}
		}

		/**/
	}

	@Override
	public QuoInvpCalResp getCalcutatedAip(QuotationInvpCalculation quotationInvpCalculation) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}