package org.arpicoinsurance.groupit.main.common;

import org.arpicoinsurance.groupit.main.helper.InvpSavePersonalInfo;
import org.arpicoinsurance.groupit.main.util.AppConstant;
import org.springframework.web.client.RestTemplate;

public class WebClient {

	public String getCustCode(InvpSavePersonalInfo _invpSaveQuotation) {
		
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
