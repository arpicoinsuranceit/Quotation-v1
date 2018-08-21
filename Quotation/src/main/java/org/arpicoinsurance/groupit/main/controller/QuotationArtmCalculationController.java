package org.arpicoinsurance.groupit.main.controller;

import java.util.Date;
import java.util.HashMap;

import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.model.Logs;
import org.arpicoinsurance.groupit.main.model.Users;
import org.arpicoinsurance.groupit.main.security.JwtDecoder;
import org.arpicoinsurance.groupit.main.service.ARTMService;
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
public class QuotationArtmCalculationController {

	@Autowired
	private ARTMService artmService;

	@Autowired
	private LogService logService;
	
	@Autowired
	private UsersService usersService;

	/*
	 * @RequestMapping(value = "/artmCalTest", method = RequestMethod.GET) public
	 * String calTest() { try { artmService.calculateMaturity(37,15, "M", new
	 * Date(), 15000.00,"divrat2"); } catch (Exception e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); } return null; }
	 */

	@RequestMapping(value = "/artmCal", method = RequestMethod.POST)
	public ResponseEntity<Object> calculateATRM(@RequestBody QuotationCalculation calculation) {

		//System.out.println(calculation.get_riderDetails().get_cRiders().toString());

		try {
			QuotationQuickCalResponse calResp = new QuotationQuickCalResponse();

			// System.out.println("call artm");
			Validation validation = new Validation(calculation);
			String valError = validation.validateArtm(calculation);
			if (valError.equals("ok")) {
				String error = validation.validateBenifict();
				if (error.equals("No")) {

					calResp = artmService.getCalcutatedARTM(calculation, false);
					
					//System.out.println(calResp.getPensionPremium1());
					//System.out.println(calResp.getPensionPremium2());
					//System.out.println(calResp.getPensionPremium3());

					if (calResp.isErrorExist()) {
						QuotationQuickCalResponse calRespPost = new QuotationQuickCalResponse();
						calRespPost.setError(calResp.getError());
						calRespPost.setErrorExist(true);
						return new ResponseEntity<Object>(calRespPost, HttpStatus.OK);
					}
				} else {
					calResp.setErrorExist(true);
					calResp.setError(error);
				}
			} else {
				calResp.setError(valError);
				calResp.setErrorExist(true);
			}
			return new ResponseEntity<Object>(calResp, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n Parameters : " + calculation.toString());
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("calculateQuotation : QuotationARTMCalculationController");
			try {
				logService.saveLog(logs);
			} catch (Exception e1) {
				System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/artmSchedule", method = RequestMethod.POST)
	public ResponseEntity<Object> calculateScheduleATRM(@RequestBody QuotationCalculation calculation) {

		try {
			QuotationQuickCalResponse calResp = new QuotationQuickCalResponse();

			Validation validation = new Validation(calculation);
			String valError = validation.validateArtm(calculation);
			if (valError.equals("ok")) {
				String error = validation.validateBenifict();
				if (error.equals("No")) {

					calResp = artmService.getCalcutatedARTM(calculation, true);

					if (calResp.isErrorExist()) {
						QuotationQuickCalResponse calRespPost = new QuotationQuickCalResponse();
						calRespPost.setError(calResp.getError());
						calRespPost.setErrorExist(true);
						return new ResponseEntity<Object>(calRespPost, HttpStatus.OK);
					}
				} else {
					calResp.setErrorExist(true);
					calResp.setError(error);
				}
			} else {
				calResp.setError(valError);
				calResp.setErrorExist(true);
			}
			return new ResponseEntity<Object>(calResp, HttpStatus.OK);

		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n Parameters : " + calculation.toString());
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("calculateQuotation : QuotationARTMCalculationController");
			try {
				logService.saveLog(logs);
			} catch (Exception e1) {
				System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/artmSavequo/{id}", method = RequestMethod.POST)
	public ResponseEntity<Object> saveARTM(@RequestBody InvpSaveQuotation _invpSaveQuotation,
			@PathVariable Integer id) {
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
					String valError = validation.validateArtm(calculation);
					if (valError.equals("ok")) {
						String error = validation.validateBenifict();
						if (error.equals("No")) {
							error = validation.saveEditValidations(_invpSaveQuotation.get_personalInfo());
							if (error.equalsIgnoreCase("ok")) {
								responseMap = artmService.saveQuotation(calculation, _invpSaveQuotation, id);
							} else {
								responseMap.replace("status", error);
							}
						} else {
							responseMap.replace("status", error);
						}
					} else {
						responseMap.replace("status", valError);
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
					+ _invpSaveQuotation.toString() + " id : " + id);
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("saveEnd : QuotationARTMCalculationController");
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

	@RequestMapping(value = "/quoArtmEdit/{userId}/{qdId}", method = RequestMethod.POST)
	public ResponseEntity<Object> editARTM(@RequestBody InvpSaveQuotation _invpSaveQuotation,
			@PathVariable("userId") Integer userId, @PathVariable("qdId") Integer qdId) {

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
					String valError = validation.validateArtm(calculation);
					if (valError.equals("ok")) {
						String error = validation.validateBenifict();
						if (error.equals("No")) {
							error = validation.saveEditValidations(_invpSaveQuotation.get_personalInfo());
							if (error.equalsIgnoreCase("ok")) {
								responseMap = artmService.editQuotation(calculation, _invpSaveQuotation, userId, qdId);
							} else {
								responseMap.replace("status", error);
							}
						} else {
							responseMap.replace("status", error);
						}
					} else {
						responseMap.replace("status", valError);
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
			logs.setOperation("editEnd : QuotationARTMCalculationController");
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

	/*
	 * @RequestMapping(value = "/artmshedule", method = RequestMethod.POST) public
	 * ArrayList<AipCalShedule> loadSheduleATRM (@RequestBody Plan plan) {
	 * 
	 * Validation validation = new Validation();
	 * if(validation.validateArtm(plan).equals("ok")) { try { return
	 * (ArrayList<AipCalShedule>) artmService.calculateARTMMaturaty(plan, 1.0,
	 * true,false).getAipCalShedules(); } catch (Exception e) { e.printStackTrace();
	 * } }
	 * 
	 * return null; }
	 * 
	 */
	
	@RequestMapping(value = "/quoArtmEditUnderwrite/{token}/{qdId}", method = RequestMethod.POST)
	public ResponseEntity<Object> editARTMUnderwrite(@RequestBody InvpSaveQuotation _invpSaveQuotation,
			@PathVariable("token") String token, @PathVariable("qdId") Integer qdId) {

		String userCode=new JwtDecoder().generate(token);
		
		HashMap<String, Object> responseMap = new HashMap<>();
		responseMap.put("status", "fail");
		QuotationCalculation calculation = null;

		Validation validation = null;
		Users user=null;
		
		try {
			if(userCode!=null) {
				 user=usersService.getUserByUserCode(userCode);
				if (_invpSaveQuotation.get_calPersonalInfo() != null) {
					calculation = new QuotationCalculation();
					calculation.set_personalInfo(_invpSaveQuotation.get_calPersonalInfo());
					calculation.set_riderDetails(_invpSaveQuotation.get_riderDetails());
					calculation.set_product(_invpSaveQuotation.get_product());
					validation = new Validation(calculation);
					String valError = validation.validateArtm(calculation);
					if (valError.equals("ok")) {
						String error = validation.validateBenifict();
						if (error.equals("No")) {
							error = validation.saveEditValidations(_invpSaveQuotation.get_personalInfo());
							if (error.equalsIgnoreCase("ok")) {
								responseMap = artmService.editQuotation(calculation, _invpSaveQuotation, user.getUserId(), qdId);
							} else {
								responseMap.replace("status", error);
							}
						} else {
							responseMap.replace("status", error);
						}
					} else {
						responseMap.replace("status", valError);
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
			logs.setOperation("editARTMUnderwrite : QuotationARTMCalculationController");
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
