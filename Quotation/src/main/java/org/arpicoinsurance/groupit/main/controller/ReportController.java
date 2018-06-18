package org.arpicoinsurance.groupit.main.controller;


import java.util.Date;

import org.arpicoinsurance.groupit.main.model.Logs;
import org.arpicoinsurance.groupit.main.reports.ItextReports;
import org.arpicoinsurance.groupit.main.service.LogService;
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
public class ReportController {

	
	@Autowired
	private ItextReports itextReport;
	
	@Autowired
	private LogService logService;
	
	@RequestMapping(value="/printQuotation/{id}",method=RequestMethod.GET,produces = "application/pdf")
	public ResponseEntity<Object> getQuotationByUserId(@PathVariable Integer id) {
		System.out.println("ddddddddddddddddddddddd" + id);
		
		try {
			Integer quoId=Integer.valueOf(id);
			byte [] pdf = itextReport.createQuotationReport(quoId);
			ResponseEntity<Object> responseEntity = null;
			
			if(pdf == null) {
				responseEntity = new ResponseEntity<Object>(null, HttpStatus.METHOD_NOT_ALLOWED);
			}else {
				responseEntity = new ResponseEntity<Object>(pdf, HttpStatus.OK);
			}
			return responseEntity;
			
		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n Parameters : " + id);
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("getQuotationByUserId : ReportController");
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
		
		//return null;
	}
}