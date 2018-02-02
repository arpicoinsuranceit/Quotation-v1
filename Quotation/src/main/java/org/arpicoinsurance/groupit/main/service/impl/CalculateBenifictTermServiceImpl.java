package org.arpicoinsurance.groupit.main.service.impl;

import org.arpicoinsurance.groupit.main.dao.BenefitsDao;
import org.arpicoinsurance.groupit.main.model.Benefits;
import org.arpicoinsurance.groupit.main.service.CalculateBenifictTermService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CalculateBenifictTermServiceImpl implements CalculateBenifictTermService{

	@Autowired
	private BenefitsDao beneficeDao;
	@Override
	public Integer calculateBenifictTerm(Integer age, String riderCode, Integer term) throws Exception {
		if(riderCode.equals("BSAS"))
			riderCode="SCB";
		if(riderCode.equals("CIBS"))
			riderCode="SCIB";
		System.out.println(age+"******************************************");
		Benefits benefits=beneficeDao.findByRiderCode(riderCode);
		if(age>=benefits.getBenefitMinAge() && age <= benefits.getBenefitMaxAge()) {
			Integer ageDiferance=benefits.getBenefitMaxAge()-age;
			if(ageDiferance >= term) {
				return term;
			}else {
				return ageDiferance;
			}
		}
		return -1;
	}

}
