package org.arpicoinsurance.groupit.main.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.arpicoinsurance.groupit.main.dao.custom.QuotationCustomDao;
import org.arpicoinsurance.groupit.main.helper.QuotationSearch;
import org.arpicoinsurance.groupit.main.helper.QuotationSearchProp;
import org.arpicoinsurance.groupit.main.security.JwtDecoder;
import org.arpicoinsurance.groupit.main.service.QuotationReceiptService;
import org.arpicoinsurance.groupit.main.webclient.ReceiptClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class QuotationReceiptServiceImpl implements QuotationReceiptService{

	@Autowired
	private QuotationCustomDao quotationCustomDao;
	
	@Autowired
	private JwtDecoder decoder;
	
	@Autowired
	private ReceiptClient client;
	
	@Override
	public List<QuotationSearch> searchQuotation(String id, String token) throws Exception {
		
	//	List<QuotationSearch> quotationSearchs = new ArrayList<>();
		
		String userCode = decoder.generate(token);
		
		System.out.println(userCode);
		
		System.out.println(id);
		
		List<String> list = client.getBranches(userCode);
		
		for (String string : list) {
			if (string.equals("HO")) {
				return quotationCustomDao.getQuotationForReceipt(id);
			}
		}
		
		String branches = getPara(list);
		
		System.out.println(branches);
		
		return quotationCustomDao.getQuotationForReceipt(id, branches);
	}

	@Override
	public List<QuotationSearchProp> searchQuotationProp(String id) throws Exception {
		return quotationCustomDao.getQuotationProp(id);
	}

	public String getPara(List<String> loccodes) {
		String locations="";
		if(loccodes != null) {
			for (String string : loccodes) {
				locations+="'"+string+"'"+",";
			}
		}
		
		locations=locations.replaceAll(",$", "");
		
		return locations;
	}
	

}
