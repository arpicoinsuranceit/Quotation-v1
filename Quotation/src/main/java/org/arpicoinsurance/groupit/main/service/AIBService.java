package org.arpicoinsurance.groupit.main.service;

import java.math.BigDecimal;
import java.util.Date;

import org.arpicoinsurance.groupit.main.helper.InvpSavePersonalInfo;
import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;

public interface AIBService {
	
	BigDecimal calculateAIBMaturaty(Integer term, Double adbrat, Double fundmarat, Double fundrat, Double contribution, Date chedat, String paymod)throws Exception;

	String saveQuotation(InvpSavePersonalInfo _invpSaveQuotation, Integer id) throws Exception;
	
	String editQuotation(InvpSavePersonalInfo _invpSaveQuotation,Integer userId,Integer qdId) throws Exception;
}
