package org.arpicoinsurance.groupit.main.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuoInvpCalResp;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.service.ASIPService;
import org.arpicoinsurance.groupit.main.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins ="*")
@RestController
public class QuotationAsipCntroller {
	
	@Autowired
	private ASIPService asipService;
	
	@RequestMapping(value="/asipCal",method=RequestMethod.POST)
	
	public QuoInvpCalResp calculateASIP(@RequestBody QuotationCalculation calculation) {
		try {	
			QuoInvpCalResp calResp = new QuoInvpCalResp();
			Validation validation = new Validation(calculation);
			
			if (validation.validateAsipProd() == 1) {
				String error = validation.validateBenifict();
				if (error.equals("No")) {
					calResp = asipService.getCalcutatedASIP(calculation);
						return calResp;
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
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "/quoAsipsave/{id}", method = RequestMethod.POST)
	public String saveInvp(@RequestBody InvpSaveQuotation _invpSaveQuotation, @PathVariable Integer id) {
		System.out.println(id);
		String resp = "Fail";
		QuotationCalculation calculation = null;
		Validation validationInvp = null;
		try {
			if (id != null) {
				if (_invpSaveQuotation.get_calPersonalInfo() != null) {
					calculation = new QuotationCalculation();
					calculation.set_personalInfo(_invpSaveQuotation.get_calPersonalInfo());
					calculation.set_riderDetails(_invpSaveQuotation.get_riderDetails());
					validationInvp = new Validation(calculation);
					if (validationInvp.validateAsipProd() == 1) {
						String error = validationInvp.validateBenifict();
						if (error.equals("No")) {

							String response = asipService.saveQuotation(calculation, _invpSaveQuotation, id);
							resp = response;
						} else {
							resp = "Error at benifict :" + error;
						}
					} else {
						resp = "Error at product";
					}
				} else {
					resp = "Incomplete";
				}
			} else {
				resp = "User can't be identify";

			}

		} catch (Exception e) {
			Logger.getLogger(QuotationInvpCalculationController.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (calculation != null) {
				calculation = null;
			}
			if (validationInvp != null) {
				validationInvp = null;
			}
		}

		return resp;
	}
}
