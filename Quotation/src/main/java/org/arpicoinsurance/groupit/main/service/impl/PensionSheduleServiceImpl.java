package org.arpicoinsurance.groupit.main.service.impl;

import java.util.List;

import org.arpicoinsurance.groupit.main.dao.PensionSheduleDao;
import org.arpicoinsurance.groupit.main.dao.QuotationDetailsDao;
import org.arpicoinsurance.groupit.main.model.PensionShedule;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;
import org.arpicoinsurance.groupit.main.service.PensionSheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PensionSheduleServiceImpl implements PensionSheduleService {
	
	@Autowired
	private PensionSheduleDao pensionSheduleDao;
	
	@Autowired
	private QuotationDetailsDao quoDetailsDao;

	@Override
	public List<PensionShedule> findByQuotationDetails(Integer qdId) throws Exception {
		QuotationDetails details=quoDetailsDao.findByQdId(qdId);
		
		return pensionSheduleDao.findByQuotationDetails(details);
	}

	

}
