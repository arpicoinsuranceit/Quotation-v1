package org.arpicoinsurance.groupit.main.webclient.Impl;

import java.util.ArrayList;
import java.util.List;

import org.arpicoinsurance.groupit.main.helper.BenefictHistory;
import org.arpicoinsurance.groupit.main.webclient.BenefictHistoryWebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BenefictHistoryWebClientImpl implements BenefictHistoryWebClient {

	@Override
	public List<BenefictHistory> getHistory(String nic) throws Exception {
		try {
			RestTemplate restTemplate = new RestTemplate();
			
			List<BenefictHistory> benefictHistories = restTemplate.postForObject("http://localhost:8085/getbeneficthistory", nic, ArrayList.class);
			return benefictHistories;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}

	}

}
