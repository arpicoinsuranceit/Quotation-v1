package org.arpicoinsurance.groupit.main.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.helper.InvpSavePersonalInfo;
import org.arpicoinsurance.groupit.main.helper.PersonalInfo;
import org.arpicoinsurance.groupit.main.helper.QuoAibCalResp;
import org.arpicoinsurance.groupit.main.model.Logs;
import org.arpicoinsurance.groupit.main.service.AIBService;
import org.arpicoinsurance.groupit.main.service.LogService;
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
public class QuotationAibCalculationController {
	@Autowired
	private AIBService aibService;

	@Autowired
	private LogService logService;

	@RequestMapping(value = "/aibCal", method = RequestMethod.POST)
	public ResponseEntity<Object> calculateAIB(@RequestBody PersonalInfo personalInfo) {
		try {
			BigDecimal bsa = aibService.calculateAIBMaturaty(2, 0.0, 0.0, 100.0, personalInfo.getBsa(), new Date(),
					"S");
			CalculationUtils calculationUtils = new CalculationUtils();
			Double adminFee = calculationUtils.getAdminFee("S");
			Double tax = calculationUtils.getTaxAmount(bsa.doubleValue() + adminFee);

			QuoAibCalResp resp = new QuoAibCalResp();
			resp.setExtraOe(adminFee + tax);
			resp.setMaturaty(bsa.doubleValue());
			return new ResponseEntity<Object>(resp, HttpStatus.OK);

		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n Parameters : " + personalInfo.toString());
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("calculateAIB : QuotationAibCalculationController");
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
		//return null;
	}

	@RequestMapping(value = "/quoAibsave/{id}", method = RequestMethod.POST)
	public ResponseEntity<Object> saveAib(@RequestBody InvpSavePersonalInfo _invpSaveQuotation,
			@PathVariable Integer id) {
		// //System.out.println(id);
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
			return new ResponseEntity<Object>(responseMap, HttpStatus.CREATED);
		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n Parameters : _invpSaveQuiotation : "
					+ _invpSaveQuotation.toString() + ", id : " + id);
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("saveAib : QuotationAibCalculationController");
			logs.setUserId(id);
			try {
				logService.saveLog(logs);
			} catch (Exception e1) {
				//System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				//System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {

		}

		
	}

	@RequestMapping(value = "/quoAibEdit/{userId}/{qdId}", method = RequestMethod.POST)
	public ResponseEntity<Object> editAib(@RequestBody InvpSavePersonalInfo _invpSaveQuotation,
			@PathVariable("userId") Integer userId, @PathVariable("qdId") Integer qdId) {

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
			return new ResponseEntity<Object>(responseMap, HttpStatus.CREATED);
		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n Parameters : _invpSaveQuiotation : "
					+ _invpSaveQuotation.toString() + ", userId : " + userId + ", quotationDetailId : " + qdId);
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("editAib : QuotationAibCalculationController");
			logs.setUserId(userId);
			try {
				logService.saveLog(logs);
			} catch (Exception e1) {
				//System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				//System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {

		}

		
	}
}
