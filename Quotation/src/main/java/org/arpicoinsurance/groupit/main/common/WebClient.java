package org.arpicoinsurance.groupit.main.common;

import org.arpicoinsurance.groupit.main.helper.InvpSavePersonalInfo;
import org.springframework.web.client.RestTemplate;

public class WebClient {

	public String getCustCode(InvpSavePersonalInfo _invpSaveQuotation) {
		////System.out.println("call web client");
		
		try {
			////System.out.println("call web try");
			final String uri = "http://10.10.10.12:8080/Infosys/testABC";
			//final String uri = "http://localhost:8085/testABC";
			RestTemplate restTemplate = new RestTemplate();
			String result = restTemplate.postForObject(uri, _invpSaveQuotation, String.class);
			 
			return result;

		}catch (Exception e) {
			e.printStackTrace();
			return null;

		}
	}
	
}
