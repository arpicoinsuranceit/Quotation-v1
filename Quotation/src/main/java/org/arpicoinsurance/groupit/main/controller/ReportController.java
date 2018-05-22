package org.arpicoinsurance.groupit.main.controller;


import java.util.Date;

import org.arpicoinsurance.groupit.main.model.Logs;
import org.arpicoinsurance.groupit.main.reports.ItextReports;
import org.arpicoinsurance.groupit.main.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
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
	public byte[] getQuotationByUserId(@PathVariable Integer id) {
		try {
			Integer quoId=Integer.valueOf(id);
			
			return itextReport.createQuotationReport(quoId);
			
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
			throw new RuntimeException(e.getMessage());
		}
		
		//return null;
	}
}