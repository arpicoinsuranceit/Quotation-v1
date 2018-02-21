package org.arpicoinsurance.groupit.main.controller;

import java.util.ArrayList;
import org.arpicoinsurance.groupit.main.helper.ViewQuotation;
import org.arpicoinsurance.groupit.main.model.Shedule;
import org.arpicoinsurance.groupit.main.service.Quo_Benef_DetailsService;
import org.arpicoinsurance.groupit.main.service.SheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class Quo_Benef_DetailsController {

	@Autowired
	private Quo_Benef_DetailsService quoBenefDetailService;
	
	@Autowired
	private SheduleService sheduleService;
	
	/*@RequestMapping(value="/quodetails",method=RequestMethod.POST)
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
	}*/
	
	@RequestMapping(value="/quodetails",method=RequestMethod.POST)
	public ArrayList<ViewQuotation> viewQuotationDetails(@RequestBody String id) {
		try {
			System.out.println(id);
			Integer quoId=Integer.valueOf(id);
			ArrayList<ViewQuotation> detailList=(ArrayList<ViewQuotation>) quoBenefDetailService.getQuotationDetails(quoId);
			return detailList;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@RequestMapping(value="/scheduledetails/{id}",method=RequestMethod.GET)
	public ArrayList<Shedule> viewQuotationDetails(@PathVariable Integer id) {
		try {
			ArrayList<Shedule> sheduleList=(ArrayList<Shedule>) sheduleService.findByQuotationDetails(id);
			return sheduleList;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
