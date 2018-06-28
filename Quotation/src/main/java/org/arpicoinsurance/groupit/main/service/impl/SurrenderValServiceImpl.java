package org.arpicoinsurance.groupit.main.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.arpicoinsurance.groupit.main.dao.QuotationDetailsDao;
import org.arpicoinsurance.groupit.main.dao.SurrendervalDao;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;
import org.arpicoinsurance.groupit.main.model.Surrendervals;
import org.arpicoinsurance.groupit.main.service.SurrenderValService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class SurrenderValServiceImpl implements SurrenderValService{

	@Autowired
	private SurrendervalDao surrenderValsDao;
	
	@Autowired
	private QuotationDetailsDao quotationDetailsDao;
	
	@Override
	public List<Surrendervals> getSurrenderValBuQuotationDetails(Integer qdId) throws Exception {
		QuotationDetails quotationDetails = quotationDetailsDao.findByQdId(qdId);
		
		return surrenderValsDao.findByQuotationDetails(quotationDetails);
	}

}
