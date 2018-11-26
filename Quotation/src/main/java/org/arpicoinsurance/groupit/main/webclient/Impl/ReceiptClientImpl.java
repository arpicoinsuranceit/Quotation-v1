package org.arpicoinsurance.groupit.main.webclient.Impl;

import java.util.ArrayList;
import java.util.List;

import org.arpicoinsurance.groupit.main.helper.CodeTransferHelper;
import org.arpicoinsurance.groupit.main.helper.ResponseHelper;
import org.arpicoinsurance.groupit.main.util.AppConstant;
import org.arpicoinsurance.groupit.main.webclient.ReceiptClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
			//String[] result = restTemplate.postForObject("http://10.10.10.11:8089/getBranches", map, String[].class);

			String[] result = restTemplate.postForObject(AppConstant.URL_GET_BRANCH, map, String[].class);


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

	@Override
	public List<CodeTransferHelper> getCodeTransferToApprove(String userCode) throws Exception {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("userCode", userCode);

		try {
			RestTemplate restTemplate = new RestTemplate();
			CodeTransferHelper[] result = restTemplate.postForObject(AppConstant.URL_GET_CODE_TRANSFERS, map, CodeTransferHelper[].class);

			List<CodeTransferHelper> codes = new ArrayList<>();
			for (CodeTransferHelper code : result) {
				codes.add(code);
			}

			return codes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ResponseEntity<Object> approveCodeTransfer(String user, String codeTransferId, String remark)
			throws Exception {
		
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("user", user);
		map.add("codeTransferId", codeTransferId);
		map.add("remark", remark);

		try {
			RestTemplate restTemplate = new RestTemplate();
			ResponseHelper result = restTemplate.postForObject(AppConstant.URL_GET_CODE_TRANSFER_APPROVE, map, ResponseHelper.class);

			return new ResponseEntity<>(result,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ResponseEntity<Object> rejectCodeTransfer(String user, String codeTransferId, String remark)
			throws Exception {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("user", user);
		map.add("codeTransferId", codeTransferId);
		map.add("remark", remark);

		try {
			RestTemplate restTemplate = new RestTemplate();
			ResponseHelper result = restTemplate.postForObject(AppConstant.URL_GET_CODE_TRANSFER_REJECT, map, ResponseHelper.class);

			return new ResponseEntity<>(result,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
