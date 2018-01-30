package org.arpicoinsurance.groupit.main.controller;

import org.arpicoinsurance.groupit.main.helper.QuoInvpCalResp;
import org.arpicoinsurance.groupit.main.helper.QuotationInvpCalculation;
import org.arpicoinsurance.groupit.main.service.ASIPService;
import org.arpicoinsurance.groupit.main.validation.ValidationInvp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class QuotationAsipController {
	
	@Autowired
	private ASIPService asipService;
	
	@RequestMapping(value = "/quoAsipCal", method = RequestMethod.POST)
	public QuoInvpCalResp calculateQuotation(@RequestBody QuotationInvpCalculation calculation) {
		try {
			QuoInvpCalResp calResp = new QuoInvpCalResp();
			ValidationInvp validationInvp = new ValidationInvp(calculation);
			if (validationInvp.validateInvpProd() == 1) {
				String error = validationInvp.validateBenifict();
				if (error.equals("No")) {
					//calResp = invpService.getCalcutatedInvp(calculation);
					if(!validationInvp.invpPostValidation(calResp)) {
						calResp.setErrorExist(true);
						calResp.setError("Product");
					}
				} else {
					calResp.setErrorExist(true);
					calResp.setError(error);
				}
			} else {
				calResp.setErrorExist(true);
				calResp.setError("Product");
			}
			return calResp;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
