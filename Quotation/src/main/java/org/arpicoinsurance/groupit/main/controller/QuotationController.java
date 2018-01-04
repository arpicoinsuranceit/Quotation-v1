package org.arpicoinsurance.groupit.main.controller;

import java.util.ArrayList;
import org.arpicoinsurance.groupit.main.helper.QuoDetails;
import org.arpicoinsurance.groupit.main.model.Quotation;
import org.arpicoinsurance.groupit.main.service.QuotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuotationController {
	
	
	@Autowired
	private QuotationService quotationService;
	
	@RequestMapping(value="/quotation/{id}",method=RequestMethod.GET)
	public Quotation getQuotation(@PathVariable Integer id) {
		try {
			Quotation quotation=quotationService.getQuotation(id);
			return quotation;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@RequestMapping(value="/quo/{id}",method=RequestMethod.GET)
	public ArrayList<QuoDetails> getQuotationByUserId(@PathVariable Integer id) {
		try {
			ArrayList<Quotation> detailList=(ArrayList<Quotation>) quotationService.getQuotationByUserId(id);
			
			ArrayList<QuoDetails> quoDetails=new ArrayList<>();
			if(detailList!=null) {
				for (Quotation quotation : detailList) {
					QuoDetails details=new QuoDetails();
					details.setQuotationNum(quotation.getQuotationNum());
					details.setProductCode(quotation.getProducts().getProductCode());
					details.setBranchCode(quotation.getUser().getBranch().getBranch_Code());
					details.setCustomerName(quotation.getCustomerDetails().getCustName());
					details.setCustomerNic(quotation.getCustomerDetails().getCustNic());
					
					quoDetails.add(details);
				}
			
				return quoDetails;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
