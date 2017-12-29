package org.arpicoinsurance.groupit.main.controller;

import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin (origins="*")
@RestController
public class QuotationCalculationController {
	
	@RequestMapping(value="/quoCal",method=RequestMethod.POST)
	public QuotationCalculation calculateQuotation(@RequestBody QuotationCalculation calculation) {
		System.out.println(calculation);
		return calculation;
	}
	
}
