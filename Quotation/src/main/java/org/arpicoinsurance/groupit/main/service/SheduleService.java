package org.arpicoinsurance.groupit.main.service;

import java.util.List;

import org.arpicoinsurance.groupit.main.model.Shedule;

public interface SheduleService {
	public List<Shedule> findByQuotationDetails(Integer qdId)throws Exception;
	
	public List<Shedule> findByQuotationDetails(Integer qId, Integer seqNo)throws Exception;
}
