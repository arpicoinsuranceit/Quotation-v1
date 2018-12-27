package org.arpicoinsurance.groupit.main.controller;

import java.util.Date;

import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.model.Logs;
import org.arpicoinsurance.groupit.main.service.ATPService;
import org.arpicoinsurance.groupit.main.service.LogService;
import org.arpicoinsurance.groupit.main.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class QuotationATPCalculationController {
	

	@Autowired
	private LogService logService;
	
	@Autowired
	private ATPService atpService;
	
	@RequestMapping(value = "/quoEndCal", method = RequestMethod.POST)
	public ResponseEntity<Object> calculateQuotation(@RequestBody QuotationCalculation calculation) {
		try {
			QuotationQuickCalResponse calResp = new QuotationQuickCalResponse();
			Validation validation = new Validation(calculation);
			if (validation.validateInvpEndProd() == 1) {
				String error = validation.validateBenifict();
				if (error.equals("No")) {
					calResp = atpService.getCalcutatedAtp(calculation);
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
			e.printStackTrace();
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n Parameters : " + calculation.toString());
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("calculateQuotation : QuotationATPCalculationController");
			try {

				logService.saveLog(logs);
			} catch (Exception e1) {
				//System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				//System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/quoEndsave/{id}", method = RequestMethod.POST)
	public ResponseEntity<Object> saveEnd(@RequestBody InvpSaveQuotation _invpSaveQuotation, @PathVariable Integer id) throws Exception {
		return null;
	}
	
	@RequestMapping(value = "/quoEndEdit/{userId}/{qdId}", method = RequestMethod.POST)
	public ResponseEntity<Object> editEnd(@RequestBody InvpSaveQuotation _invpSaveQuotation,
			@PathVariable("userId") Integer userId, @PathVariable("qdId") Integer qdId) throws Exception {
		return null;
	}

	@RequestMapping(value = "/quoEndEditUnderwrite/{token}/{qdId}", method = RequestMethod.POST)
	public ResponseEntity<Object> editEndUnderwrite(@RequestBody InvpSaveQuotation _invpSaveQuotation,
			@PathVariable("token") String token, @PathVariable("qdId") Integer qdId) {
		return null;
	}

	
		

}
