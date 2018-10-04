package org.arpicoinsurance.groupit.main.controller;

import java.util.Date;
import java.util.HashMap;

import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.model.Logs;
import org.arpicoinsurance.groupit.main.service.DTAPLService;
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
public class QuotationDtaplCalculationController {

	@Autowired
	private DTAPLService dtaplService;

	@Autowired
	private LogService logService;

	@RequestMapping(value = "/quoDtaplCal", method = RequestMethod.POST)
	public ResponseEntity<Object> calculateQuotation(@RequestBody QuotationCalculation calculation) {
		Validation validation = null;
		QuotationQuickCalResponse calResp = null;
		try {
			calResp = new QuotationQuickCalResponse();
			validation = new Validation(calculation);
			String error = validation.validateBenifict();
			if (calculation.get_personalInfo().getMage() + calculation.get_personalInfo().getTerm() < 70) {
				if (error.equals("No")) {
					calResp = dtaplService.getCalcutatedDta(calculation);
					if (calResp.isErrorExist()) {
						QuotationQuickCalResponse calRespPost = new QuotationQuickCalResponse();
						calRespPost.setError(calResp.getError());
						calRespPost.setErrorExist(true);
						return new ResponseEntity<Object>(calRespPost, HttpStatus.OK);
					}
				}

				else {
					calResp.setError(error);
					calResp.setErrorExist(true);
				}
			} else {
				calResp.setError("Term is too large for Mainlife age");
				calResp.setErrorExist(true);
			}
			return new ResponseEntity<Object>(calResp, HttpStatus.OK);

		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n Parameters : " + calculation.toString());
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("calculateQuotation : QuotationDtaplCalculationController");
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
			if (validation != null) {
				validation = null;
			}
			if (calResp != null) {
				calResp = null;
			}

		}
		// return null;
	}

	@RequestMapping(value = "/quoDtaplsave/{id}", method = RequestMethod.POST)
	public ResponseEntity<Object> saveDtapl(@RequestBody InvpSaveQuotation _invpSaveQuotation,
			@PathVariable Integer id) {
		// //System.out.println(id);
		HashMap<String, Object> responseMap = new HashMap<>();
		responseMap.put("status", "fail");
		QuotationCalculation calculation = null;

		Validation validation = null;
		try {
			if (id != null) {
				if (_invpSaveQuotation.get_calPersonalInfo() != null) {
					calculation = new QuotationCalculation();
					calculation.set_personalInfo(_invpSaveQuotation.get_calPersonalInfo());
					calculation.set_riderDetails(_invpSaveQuotation.get_riderDetails());
					calculation.set_product(_invpSaveQuotation.get_product());
					validation = new Validation(calculation);
					String error = validation.validateBenifict();

					if (error.equals("No")) {
						error = validation.saveEditValidations(_invpSaveQuotation.get_personalInfo());
						if (error.equalsIgnoreCase("ok")) {
							if (calculation.get_personalInfo().getMage()
									+ calculation.get_personalInfo().getTerm() < 70) {

								responseMap = dtaplService.saveQuotation(calculation, _invpSaveQuotation, id);

							} else {
								responseMap.replace("status", "Term is too large for mainlife age..");
							}
						} else {
							responseMap.replace("status", error);
						}
					} else {
						responseMap.replace("status", error);
					}

				} else {
					responseMap.replace("status", "Incomplete");
				}
			} else {
				responseMap.replace("status", "User can't be identify");
			}
			return new ResponseEntity<Object>(responseMap, HttpStatus.CREATED);
		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n Parameters : _invpSaveQuotation : "
					+ _invpSaveQuotation.toString() + " id : " + id);
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setUserId(id);
			logs.setOperation("saveDtapl : QuotationDtaplCalculationController");
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

			if (calculation != null) {
				calculation = null;
			}
			if (validation != null) {
				validation = null;
			}

		}
	}

	@RequestMapping(value = "/quoDtaplEdit/{userId}/{qdId}", method = RequestMethod.POST)
	public ResponseEntity<Object> editDtapl(@RequestBody InvpSaveQuotation _invpSaveQuotation,
			@PathVariable("userId") Integer userId, @PathVariable("qdId") Integer qdId) {

		/*
		 * //System.out.println(userId); //System.out.println(qdId);
		 * //System.out.println(_invpSaveQuotation.get_calPersonalInfo().getFrequance());
		 * //System.out.println(_invpSaveQuotation.get_personalInfo().get_plan().
		 * get_frequance());
		 */
		HashMap<String, Object> responseMap = new HashMap<>();
		responseMap.put("status", "fail");
		QuotationCalculation calculation = null;

		Validation validation = null;
		try {
			if (userId != null) {
				if (_invpSaveQuotation.get_calPersonalInfo() != null) {
					calculation = new QuotationCalculation();
					calculation.set_personalInfo(_invpSaveQuotation.get_calPersonalInfo());
					calculation.set_riderDetails(_invpSaveQuotation.get_riderDetails());
					calculation.set_product(_invpSaveQuotation.get_product());
					validation = new Validation(calculation);
					String error = validation.validateBenifict();

					if (error.equals("No")) {
						error = validation.saveEditValidations(_invpSaveQuotation.get_personalInfo());
						if (error.equalsIgnoreCase("ok")) {
							if (calculation.get_personalInfo().getMage()
									+ calculation.get_personalInfo().getTerm() < 70) {

								responseMap = dtaplService.editQuotation(calculation, _invpSaveQuotation, userId, qdId);
							} else {
								responseMap.replace("status", "Term is too large for mainlife age..");

							}
						} else {
							responseMap.replace("status", error);
						}
					} else {
						responseMap.replace("status", error);
					}

				} else {
					responseMap.replace("status", "Incomplete");
				}
			} else {
				responseMap.replace("status", "User can't be identify");
			}
			return new ResponseEntity<Object>(responseMap, HttpStatus.CREATED);
		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n Parameters : _invpSaveQuotation : "
					+ _invpSaveQuotation.toString() + " userId : " + userId + " qdId : " + qdId);
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setUserId(userId);
			logs.setOperation("editDtapl : QuotationDtaplCalculationController");
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
			if (calculation != null) {
				calculation = null;
			}
			if (validation != null) {
				validation = null;
			}
		}
	}
}
