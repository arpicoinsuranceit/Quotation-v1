package org.arpicoinsurance.groupit.main.service;

import java.util.HashMap;

import org.arpicoinsurance.groupit.main.helper.AIPCalResp;
import org.arpicoinsurance.groupit.main.helper.InvpSavePersonalInfo;
import org.arpicoinsurance.groupit.main.helper.Plan;

public interface ARTMService {
	
	AIPCalResp calculateARTMMaturaty(Plan plan, Double intrat, boolean shedule)throws Exception;
	
	HashMap<String, Object> saveQuotation(InvpSavePersonalInfo _invpSaveQuotation, Integer id) throws Exception;

	HashMap<String, Object> editQuotation(InvpSavePersonalInfo _invpSaveQuotation, Integer userId, Integer qdId) throws Exception;
}

