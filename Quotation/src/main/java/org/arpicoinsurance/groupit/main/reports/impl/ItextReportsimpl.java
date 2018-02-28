package org.arpicoinsurance.groupit.main.reports.impl;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.arpicoinsurance.groupit.main.helper.QuoCustomer;
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
		QuoCustomer quoCustomer = setCustomerDetails(quotationDetails);

		
	    if(quotationView != null) {
			
		if(quotationDetails.getQuotation().getProducts().getProductCode().equalsIgnoreCase("AIP")) {
				quotationReportService.createAIPReport(quotationDetails, quotationView, quoCustomer);
				
		} else if (quotationDetails.getQuotation().getProducts().getProductCode().equalsIgnoreCase("AIB")) {
				quotationReportService.createAIBReport(quotationDetails, quotationView, quoCustomer);
		
		} else if (quotationDetails.getQuotation().getProducts().getProductCode().equalsIgnoreCase("INVP") || 
				  (quotationDetails.getQuotation().getProducts().getProductCode().equalsIgnoreCase("ASIP"))) {
				quotationReportService.createINVPReport(quotationDetails, quotationView, quoCustomer);
		
		} else if (quotationDetails.getQuotation().getProducts().getProductCode().equalsIgnoreCase("DTA") || 
				  (quotationDetails.getQuotation().getProducts().getProductCode().equalsIgnoreCase("DTAPL"))) {
				quotationReportService.createDTAReport(quotationDetails, quotationView, quoCustomer);
		
		} else if (quotationDetails.getQuotation().getProducts().getProductCode().equalsIgnoreCase("ATRM")) {
				quotationReportService.createATRMReport(quotationDetails, quotationView, quoCustomer);
		
		}else if (quotationDetails.getQuotation().getProducts().getProductCode().equalsIgnoreCase("END1")) {
				quotationReportService.createEND1Report(quotationDetails, quotationView, quoCustomer);
		
		}else if (quotationDetails.getQuotation().getProducts().getProductCode().equalsIgnoreCase("ASFP")) {
				quotationReportService.createASFPReport(quotationDetails, quotationView, quoCustomer);
		
		}else if (quotationDetails.getQuotation().getProducts().getProductCode().equalsIgnoreCase("ARP")) {
				quotationReportService.createARPReport(quotationDetails, quotationView, quoCustomer);
		}
		
		
		
		}
		
		
		return "success";
			
		
	        
	}
	
	//set customer and spouse details according to quotationdetail object
		private QuoCustomer setCustomerDetails(QuotationDetails quoDetails) {
			QuoCustomer customer=new QuoCustomer();
			customer.setTerm(quoDetails.getPolTerm());
			customer.setMode(quoDetails.getPayMode());
			
			if(quoDetails.getPayMode()!=null) {
				if(quoDetails.getPayMode().equals("M")) {
					customer.setModePremium(quoDetails.getPremiumMonth());
					customer.setTotPremium(quoDetails.getPremiumMonthT());
				}else if(quoDetails.getPayMode().equals("Y")) {
					customer.setModePremium(quoDetails.getPremiumYear());
					customer.setTotPremium(quoDetails.getPremiumYearT());
				}else if(quoDetails.getPayMode().equals("Q")) {
					customer.setModePremium(quoDetails.getPremiumQuater());
					customer.setTotPremium(quoDetails.getPremiumQuaterT());
				}else if(quoDetails.getPayMode().equals("H")) {
					customer.setModePremium(quoDetails.getPremiumHalf());
					customer.setTotPremium(quoDetails.getPremiumHalfT());
				}else if(quoDetails.getPayMode().equals("S")) {
					customer.setModePremium(quoDetails.getPremiumSingle());
					customer.setTotPremium(quoDetails.getPremiumSingleT());
				}else {
					
				}
				SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
				
				LocalDate dateOfBirth = LocalDate.parse(dateFormat.format(quoDetails.getCustomerDetails().getCustDob()));
			    LocalDate currentDate = LocalDate.parse(dateFormat.format(quoDetails.getQuotationquotationCreateDate()));
			    long diffInYears = ChronoUnit.YEARS.between(dateOfBirth, currentDate);
			    diffInYears+=1;
			    String age=Long.toString(diffInYears);
			
				customer.setMainLifeName(quoDetails.getCustomerDetails().getCustName());
				customer.setMainLifeOccupation(quoDetails.getCustomerDetails().getOccupation().getOcupationName());

				customer.setMainLifeAge(Integer.parseInt(age));
				
				LocalDate sdateOfBirth = LocalDate.parse(dateFormat.format(quoDetails.getCustomerDetails().getCustDob()));
			    LocalDate scurrentDate = LocalDate.parse(dateFormat.format(quoDetails.getQuotationquotationCreateDate()));
			    long sdiffInYears = ChronoUnit.YEARS.between(sdateOfBirth, scurrentDate);
			    sdiffInYears+=1;
			    String sage=Long.toString(sdiffInYears);
				
				if(quoDetails.getSpouseDetails()!=null) {
					customer.setSpouseName(quoDetails.getSpouseDetails().getCustName());
					customer.setSpouseOccupation(quoDetails.getSpouseDetails().getOccupation().getOcupationName());
					customer.setSpouseAge(Integer.parseInt(sage));
				}else {
					customer.setSpouseName(null);
					customer.setSpouseOccupation(null);
					customer.setSpouseAge(null);
				}
				
			
			}
			
					
			return customer;
		}
	
}