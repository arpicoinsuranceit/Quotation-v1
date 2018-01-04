package org.arpicoinsurance.groupit.main.dao;

import java.util.ArrayList;
import org.arpicoinsurance.groupit.main.model.Quotation;

public interface QuotationDao{

	Quotation findOne(Integer id) throws Exception;
	
	Integer deleteOne(Integer id) throws Exception;
	
	ArrayList<Quotation> findByUserId(Integer id)throws Exception;
}
