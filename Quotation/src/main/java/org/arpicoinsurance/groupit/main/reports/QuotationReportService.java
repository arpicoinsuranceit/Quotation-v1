package org.arpicoinsurance.groupit.main.reports;

import org.arpicoinsurance.groupit.main.helper.QuoCustomer;
import org.arpicoinsurance.groupit.main.helper.QuotationView;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;

public interface QuotationReportService {
	
	String createAIPReport(QuotationDetails quotationDetails,QuotationView quotationView, QuoCustomer quoCustomer) throws Exception;
	
	String createAIBReport(QuotationDetails quotationDetails,QuotationView quotationView, QuoCustomer quoCustomer) throws Exception;
	
	String createINVPReport(QuotationDetails quotationDetails,QuotationView quotationView, QuoCustomer quoCustomer) throws Exception;

	String createDTAReport(QuotationDetails quotationDetails,QuotationView quotationView, QuoCustomer quoCustomer) throws Exception;

	String createATRMReport(QuotationDetails quotationDetails,QuotationView quotationView, QuoCustomer quoCustomer) throws Exception;

	String createEND1Report(QuotationDetails quotationDetails,QuotationView quotationView, QuoCustomer quoCustomer) throws Exception;

	String createASFPReport(QuotationDetails quotationDetails,QuotationView quotationView, QuoCustomer quoCustomer) throws Exception;

	String createARPReport(QuotationDetails quotationDetails,QuotationView quotationView, QuoCustomer quoCustomer) throws Exception;

	
}
