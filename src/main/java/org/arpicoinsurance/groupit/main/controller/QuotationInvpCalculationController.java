package org.arpicoinsurance.groupit.main.controller;

import java.util.Date;
import java.util.HashMap;
import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.model.Logs;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.service.INVPService;
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
public class QuotationInvpCalculationController {

	@Autowired
	private INVPService invpService;

	@Autowired
	private LogService logService;

	@RequestMapping(value = "/quoInvpCal", method = RequestMethod.POST)
	public ResponseEntity<Object> calculateQuotation(@RequestBody QuotationCalculation calculation) {
		//// ******************do post validations before send response
		try {
			QuotationQuickCalResponse calResp = new QuotationQuickCalResponse();
			Validation validation = new Validation(calculation);
			if (validation.validateInvpEndProd() == 1) {
				String error = validation.validateBenifict();

				// System.out.println(error + "aaaaaaaaaaaaaaaaaaaaaaaaaaa");
				if ((calculation.get_personalInfo().getMage() + calculation.get_personalInfo().getTerm()) <= 70) {
					if (error.equals("No")) {
						calResp = invpService.getCalcutatedInvp(calculation);
						if (validation.InvpPostValidation(calResp)) {
							return new ResponseEntity<Object>(calResp, HttpStatus.OK);
						} else {
							calResp.setErrorExist(true);
							calResp.setError("Product");
						}
					} else {
						calResp.setErrorExist(true);
						calResp.setError(error);
					}
				} else {
					calResp.setError("Term is too large for mainlife age..");
					calResp.setErrorExist(true);
				}
			} else {
				calResp.setErrorExist(true);
				calResp.setError("Product");
			}
			return new ResponseEntity<Object>(calResp, HttpStatus.OK);
		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n Parameters : " + calculation.toString());
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("calculateQuotation : QuotationInvpCalculationController");
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

	@RequestMapping(value = "/quoInvpsave/{id}", method = RequestMethod.POST)
	public ResponseEntity<Object> saveInvp(@RequestBody InvpSaveQuotation _invpSaveQuotation,
			@PathVariable Integer id) {
		// System.out.println(id);

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
					if ((calculation.get_personalInfo().getMage() + calculation.get_personalInfo().getTerm()) <= 70) {
						if (validation.validateInvpEndProd() == 1) {
							String error = validation.validateBenifict();

							// System.out.println(error + "aaaaaaaaaaaaaaaaaaaaaaaaaaa");

							if (error.equals("No")) {
								error = validation.saveEditValidations(_invpSaveQuotation.get_personalInfo());
								if (error.equalsIgnoreCase("ok")) {

									responseMap = invpService.saveQuotation(calculation, _invpSaveQuotation, id);
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
						responseMap.replace("status", "Term is too large for mainlife age..");
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
			logs.setOperation("saveInvp : QuotationInvpCalculationController");
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

	@RequestMapping(value = "/quoInvpEdit/{userId}/{qdId}", method = RequestMethod.POST)
	public ResponseEntity<Object> editInvp(@RequestBody InvpSaveQuotation _invpSaveQuotation,
			@PathVariable("userId") Integer userId, @PathVariable("qdId") Integer qdId) {

		/*
		 * System.out.println(userId); System.out.println(qdId);
		 * System.out.println(_invpSaveQuotation.get_calPersonalInfo().getFrequance());
		 * System.out.println(_invpSaveQuotation.get_personalInfo().get_plan().
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
					if ((calculation.get_personalInfo().getMage() + calculation.get_personalInfo().getTerm()) <= 70) {
						if (validation.validateInvpEndProd() == 1) {
							String error = validation.validateBenifict();

							// System.out.println(error + "aaaaaaaaaaaaaaaaaaaaaaaaaaa");

							if (error.equals("No")) {
								error = validation.saveEditValidations(_invpSaveQuotation.get_personalInfo());
								if (error.equalsIgnoreCase("ok")) {
									responseMap = invpService.editQuotation(calculation, _invpSaveQuotation, userId,
											qdId);
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
						responseMap.replace("status", "Term is too large for mainlife age..");
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
			logs.setOperation("editInvp : QuotationInvpCalculationController");
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
