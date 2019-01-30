package org.arpicoinsurance.groupit.main.webclient.Impl;

import java.util.ArrayList;
import java.util.List;

import org.arpicoinsurance.groupit.main.helper.Agent;
import org.arpicoinsurance.groupit.main.helper.CodeTransfer;
import org.arpicoinsurance.groupit.main.helper.CodeTransferHelper;
import org.arpicoinsurance.groupit.main.helper.CodeTransferHelperDto;
import org.arpicoinsurance.groupit.main.helper.ResponseHelper;
import org.arpicoinsurance.groupit.main.helper.SaveCodeTransfer;
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
	public List<CodeTransferHelper> getCodeTransferToApprove(String userCode,String dashPara,String userType) throws Exception {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("userCode", userCode);
		map.add("dashPara", dashPara);
		map.add("userType", userType);

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

	@Override
	public List<Agent> getAgents(Integer agentCode, String token, String branchCode) throws Exception {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("agentCode", Integer.toString(agentCode));
		map.add("token", token);
		map.add("branchCode", branchCode);

		try {
			RestTemplate restTemplate = new RestTemplate();
			Agent[] result = restTemplate.postForObject(AppConstant.URL_GET_AGENTS, map, Agent[].class);

			List<Agent> codes = new ArrayList<>();
			for (Agent code : result) {
				codes.add(code);
			}

			return codes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public List<Agent> getAgentsByRegion(Integer agentCode, String token, String branchCode) throws Exception {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("agentCode", Integer.toString(agentCode));
		map.add("token", token);
		map.add("branchCode", branchCode);

		try {
			RestTemplate restTemplate = new RestTemplate();
			Agent[] result = restTemplate.postForObject(AppConstant.URL_GET_AGENTS_BY_REGION, map, Agent[].class);

			List<Agent> codes = new ArrayList<>();
			for (Agent code : result) {
				codes.add(code);
			}

			return codes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Agent getAgentDetails(String agentCode) throws Exception {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("agentCode", agentCode);

		try {
			RestTemplate restTemplate = new RestTemplate();
			Agent result = restTemplate.postForObject(AppConstant.URL_GET_AGENT_DETAILS, agentCode, Agent.class);

			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public List<CodeTransfer> getPendingCodeTransferPrp(String token) throws Exception {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("token", token);

		try {
			RestTemplate restTemplate = new RestTemplate();
			CodeTransfer[] result = restTemplate.postForObject(AppConstant.URL_GET_PENDING_PRP, map, CodeTransfer[].class);

			List<CodeTransfer> codes = new ArrayList<>();
			for (CodeTransfer code : result) {
				codes.add(code);
			}

			return codes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public List<CodeTransfer> getPendingCodeTransfersPol(String token) throws Exception {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("token", token);

		try {
			RestTemplate restTemplate = new RestTemplate();
			CodeTransfer[] result = restTemplate.postForObject(AppConstant.URL_GET_PENDING_POL, map, CodeTransfer[].class);

			List<CodeTransfer> codes = new ArrayList<>();
			for (CodeTransfer code : result) {
				codes.add(code);
			}

			return codes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public List<CodeTransfer> getCanceledCodeTransfersPrp(String token) throws Exception {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("token", token);

		try {
			RestTemplate restTemplate = new RestTemplate();
			CodeTransfer[] result = restTemplate.postForObject(AppConstant.URL_GET_CANCELED_PRP, map, CodeTransfer[].class);

			List<CodeTransfer> codes = new ArrayList<>();
			for (CodeTransfer code : result) {
				codes.add(code);
			}

			return codes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public List<CodeTransfer> getCanceledCodeTransfersPol(String token) throws Exception {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("token", token);

		try {
			RestTemplate restTemplate = new RestTemplate();
			CodeTransfer[] result = restTemplate.postForObject(AppConstant.URL_GET_CANCELED_POL, map, CodeTransfer[].class);

			List<CodeTransfer> codes = new ArrayList<>();
			for (CodeTransfer code : result) {
				codes.add(code);
			}

			return codes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public ResponseEntity<Object> getCodePendingProposalDetails(String token,String dashPara,String userType) throws Exception {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("token", token);
		map.add("dashPara", dashPara);
		map.add("userType", userType);

		try {
			RestTemplate restTemplate = new RestTemplate();
			CodeTransferHelperDto[] result = restTemplate.postForObject(AppConstant.URL_GET_CODE_PENDING_PRP, map, CodeTransferHelperDto[].class);

			List<CodeTransferHelperDto> codes = new ArrayList<>();
			for (CodeTransferHelperDto code : result) {
				codes.add(code);
			}

			return new ResponseEntity<>(codes,HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public ResponseEntity<Object> saveCodeTransferPrp(SaveCodeTransfer saveCodeTransferDto) throws Exception {

		try {
			RestTemplate restTemplate = new RestTemplate();
			ResponseHelper result = restTemplate.postForObject(AppConstant.URL_GET_CODE_SAVE_PRP, saveCodeTransferDto, ResponseHelper.class);

			return new ResponseEntity<>(result,HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
