package org.arpicoinsurance.groupit.main.controller;

import java.util.Date;
import java.util.HashMap;
import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.model.Logs;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.service.ATRMService;
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
public class QuotationAtrmCalculationController {

	@Autowired
	private ATRMService atrmService;

	@Autowired
	private LogService logService;

	private double totPre = 0.0;

	@RequestMapping(value = "/quoAtrmCal", method = RequestMethod.POST)
	public ResponseEntity<Object> calculateQuotation(@RequestBody QuotationCalculation calculation) {
		// System.out.println(calculation);
		// System.out.println(calculation.get_personalInfo().getSgenger() +
		// "***************************");

		// System.out.println(calculation.get_personalInfo().getMgenger() +
		// "************************************");
		try {
			QuotationQuickCalResponse calResp = new QuotationQuickCalResponse();
			Validation validation = new Validation(calculation);
			if (validation.validateInvpEndProd() == 1) {
				String error = validation.validateBenifict();
				if (error.equals("No")) {
					calResp = atrmService.getCalcutatedAtrm(calculation);
					totPre = calResp.getTotPremium();
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
				calResp.setError("Product");
			}
			return new ResponseEntity<Object>(calResp, HttpStatus.OK);
		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n Parameters : " + calculation.toString());
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("calculateQuotation : QuotationAtrmCalculationController");
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

	@RequestMapping(value = "/quoAtrmsave/{id}", method = RequestMethod.POST)
	public ResponseEntity<Object> saveAtrm(@RequestBody InvpSaveQuotation _invpSaveQuotation,
			@PathVariable Integer id) {

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
								if (validation.validateInvpProdTotPremium(totPre).equals(1)) {
									responseMap = atrmService.saveQuotation(calculation, _invpSaveQuotation, id);
								} else {
									responseMap.replace("status", "Total Premium must be greater than 1250");
								}
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
			logs.setData("Error : " + e.getMessage() + ",\n Parameters : _invpSaveQuotation : " + calculation.toString()
					+ ", id : " + id);
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("saveAtrm : QuotationAtrmCalculationController");
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

	@RequestMapping(value = "/quoAtrmEdit/{userId}/{qdId}", method = RequestMethod.POST)
	public ResponseEntity<Object> editAtrm(@RequestBody InvpSaveQuotation _invpSaveQuotation,
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
					if (validation.validateInvpEndProd() == 1) {
						String error = validation.validateBenifict();
						if (error.equals("No")) {
							error = validation.saveEditValidations(_invpSaveQuotation.get_personalInfo());
							if (error.equalsIgnoreCase("ok")) {
								if (validation.validateInvpProdTotPremium(totPre).equals(1)) {
									responseMap = atrmService.editQuotation(calculation, _invpSaveQuotation, userId,
											qdId);
								} else {
									responseMap.replace("status", "Total Premium must be greater than 1250");
								}
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
			logs.setData("Error : " + e.getMessage() + ",\n Parameters : _invpSaveQuotation : " + calculation.toString()
					+ ", userId : " + userId + ", qdId : " + qdId);
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("editAtrm : QuotationAtrmCalculationController");
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
