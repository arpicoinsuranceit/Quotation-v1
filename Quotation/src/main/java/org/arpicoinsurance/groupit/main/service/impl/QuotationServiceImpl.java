package org.arpicoinsurance.groupit.main.service.impl;

import java.util.List;
import javax.transaction.Transactional;

import org.arpicoinsurance.groupit.main.dao.custom.QuotationDaoCustom;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Details;
import org.arpicoinsurance.groupit.main.model.Quotation;
import org.arpicoinsurance.groupit.main.service.QuotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class QuotationServiceImpl implements QuotationService{
	
	@Autowired
	private QuotationDaoCustom quotationDaoCustom;

	@Override
	public boolean saveQuotation(Quo_Benef_Details qbd) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateQuotation(Quo_Benef_Details qbd) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteQuotation(Integer id) throws Exception {
		if(quotationDaoCustom.deleteOne(id) != null ) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public Quotation getQuotation(Integer id) throws Exception {
		return quotationDaoCustom.findOne(id);
	}

	@Override
	public List<Quotation> getAllQuotation() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Quotation> getQuotationByUserId(Integer id) throws Exception {
		return quotationDaoCustom.findByUserId(id);
	}

}
