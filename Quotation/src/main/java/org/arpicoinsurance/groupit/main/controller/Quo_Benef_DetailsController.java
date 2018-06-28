package org.arpicoinsurance.groupit.main.controller;

import java.util.ArrayList;
import java.util.Date;

import org.arpicoinsurance.groupit.main.helper.ViewQuotation;
import org.arpicoinsurance.groupit.main.model.Logs;
import org.arpicoinsurance.groupit.main.model.Shedule;
import org.arpicoinsurance.groupit.main.service.LogService;
import org.arpicoinsurance.groupit.main.service.Quo_Benef_DetailsService;
import org.arpicoinsurance.groupit.main.service.SheduleService;
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
public class Quo_Benef_DetailsController {

	@Autowired
	private Quo_Benef_DetailsService quoBenefDetailService;
	
	@Autowired
	private SheduleService sheduleService;
	
	@Autowired
	private LogService logService;
	
	/*@RequestMapping(value="/quodetails",method=RequestMethod.POST)
	public ArrayList<QuotationView> getQuotationByUserId(@RequestBody String id) {
		try {
			System.out.println(id);
			Integer quoId=Integer.valueOf(id);
			ArrayList<QuotationView> detailList=(ArrayList<QuotationView>) quoBenefDetailService.getQuo_Benef_DetailsByQuoDetailId(quoId);
			return detailList;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}*/
	
	@RequestMapping(value="/quodetails",method=RequestMethod.POST)
	public ResponseEntity<Object> viewQuotationDetails(@RequestBody String id) {
		try {
			System.out.println(id);
			Integer quoId=Integer.valueOf(id);
			ArrayList<ViewQuotation> detailList=(ArrayList<ViewQuotation>) quoBenefDetailService.getQuotationDetails(quoId);
			return new ResponseEntity<Object>(detailList , HttpStatus.OK);
			
		} catch (Exception e) {
			//e.printStackTrace();
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\nParameters : " + id);
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("viewQuotationDetails(String) : Quo_Benef_DetailsController");
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
		
		//return null;
	}
	
	@RequestMapping(value="/quodetailsView",method=RequestMethod.POST)
	public ResponseEntity<Object> viewQuotationDetailsView(@RequestBody String id) {
		try {
			//System.out.println(id);
			Integer quoId=Integer.valueOf(id);
			ArrayList<ViewQuotation> detailList=(ArrayList<ViewQuotation>) quoBenefDetailService.getQuotationDetailsView(quoId);
			return new ResponseEntity<Object>(detailList , HttpStatus.OK);
			
		} catch (Exception e) { 
			e.printStackTrace();
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\nParameters : " + id);
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("viewQuotationDetails(String) : Quo_Benef_DetailsController");
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
		
		//return null;
	}
	
	
	
	@RequestMapping(value="/scheduledetails/{id}",method=RequestMethod.GET)
	public ResponseEntity<Object> viewQuotationDetails(@PathVariable Integer id) {
		try {
			ArrayList<Shedule> sheduleList=(ArrayList<Shedule>) sheduleService.findByQuotationDetails(id);
			return new ResponseEntity<Object>(sheduleList , HttpStatus.OK);
			
		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\nParameters : " + id);
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("viewQuotationDetails(Integer) : Quo_Benef_DetailsController");
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
		
		//return null;
	}
}
