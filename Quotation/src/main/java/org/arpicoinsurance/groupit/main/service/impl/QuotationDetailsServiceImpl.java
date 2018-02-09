package org.arpicoinsurance.groupit.main.service.impl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import javax.transaction.Transactional;
import org.arpicoinsurance.groupit.main.dao.QuotationDetailsDao;
import org.arpicoinsurance.groupit.main.helper.EditQuotation;
import org.arpicoinsurance.groupit.main.helper.MainLife;
import org.arpicoinsurance.groupit.main.helper.Spouse;
import org.arpicoinsurance.groupit.main.model.CustomerDetails;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Details;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;
import org.arpicoinsurance.groupit.main.service.Quo_Benef_DetailsService;
import org.arpicoinsurance.groupit.main.service.QuotationDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class QuotationDetailsServiceImpl implements QuotationDetailsService{
	
	@Autowired
	private QuotationDetailsDao quotationDetailsDao;
	
	@Autowired
	private Quo_Benef_DetailsService quo_Benef_DetailsService;

	@Override
	public QuotationDetails findQuotationDetails(Integer qdId) throws Exception {
		return quotationDetailsDao.findByQdId(qdId);
	}

	@Override
	public EditQuotation editQuotationDetails(Integer qdId) throws Exception {
		QuotationDetails details=findQuotationDetails(qdId);
		EditQuotation editQuotation=new EditQuotation();
		MainLife mainLife=new MainLife();
		Spouse spouse=new Spouse();
		if(details != null) {
			CustomerDetails customerDetails=details.getQuotation().getCustomerDetails();
			mainLife.set_mName(customerDetails.getCustName());
			
			/*LocalDate dateOfBirth = LocalDate.parse(customerDetails.getCustDob().toString());
		    LocalDate currentDate = LocalDate.parse(details.getQuotationquotationCreateDate().toString());
		    long diffInYears = ChronoUnit.YEARS.between(dateOfBirth, currentDate);
		    diffInYears+=1;
		    String age=Long.toString(diffInYears);*/
		    
		    mainLife.set_mAge("23");
		    mainLife.set_mDob(customerDetails.getCustDob().toString());
		    mainLife.set_mEmail(customerDetails.getCustEmail());
		    mainLife.set_mGender(customerDetails.getCustGender());
		    mainLife.set_mMobile(customerDetails.getCustTel());
		    mainLife.set_mNic(customerDetails.getCustNic());
		    mainLife.set_mOccupation(customerDetails.getOccupation().getOcupationName());
		    mainLife.set_mSmoking("No");
		    mainLife.set_mTitle(customerDetails.getCustTitle());
		    
		    
		    if(details.getQuotation().getSpouseDetails() != null) {
		    	CustomerDetails spouseDetails=details.getQuotation().getSpouseDetails();
				spouse.set_sName(spouseDetails.getCustName());
				
				/*LocalDate sdateOfBirth = LocalDate.parse(customerDetails.getCustDob().toString());
			    LocalDate scurrentDate = LocalDate.parse(details.getQuotationquotationCreateDate().toString());
			    long sdiffInYears = ChronoUnit.YEARS.between(dateOfBirth, currentDate);
			    sdiffInYears+=1;
			    String sage=Long.toString(sdiffInYears);*/
			    
				spouse.set_sActive(true);
			    spouse.set_sAge("23");
			    spouse.set_sDob(spouseDetails.getCustDob().toString());
			    spouse.set_sGender(spouseDetails.getCustGender());
			    spouse.set_sNic(spouseDetails.getCustNic());
			    spouse.set_sOccupation(spouseDetails.getOccupation().getOcupationName());
			    spouse.set_sTitle(spouseDetails.getCustTitle());
			    
			    
		    }
		}
		
		editQuotation.set_mainlife(mainLife);
		editQuotation.set_spouse(spouse);
		/*ArrayList<Quo_Benef_Details> benef=(ArrayList<Quo_Benef_Details>) quo_Benef_DetailsService.findByQuotationDetails(details);
		
		for (Quo_Benef_Details quo_Benef_Details : benef) {
			System.out.println(quo_Benef_Details.getBenefit().getRiderCode());
		}*/
		
		return editQuotation;
	}
	
	

}
