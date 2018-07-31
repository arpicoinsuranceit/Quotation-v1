package org.arpicoinsurance.groupit.main.service;

import java.util.List;

import org.arpicoinsurance.groupit.main.model.Nominee;

public interface NomineeService {
	public List<Nominee> findByQuotationDetails(Integer qdId)throws Exception;
}
