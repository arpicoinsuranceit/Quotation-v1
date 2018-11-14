package org.arpicoinsurance.groupit.main.webclient.Impl;

import java.util.HashMap;

import org.arpicoinsurance.groupit.main.helper.CommisionRatePara;
import org.arpicoinsurance.groupit.main.util.AppConstant;
import org.arpicoinsurance.groupit.main.webclient.CommisionRateWC;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CommisionRateWCImpl implements CommisionRateWC {

	@Override
	public HashMap<String, Double> getCommisionRate(CommisionRatePara commisionRatePara) throws Exception {
		try {
			final String uri = AppConstant.URL_COMMISION_RATE;
			RestTemplate restTemplate = new RestTemplate();
			HashMap<String, Double> result = restTemplate.postForObject(uri, commisionRatePara, HashMap.class);

			return result;

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}
	}

}
