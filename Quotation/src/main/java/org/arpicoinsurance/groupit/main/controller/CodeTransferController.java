package org.arpicoinsurance.groupit.main.controller;

import java.util.List;

import org.arpicoinsurance.groupit.main.helper.Agent;
import org.arpicoinsurance.groupit.main.helper.CodeTransfer;
import org.arpicoinsurance.groupit.main.helper.CodeTransferHelper;
import org.arpicoinsurance.groupit.main.helper.CodeTransferReqHelper;
import org.arpicoinsurance.groupit.main.helper.SaveCodeTransfer;
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
	
	@RequestMapping(value="/code_transfer/getCodeTransfersToApprove", method = RequestMethod.POST)
	public List<CodeTransferHelper> getCodeTransfersToApprove(@RequestParam("userCode") String userCode,@RequestParam("dashPara") String dashPara,@RequestParam("userType") String userType)throws Exception{
		return receiptClient.getCodeTransferToApprove(userCode,dashPara,userType);
	}
    
	@RequestMapping(value="/code_transfer/rejectCodeTransfer", method = RequestMethod.POST)
	public ResponseEntity<Object> rejectCodeTran(@RequestBody CodeTransferReqHelper reqHelper)throws Exception{
		
		return receiptClient.rejectCodeTransfer(reqHelper.getUser(),reqHelper.getTransId(),reqHelper.getRemark());
	}
	
	@RequestMapping(value="/code_transfer/approveCodeTransfer", method = RequestMethod.POST)
	public ResponseEntity<Object> approveCodeTran(@RequestBody CodeTransferReqHelper reqHelper)throws Exception{
		
		return receiptClient.approveCodeTransfer(reqHelper.getUser(),reqHelper.getTransId(),reqHelper.getRemark());
	}
	
	@RequestMapping(value = "/code_transfer/getAgentByBranch", method = RequestMethod.POST)
	public List<Agent> getAgentDtos (@RequestParam Integer agentCode, @RequestParam String token, @RequestParam String branchCode) throws Exception{
		
		return receiptClient.getAgents(agentCode, token, branchCode);
	}
	
	@RequestMapping(value = "/code_transfer/getAgentsDetails", method = RequestMethod.POST)
	public Agent getAgentDetails(@RequestBody String agentCode) throws Exception{
		
		return receiptClient.getAgentDetails(agentCode);
	}
	
	@RequestMapping(value="/code_transfer/getPendingCodeTransfersPrp/{token:.+}", method = RequestMethod.GET)
	public List<CodeTransfer> getPendingCodeTransfersPrp(@PathVariable String token)throws Exception{
		return receiptClient.getPendingCodeTransferPrp(token);
	}
	
	@RequestMapping(value="/code_transfer/getPendingCodeTransfersPol/{token:.+}", method = RequestMethod.GET)
	public List<CodeTransfer> getPendingCodeTransfersPol(@PathVariable String token)throws Exception{
		return receiptClient.getPendingCodeTransfersPol(token);
	}
	
	@RequestMapping(value="/code_transfer/getCanceledCodeTransfersPrp/{token:.+}", method = RequestMethod.GET)
	public List<CodeTransfer> getCanceledCodeTransfersPrp(@PathVariable String token)throws Exception{
		return receiptClient.getCanceledCodeTransfersPrp(token);
	}
	
	@RequestMapping(value="/code_transfer/getCanceledCodeTransfersPol/{token:.+}", method = RequestMethod.GET)
	public List<CodeTransfer> getCanceledCodeTransfersPol(@PathVariable String token)throws Exception{
		return receiptClient.getCanceledCodeTransfersPol(token);
	}
	
	@RequestMapping(value="/code_transfer/getCodePendingProposalDetails/{token:.+}", method = RequestMethod.GET)
	public ResponseEntity<Object> getCodePendingProposalDetails(@PathVariable("token")String token)throws Exception{
		return receiptClient.getCodePendingProposalDetails(token);
	}
	
	@RequestMapping(value="/code_transfer/saveCodeTranPrp", method = RequestMethod.POST)
	public ResponseEntity<Object> saveCodeTranPrp(@RequestBody SaveCodeTransfer saveCodeTransferDto)throws Exception{
		
		//System.out.println(saveCodeTransferDto.toString());
		return receiptClient.saveCodeTransferPrp(saveCodeTransferDto);
	}
	
}
