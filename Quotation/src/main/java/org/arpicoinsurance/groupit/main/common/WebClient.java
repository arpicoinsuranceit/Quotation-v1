package org.arpicoinsurance.groupit.main.common;

import org.arpicoinsurance.groupit.main.helper.InvpSavePersonalInfo;
import org.arpicoinsurance.groupit.main.util.AppConstant;
import org.springframework.web.client.RestTemplate;

public class WebClient {

	public String getCustCode(InvpSavePersonalInfo _invpSaveQuotation) {
		

			//System.out.println("call web try");
			//final String uri = "http://10.10.10.12:8080/Infosys/testABC";
			//final String uri = "http://10.10.10.11:8087/testABC";
			//final String uri = "http://localhost:8085/testABC";

		try {
			final String uri = AppConstant.URL_GET_CUST_CODE;


			RestTemplate restTemplate = new RestTemplate();
			String result = restTemplate.postForObject(uri, _invpSaveQuotation, String.class);
			 
			return result;

		}catch (Exception e) {
			e.printStackTrace();
			return null;

		}
	}
	
}
