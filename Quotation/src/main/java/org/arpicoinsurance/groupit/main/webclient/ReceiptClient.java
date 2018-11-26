package org.arpicoinsurance.groupit.main.webclient;

import java.util.List;

import org.arpicoinsurance.groupit.main.helper.CodeTransferHelper;
import org.springframework.http.ResponseEntity;

public interface ReceiptClient {

	List<String> getBranches(String userCode) throws Exception;
	
	List<CodeTransferHelper> getCodeTransferToApprove(String userCode) throws Exception;
	
	ResponseEntity<Object> approveCodeTransfer(String user,String codeTransferId,String remark) throws Exception;
	
	ResponseEntity<Object> rejectCodeTransfer(String user,String codeTransferId,String remark) throws Exception;
	
}
