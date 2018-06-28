package org.arpicoinsurance.groupit.main.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.arpicoinsurance.groupit.main.dao.custom.QuotationCustomDao;
import org.arpicoinsurance.groupit.main.helper.QuotationSearch;
import org.arpicoinsurance.groupit.main.service.QuotationReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class QuotationReceiptServiceImpl implements QuotationReceiptService{

	@Autowired
	private QuotationCustomDao quotationCustomDao;
	
	@Override
	public List<QuotationSearch> searchQuotation(String id) throws Exception {
		
	//	List<QuotationSearch> quotationSearchs = new ArrayList<>();
		
		System.out.println(id);
		
		return quotationCustomDao.getQuotation(id);
	}

	

}
