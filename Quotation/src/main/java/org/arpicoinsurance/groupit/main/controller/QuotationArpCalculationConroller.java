package org.arpicoinsurance.groupit.main.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.service.ARPService;
import org.arpicoinsurance.groupit.main.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class QuotationArpCalculationConroller {

	@Autowired
	private ARPService arpServie;

	@RequestMapping(value = "/arpCal", method = RequestMethod.POST)
	public QuotationQuickCalResponse calculateARP(@RequestBody QuotationCalculation calculation) {
		try {
			QuotationQuickCalResponse calResp = new QuotationQuickCalResponse();
			Validation validation = new Validation(calculation);
			if (validation.validateArpProd() == 1) {
				String error = validation.validateBenifict();
				if (error.equals("No")) {
					calResp = arpServie.getCalcutatedArp(calculation);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/quoArpsave/{id}", method = RequestMethod.POST)
	public String saveArp(@RequestBody InvpSaveQuotation _invpSaveQuotation, @PathVariable Integer id) {
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
					calculation.set_product(_invpSaveQuotation.get_product());
					validationInvp = new Validation(calculation);
					if (validationInvp.validateAsipProd() == 1) {
						String error = validationInvp.validateBenifict();
						if (error.equals("No")) {

							String response = arpServie.saveQuotation(calculation, _invpSaveQuotation, id);
							resp = response;
						} else {
							resp = error;
						}
					} else {
						resp = "Error at product ";
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
	
	@RequestMapping(value = "/quoArpEdit/{userId}/{qdId}", method = RequestMethod.POST)
	public String editArp(@RequestBody InvpSaveQuotation _invpSaveQuotation, @PathVariable("userId") Integer userId
			, @PathVariable("qdId") Integer qdId) {
		
		System.out.println(userId);
		System.out.println(qdId);
		System.out.println(_invpSaveQuotation.get_calPersonalInfo().getFrequance());
		System.out.println(_invpSaveQuotation.get_personalInfo().get_plan().get_frequance());

		String resp = "Fail";
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
					if (validation.validateAsipProd() == 1) {
						String error = validation.validateBenifict();
						if (error.equals("No")) {

							String response = arpServie.editQuotation(calculation, _invpSaveQuotation, userId,qdId);
							resp = response;
						} else {
							resp = error;
						}
					} else {
						resp = "Error at product ";
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
