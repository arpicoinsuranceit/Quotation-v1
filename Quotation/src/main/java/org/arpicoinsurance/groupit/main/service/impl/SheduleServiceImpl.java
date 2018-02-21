package org.arpicoinsurance.groupit.main.service.impl;

import java.util.List;
import org.arpicoinsurance.groupit.main.dao.QuotationDetailsDao;
import org.arpicoinsurance.groupit.main.dao.SheduleDao;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;
import org.arpicoinsurance.groupit.main.model.Shedule;
import org.arpicoinsurance.groupit.main.service.SheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SheduleServiceImpl implements SheduleService {
	
	@Autowired
	private SheduleDao sheduleDao;
	
	@Autowired
	private QuotationDetailsDao quoDetailsDao;

	@Override
	public List<Shedule> findByQuotationDetails(Integer qdId) throws Exception {
		QuotationDetails details=quoDetailsDao.findByQdId(qdId);
		
		return sheduleDao.findByQuotationDetails(details);
	}

	

}
