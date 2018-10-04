package org.arpicoinsurance.groupit.main.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.helper.AIPCalResp;
import org.arpicoinsurance.groupit.main.helper.AipCalShedule;
import org.arpicoinsurance.groupit.main.helper.InvpSavePersonalInfo;
import org.arpicoinsurance.groupit.main.helper.Plan;
import org.arpicoinsurance.groupit.main.model.Logs;
import org.arpicoinsurance.groupit.main.service.AIPService;
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
@CrossOrigin(origins = "*")
public class QuotationAipCalculationController {

	@Autowired
	private AIPService aipService;

	@Autowired
	private LogService logService;
	
	@Autowired
	private CalculationUtils calculationUtils;

	@RequestMapping(value = "/aipCal", method = RequestMethod.POST)
	public ResponseEntity<Object> calculateAIP(@RequestBody Plan plan) throws Exception {
		CalculationUtils calUtil = new CalculationUtils();
		// //System.out.println(plan.toString());

		Validation validation = new Validation();

		String message = validation.validateAIP(plan.getAge(), plan.get_frequance(), plan.get_bsa(), plan.get_term());

		//String message = "ok";
		
		if (!message.equals("ok")) {
			
			AIPCalResp aipCalResp = new AIPCalResp();
			aipCalResp.setError(message);
			aipCalResp.setErrorExist(true);
			return new ResponseEntity<Object>(aipCalResp, HttpStatus.OK);
		}

		Double contribution = plan.get_bsa();
		Double fundMrate = calUtil.getFndMngRate(contribution,plan.get_frequance());

		////System.out.println(fundMrate);

		try {
			AIPCalResp aipCalResp = aipService.calculateAIPMaturaty(plan.get_term(), 2.0, fundMrate, 9.0, contribution,
					new Date(), plan.get_frequance(), false, true);
			AIPCalResp aipCalResp11 = aipService.calculateAIPMaturaty(plan.get_term(), 2.0, fundMrate, 10.0,
					contribution, new Date(), plan.get_frequance(), false, false);
			AIPCalResp aipCalResp12 = aipService.calculateAIPMaturaty(plan.get_term(), 2.0, fundMrate, 11.0,
					contribution, new Date(), plan.get_frequance(), false, false);

			aipCalResp.setMaturaty10(aipCalResp11.getMaturaty());
			aipCalResp.setMaturaty12(aipCalResp12.getMaturaty());

			return new ResponseEntity<Object>(aipCalResp, HttpStatus.OK);

		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n Parameters : " + plan.toString());
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("calculateAIP : QuotationAipCalculationController");
			try {
				logService.saveLog(logs);
			} catch (Exception e1) {
				//System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				//System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		} finally {
			if (validation != null)
				validation = null;
		}
		// return null;
	}

	@RequestMapping(value = "/aipshedule", method = RequestMethod.POST)
	public ResponseEntity<Object> loadSheduleAIP(@RequestBody Plan plan) {

		Validation validation = new Validation();

		String message = validation.validateAIP(plan.getAge(), plan.get_frequance(), plan.get_bsa(), plan.get_term());

		//String message = "ok";
		
		if (!message.equals("ok")) {
			AIPCalResp aipCalResp = new AIPCalResp();
			aipCalResp.setError(message);
			aipCalResp.setErrorExist(true);
			return new ResponseEntity<Object>(aipCalResp, HttpStatus.OK);
		}

		CalculationUtils calUtil = new CalculationUtils();

		Double contribution = plan.get_bsa();
		Double fundMrate = calUtil.getFndMngRate(contribution,plan.get_frequance());

		////System.out.println(fundMrate);
		try {
			AIPCalResp aipCalResp = aipService.calculateAIPMaturaty(plan.get_term(), 2.0, fundMrate, 9.0, contribution,
					new Date(), plan.get_frequance(), true, false);

			ArrayList<AipCalShedule> aipCalSchedule = (ArrayList<AipCalShedule>) aipCalResp.getAipCalShedules();
			if (aipCalSchedule != null && !aipCalSchedule.isEmpty()) {
				return new ResponseEntity<Object>(aipCalSchedule, HttpStatus.OK);
			}

			return null;

		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n Parameters : " + plan.toString());
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("loadSheduleAIP : QuotationAipCalculationController");
			try {
				logService.saveLog(logs);
			} catch (Exception e1) {
				//System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				//System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (validation != null)
				validation = null;
		}
		// return null;
	}

	@RequestMapping(value = "/aipSavequo/{id}", method = RequestMethod.POST)
	public ResponseEntity<Object> saveAIP(@RequestBody InvpSavePersonalInfo _invpSaveQuotation,
			@PathVariable Integer id) throws Exception {
		HashMap<String, Object> responseMap = new HashMap<>();
		responseMap.put("status", "fail");
		
		String phone = calculationUtils.getPhoneNo(_invpSaveQuotation.get_mainlife().get_mMobile());
		
		if(!phone.equals("Error")) {
			_invpSaveQuotation.get_mainlife().set_mMobile(phone);
		} else {
			responseMap.replace("status", "Phone No Invalied");
			return new ResponseEntity<Object>(responseMap, HttpStatus.BAD_REQUEST);
		}
		
		Validation validation = new Validation();
		String message = "Error";
		if (_invpSaveQuotation != null) {
			message = validation.validateAIP(Integer.parseInt(_invpSaveQuotation.get_mainlife().get_mAge()),
					_invpSaveQuotation.get_plan().get_frequance(), _invpSaveQuotation.get_plan().get_bsa(),
					_invpSaveQuotation.get_plan().get_term());
			 
		}
		if (!message.equalsIgnoreCase("ok")) {
			responseMap.replace("status", message);
			return new ResponseEntity<Object>(responseMap, HttpStatus.BAD_REQUEST);
		}
		
		message = validation.saveEditValidations(_invpSaveQuotation);

		if (!message.equalsIgnoreCase("ok")) {
			responseMap.replace("status", message);
			return new ResponseEntity<Object>(responseMap, HttpStatus.BAD_REQUEST);
		}
		
		try {
			if (id != null) {
				responseMap = aipService.saveQuotation(_invpSaveQuotation, id);
			} 
			return new ResponseEntity<Object>(responseMap, HttpStatus.CREATED);

		} catch (Exception e) {
			e.printStackTrace();
			Logs logs = new Logs();
			/*logs.setData("Error : " + e.getMessage() + ",\n Parameters : _invpSaveQuotation :"
					+ _invpSaveQuotation.toString() + " , id : " + id);
		*/	logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("saveAIP : QuotationAipCalculationController");
			logs.setUserId(id);
			try {
				logService.saveLog(logs);
			} catch (Exception e1) {
				//System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				//System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (validation != null)
				validation = null;
		}

	}

	@RequestMapping(value = "/quoAipEdit/{userId}/{qdId}", method = RequestMethod.POST)
	public ResponseEntity<Object> editAip(@RequestBody InvpSavePersonalInfo _invpSaveQuotation,
			@PathVariable("userId") Integer userId, @PathVariable("qdId") Integer qdId) throws Exception {
		HashMap<String, Object> responseMap = new HashMap<>();
		responseMap.put("status", "fail");
		
		String phone = calculationUtils.getPhoneNo(_invpSaveQuotation.get_mainlife().get_mMobile());
		
		if(!phone.equals("Error")) {
			_invpSaveQuotation.get_mainlife().set_mMobile(phone);
		} else {
			responseMap.replace("status", "Phone No Invalied");
			return new ResponseEntity<Object>(responseMap, HttpStatus.BAD_REQUEST);
		}

		Validation validation = new Validation();
		String message = "Error";
		if (_invpSaveQuotation != null) {
			message = validation.validateAIP(Integer.parseInt(_invpSaveQuotation.get_mainlife().get_mAge()),
					_invpSaveQuotation.get_plan().get_frequance(), _invpSaveQuotation.get_plan().get_bsa(),
					_invpSaveQuotation.get_plan().get_term());
			// message = "ok";
		}
		if (!message.equals("ok")) {
			responseMap.replace("status", message);
			return new ResponseEntity<Object>(responseMap, HttpStatus.OK);
		}

		message = validation.saveEditValidations(_invpSaveQuotation);

		if (!message.equalsIgnoreCase("ok")) {
			responseMap.replace("status", message);
			return new ResponseEntity<Object>(responseMap, HttpStatus.BAD_REQUEST);
		}
		
		try {
			if (userId != null) {
				if (qdId != null) {
					responseMap = aipService.editQuotation(_invpSaveQuotation, userId, qdId);
				}
			}
			return new ResponseEntity<Object>(responseMap, HttpStatus.CREATED);
		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n Parameters : _invpSaveQuotation :"
					+ _invpSaveQuotation.toString() + " , userId : " + userId + " , qdId : " + qdId);
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("editAip : QuotationAipCalculationController");
			logs.setUserId(userId);
			try {
				logService.saveLog(logs);
			} catch (Exception e1) {
				//System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				//System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (validation != null)
				validation = null;
		}

	}
}