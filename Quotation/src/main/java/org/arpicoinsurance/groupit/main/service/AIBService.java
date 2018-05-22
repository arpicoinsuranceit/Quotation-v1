package org.arpicoinsurance.groupit.main.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import org.arpicoinsurance.groupit.main.helper.InvpSavePersonalInfo;

public interface AIBService {
	
	BigDecimal calculateAIBMaturaty(Integer term, Double adbrat, Double fundmarat, Double fundrat, Double contribution, Date chedat, String paymod)throws Exception;

	HashMap<String, Object> saveQuotation(InvpSavePersonalInfo _invpSaveQuotation, Integer id) throws Exception;
	
	HashMap<String, Object> editQuotation(InvpSavePersonalInfo _invpSaveQuotation,Integer userId,Integer qdId) throws Exception;
}
