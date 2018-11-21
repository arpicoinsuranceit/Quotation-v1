package org.arpicoinsurance.groupit.main.controller;

import java.util.List;

import org.arpicoinsurance.groupit.main.helper.CodeTransferHelper;
import org.arpicoinsurance.groupit.main.webclient.ReceiptClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class CodeTransferController {

	@Autowired
	private ReceiptClient receiptClient;
	
	@RequestMapping(value="/code_transfer/getCodeTransfersToApprove", method = RequestMethod.POST)
	public List<CodeTransferHelper> getCodeTransfersToApprove(@RequestParam("userCode")String userCode)throws Exception{
		return receiptClient.getCodeTransferToApprove(userCode);
	}
    
	@RequestMapping(value="/code_transfer/rejectCodeTransfer", method = RequestMethod.POST)
	public ResponseEntity<Object> rejectCodeTran(@RequestParam("user")String user,@RequestParam("codeTransferId")String codeTransferId
			,@RequestParam("remark")String remark)throws Exception{
		
		return receiptClient.rejectCodeTransfer(user,codeTransferId,remark);
	}
	
	@RequestMapping(value="/code_transfer/approveCodeTransfer", method = RequestMethod.POST)
	public ResponseEntity<Object> approveCodeTran(@RequestParam("user")String user,@RequestParam("codeTransferId")String codeTransferId
			,@RequestParam("remark")String remark)throws Exception{
		
		return receiptClient.approveCodeTransfer(user,codeTransferId,remark);
	}
	
}
