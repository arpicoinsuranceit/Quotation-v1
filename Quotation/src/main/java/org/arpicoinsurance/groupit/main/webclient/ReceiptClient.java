package org.arpicoinsurance.groupit.main.webclient;

import java.util.List;

public interface ReceiptClient {

	List<String> getBranches(String userCode) throws Exception;
	
}
