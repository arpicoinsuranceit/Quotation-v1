package org.arpicoinsurance.groupit.main.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import org.arpicoinsurance.groupit.main.dao.NomineeDao;
import org.arpicoinsurance.groupit.main.dao.QuotationDetailsDao;
import org.arpicoinsurance.groupit.main.model.Nominee;
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

	@Override
	public List<Nominee> findByQuotationDetails(Integer qdId) throws Exception {
		QuotationDetails details=quoDetailsDao.findByQdId(qdId);
		
		System.out.println("Called nominee service");
		
		System.out.println(details);
		
		List<Nominee> nominee=nomineeDao.findByQuotationDetails(details);
		
		nominee.forEach(n ->{
			n.setNomineeDateofBirth(new SimpleDateFormat("dd-MM-yyyy").format(n.getNomineeDob()));
		});
		
		return nominee;
	}

	

}
