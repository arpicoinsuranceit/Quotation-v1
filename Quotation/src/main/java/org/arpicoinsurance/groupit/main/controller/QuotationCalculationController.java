package org.arpicoinsurance.groupit.main.controller;

import org.arpicoinsurance.groupit.main.helper.QuoCalResp;
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
	public QuoCalResp calculateQuotation(@RequestBody QuotationCalculation calculation) {
		System.out.println(calculation);
		QuoCalResp calResp=new QuoCalResp();
		calResp.setAdb(10.00);
		calResp.setAdbs(10.00);
		calResp.setAddBenif(10.00);
		calResp.setAt10(5.00);
		calResp.setAt6(8.00);
		calResp.setAt8(7.00);
		calResp.setAtpb(10.00);
		calResp.setBasicSumAssured(10.00);
		calResp.setBsa(10.00);
		calResp.setBsas(10.00);
		calResp.setCib(10.00);
		calResp.setCibc(10.00);
		calResp.setCibs(10.00);
		calResp.setExtraOE(10.00);
		calResp.setFeb(10.00);
		calResp.setFebs(10.00);
		calResp.setHb(10.00);
		calResp.setHbc(10.00);
		calResp.setHbs(10.00);
		calResp.setHrb(10.00);
		calResp.setHrbc(10.00);
		calResp.setHrbs(10.00);
		calResp.setMifdb(10.00);
		calResp.setMifdbt(10.00);
		calResp.setMifdt(10.00);
		calResp.setPpdb(10.00);
		calResp.setPpdbs(10.00);
		calResp.setSuhrb(10.00);
		calResp.setSuhrbc(10.00);
		calResp.setSuhrbs(10.00);
		calResp.setTotPremium(30.00);
		calResp.setTpdasb(10.00);
		calResp.setTpdasbs(10.00);
		calResp.setTpdb(10.00);
		calResp.setTpdbs(10.00);
		calResp.setWpb(10.00);
		calResp.setWpbs(10.00); 
		
		return calResp;
	}
	
}
