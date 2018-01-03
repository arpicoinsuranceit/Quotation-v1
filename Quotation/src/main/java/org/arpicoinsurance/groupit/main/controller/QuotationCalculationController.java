package org.arpicoinsurance.groupit.main.controller;

import java.math.BigDecimal;
import java.util.Date;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.helper.QuoCalResp;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.service.INVPService;
import org.arpicoinsurance.groupit.main.service.rider.ADBService;
import org.arpicoinsurance.groupit.main.service.rider.ATPBService;
import org.arpicoinsurance.groupit.main.service.rider.InvpMaturityService;
import org.arpicoinsurance.groupit.main.service.rider.TPDASBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mysql.fabric.xmlrpc.base.Data;

@CrossOrigin (origins="*")
@RestController
public class QuotationCalculationController {
	
	@Autowired
	private INVPService invpService ;
	
	
	@RequestMapping(value="/quoCal",method=RequestMethod.POST)
	public QuoCalResp calculateQuotation(@RequestBody QuotationCalculation calculation) {
		//System.out.println(calculation);
		
		try {		
			QuoCalResp calResp=invpService.getCalcutatedInvp(calculation);
			return calResp;
			/// Calculate BSA Premium ///
			/*BigDecimal premumBSA = invpService.calculateL2(30, calculation.get_personalInfo().getTerm(), 8.0, new Date(), 500000.0, CalculationUtils.getPayterm(calculation.get_personalInfo().getFrequance()));
			System.out.println("BSA Premium : " +premumBSA);
			/// Add Rebate to BSA ///
			double premum = invpService.addRebatetoBSAPremium(CalculationUtils.getRebate(calculation.get_personalInfo().getTerm(), calculation.get_personalInfo().getFrequance()), premumBSA);
			System.out.println("BSA Premium with Rebate : " +premum);
			/// Calculate L3 Maturity ///
			BigDecimal sumAssuredL3 = invpService.calculateMaturity(30, calculation.get_personalInfo().getTerm(), 8.0, new Date(), 500000.0, CalculationUtils.getPayterm(calculation.get_personalInfo().getFrequance()));
			System.out.println("Illustrated Maturity Value 8% : " +sumAssuredL3);
			/// Calculate L4 Maturity ///
			BigDecimal sumAssuredL4 = invpService.calculateMaturity(30, calculation.get_personalInfo().getTerm(), 10.0, new Date(), 500000.0, CalculationUtils.getPayterm(calculation.get_personalInfo().getFrequance()));
			System.out.println("Illustrated Maturity Value 10% : " +sumAssuredL4);
			/// Calculate L5 Maturity ///
			BigDecimal sumAssuredL5 = invpService.calculateMaturity(30, calculation.get_personalInfo().getTerm(), 12.0, new Date(), 500000.0, CalculationUtils.getPayterm(calculation.get_personalInfo().getFrequance()));
			System.out.println("Illustrated Maturity Value 12% : " +sumAssuredL5);
			/// Calculate ATPB ///
			BigDecimal premumATPB = atpbService.calculateATPB(30, calculation.get_personalInfo().getTerm(), new Date(), 500000.0, calculation.get_personalInfo().getFrequance(), 1.0);
			System.out.println("ATPB Premium : " +premumATPB);
			/// Calculate ADB ///
			BigDecimal premumADB = adbService.calculateADB(500000.0, calculation.get_personalInfo().getFrequance(), 1.0);
			System.out.println("ADB Premium : " +premumADB);
			/// Calculate TPDASB ///
			BigDecimal premumTPDASB = tpdasbService.calculateTPDASB(30, new Date(), 500000.0, calculation.get_personalInfo().getFrequance(), 1.0);
			System.out.println("TPDASB Premium : " +premumTPDASB);
			
			*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value="/ageCal",method=RequestMethod.GET)
	public Integer calculateAge() {
		//System.out.println(calculation);
		
		try {		
			
			return 30;
			/// Calculate BSA Premium ///
			/*BigDecimal premumBSA = invpService.calculateL2(30, calculation.get_personalInfo().getTerm(), 8.0, new Date(), 500000.0, CalculationUtils.getPayterm(calculation.get_personalInfo().getFrequance()));
			System.out.println("BSA Premium : " +premumBSA);
			/// Add Rebate to BSA ///
			double premum = invpService.addRebatetoBSAPremium(CalculationUtils.getRebate(calculation.get_personalInfo().getTerm(), calculation.get_personalInfo().getFrequance()), premumBSA);
			System.out.println("BSA Premium with Rebate : " +premum);
			/// Calculate L3 Maturity ///
			BigDecimal sumAssuredL3 = invpService.calculateMaturity(30, calculation.get_personalInfo().getTerm(), 8.0, new Date(), 500000.0, CalculationUtils.getPayterm(calculation.get_personalInfo().getFrequance()));
			System.out.println("Illustrated Maturity Value 8% : " +sumAssuredL3);
			/// Calculate L4 Maturity ///
			BigDecimal sumAssuredL4 = invpService.calculateMaturity(30, calculation.get_personalInfo().getTerm(), 10.0, new Date(), 500000.0, CalculationUtils.getPayterm(calculation.get_personalInfo().getFrequance()));
			System.out.println("Illustrated Maturity Value 10% : " +sumAssuredL4);
			/// Calculate L5 Maturity ///
			BigDecimal sumAssuredL5 = invpService.calculateMaturity(30, calculation.get_personalInfo().getTerm(), 12.0, new Date(), 500000.0, CalculationUtils.getPayterm(calculation.get_personalInfo().getFrequance()));
			System.out.println("Illustrated Maturity Value 12% : " +sumAssuredL5);
			/// Calculate ATPB ///
			BigDecimal premumATPB = atpbService.calculateATPB(30, calculation.get_personalInfo().getTerm(), new Date(), 500000.0, calculation.get_personalInfo().getFrequance(), 1.0);
			System.out.println("ATPB Premium : " +premumATPB);
			/// Calculate ADB ///
			BigDecimal premumADB = adbService.calculateADB(500000.0, calculation.get_personalInfo().getFrequance(), 1.0);
			System.out.println("ADB Premium : " +premumADB);
			/// Calculate TPDASB ///
			BigDecimal premumTPDASB = tpdasbService.calculateTPDASB(30, new Date(), 500000.0, calculation.get_personalInfo().getFrequance(), 1.0);
			System.out.println("TPDASB Premium : " +premumTPDASB);
			
			*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
