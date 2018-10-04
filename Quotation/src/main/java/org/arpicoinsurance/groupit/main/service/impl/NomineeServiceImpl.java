package org.arpicoinsurance.groupit.main.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import org.arpicoinsurance.groupit.main.dao.NomineeDao;
import org.arpicoinsurance.groupit.main.dao.QuotationDao;
import org.arpicoinsurance.groupit.main.dao.QuotationDetailsDao;
import org.arpicoinsurance.groupit.main.model.Nominee;
import org.arpicoinsurance.groupit.main.model.Quotation;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;
import org.arpicoinsurance.groupit.main.service.NomineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NomineeServiceImpl implements NomineeService {
	
	@Autowired
	private NomineeDao nomineeDao;
	
	@Autowired
	private QuotationDetailsDao quoDetailsDao;
	
	@Autowired
	private QuotationDao quotationDao;


	@Override
	public List<Nominee> findByQuotationDetails(Integer seqNo, Integer qId) throws Exception {
		Quotation quotation = quotationDao.findById(qId);
		if(quotation != null ) {
			QuotationDetails details = quoDetailsDao.findByQuotationAndSeqnum(quotation, seqNo);
			
			List<Nominee> nominee=nomineeDao.findByQuotationDetails(details);
			
			nominee.forEach(n ->{
				n.setNomineeDateofBirth(new SimpleDateFormat("dd-MM-yyyy").format(n.getNomineeDob()));
			});
			
			return nominee;
		}
		return null;
	}

	

}
