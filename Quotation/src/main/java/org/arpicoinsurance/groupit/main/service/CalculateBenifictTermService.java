package org.arpicoinsurance.groupit.main.service;


public interface CalculateBenifictTermService {
	
	Integer calculateBenifictTerm(Integer age, String riderCode, Integer term) throws Exception;
	
}
