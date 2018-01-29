package org.arpicoinsurance.groupit.main.controller;

import java.util.ArrayList;
import java.util.Date;

import org.arpicoinsurance.groupit.main.helper.AIPCalResp;
import org.arpicoinsurance.groupit.main.helper.AipCalShedule;
import org.arpicoinsurance.groupit.main.helper.Plan;
import org.arpicoinsurance.groupit.main.service.AIPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class QuoAipCalController {
	
	@Autowired
	private AIPService aipService;
	
	
	
	@RequestMapping(value="/aipCal",method=RequestMethod.POST)
	public AIPCalResp calculateAIP(@RequestBody Plan plan) {

		Double contribution =plan.get_bsa();
		System.out.println(plan.get_frequance()+"*************");
		System.out.println(plan.get_bsa()+"*************");
		System.out.println(plan.get_term()+"*************");
		try {		
			AIPCalResp aipCalResp =aipService.calculateAIPMaturaty(plan.get_term(), 2.0, 0.02, 9.5,contribution, new Date(), plan.get_frequance(),false);
			AIPCalResp aipCalResp11 = aipService.calculateAIPMaturaty(plan.get_term(), 2.0, 0.02, 11.0, contribution, new Date(), plan.get_frequance(),false);
			AIPCalResp aipCalResp12 = aipService.calculateAIPMaturaty(plan.get_term(), 2.0, 0.02, 12.5, contribution, new Date(), plan.get_frequance(),false);
			
			aipCalResp.setMaturaty10(aipCalResp11.getMaturaty());
			aipCalResp.setMaturaty12(aipCalResp12.getMaturaty());
			
			return aipCalResp;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value="/aipshedule",method=RequestMethod.POST)
	public ArrayList<AipCalShedule> loadSheduleAIP(@RequestBody Plan plan) {
		Double contribution =plan.get_bsa();
		System.out.println(plan.get_frequance()+"*************");
		System.out.println(plan.get_bsa()+"*************");
		System.out.println(plan.get_term()+"*************");
		try {		
			AIPCalResp aipCalResp=aipService.calculateAIPMaturaty(plan.get_term(), 2.0, 0.02, 9.5, contribution, new Date(), plan.get_frequance(),true);
			//aipService.calculateAIPMaturaty(plan.get_term(), 2.0, 0.02, 11.0, contribution, new Date(), plan.get_frequance(),false);
			//aipService.calculateAIPMaturaty(plan.get_term(), 2.0, 0.02, 12.5, contribution, new Date(), plan.get_frequance(),false);
			
			ArrayList<AipCalShedule> aipCalSchedule=(ArrayList<AipCalShedule>) aipCalResp.getAipCalShedules();
			if(aipCalSchedule!=null && !aipCalSchedule.isEmpty()) {
				return aipCalSchedule;
			}
			
			return null;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
