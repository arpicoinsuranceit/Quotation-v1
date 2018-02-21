package org.arpicoinsurance.groupit.main.controller;

import java.util.ArrayList;

import org.arpicoinsurance.groupit.main.helper.QuotationView;
import org.arpicoinsurance.groupit.main.service.Quo_Benef_DetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class Quo_Benef_DetailsController {

	@Autowired
	private Quo_Benef_DetailsService quoBenefDetailService;
	
	@RequestMapping(value="/quodetails",method=RequestMethod.POST)
	public ArrayList<QuotationView> getQuotationByUserId(@RequestBody String id) {
		try {
			System.out.println(id);
			Integer quoId=Integer.valueOf(id);
			ArrayList<QuotationView> detailList=(ArrayList<QuotationView>) quoBenefDetailService.getQuo_Benef_DetailsByQuoDetailId(quoId);
			return detailList;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
