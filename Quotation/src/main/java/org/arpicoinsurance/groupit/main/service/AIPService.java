package org.arpicoinsurance.groupit.main.service;

import java.util.Date;

import org.arpicoinsurance.groupit.main.helper.AIPCalResp;
import org.arpicoinsurance.groupit.main.helper.InvpSavePersonalInfo;

public interface AIPService {
	
	AIPCalResp calculateAIPMaturaty(Integer term, Double adbrat, Double fundmarat, Double intrat, Double contribution, Date chedat, String paymod, boolean schedule)throws Exception;
	
	String saveQuotation(InvpSavePersonalInfo _invpSaveQuotation, Integer id) throws Exception;
}

