package org.arpicoinsurance.groupit.main.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.helper.InvpSavePersonalInfo;
import org.arpicoinsurance.groupit.main.helper.PersonalInfo;
import org.arpicoinsurance.groupit.main.helper.QuoAibCalResp;
import org.arpicoinsurance.groupit.main.service.AIBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class QuotationAibCalculationController {
	@Autowired
	private AIBService aibService;

	@RequestMapping(value = "/aibCal", method = RequestMethod.POST)
	public QuoAibCalResp calculateAIB(@RequestBody PersonalInfo personalInfo) {
		try {
			BigDecimal bsa = aibService.calculateAIBMaturaty(2, 0.0, 0.0, 100.0, personalInfo.getBsa(), new Date(),
					"S");
			CalculationUtils calculationUtils = new CalculationUtils();
			Double adminFee = calculationUtils.getAdminFee("S");
			Double tax = calculationUtils.getTaxAmount(bsa.doubleValue() + adminFee);

			QuoAibCalResp resp = new QuoAibCalResp();
			resp.setExtraOe(adminFee + tax);
			resp.setMaturaty(bsa.doubleValue());
			return resp;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/quoAibsave/{id}", method = RequestMethod.POST)
	public HashMap<String, Object> saveInvp(@RequestBody InvpSavePersonalInfo _invpSaveQuotation, @PathVariable Integer id) {
		System.out.println(id);
		String resp = "Fail";
		HashMap<String, Object> responseMap = new HashMap<>();
		responseMap.put("status", "fail");
		try {
			if (id != null) {
				if (_invpSaveQuotation != null) {
					responseMap = aibService.saveQuotation(_invpSaveQuotation, id);

				} else {
					responseMap.replace("status", "Incomplete");
				}
			} else {
				responseMap.replace("status", "User can't be identify");

			}

		} catch (Exception e) {
			Logger.getLogger(QuotationInvpCalculationController.class.getName()).log(Level.SEVERE, null, e);
		} finally {

		}

		return responseMap;
	}

	@RequestMapping(value = "/quoAibEdit/{userId}/{qdId}", method = RequestMethod.POST)
	public HashMap<String, Object> editAib(@RequestBody InvpSavePersonalInfo _invpSaveQuotation, @PathVariable("userId") Integer userId,
			@PathVariable("qdId") Integer qdId) {
		
		String resp = "Fail";
		HashMap<String, Object> responseMap = new HashMap<>();
		responseMap.put("status", "fail");
		try {
			if (userId != null) {
				if (qdId != null) {
					if (_invpSaveQuotation != null) {
						responseMap = aibService.editQuotation(_invpSaveQuotation, userId, qdId);

					} else {
						responseMap.replace("status", "Incomplete");
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

		}

		return responseMap;
	}
}
