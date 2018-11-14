package org.arpicoinsurance.groupit.main.webclient.Impl;

import org.arpicoinsurance.groupit.main.util.AppConstant;
import org.arpicoinsurance.groupit.main.webclient.ArtmLstYearRate;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
class ArtmLstYearRateImpl implements ArtmLstYearRate {

	@Override
	public Double getLstYearRate(String quoCrtDate) throws Exception {

		try {

			final String uri = AppConstant.URL_LAST_YEAR_RATE;
			RestTemplate restTemplate = new RestTemplate();

			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
			map.add("quoCreDate", quoCrtDate);

			Double result = restTemplate.postForObject(uri, map, Double.class);
			return result;

		} catch (Exception e) {
			e.printStackTrace();

			return null;

		}
	}

}
