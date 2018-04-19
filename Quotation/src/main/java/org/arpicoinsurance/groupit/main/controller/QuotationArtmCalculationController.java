package org.arpicoinsurance.groupit.main.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.arpicoinsurance.groupit.main.helper.AIPCalResp;
import org.arpicoinsurance.groupit.main.helper.AipCalShedule;
import org.arpicoinsurance.groupit.main.helper.InvpSavePersonalInfo;
import org.arpicoinsurance.groupit.main.helper.Plan;
import org.arpicoinsurance.groupit.main.service.ARTMService;
import org.arpicoinsurance.groupit.main.service.ATRMService;
import org.arpicoinsurance.groupit.main.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class QuotationArtmCalculationController {

	@Autowired
	private ARTMService artmService;
	
	@RequestMapping(value = "/artmCal", method = RequestMethod.POST)
	public AIPCalResp calculateATRM (@RequestBody Plan plan) {
		
		Validation validation = new Validation();
		String valError = validation.validateArtm(plan);
		if(valError.equals("ok")) {
			try {
				return artmService.calculateARTMMaturaty(plan, 1.0, false);
			} catch (Exception e) {
				e.printStackTrace();
				AIPCalResp aipCalResp = new AIPCalResp();
				aipCalResp.setError("Error At calculation");
				
				return aipCalResp;
				
			}
		}else {
			AIPCalResp aipCalResp = new AIPCalResp();
			aipCalResp.setError(valError);
			
			return aipCalResp;
		}
		
	}
	@RequestMapping(value = "/artmshedule", method = RequestMethod.POST)
	public ArrayList<AipCalShedule> loadSheduleATRM (@RequestBody Plan plan) {
		
		Validation validation = new Validation();
		if(validation.validateArtm(plan).equals("ok")) {
			try {
				return (ArrayList<AipCalShedule>) artmService.calculateARTMMaturaty(plan, 1.0, true).getAipCalShedules();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	@RequestMapping(value = "/artmSavequo/{id}", method = RequestMethod.POST)
	public HashMap<String, Object> saveAIP(@RequestBody InvpSavePersonalInfo _invpSaveQuotation, @PathVariable Integer id) {
		HashMap<String, Object> responseMap = new HashMap<>();
		responseMap.put("status", "fail");
		try {
			if (id != null) {
				if (_invpSaveQuotation != null) {
					Validation validation = new Validation();
					if(validation.validateArtm(_invpSaveQuotation.get_plan()).equals("ok")) {
						return artmService.saveQuotation(_invpSaveQuotation, id);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return responseMap;
	}
	
	@RequestMapping(value = "/quoArtmEdit/{userId}/{qdId}", method = RequestMethod.POST)
	public HashMap<String, Object> editAip(@RequestBody InvpSavePersonalInfo _invpSaveQuotation, @PathVariable("userId") Integer userId,
			@PathVariable("qdId") Integer qdId) {
		HashMap<String, Object> responseMap = new HashMap<>();
		responseMap.put("status", "fail");
		try {
			if (userId != null) {
				if (qdId != null) {
					if (_invpSaveQuotation != null) {
						Validation validation = new Validation();
						if(validation.validateArtm(_invpSaveQuotation.get_plan()).equals("ok")) {
							return artmService.editQuotation(_invpSaveQuotation, userId, qdId);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return responseMap;
	}

	
}
