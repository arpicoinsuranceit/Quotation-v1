package org.arpicoinsurance.groupit.main.webclient.Impl;

import java.util.HashMap;

import org.arpicoinsurance.groupit.main.helper.CommisionRatePara;
import org.arpicoinsurance.groupit.main.webclient.CommisionRateWC;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CommisionRateWCImpl implements CommisionRateWC {

	@Override
	public HashMap<String, Double> getCommisionRate(CommisionRatePara commisionRatePara) throws Exception {
		try {
//			//System.out.println(commisionRatePara.getPrdcod() + " " + commisionRatePara.getComyer() + " "
//					+ commisionRatePara.getToterm());
			final String uri = "http://10.10.10.12:8080/Infosys/commisionRate";
			//final String uri = "http://localhost:8085/commisionRate";
			RestTemplate restTemplate = new RestTemplate();
			HashMap<String, Double> result = restTemplate.postForObject(uri, commisionRatePara, HashMap.class);

			return result;

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}
	}

}
