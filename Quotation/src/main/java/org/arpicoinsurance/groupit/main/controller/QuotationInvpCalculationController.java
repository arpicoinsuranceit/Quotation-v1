package org.arpicoinsurance.groupit.main.controller;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.service.INVPService;
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
public class QuotationInvpCalculationController {

	@Autowired
	private INVPService invpService;

	@RequestMapping(value = "/quoInvpCal", method = RequestMethod.POST)
	public QuotationQuickCalResponse calculateQuotation(@RequestBody QuotationCalculation calculation) {
		//// ******************do post validations before send response
		try {
			QuotationQuickCalResponse calResp = new QuotationQuickCalResponse();
			Validation validation = new Validation(calculation);
			if (validation.validateInvpEndProd() == 1) {
				String error = validation.validateBenifict();

				System.out.println(error + "aaaaaaaaaaaaaaaaaaaaaaaaaaa");
				if ((calculation.get_personalInfo().getMage() + calculation.get_personalInfo().getTerm()) <= 70) {
					if (error.equals("No")) {
						calResp = invpService.getCalcutatedInvp(calculation);
						if (validation.InvpPostValidation(calResp)) {
							return calResp;
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
			return calResp;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/quoInvpsave/{id}", method = RequestMethod.POST)
	public HashMap<String, Object> saveInvp(@RequestBody InvpSaveQuotation _invpSaveQuotation, @PathVariable Integer id) {
		System.out.println(id);

		String resp = "Fail";
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

							System.out.println(error + "aaaaaaaaaaaaaaaaaaaaaaaaaaa");

							if (error.equals("No")) {

								responseMap = invpService.saveQuotation(calculation, _invpSaveQuotation, id);
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

		return responseMap;
	}

	@RequestMapping(value = "/quoInvpEdit/{userId}/{qdId}", method = RequestMethod.POST)
	public HashMap<String, Object> editInvp(@RequestBody InvpSaveQuotation _invpSaveQuotation, @PathVariable("userId") Integer userId,
			@PathVariable("qdId") Integer qdId) {

		System.out.println(userId);
		System.out.println(qdId);
		System.out.println(_invpSaveQuotation.get_calPersonalInfo().getFrequance());
		System.out.println(_invpSaveQuotation.get_personalInfo().get_plan().get_frequance());

		String resp = "Fail";
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

							System.out.println(error + "aaaaaaaaaaaaaaaaaaaaaaaaaaa");

							if (error.equals("No")) {

								responseMap = invpService.editQuotation(calculation, _invpSaveQuotation, userId,
										qdId);
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

		return responseMap;
	}

}
