package org.arpicoinsurance.groupit.main.controller;

import java.util.ArrayList;
import java.util.Date;

import org.arpicoinsurance.groupit.main.model.Logs;
import org.arpicoinsurance.groupit.main.model.Occupation;
import org.arpicoinsurance.groupit.main.service.LogService;
import org.arpicoinsurance.groupit.main.service.OccupationServce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class OccupationController {

	@Autowired
	private OccupationServce occupationService;

	@Autowired
	private LogService logService;

	@RequestMapping(method = RequestMethod.GET, value = "/occupation")
	public ResponseEntity<Object> getAll() {
		try {
			ArrayList<Occupation> occList = (ArrayList<Occupation>) occupationService.getAllOccupations();
			for (Occupation occupation : occList) {
				occupation.setOcupationCreateBy(null);
				occupation.setOcupationCreateDate(null);
				occupation.setOcupationModifyBy(null);
				occupation.setOcupationModifyDate(null);
			}
			return new ResponseEntity<Object>(occList, HttpStatus.OK);
		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage());
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("getAll : OccupationController");
			try {
				logService.saveLog(logs);
			} catch (Exception e1) {
				//System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				//System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// return null;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/occupation/{ocuCode}")
	public ResponseEntity<Object> getByOccupationCode(@PathVariable String ocuCode) {
		try {
			Occupation occList = occupationService.getOccupationByCode(ocuCode);

			return new ResponseEntity<Object>(occList, HttpStatus.OK);
		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage());
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("getAll : OccupationController");
			try {
				logService.saveLog(logs);
			} catch (Exception e1) {
				System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// return null;
	}
}
