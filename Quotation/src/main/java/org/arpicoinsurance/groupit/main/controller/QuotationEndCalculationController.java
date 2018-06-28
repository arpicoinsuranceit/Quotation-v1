package org.arpicoinsurance.groupit.main.controller;

import java.util.Date;
import java.util.HashMap;

import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.model.Logs;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.service.ENDService;
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

@CrossOrigin(origins = "*")
@RestController
public class QuotationEndCalculationController {

	@Autowired
	private ENDService endService;

	@Autowired
	private LogService logService;

	@RequestMapping(value = "/quoEndCal", method = RequestMethod.POST)
	public ResponseEntity<Object> calculateQuotation(@RequestBody QuotationCalculation calculation) {

		// System.out.println("called");

		// System.out.println(calculation.toString());

		try {
			QuotationQuickCalResponse calResp = new QuotationQuickCalResponse();
			Validation validation = new Validation(calculation);
			if (validation.validateInvpEndProd() == 1) {
				String error = validation.validateBenifict();
				if (error.equals("No")) {
					calResp = endService.getCalcutatedEnd(calculation);
					if (calResp.isErrorExist()) {
						QuotationQuickCalResponse calRespPost = new QuotationQuickCalResponse();
						calRespPost.setError(calResp.getError());
						calRespPost.setErrorExist(true);
						return new ResponseEntity<Object>(calRespPost, HttpStatus.OK);
					}
				} else {
					calResp.setErrorExist(true);
					calResp.setError(error);
				}
			} else {
				calResp.setErrorExist(true);
				calResp.setError(
						"BSA must be Greater than or Equal 250000 and Age + Term must be Less than or Equal 70");
			}
			return new ResponseEntity<Object>(calResp, HttpStatus.OK);
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
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// return null;
	}

	@RequestMapping(value = "/quoEndsave/{id}", method = RequestMethod.POST)
	public ResponseEntity<Object> saveEnd(@RequestBody InvpSaveQuotation _invpSaveQuotation, @PathVariable Integer id) {
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
					if (validation.validateInvpEndProd() == 1) {
						String error = validation.validateBenifict();
						if (error.equals("No")) {
							error = validation.saveEditValidations(_invpSaveQuotation.get_personalInfo());
							if (error.equalsIgnoreCase("ok")) {
								responseMap = endService.saveQuotation(calculation, _invpSaveQuotation, id);

							} else {
								responseMap.replace("status", error);
							}
						} else {
							responseMap.replace("status", error);
						}
					} else {
						responseMap.replace("status",
								"BSA must be Greater than or Equal 250000 and Age + Term must be Less than or Equal 70");
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
			logs.setOperation("saveEnd : QuotationEndCalculationController");
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
			if (calculation != null) {
				calculation = null;
			}
			if (validation != null) {
				validation = null;
			}
		}
	}

	@RequestMapping(value = "/quoEndEdit/{userId}/{qdId}", method = RequestMethod.POST)
	public ResponseEntity<Object> editEnd(@RequestBody InvpSaveQuotation _invpSaveQuotation,
			@PathVariable("userId") Integer userId, @PathVariable("qdId") Integer qdId) {

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
					if (validation.validateInvpEndProd() == 1) {
						String error = validation.validateBenifict();

						if (error.equals("No")) {
							error = validation.saveEditValidations(_invpSaveQuotation.get_personalInfo());
							if (error.equalsIgnoreCase("ok")) {
								responseMap = endService.editQuotation(calculation, _invpSaveQuotation, userId, qdId);

							} else {
								responseMap.replace("status", error);
							}
						} else {
							responseMap.replace("status", error);
						}
					} else {
						responseMap.replace("status", "Error at product");
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
			logs.setOperation("editEnd : QuotationEndCalculationController");
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
			if (calculation != null) {
				calculation = null;
			}
			if (validation != null) {
				validation = null;
			}
		}
	}

}
