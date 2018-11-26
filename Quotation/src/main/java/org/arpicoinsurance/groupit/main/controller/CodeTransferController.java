package org.arpicoinsurance.groupit.main.controller;

import java.util.List;

import org.arpicoinsurance.groupit.main.helper.CodeTransferHelper;
import org.arpicoinsurance.groupit.main.helper.CodeTransferReqHelper;
import org.arpicoinsurance.groupit.main.webclient.ReceiptClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class CodeTransferController {

	@Autowired
	private ReceiptClient receiptClient;
	
	@RequestMapping(value="/code_transfer/getCodeTransfersToApprove/{userCode:.+}", method = RequestMethod.GET)
	public List<CodeTransferHelper> getCodeTransfersToApprove(@PathVariable String userCode)throws Exception{
		return receiptClient.getCodeTransferToApprove(userCode);
	}
    
	@RequestMapping(value="/code_transfer/rejectCodeTransfer", method = RequestMethod.POST)
	public ResponseEntity<Object> rejectCodeTran(@RequestBody CodeTransferReqHelper reqHelper)throws Exception{
		
		return receiptClient.rejectCodeTransfer(reqHelper.getUser(),reqHelper.getTransId(),reqHelper.getRemark());
	}
	
	@RequestMapping(value="/code_transfer/approveCodeTransfer", method = RequestMethod.POST)
	public ResponseEntity<Object> approveCodeTran(@RequestBody CodeTransferReqHelper reqHelper)throws Exception{
		
		return receiptClient.approveCodeTransfer(reqHelper.getUser(),reqHelper.getTransId(),reqHelper.getRemark());
	}
	
}
