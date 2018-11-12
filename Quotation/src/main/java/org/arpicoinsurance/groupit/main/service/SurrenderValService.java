package org.arpicoinsurance.groupit.main.service;

import java.util.List;

import org.arpicoinsurance.groupit.main.model.Surrendervals;

public interface SurrenderValService {

	List<Surrendervals> getSurrenderValBuQuotationDetails(Integer qId, Integer seqNo) throws Exception;
	
}
