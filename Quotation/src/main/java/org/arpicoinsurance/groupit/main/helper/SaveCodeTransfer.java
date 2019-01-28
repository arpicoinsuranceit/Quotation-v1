package org.arpicoinsurance.groupit.main.helper;

import java.util.ArrayList;

public class SaveCodeTransfer {
	
	private String token;
	private String agent;
	private String reason;
	private ArrayList<CodeTransferHelperDto> codeTransferHelpers;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public ArrayList<CodeTransferHelperDto> getCodeTransferHelpers() {
		return codeTransferHelpers;
	}
	public void setCodeTransferHelpers(ArrayList<CodeTransferHelperDto> codeTransferHelpers) {
		this.codeTransferHelpers = codeTransferHelpers;
	}
	@Override
	public String toString() {
		return "SaveCodeTransferDto [token=" + token + ", agent=" + agent + ", reason=" + reason
				+ ", codeTransferHelpers=" + codeTransferHelpers + "]";
	}
	
	

}
