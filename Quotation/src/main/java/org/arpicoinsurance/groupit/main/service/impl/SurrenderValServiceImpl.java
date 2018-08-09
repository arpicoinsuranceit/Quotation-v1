package org.arpicoinsurance.groupit.main.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.arpicoinsurance.groupit.main.dao.QuotationDao;
import org.arpicoinsurance.groupit.main.dao.QuotationDetailsDao;
import org.arpicoinsurance.groupit.main.dao.SurrendervalDao;
import org.arpicoinsurance.groupit.main.model.Quotation;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;
import org.arpicoinsurance.groupit.main.model.Surrendervals;
import org.arpicoinsurance.groupit.main.service.SurrenderValService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class SurrenderValServiceImpl implements SurrenderValService {

	@Autowired
	private SurrendervalDao surrenderValsDao;

	@Autowired
	private QuotationDao quotationDao;
	
	@Autowired
	private QuotationDetailsDao quotationDetailsDao;

	@Override
	public List<Surrendervals> getSurrenderValBuQuotationDetails(Integer qId, Integer seqNo) throws Exception {
		Quotation quotation = quotationDao.findById(qId);
		
		QuotationDetails quotationDetails = quotationDetailsDao.findByQuotationAndSeqnum(quotation, seqNo);

		return surrenderValsDao.findByQuotationDetails(quotationDetails);
	}

}
