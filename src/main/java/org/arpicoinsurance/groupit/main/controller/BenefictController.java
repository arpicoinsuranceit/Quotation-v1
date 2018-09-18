package org.arpicoinsurance.groupit.main.controller;

import java.util.Date;

import org.arpicoinsurance.groupit.main.model.Logs;
import org.arpicoinsurance.groupit.main.service.BeneficeService;
import org.arpicoinsurance.groupit.main.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class BenefictController {

	@Autowired
	private BeneficeService beneficeService;
	
	@Autowired
	private LogService logService;
	
	@RequestMapping(value = "activebenef", method = RequestMethod.GET)
	public ResponseEntity<Object> getActivatedBeneficts() {
		try {
			return new ResponseEntity<Object>(beneficeService.getActiveBenefices(), HttpStatus.OK);
		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage());
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("getActivatedBeneficts : BenefictController");
			try {
				logService.saveLog(logs);
			} catch (Exception e1) {
				System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
