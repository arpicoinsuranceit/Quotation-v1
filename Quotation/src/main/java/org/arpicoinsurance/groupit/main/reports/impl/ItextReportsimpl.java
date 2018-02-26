package org.arpicoinsurance.groupit.main.reports.impl;

import org.arpicoinsurance.groupit.main.helper.QuotationView;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;
import org.arpicoinsurance.groupit.main.reports.ItextReports;
import org.arpicoinsurance.groupit.main.reports.QuotationReportService;
import org.arpicoinsurance.groupit.main.service.Quo_Benef_DetailsService;
import org.arpicoinsurance.groupit.main.service.QuotationDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItextReportsimpl implements ItextReports{
	
		
	@Autowired
	private Quo_Benef_DetailsService quoBenefDetailService;
	
	@Autowired
	private QuotationDetailsService quotationDetailsService;
	
	@Autowired
	private QuotationReportService quotationReportService;
	
	
	@Override
	public String createQuotationReport(Integer quoId) throws Exception {
		
		QuotationDetails quotationDetails=quotationDetailsService.findQuotationDetails(quoId);
		QuotationView quotationView = quoBenefDetailService.getQuo_Benef_DetailByQuoDetailId(quotationDetails);
		
		//System.out.println(quotationView.getCustDetails().);
		
		if(quotationView != null) {
			
		if(quotationDetails.getQuotation().getProducts().getProductCode().equalsIgnoreCase("AIP")) {
				quotationReportService.createAIPReport(quotationDetails, quotationView);
				
		} else if (quotationDetails.getQuotation().getProducts().getProductCode().equalsIgnoreCase("AIB")) {
				quotationReportService.createAIBReport(quotationDetails, quotationView);
		
		} else if (quotationDetails.getQuotation().getProducts().getProductCode().equalsIgnoreCase("INVP") || 
				  (quotationDetails.getQuotation().getProducts().getProductCode().equalsIgnoreCase("ASIP"))) {
				quotationReportService.createINVPReport(quotationDetails, quotationView);
		
		} else if (quotationDetails.getQuotation().getProducts().getProductCode().equalsIgnoreCase("DTA") || 
				  (quotationDetails.getQuotation().getProducts().getProductCode().equalsIgnoreCase("DTAPL"))) {
				quotationReportService.createDTAReport(quotationDetails, quotationView);
		
		} else if (quotationDetails.getQuotation().getProducts().getProductCode().equalsIgnoreCase("ATRM")) {
				quotationReportService.createATRMReport(quotationDetails, quotationView);
		
		}else if (quotationDetails.getQuotation().getProducts().getProductCode().equalsIgnoreCase("END1")) {
				quotationReportService.createEND1Report(quotationDetails, quotationView);
		
		}else if (quotationDetails.getQuotation().getProducts().getProductCode().equalsIgnoreCase("ASFP")) {
				quotationReportService.createASFPReport(quotationDetails, quotationView);
		
		}else if (quotationDetails.getQuotation().getProducts().getProductCode().equalsIgnoreCase("ARP")) {
				quotationReportService.createARPReport(quotationDetails, quotationView);
		}
		
		
		
		}
		
		
		return "success";
			
		
	        
	}
	
}