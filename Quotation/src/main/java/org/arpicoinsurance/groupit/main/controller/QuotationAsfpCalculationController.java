package org.arpicoinsurance.groupit.main.controller;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.service.ASFPService;
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
public class QuotationAsfpCalculationController {

	@Autowired
	private ASFPService asfpService;

	// private Double totPre=0.0;

	@RequestMapping(value = "/quoAsfpCal", method = RequestMethod.POST)
	public QuotationQuickCalResponse calculateQuotation(@RequestBody QuotationCalculation calculation) {
		System.out.println(calculation.get_personalInfo().getMgenger());
		try {
			QuotationQuickCalResponse calResp = new QuotationQuickCalResponse();
			Validation validation = new Validation(calculation);
			if (validation.validateAsfpProd() == 1) {
				String error = validation.validateBenifict();
				if (error.equals("No")) {
					calResp = asfpService.getCalcutatedAsfp(calculation);
					// totPre=calResp.getTotPremium();
					if (calResp.isErrorExist()) {
						QuotationQuickCalResponse calRespPost = new QuotationQuickCalResponse();
						calRespPost.setError(calResp.getError());
						calRespPost.setErrorExist(true);
						return calRespPost;
					}
				} else {
					calResp.setErrorExist(true);
					calResp.setError(error);
				}
			} else {
				calResp.setErrorExist(true);
				calResp.setError("Product");
			}

			if (calResp.getL2() == 0) {
				calResp.setL2(calculation.get_personalInfo().getBsa());
			}

			return calResp;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/quoAsfpsave/{id}", method = RequestMethod.POST)
	public HashMap<String, Object> saveAsfp(@RequestBody InvpSaveQuotation _invpSaveQuotation,
			@PathVariable Integer id) {
		System.out.println(id);
		String resp = "Fail";

		HashMap<String, Object> responseMap = new HashMap<>();
		responseMap.put("status", "fail");

		QuotationCalculation calculation = null;
		Validation validation = null;
		CalculationUtils utils = new CalculationUtils();
		try {
			if (id != null) {
				if (_invpSaveQuotation.get_calPersonalInfo() != null) {
					calculation = new QuotationCalculation();
					calculation.set_personalInfo(_invpSaveQuotation.get_calPersonalInfo());
					calculation.set_riderDetails(_invpSaveQuotation.get_riderDetails());
					calculation.set_product(_invpSaveQuotation.get_product());
					validation = new Validation(calculation);
					if (validation.validateAsfpProd() == 1) {
						String error = validation.validateBenifict();
						if (error.equals("No")) {

							// if(validation.validateAsfpProdTotPremium(totPre,utils.getPayterm(calculation.get_personalInfo().getFrequance())).equals(1))
							// {
							responseMap = asfpService.saveQuotation(calculation, _invpSaveQuotation, id);

							// }else {
							/// resp = "Total Premium times frequency must be greater than 1250 times
							// frequency";
							// }

						} else {
							responseMap.replace("status", "Error at benifict :" + error);
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

		} catch (Exception e) {
			Logger.getLogger(QuotationAsfpCalculationController.class.getName()).log(Level.SEVERE, null, e);
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

	@RequestMapping(value = "/quoAsfpEdit/{userId}/{qdId}", method = RequestMethod.POST)
	public HashMap<String, Object> editAsfp(@RequestBody InvpSaveQuotation _invpSaveQuotation,
			@PathVariable("userId") Integer userId, @PathVariable("qdId") Integer qdId) {
		System.out.println(qdId);
		String resp = "Fail";
		HashMap<String, Object> responseMap = new HashMap<>();
		responseMap.put("status", "fail");
		QuotationCalculation calculation = null;
		Validation validation = null;
		CalculationUtils utils = new CalculationUtils();
		try {
			if (userId != null) {
				if (_invpSaveQuotation.get_calPersonalInfo() != null) {
					calculation = new QuotationCalculation();
					calculation.set_personalInfo(_invpSaveQuotation.get_calPersonalInfo());
					calculation.set_riderDetails(_invpSaveQuotation.get_riderDetails());
					calculation.set_product(_invpSaveQuotation.get_product());
					validation = new Validation(calculation);
					if (validation.validateAsfpProd() == 1) {
						String error = validation.validateBenifict();
						if (error.equals("No")) {

							responseMap = asfpService.editQuotation(calculation, _invpSaveQuotation, userId, qdId);
							
						} else {
							resp = error;
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

		} catch (Exception e) {
			Logger.getLogger(QuotationAsfpCalculationController.class.getName()).log(Level.SEVERE, null, e);
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
