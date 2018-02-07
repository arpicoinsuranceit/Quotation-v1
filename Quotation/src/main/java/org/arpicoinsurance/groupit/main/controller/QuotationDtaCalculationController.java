package org.arpicoinsurance.groupit.main.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.service.DTAService;
import org.arpicoinsurance.groupit.main.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class QuotationDtaCalculationController {

	@Autowired
	private DTAService dtaService;

	@RequestMapping(value = "/quoDtaCal", method = RequestMethod.POST)
	public QuotationQuickCalResponse calculateQuotation(@RequestBody QuotationCalculation calculation) {

		try {
			QuotationQuickCalResponse calResp = new QuotationQuickCalResponse();
			calResp = dtaService.getCalcutatedDta(calculation);
			return calResp;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/quoDtasave/{id}", method = RequestMethod.POST)
	public String saveInvp(@RequestBody InvpSaveQuotation _invpSaveQuotation, @PathVariable Integer id) {
		System.out.println(id);
		String resp = "Fail";
		QuotationCalculation calculation = null;

		Validation validation = null;
		try {
			if (id != null) {
				if (_invpSaveQuotation.get_calPersonalInfo() != null) {
					calculation = new QuotationCalculation();
					calculation.set_personalInfo(_invpSaveQuotation.get_calPersonalInfo());
					calculation.set_riderDetails(_invpSaveQuotation.get_riderDetails());
					validation = new Validation(calculation);
					String error = validation.validateBenifict();

					if (error.equals("No")) {

						String response = dtaService.saveQuotation(calculation, _invpSaveQuotation, id);
						resp = response;
					} else {
						resp =  error;
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
			if (validation != null) {
				validation = null;
			}
			
		}

		return resp;
	}

}
