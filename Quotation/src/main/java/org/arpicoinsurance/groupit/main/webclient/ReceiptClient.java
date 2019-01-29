package org.arpicoinsurance.groupit.main.webclient;

import java.util.List;

import org.arpicoinsurance.groupit.main.helper.Agent;
import org.arpicoinsurance.groupit.main.helper.CodeTransfer;
import org.arpicoinsurance.groupit.main.helper.CodeTransferHelper;
import org.arpicoinsurance.groupit.main.helper.SaveCodeTransfer;
import org.springframework.http.ResponseEntity;

public interface ReceiptClient {

	List<String> getBranches(String userCode) throws Exception;
	
	List<CodeTransferHelper> getCodeTransferToApprove(String userCode,String dashPara,String userType) throws Exception;
	
	ResponseEntity<Object> approveCodeTransfer(String user,String codeTransferId,String remark) throws Exception;
	
	ResponseEntity<Object> rejectCodeTransfer(String user,String codeTransferId,String remark) throws Exception;
	
	List<Agent> getAgents (Integer agentCode, String token, String branchCode) throws Exception;
	
	Agent getAgentDetails(String agentCode) throws Exception;

	List<CodeTransfer> getPendingCodeTransferPrp(String token)throws Exception;

	List<CodeTransfer> getPendingCodeTransfersPol(String token)throws Exception;

	List<CodeTransfer> getCanceledCodeTransfersPrp(String token)throws Exception;

	List<CodeTransfer> getCanceledCodeTransfersPol(String token)throws Exception;

	ResponseEntity<Object> getCodePendingProposalDetails(String token)throws Exception;

	ResponseEntity<Object> saveCodeTransferPrp(SaveCodeTransfer saveCodeTransferDto)throws Exception;
	
}
