package org.arpicoinsurance.groupit.main.service.impl;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.arpicoinsurance.groupit.main.dao.Quo_Benef_DetailsDao;
import org.arpicoinsurance.groupit.main.dao.QuotationDetailsDao;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Details;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;
import org.arpicoinsurance.groupit.main.service.Quo_Benef_DetailsService;
import org.arpicoinsurance.groupit.main.service.QuotationDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class QuotationDetailsServiceImpl implements QuotationDetailsService{
	
	@Autowired
	private QuotationDetailsDao quotationDetailsDao;
	
	@Autowired
	private Quo_Benef_DetailsService quo_Benef_DetailsService;

	@Override
	public QuotationDetails findQuotationDetails(Integer qdId) throws Exception {
		return quotationDetailsDao.findByQdId(qdId);
	}

	@Override
	public QuotationDetails editQuotationDetails(Integer qdId) throws Exception {
		QuotationDetails details=findQuotationDetails(qdId);
		ArrayList<Quo_Benef_Details> benef=(ArrayList<Quo_Benef_Details>) quo_Benef_DetailsService.findByQuotationDetails(details);
		
		for (Quo_Benef_Details quo_Benef_Details : benef) {
			System.out.println(quo_Benef_Details.getBenefit().getBenefitName());
		}
		
		return null;
	}
	
	

}
