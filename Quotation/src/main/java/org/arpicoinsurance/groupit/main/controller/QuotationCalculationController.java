package org.arpicoinsurance.groupit.main.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import org.arpicoinsurance.groupit.main.service.ARPService;
import org.arpicoinsurance.groupit.main.service.ASFPService;
import org.arpicoinsurance.groupit.main.service.ASIPService;
import org.arpicoinsurance.groupit.main.service.ATRMService;
import org.arpicoinsurance.groupit.main.service.DTAPLService;
import org.arpicoinsurance.groupit.main.service.DTAService;
import org.arpicoinsurance.groupit.main.service.ENDService;
import org.arpicoinsurance.groupit.main.service.rider.JLBPLService;
import org.arpicoinsurance.groupit.main.service.rider.JLBService;
import org.arpicoinsurance.groupit.main.service.rider.SFPOService;
import org.arpicoinsurance.groupit.main.service.rider.TPDDTAPLService;
import org.arpicoinsurance.groupit.main.service.rider.TPDDTASPLService;
import org.arpicoinsurance.groupit.main.service.rider.TPDDTASService;
import org.arpicoinsurance.groupit.main.service.rider.TPDDTAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin (origins="*")
@RestController
public class QuotationCalculationController {
	

	
	@Autowired
	private ASIPService asipService;
	
	@Autowired
	private ARPService arpService;
	
	@Autowired
	private ASFPService asfpService;
	
	@Autowired
	private SFPOService sfpoService;
	
	@Autowired
	private ATRMService atrmService;
	
	@Autowired
	private ENDService endService;
	
	@Autowired
	private DTAService dtaService;
	
	@Autowired
	private TPDDTAService tpddtaService;
	
	@Autowired
	private JLBService jlbService;
	
	@Autowired
	private TPDDTASService tpddtasService;
	
	@Autowired
	private DTAPLService dtaplService;
	
	@Autowired
	private TPDDTAPLService tpddtaplService;
	
	@Autowired
	private JLBPLService jlbplService;
	
	@Autowired
	private TPDDTASPLService tpddtasplService;
	
	
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
	
	
	@RequestMapping(value="/asfpCal",method=RequestMethod.POST)
	public String calculateASFP() {
		/*try {		
			asfpService.calculateL10(29, 10, 6.0, new Date(), 100000.0, 1);
			asfpService.calculateL2(10, 100000.00);
			sfpoService.calculateSFPO(29, 10, new Date(), 1000000.0, "Y", 1.0);
			return "ok";
		
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		return null;
	}
	
	@RequestMapping(value="/atrmCal",method=RequestMethod.POST)
	public String calculateATRM() {
		try {		
			atrmService.calculateL2(1,29, 30, 0.0, new Date(), 2500000.0, 12);
			return "ok";
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value="/endCal",method=RequestMethod.POST)
	public String calculateEND() {
		try {		
			endService.calculateL2(1,29, 10, 0.0, new Date(), 500000.0, 12);
			endService.calculateMaturity(10, 500000.00);
			return "ok";
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value="/dtaCal",method=RequestMethod.POST)
	public String calculateDTA() {
		/*try {		
			dtaService.calculateL2(29, 10, 22.0, "M", new Date(), 15000000.0);
			tpddtaService.calculateTPDDTA(29, 10, 22.0, "M", new Date(), 15000000.0);
			jlbService.calculateJLB(27, 10, 22.0, "F", new Date(), 15000000.0);
			tpddtasService.calculateTPDDTAS(27, 10, 22.0, "F", new Date(), 15000000.0);
			return "ok";
		
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		return null;
	}
	
	@RequestMapping(value="/dtaplCal",method=RequestMethod.POST)
	public String calculateDTAPL() {
		/*try {		
			dtaplService.calculateL2(29, 10, 22.0, "M", new Date(), 15000000.0);
			tpddtaplService.calculateTPDDTAPL(29, 10, 22.0, "M", new Date(), 15000000.0);
			jlbplService.calculateJLBPL(27, 10, 22.0, "F", new Date(), 15000000.0);
			tpddtasplService.calculateTPDDTASPL(27, 10, 22.0, "F", new Date(), 15000000.0);
			return "ok";
		
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		return null;
	}
		
	
}