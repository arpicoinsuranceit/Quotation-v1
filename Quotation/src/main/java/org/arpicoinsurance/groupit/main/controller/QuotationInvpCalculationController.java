package org.arpicoinsurance.groupit.main.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuoCalResp;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.service.INVPService;
import org.arpicoinsurance.groupit.main.validation.ValidationInvp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class QuotationInvpCalculationController {

	@Autowired
	private INVPService invpService;

	@RequestMapping(value = "/quoInvpCal", method = RequestMethod.POST)
	public QuoCalResp calculateQuotation(@RequestBody QuotationCalculation calculation) {
		// System.out.println(calculation);
		//// ******************do post validations before send response
		try {
			QuoCalResp calResp = new QuoCalResp();
			ValidationInvp validationInvp = new ValidationInvp(calculation);
			if (validationInvp.validateInvpProd() == 1) {
				String error = validationInvp.validateBenifict();
				if (error.equals("No")) {
					calResp = invpService.getCalcutatedInvp(calculation);
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

	@RequestMapping(value = "/ageCal", method = RequestMethod.POST)
	public Long calculateAge(@RequestBody String dob) {
		try {
			LocalDate dateOfBirth = LocalDate.parse(dob);
			LocalDate currentDate = LocalDate.now();
			long diffInYears = ChronoUnit.YEARS.between(dateOfBirth, currentDate);
			return diffInYears + 1;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/quoInvpsave", method = RequestMethod.POST)
	public String saveInvp(@RequestBody InvpSaveQuotation _invpSaveQuotation) {
		String resp = "Fail";
		QuotationCalculation calculation = null;
		ValidationInvp validationInvp=null;
		try {
			if (_invpSaveQuotation.get_calPersonalInfo() != null) {
				calculation = new QuotationCalculation();
				calculation.set_personalInfo(_invpSaveQuotation.get_calPersonalInfo());
				calculation.set_riderDetails(_invpSaveQuotation.get_riderDetails());
				validationInvp = new ValidationInvp(calculation);
				if (validationInvp.validateInvpProd() == 1) {
					String error = validationInvp.validateBenifict();
					if (error.equals("No")) {
						
						String response=invpService.saveQuotation(calculation, _invpSaveQuotation);
						resp=response;
					} else {
						resp="Error at benifict :"+ error;
					}
				} else {
					resp="Error at product";
				}
			}else {
				resp = "Incomplete";
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
