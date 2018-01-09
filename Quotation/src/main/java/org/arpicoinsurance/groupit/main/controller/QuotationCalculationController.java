package org.arpicoinsurance.groupit.main.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.helper.QuoCalResp;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.service.INVPService;
import org.arpicoinsurance.groupit.main.service.rider.ADBService;
import org.arpicoinsurance.groupit.main.service.rider.ATPBService;
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value="/ageCal",method=RequestMethod.POST)
	public Long calculateAge(@RequestBody String dob) {
		try {		
			LocalDate dateOfBirth = LocalDate.parse(dob);
		     LocalDate currentDate = LocalDate.now();
		     long diffInYears = ChronoUnit.YEARS.between(dateOfBirth, currentDate);
			return diffInYears+1;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
