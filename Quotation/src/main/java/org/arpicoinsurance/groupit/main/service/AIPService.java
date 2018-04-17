package org.arpicoinsurance.groupit.main.service;

import java.util.Date;
import java.util.HashMap;

import org.arpicoinsurance.groupit.main.helper.AIPCalResp;
import org.arpicoinsurance.groupit.main.helper.InvpSavePersonalInfo;

public interface AIPService {
	
	AIPCalResp calculateAIPMaturaty(Integer term, Double adbrat, Double fundmarat, Double intrat, Double contribution, Date chedat, String paymod, boolean schedule)throws Exception;
	
	HashMap<String, Object> saveQuotation(InvpSavePersonalInfo _invpSaveQuotation, Integer id) throws Exception;

	HashMap<String, Object> editQuotation(InvpSavePersonalInfo _invpSaveQuotation, Integer userId, Integer qdId) throws Exception;
}

