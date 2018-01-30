package org.arpicoinsurance.groupit.main.common;

import java.math.BigDecimal;
import java.util.Date;

import org.arpicoinsurance.groupit.main.helper.QuoInvpCalResp;
import org.arpicoinsurance.groupit.main.service.rider.ADBSService;
import org.arpicoinsurance.groupit.main.service.rider.ADBService;
import org.arpicoinsurance.groupit.main.service.rider.ATPBService;
import org.arpicoinsurance.groupit.main.service.rider.TPDASBSService;
import org.arpicoinsurance.groupit.main.service.rider.TPDASBService;
import org.springframework.beans.factory.annotation.Autowired;

public class BenifictCalculation {
	
	@Autowired
	private ATPBService atpbService;
	
	@Autowired
	private ADBService adbService;
	
	@Autowired
	private ADBSService adbsService;
	
	@Autowired
	private TPDASBService tpdasbService;
	
	@Autowired
	private TPDASBSService tpdasbsbService;
	
	public Double calculateBenifPremium(String type, Double ridsumasu, String gender, Integer age, String payFrequency,
			Integer term, Double occupationValue, QuoInvpCalResp calResp) throws Exception{
		/*switch (type) {
		case "ADB":
				System.out.println(ridsumasu);
				System.out.println(payFrequency);
				
				BigDecimal adb=adbService.calculateADB(ridsumasu, payFrequency, 0.0,0.0);
				calResp.setAdb(adb.doubleValue());
			break;
		case "ADBS":
				BigDecimal adbs= adbsService.calculateADBS(ridsumasu, payFrequency, 0.0,0.0);
				calResp.setAdbs(adbs.doubleValue());
			break;
		case "ATPB":
				BigDecimal atpb= atpbService.calculateATPB(age, term, new Date(), ridsumasu, payFrequency, 0.0);
				calResp.setAtpb(atpb.doubleValue());
		break;
		case "TPDASB":
				BigDecimal tpdasb= tpdasbService.calculateTPDASB(age, new Date(), ridsumasu, payFrequency, 0.0);
				calResp.setTpdasb(tpdasb.doubleValue());
		break;
		case "TPDASBS":
				BigDecimal tpdasbs= tpdasbsbService.calculateTPDASBS(age, new Date(), ridsumasu, payFrequency, 0.0);
				calResp.setTpdasbs(tpdasbs.doubleValue());
		break;

		default:
			break;
		}
		*/
		return 0.0;
	}
}
