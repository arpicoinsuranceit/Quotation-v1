package org.arpicoinsurance.groupit.main.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.arpicoinsurance.groupit.main.helper.AIPCalResp;
import org.arpicoinsurance.groupit.main.helper.AipCalShedule;
import org.arpicoinsurance.groupit.main.helper.InvpSavePersonalInfo;
import org.arpicoinsurance.groupit.main.helper.Plan;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.model.Logs;
import org.arpicoinsurance.groupit.main.service.ARTMService;
import org.arpicoinsurance.groupit.main.service.LogService;
import org.arpicoinsurance.groupit.main.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin (origins = "*")
public class QuotationArtmCalculationController {

	@Autowired
	private ARTMService artmService;
	
	@Autowired
	private LogService logService;
	
	@RequestMapping(value = "/artmCal", method = RequestMethod.POST)
	public ResponseEntity<Object> calculateATRM (@RequestBody QuotationCalculation calculation) {
		
		try {
			QuotationQuickCalResponse calResp = new QuotationQuickCalResponse();
			
			//System.out.println("call artm");
			Validation validation = new Validation(calculation);
			String valError = validation.validateArtm(calculation);
			if(valError.equals("ok")) {
				String error = validation.validateBenifict();
				if (error.equals("No")) {
					
					
					
					if (calResp.isErrorExist()) {
						QuotationQuickCalResponse calRespPost = new QuotationQuickCalResponse();
						calRespPost.setError(calResp.getError());
						calRespPost.setErrorExist(true);
						return new ResponseEntity<Object> (calRespPost, HttpStatus.OK);
					}
				} else {
					calResp.setErrorExist(true);
					calResp.setError(error);
				}
			}else {
				calResp.setError(valError);
				calResp.setErrorExist(true);
			}
			return new ResponseEntity<Object> (calResp, HttpStatus.OK);
			
		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n Parameters : " + calculation.toString());
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("calculateQuotation : QuotationEndCalculationController");
			try {
				logService.saveLog(logs);
			} catch (Exception e1) {
				System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			return new ResponseEntity<Object> (e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
	}
	
	
	
	
	
	/*@RequestMapping(value = "/artmshedule", method = RequestMethod.POST)
	public ArrayList<AipCalShedule> loadSheduleATRM (@RequestBody Plan plan) {
		
		Validation validation = new Validation();
		if(validation.validateArtm(plan).equals("ok")) {
			try {
				return (ArrayList<AipCalShedule>) artmService.calculateARTMMaturaty(plan, 1.0, true,false).getAipCalShedules();
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
	}*/
	

	
}
