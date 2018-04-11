package org.arpicoinsurance.groupit.main.controller;


import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import org.arpicoinsurance.groupit.main.reports.ItextReports;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class ReportController {

	
	@Autowired
	private ItextReports itextReport;
	
	@RequestMapping(value="/printQuotation/{id}",method=RequestMethod.GET,produces = "application/pdf")
	@ResponseBody
	public HttpServletResponse getQuotationByUserId(@PathVariable Integer id, HttpServletResponse response) {
		try {
			Integer quoId=Integer.valueOf(id);
			HashMap< String , Object> details = itextReport.createQuotationReport(quoId);
			
			byte[] contents = (byte[]) details.get("PDF");
			
			response.setContentType("application/pdf");
			response.setHeader( "filename", details.get("quoId").toString());
			response.getOutputStream().write(contents);
			response.getOutputStream().flush();
			
			
			return response;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}