package org.arpicoinsurance.groupit.main.controller;


import org.arpicoinsurance.groupit.main.reports.ItextReports;
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
	
	@RequestMapping(value="/printQuotation/{id}",method=RequestMethod.GET,produces = "application/pdf")
	public byte[] getQuotationByUserId(@PathVariable Integer id) {
		try {
			Integer quoId=Integer.valueOf(id);
			
			return itextReport.createQuotationReport(quoId);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}