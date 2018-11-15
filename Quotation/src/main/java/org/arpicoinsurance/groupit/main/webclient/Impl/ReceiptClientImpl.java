package org.arpicoinsurance.groupit.main.webclient.Impl;

import java.util.ArrayList;
import java.util.List;

import org.arpicoinsurance.groupit.main.webclient.ReceiptClient;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class ReceiptClientImpl implements ReceiptClient{

	@Override
	public List<String> getBranches(String userCode) throws Exception {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("userCode", userCode);

		try {
			RestTemplate restTemplate = new RestTemplate();
			String[] result = restTemplate.postForObject("http://10.10.10.11:8089/getBranches", map, String[].class);

			List<String> braches = new ArrayList<>();
			for (String branch : result) {
				braches.add(branch);
			}

			return braches;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
