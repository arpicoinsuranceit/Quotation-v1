package org.arpicoinsurance.groupit.main.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.arpicoinsurance.groupit.main.helper.AIPCalResp;
import org.arpicoinsurance.groupit.main.helper.AipCalShedule;
import org.arpicoinsurance.groupit.main.helper.InvpSavePersonalInfo;
import org.arpicoinsurance.groupit.main.helper.Plan;
import org.arpicoinsurance.groupit.main.model.Logs;
import org.arpicoinsurance.groupit.main.service.AIPService;
import org.arpicoinsurance.groupit.main.service.LogService;
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

	@RequestMapping(value = "/aipCal", method = RequestMethod.POST)
	public ResponseEntity<Object> calculateAIP(@RequestBody Plan plan) throws Exception {

		// System.out.println(plan.toString());

		Double contribution = plan.get_bsa();
		try {
			AIPCalResp aipCalResp = aipService.calculateAIPMaturaty(plan.get_term(), 2.0, 0.2, 9.0, contribution,
					new Date(), plan.get_frequance(), false, true);
			AIPCalResp aipCalResp11 = aipService.calculateAIPMaturaty(plan.get_term(), 2.0, 0.2, 10.0, contribution,
					new Date(), plan.get_frequance(), false, false);
			AIPCalResp aipCalResp12 = aipService.calculateAIPMaturaty(plan.get_term(), 2.0, 0.2, 11.0, contribution,
					new Date(), plan.get_frequance(), false, false);

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
				System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
//		 return null;
	}

	@RequestMapping(value = "/aipshedule", method = RequestMethod.POST)
	public ResponseEntity<Object> loadSheduleAIP(@RequestBody Plan plan) {
		Double contribution = plan.get_bsa();
		try {
			AIPCalResp aipCalResp = aipService.calculateAIPMaturaty(plan.get_term(), 2.0, 0.2, 9.0, contribution,
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
				System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// return null;
	}

	@RequestMapping(value = "/aipSavequo/{id}", method = RequestMethod.POST)
	public ResponseEntity<Object> saveAIP(@RequestBody InvpSavePersonalInfo _invpSaveQuotation,
			@PathVariable Integer id) {
		HashMap<String, Object> responseMap = new HashMap<>();
		responseMap.put("status", "fail");
		try {
			if (id != null) {
				if (_invpSaveQuotation != null) {

					if ((Integer.parseInt(_invpSaveQuotation.get_mainlife().get_mAge())
							+ _invpSaveQuotation.get_plan().get_term()) <= 100) {

						responseMap = aipService.saveQuotation(_invpSaveQuotation, id);
					} else {
						responseMap.replace("status", "Term is too large for Mainlife age");
					}
				}
			}
			return new ResponseEntity<Object>(responseMap, HttpStatus.CREATED);

		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n Parameters : _invpSaveQuotation :"
					+ _invpSaveQuotation.toString() + " , id : " + id);
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("saveAIP : QuotationAipCalculationController");
			logs.setUserId(id);
			try {
				logService.saveLog(logs);
			} catch (Exception e1) {
				System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {

		}
		
	}

	@RequestMapping(value = "/quoAipEdit/{userId}/{qdId}", method = RequestMethod.POST)
	public ResponseEntity<Object> editAip(@RequestBody InvpSavePersonalInfo _invpSaveQuotation,
			@PathVariable("userId") Integer userId, @PathVariable("qdId") Integer qdId) {
		HashMap<String, Object> responseMap = new HashMap<>();
		responseMap.put("status", "fail");
		try {
			if (userId != null) {
				if (qdId != null) {
					if (_invpSaveQuotation != null) {
						if ((Integer.parseInt(_invpSaveQuotation.get_mainlife().get_mAge())
								+ _invpSaveQuotation.get_plan().get_term()) <= 100) {
							/*
							 * if ((Integer.parseInt(_invpSaveQuotation.get_mainlife().get_mAge()) +
							 * _invpSaveQuotation.get_plan().get_term()) <= 70) {
							 */
							responseMap = aipService.editQuotation(_invpSaveQuotation, userId, qdId);
						} else {
							responseMap.replace("status", "Term is too large for Mainlife age");
						}
					}
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
				System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {

		}
		
	}
}