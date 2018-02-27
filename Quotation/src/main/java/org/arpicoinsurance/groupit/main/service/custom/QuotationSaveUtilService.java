package org.arpicoinsurance.groupit.main.service.custom;

import java.util.ArrayList;
import java.util.List;

import org.arpicoinsurance.groupit.main.helper.Benifict;
import org.arpicoinsurance.groupit.main.helper.Children;
import org.arpicoinsurance.groupit.main.helper.InvpSavePersonalInfo;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.helper.RiderDetails;
import org.arpicoinsurance.groupit.main.model.Child;
import org.arpicoinsurance.groupit.main.model.CustChildDetails;
import org.arpicoinsurance.groupit.main.model.CustomerDetails;
import org.arpicoinsurance.groupit.main.model.Occupation;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Child_Details;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Details;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;
import org.arpicoinsurance.groupit.main.model.Shedule;
import org.arpicoinsurance.groupit.main.model.Users;

public interface QuotationSaveUtilService {
	
	 CustomerDetails getCustomerDetail(Occupation occupationMainlife, InvpSavePersonalInfo get_personalInfo,
				Users user)throws Exception;
	 
	 CustomerDetails getSpouseDetail(Occupation occupationSpouse, InvpSavePersonalInfo get_personalInfo,
				Users user) throws Exception;
	 
	 public ArrayList<Child> getChilds(ArrayList<Children> get_childrenList) throws Exception;
	 
	 QuotationDetails getQuotationDetail(QuotationQuickCalResponse calResp, QuotationCalculation calculation, Double lifePos)
				throws Exception;
	 
	 ArrayList<Quo_Benef_Details> getBenifDetails(RiderDetails get_riderDetails, QuotationQuickCalResponse calResp,
				QuotationDetails quotationDetails, List<Children> childrenList, Integer integer) throws Exception;
	 
	 ArrayList<Quo_Benef_Child_Details> getChildBenif(ArrayList<Quo_Benef_Details> benef_DetailsList,
				ArrayList<CustChildDetails> custChildDetailsList, ArrayList<Child> childList,
				ArrayList<Children> get_childrenList, Integer term, String frequancy, ArrayList<Benifict> benifictListC)
				throws Exception;
	 
	 ArrayList<Shedule> getSheduleDtaDtapl(QuotationQuickCalResponse calResp, QuotationDetails quotationDetails) throws Exception;
	 
	 ArrayList<Quo_Benef_Details> addMaturity(String product, ArrayList<Quo_Benef_Details> benefictList, QuotationQuickCalResponse calResp, Integer term, QuotationDetails quotationDetails) throws Exception;
}
