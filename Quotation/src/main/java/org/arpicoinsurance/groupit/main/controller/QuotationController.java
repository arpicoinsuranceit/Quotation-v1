package org.arpicoinsurance.groupit.main.controller;

import java.util.ArrayList;

import org.arpicoinsurance.groupit.main.helper.EditQuotation;
import org.arpicoinsurance.groupit.main.helper.QuoDetails;
import org.arpicoinsurance.groupit.main.model.Quotation;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;
import org.arpicoinsurance.groupit.main.service.QuotationDetailsService;
import org.arpicoinsurance.groupit.main.service.QuotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class QuotationController {
	
	
	@Autowired
	private QuotationService quotationService;
	
	@Autowired
	private QuotationDetailsService quoDetailsService;
	
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
	
	@RequestMapping(value="/quo",method=RequestMethod.POST)
	public ArrayList<QuoDetails> getQuotationByUserId(@RequestBody String id) {
		Integer userId=Integer.valueOf(id);
		try {
			return quotationService.getQuotationDetails(userId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value="/quotationDetails",method=RequestMethod.POST)
	public EditQuotation getQuotationDetailsById(@RequestBody String id) {
		Integer qdId=Integer.valueOf(id);
		try {
			EditQuotation quoDetails= quoDetailsService.editQuotationDetails(qdId);
			return quoDetails;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value="/product",method=RequestMethod.POST)
	public String getProduct(@RequestBody String id) {
		Integer qdId=Integer.valueOf(id);
		try {
			QuotationDetails quoDetails= quoDetailsService.findQuotationDetails(qdId);
			System.out.println(quoDetails.getQuotation().getProducts().getProductCode());
			return quoDetails.getQuotation().getProducts().getProductCode();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
