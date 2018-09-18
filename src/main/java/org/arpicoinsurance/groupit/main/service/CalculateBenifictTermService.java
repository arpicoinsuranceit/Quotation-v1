package org.arpicoinsurance.groupit.main.service;


public interface CalculateBenifictTermService {
	
	Integer calculateBenifictTerm(Integer age, String riderCode, Integer term) throws Exception;
	
	Integer calculateChildBenifictTermARP(Integer age, String riderCode, Integer term, String payingTerm) throws Exception;
	
	
	
}
