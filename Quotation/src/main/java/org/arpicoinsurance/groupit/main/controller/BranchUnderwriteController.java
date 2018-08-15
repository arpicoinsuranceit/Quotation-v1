package org.arpicoinsurance.groupit.main.controller;

import java.util.Date;

import org.arpicoinsurance.groupit.main.model.Logs;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;
import org.arpicoinsurance.groupit.main.service.BranchUnderwriteService;
import org.arpicoinsurance.groupit.main.service.LogService;
import org.arpicoinsurance.groupit.main.service.QuotationDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class BranchUnderwriteController {

	@Autowired
	private BranchUnderwriteService branchUnderwriteService;
	
	@Autowired
	private QuotationDetailsService quotationDetailSevice;
	
	@Autowired
	private LogService logService;
	
	@RequestMapping(value = "/loadProposalToUnderwrite/{token}", method = RequestMethod.GET)
	public ResponseEntity<Object>  getProposalToUnderwrite (@PathVariable("token") String token) {
		try {
			return new ResponseEntity<>(branchUnderwriteService.getQuotationToUnderwrite(token), HttpStatus.OK);
		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage());
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("getProposalToUnderwrite : BranchUnderwriteController");
			try {
				logService.saveLog(logs);
			} catch (Exception e1) {
				System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/getQuotationDetailFromSeqNo",method=RequestMethod.POST)
	public Integer viewQuotation(@RequestParam("seqNo") Integer seqNo,@RequestParam("qId") Integer qId) {
		try {
			
			QuotationDetails details=quotationDetailSevice.findByQuotationAndSeqnum(qId, seqNo);
			if(details!=null) {
				return details.getQdId();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
