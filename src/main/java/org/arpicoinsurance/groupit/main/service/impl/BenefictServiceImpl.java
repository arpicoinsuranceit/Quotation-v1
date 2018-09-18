package org.arpicoinsurance.groupit.main.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.arpicoinsurance.groupit.main.dao.BenefitsDao;
import org.arpicoinsurance.groupit.main.model.Benefits;
import org.arpicoinsurance.groupit.main.service.BeneficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BenefictServiceImpl implements BeneficeService{

	@Autowired 
	private BenefitsDao beneficeDao;
	
	@Override
	public List<String> getActiveBenefices() throws Exception {
		
		List <String> beneficeList = new ArrayList<>();
		
		List<Benefits> benefits = beneficeDao.findAllByActive(1);
		
		for (Benefits benefit : benefits) {
			beneficeList.add(benefit.getRiderCode());
		}
		return beneficeList;
	}

	
	
}
