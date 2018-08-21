package org.arpicoinsurance.groupit.main.service;

import java.util.List;

import org.arpicoinsurance.groupit.main.model.PensionShedule;

public interface PensionSheduleService {
	public List<PensionShedule> findByQuotationDetails(Integer seqNo, Integer qId)throws Exception;
}
