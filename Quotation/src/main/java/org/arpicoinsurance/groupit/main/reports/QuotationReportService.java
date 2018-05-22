package org.arpicoinsurance.groupit.main.reports;

import org.arpicoinsurance.groupit.main.helper.QuoCustomer;
import org.arpicoinsurance.groupit.main.helper.QuotationView;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;


public interface QuotationReportService {
	
	byte[] createAIPReport(QuotationDetails quotationDetails,QuotationView quotationView, QuoCustomer quoCustomer) throws Exception;
	
	byte[] createAIBReport(QuotationDetails quotationDetails,QuotationView quotationView, QuoCustomer quoCustomer) throws Exception;
	
	byte[] createINVPReport(QuotationDetails quotationDetails,QuotationView quotationView, QuoCustomer quoCustomer) throws Exception;

	byte[] createDTAReport(QuotationDetails quotationDetails,QuotationView quotationView, QuoCustomer quoCustomer) throws Exception;

	byte[] createATRMReport(QuotationDetails quotationDetails,QuotationView quotationView, QuoCustomer quoCustomer) throws Exception;

	byte[] createEND1Report(QuotationDetails quotationDetails,QuotationView quotationView, QuoCustomer quoCustomer) throws Exception;

	byte[] createASFPReport(QuotationDetails quotationDetails,QuotationView quotationView, QuoCustomer quoCustomer) throws Exception;

	byte[] createARPReport(QuotationDetails quotationDetails,QuotationView quotationView, QuoCustomer quoCustomer) throws Exception;

	
}
