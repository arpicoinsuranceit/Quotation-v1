package org.arpicoinsurance.groupit.main.controller;

import java.util.ArrayList;

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
		System.out.println("Called ");
		try {
			Quotation quotation=quotationService.getQuotation(id);
			return quotation;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@RequestMapping(value="/quo/{id}",method=RequestMethod.GET)
	public ArrayList<Quotation> getQuotationByUserId(@PathVariable Integer id) {
		System.out.println("Called ");
		try {
			ArrayList<Quotation> detailList=(ArrayList<Quotation>) quotationService.getQuotationByUserId(id);
			System.out.println(detailList.get(0));
			return (ArrayList<Quotation>) quotationService.getQuotationByUserId(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
