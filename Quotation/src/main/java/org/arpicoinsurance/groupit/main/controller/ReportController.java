package org.arpicoinsurance.groupit.main.controller;

import java.util.ArrayList;

import org.arpicoinsurance.groupit.main.helper.QuotationView;
import org.arpicoinsurance.groupit.main.reports.InvpReports;
import org.arpicoinsurance.groupit.main.service.Quo_Benef_DetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class ReportController {

	@Autowired
	private Quo_Benef_DetailsService quoBenefDetailService;
	
	@Autowired
	private InvpReports invpReport;


	
	@RequestMapping(value="/print",method=RequestMethod.POST)
	public ArrayList<QuotationView> getQuotationByUserId(@RequestBody String id) {
		try {
			System.out.println(id);
			Integer quoId=Integer.valueOf(id);
			ArrayList<QuotationView> detailList=(ArrayList<QuotationView>) quoBenefDetailService.getQuo_Benef_DetailsByQuoDetailId(quoId);
			if(detailList!=null) {
				invpReport.createINVPReport(detailList.get(0));
			}
			
			
			return detailList;
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}