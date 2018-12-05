package org.arpicoinsurance.groupit.main.webclient.Impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.arpicoinsurance.groupit.main.helper.BenefictHistory;
import org.arpicoinsurance.groupit.main.util.AppConstant;
import org.arpicoinsurance.groupit.main.webclient.BenefictHistoryWebClient;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class BenefictHistoryWebClientImpl implements BenefictHistoryWebClient {

	@Override
	public List<BenefictHistory> getHistory(String nic) throws Exception {
		try {
<<<<<<< HEAD
			RestTemplate restTemplate = new RestTemplate();

			//List<BenefictHistory> benefictHistories = restTemplate.postForObject("http://10.10.10.11:8087/getbeneficthistory", nic, ArrayList.class);

			List<BenefictHistory> benefictHistories = restTemplate.postForObject(AppConstant.URL_BENEFICT_HISTORY, nic, ArrayList.class);

			return benefictHistories;
=======
			
			RestTemplate restTemplate = new RestTemplate();
			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
			map.add("nic", nic); 
			BenefictHistory [] benefictHistories = restTemplate.postForObject(AppConstant.URL_BENEFICT_HISTORY, map, BenefictHistory[].class);
			 
			List<BenefictHistory> histories = Arrays.asList(benefictHistories);
			
			return histories;
			
>>>>>>> refs/remotes/origin/branch-139
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}

	}

}
