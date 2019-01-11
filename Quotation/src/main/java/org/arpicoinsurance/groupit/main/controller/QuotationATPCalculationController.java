package org.arpicoinsurance.groupit.main.controller;

import java.util.Date;
import java.util.HashMap;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.model.Logs;
import org.arpicoinsurance.groupit.main.model.Users;
import org.arpicoinsurance.groupit.main.security.JwtDecoder;
import org.arpicoinsurance.groupit.main.service.ATPService;
import org.arpicoinsurance.groupit.main.service.LogService;
import org.arpicoinsurance.groupit.main.service.UsersService;
import org.arpicoinsurance.groupit.main.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class QuotationATPCalculationController {

	@Autowired
	private CalculationUtils calculationUtils;

	@Autowired
	private LogService logService;

	@Autowired
	private ATPService atpService;

	@Autowired
	private UsersService usersService;

	@RequestMapping(value = "/quoAtpCal", method = RequestMethod.POST)
	public ResponseEntity<Object> calculateQuotation(@RequestBody QuotationCalculation calculation) {
		try {
			QuotationQuickCalResponse calResp = new QuotationQuickCalResponse();
			Validation validation = new Validation(calculation);
			if (validation.validateAtpProd() == 1) {
//				String error = validation.validateBenifict();
//				if (error.equals("No")) {
				calculation.set_product("ATP");
				calResp = atpService.getCalcutatedAtp(calculation);
				if (calResp.isErrorExist()) {
					QuotationQuickCalResponse calRespPost = new QuotationQuickCalResponse();
					calRespPost.setError(calResp.getError());
					calRespPost.setErrorExist(true);
					return new ResponseEntity<Object>(calRespPost, HttpStatus.OK);
				}
//				} else {
//					calResp.setErrorExist(true);
//					calResp.setError(error);
//				}
			} else {
				calResp.setErrorExist(true);
				calResp.setError(
						"Invesment Amount must be Greater than or Equal 200,000 and Age + Term must be Less than or Equal 70");
			}
			return new ResponseEntity<Object>(calResp, HttpStatus.OK);
		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n Parameters : " + calculation.toString());
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("calculateQuotation : QuotationATPCalculationController");
			try {

				logService.saveLog(logs);
			} catch (Exception e1) {
				// System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				// System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/quoAtpsave/{id}", method = RequestMethod.POST)
	public ResponseEntity<Object> saveAtp(@RequestBody InvpSaveQuotation _invpSaveQuotation, @PathVariable Integer id)
			throws Exception {
		HashMap<String, Object> responseMap = new HashMap<>();
		responseMap.put("status", "fail");

		String phone = calculationUtils.getPhoneNo(_invpSaveQuotation.get_personalInfo().get_mainlife().get_mMobile());

		if (!phone.equals("Error")) {
			_invpSaveQuotation.get_personalInfo().get_mainlife().set_mMobile(phone);
		} else {
			responseMap.replace("status", "Phone No Invalied");
			return new ResponseEntity<Object>(responseMap, HttpStatus.BAD_REQUEST);
		}
		System.out.println("/////////////////////////// Phone Validation");
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
					if (validation.validateAtpProd() == 1) {
						String error = "No";
						if (error.equals("No")) {
							error = validation.saveEditValidations(_invpSaveQuotation.get_personalInfo());

							System.out.println("/////////////////////////// Validation" + error);

							if (error.equalsIgnoreCase("ok")) {
								responseMap = atpService.saveQuotation(calculation, _invpSaveQuotation, id);

							} else {
								responseMap.replace("status", error);
							}
						} else {
							responseMap.replace("status", error);
						}
					} else {
						responseMap.replace("status",
								"Invesment Amount must be Greater than or Equal 200000 and Age + Term must be Less than or Equal 70");
					}
				} else {
					responseMap.replace("status", "Incomplete");
				}
			} else {
				responseMap.replace("status", "User can't be identify");

			}
			return new ResponseEntity<Object>(responseMap, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n Parameters : _invpSaveQuotation : "
					+ _invpSaveQuotation.toString() + " id : " + id);
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("saveAtp : QuotationATPCalculationController");
			try {
				logService.saveLog(logs);
			} catch (Exception e1) {
				// System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				// System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (calculation != null) {
				calculation = null;
			}
			if (validation != null) {
				validation = null;
			}
		}
	}

	@RequestMapping(value = "/quoAtpEdit/{userId}/{qdId}", method = RequestMethod.POST)
	public ResponseEntity<Object> editEnd(@RequestBody InvpSaveQuotation _invpSaveQuotation,
			@PathVariable("userId") Integer userId, @PathVariable("qdId") Integer qdId) throws Exception {
		HashMap<String, Object> responseMap = new HashMap<>();
		responseMap.put("status", "fail");
		QuotationCalculation calculation = null;

		String phone = calculationUtils.getPhoneNo(_invpSaveQuotation.get_personalInfo().get_mainlife().get_mMobile());

		if (!phone.equals("Error")) {
			_invpSaveQuotation.get_personalInfo().get_mainlife().set_mMobile(phone);
		} else {
			responseMap.replace("status", "Phone No Invalied");
			return new ResponseEntity<Object>(responseMap, HttpStatus.BAD_REQUEST);
		}

		Validation validation = null;
		try {
			if (userId != null) {
				if (_invpSaveQuotation.get_calPersonalInfo() != null) {
					calculation = new QuotationCalculation();
					calculation.set_personalInfo(_invpSaveQuotation.get_calPersonalInfo());
					calculation.set_riderDetails(_invpSaveQuotation.get_riderDetails());
					calculation.set_product(_invpSaveQuotation.get_product());
					validation = new Validation(calculation);
					if (validation.validateAtpProd() == 1) {
						String error = "No";

						if (error.equals("No")) {
							error = validation.saveEditValidations(_invpSaveQuotation.get_personalInfo());
							if (error.equalsIgnoreCase("ok")) {
								responseMap = atpService.editQuotation(calculation, _invpSaveQuotation, userId, qdId,
										1);

							} else {
								responseMap.replace("status", error);
							}
						} else {
							responseMap.replace("status", error);
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
			return new ResponseEntity<Object>(responseMap, HttpStatus.CREATED);
		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n Parameters : _invpSaveQuotation : "
					+ _invpSaveQuotation.toString() + " userId : " + userId + " qdId : " + qdId);
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("editEnd : QuotationEndCalculationController");
			try {
				logService.saveLog(logs);
			} catch (Exception e1) {
				// System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				// System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (calculation != null) {
				calculation = null;
			}
			if (validation != null) {
				validation = null;
			}
		}
	}

	@RequestMapping(value = "/quoAtpEditUnderwrite/{token}/{qdId}", method = RequestMethod.POST)
	public ResponseEntity<Object> editEndUnderwrite(@RequestBody InvpSaveQuotation _invpSaveQuotation,
			@PathVariable("token") String token, @PathVariable("qdId") Integer qdId) {
		String userCode = new JwtDecoder().generate(token);

		HashMap<String, Object> responseMap = new HashMap<>();
		responseMap.put("status", "fail");
		QuotationCalculation calculation = null;

		Validation validation = null;
		Users user = null;

		try {
			if (userCode != null) {
				user = usersService.getUserByUserCode(userCode);
				if (_invpSaveQuotation.get_calPersonalInfo() != null) {
					calculation = new QuotationCalculation();
					calculation.set_personalInfo(_invpSaveQuotation.get_calPersonalInfo());
					calculation.set_riderDetails(_invpSaveQuotation.get_riderDetails());
					calculation.set_product(_invpSaveQuotation.get_product());
					validation = new Validation(calculation);
					if (validation.validateAtpProd() == 1) {
						String error = "No";

						if (error.equals("No")) {
							error = validation.saveEditValidations(_invpSaveQuotation.get_personalInfo());
							if (error.equalsIgnoreCase("ok")) {
								responseMap = atpService.editQuotation(calculation, _invpSaveQuotation,
										user.getUserId(), qdId, 2);

							} else {
								responseMap.replace("status", error);
							}
						} else {
							responseMap.replace("status", error);
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
			return new ResponseEntity<Object>(responseMap, HttpStatus.CREATED);
		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n Parameters : _invpSaveQuotation : "
					+ _invpSaveQuotation.toString() + " userId : " + user.getUserId() + " qdId : " + qdId);
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("editEndUnderwrite : QuotationEndCalculationController");
			try {
				logService.saveLog(logs);
			} catch (Exception e1) {
				System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (calculation != null) {
				calculation = null;
			}
			if (validation != null) {
				validation = null;
			}
		}
	}

}
