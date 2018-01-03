package org.arpicoinsurance.groupit.main.controller;

import java.util.ArrayList;

import org.arpicoinsurance.groupit.main.dao.Quo_Benef_DetailsDao;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Details;
import org.arpicoinsurance.groupit.main.model.Quotation;
import org.arpicoinsurance.groupit.main.service.Quo_Benef_DetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class Quo_Benef_DetailsController {

	@Autowired
	private Quo_Benef_DetailsService quoBenefDetailService;
	
	@Autowired
	private Quo_Benef_DetailsDao quoDao;
	
	@RequestMapping(method=RequestMethod.GET, value="/quobenfdetail")
	public ArrayList<Quo_Benef_Details> getByQuotationNum(@PathVariable Integer id){
		try {
			ArrayList<Quo_Benef_Details>qbdList=(ArrayList<Quo_Benef_Details>) quoBenefDetailService.getQuo_Benef_DetailsByUser(id);
			
			for (Quo_Benef_Details quo_Benef_Details : qbdList) {
				
			}
			
			return qbdList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
		
	}
	
	
	
}
