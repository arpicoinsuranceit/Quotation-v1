package org.arpicoinsurance.groupit.main.service.impl;

import org.arpicoinsurance.groupit.main.dao.BenefitsDao;
import org.arpicoinsurance.groupit.main.model.Benefits;
import org.arpicoinsurance.groupit.main.service.CalculateBenifictTermService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CalculateBenifictTermServiceImpl implements CalculateBenifictTermService {

	@Autowired
	private BenefitsDao beneficeDao;

	@Override
	public Integer calculateBenifictTerm(Integer age, String riderCode, Integer term) throws Exception {
		if (riderCode.equals("BSAS"))
			riderCode = "SCB";
		if (riderCode.equals("CIBS"))
			riderCode = "SCIB";
		if (riderCode.equals("HRBF"))
			riderCode = "HCBF";
		if (riderCode.equals("HRBFS"))
			riderCode = "HCBFS";
		if (riderCode.equals("HRBFC"))
			riderCode = "HCBFC";
		if (riderCode.equals("HRBI"))
			riderCode = "HCBI";
		if (riderCode.equals("HRBIS"))
			riderCode = "HCBIS";
		if (riderCode.equals("HRBIC"))
			riderCode = "HCBIC";
		if (riderCode.equals("SUHRB"))
			riderCode = "SHCBI";
		if (riderCode.equals("SUHRBC"))
			riderCode = "SHCBIC";
		if (riderCode.equals("SUHRBS"))
			riderCode = "SHCBIS";
		if (riderCode.equals("CIBS"))
			riderCode = "SCIB";
//		System.out.println(age + "******************************************");
		Benefits benefits = beneficeDao.findByRiderCode(riderCode);
//		System.out.println(riderCode+ "******************************************");
		if (age >= benefits.getBenefitMinAge() && age <= benefits.getBenefitMaxAge()) {
			Integer ageDiferance = benefits.getBenefitMaxAge() - age;
			if (ageDiferance >= term) {
				return term;
			} else {
				return ageDiferance;
			}
		}
		return -1;
	}

	@Override
	public Integer calculateChildBenifictTermARP(Integer age, String riderCode, Integer term, String payingTerm)
			throws Exception {

		int payTerm = 0;

		if (!payingTerm.equals("S")) {
			try {
				payTerm = Integer.parseInt(payingTerm);
			} catch (Exception e) {
				return -1;
			}
		}
		
		switch (riderCode) {
		case "HRBIC":
			riderCode="HCBIC";
			break;
		case "HRBFC":
			riderCode="HCBFC";
			break;
		case "SUHRBC":
			riderCode="SHCBIC";
			break;

		default:
			break;
		}
		

		Benefits benefits = beneficeDao.findByRiderCode(riderCode);
		
//		System.out.println(benefits.getBenefitMaxAge());
//		System.out.println(age);
//		System.out.println(payTerm);
		// if (age >= benefits.getBenefitMinAge() && age <= benefits.getBenefitMaxAge())
		// {
		if (((benefits.getBenefitMaxAge() - age) - payTerm) >= 5) {
			
			return benefits.getBenefitMaxAge() - age;
		}
		return -1;
	}

	/*
	 * public Integer calculateBenifictTermARP(Integer age, String riderCode,
	 * Integer term, String payingTerm) throws Exception { if
	 * (riderCode.equals("BSAS")) riderCode = "SCB"; if (riderCode.equals("CIBS"))
	 * riderCode = "SCIB"; System.out.println(age +
	 * "******************************************");
	 * 
	 * int payTerm = 0;
	 * 
	 * if (!payingTerm.equals("S")) { try { payTerm = Integer.parseInt(payingTerm);
	 * } catch (Exception e) { return -1; } }
	 * 
	 * Benefits benefits = beneficeDao.findByRiderCode(riderCode); if (age >=
	 * benefits.getBenefitMinAge() && age <= benefits.getBenefitMaxAge()) { Integer
	 * ageDiferance = benefits.getBenefitMaxAge() - (age+payTerm); if (ageDiferance
	 * >= term) { return term; } else { return ageDiferance; } } return -1; }
	 */

}
