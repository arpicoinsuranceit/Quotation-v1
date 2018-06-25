package org.arpicoinsurance.groupit.main.controller;

import java.util.Date;
import java.util.HashMap;

import org.arpicoinsurance.groupit.main.helper.EditQuotation;
import org.arpicoinsurance.groupit.main.model.Logs;
import org.arpicoinsurance.groupit.main.model.Quotation;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;
import org.arpicoinsurance.groupit.main.service.LogService;
import org.arpicoinsurance.groupit.main.service.QuotationDetailsService;
import org.arpicoinsurance.groupit.main.service.QuotationService;
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
public class QuotationController {

	@Autowired
	private QuotationService quotationService;

	@Autowired
	private QuotationDetailsService quoDetailsService;

	@Autowired
	private LogService logService;

	@RequestMapping(value = "/quotation/{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> getQuotation(@PathVariable Integer id) {
		try {
			Quotation quotation = quotationService.getQuotation(id);
			return new ResponseEntity<Object>(quotation, HttpStatus.OK);
		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n id : " + id);
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("getQuotation : QuotationController");
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

		// return null;
	}

	@RequestMapping(value = "/quo", method = RequestMethod.POST)
	public ResponseEntity<Object> getQuotationByUserId(@RequestBody String id) {
		Integer userId = Integer.valueOf(id);
		try {
			return new ResponseEntity<Object>(quotationService.getQuotationDetails(userId), HttpStatus.OK);
		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n id : " + id);
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("getQuotation : QuotationController");
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
		// return null;
	}

	@RequestMapping(value = "/quotationDetails", method = RequestMethod.POST)
	public ResponseEntity<Object> getQuotationDetailsById(@RequestBody String id) {
		Integer qdId = Integer.valueOf(id);
		try {
			System.out.println("testtttttttttttt");
			EditQuotation quoDetails = quoDetailsService.editQuotationDetails(qdId);
			return new ResponseEntity<Object>(quoDetails, HttpStatus.OK);
		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n id : " + id);
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("Get QuotationDetail By id");
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
		// return null;
	}
	
	@RequestMapping(value = "/quotationDetailsView", method = RequestMethod.POST)
	public ResponseEntity<Object> getQuotationDetailsViewById(@RequestBody String id) {
		Integer qdId = Integer.valueOf(id);
<<<<<<< HEAD
		System.out.println("iddddddddddd");

=======
>>>>>>> refs/remotes/origin/branch-120
		try {
			EditQuotation quoDetails = quoDetailsService.editQuotationDetailsView(qdId);
			return new ResponseEntity<Object>(quoDetails, HttpStatus.OK);
		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n id : " + id);
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("Get QuotationDetail By id");
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
		// return null;
	}

	@RequestMapping(value = "/product", method = RequestMethod.POST)
	public ResponseEntity<Object> getProduct(@RequestBody String id) {
		Integer qdId = Integer.valueOf(id);
		try {
			QuotationDetails quoDetails = quoDetailsService.findQuotationDetails(qdId);
			return new ResponseEntity<Object>(quoDetails.getQuotation().getProducts().getProductCode(), HttpStatus.OK);
		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n id : " + id);
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("Get Product");
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
		// return null;
	}

	@RequestMapping(value = "/findQuotation", method = RequestMethod.POST)
	public ResponseEntity<Object> findQuotationToPrint(@RequestBody String id) {
		// System.out.println("Find Quotation Called..." + id);
		Integer qdId = Integer.valueOf(id);
		HashMap<String, String> map = new HashMap<>();

		try {
			QuotationDetails quoDetails = quoDetailsService.findFirstByQuotationOrderByQdIdDesc(qdId);
			if (quoDetails != null) {
				map.put("status", "1");
				map.put("quotationNumber", Integer.toString(quoDetails.getQuotation().getId()));
				map.put("quotationDetId", Integer.toString(quoDetails.getQdId()));
				map.put("agentCode", quoDetails.getQuotation().getUser().getUserCode());
				map.put("agentName", quoDetails.getQuotation().getUser().getUser_Name());
				map.put("branchCode", quoDetails.getQuotation().getUser().getBranch().getBranch_Code());
				map.put("branchName", quoDetails.getQuotation().getUser().getBranch().getBranch_Name());
				map.put("productName", quoDetails.getQuotation().getProducts().getProductName());
				map.put("productCode", quoDetails.getQuotation().getProducts().getProductCode());
				map.put("custName", quoDetails.getCustomerDetails().getCustName());
				map.put("custNic", quoDetails.getCustomerDetails().getCustNic());
				map.put("date", String.valueOf(quoDetails.getQuotationquotationCreateDate()));
			} else {
				map.put("status", "0");
			}
			return new ResponseEntity<Object>(map, HttpStatus.OK);
		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n id : " + id);
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("Find Quotation to Print");
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

	/*
	 * @RequestMapping(value="/getSumAtRisk",method=RequestMethod.POST) public
	 * HashMap<String, Object> getCalculateSumAtrisk(@RequestParam
	 * QuotationCalculation calculation,@RequestParam Double previous,@RequestParam
	 * String custCode) {
	 * 
	 * try { return healthReqService.getSumAtRiskDetailsMainLife(calculation,
	 * previous, custCode); } catch (Exception e) { e.printStackTrace(); } return
	 * null; }
	 */

}
