package org.arpicoinsurance.groupit.main.dao.custom;

import java.util.List;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Details;


public interface Quo_Benef_DetailsDaoCustom{
	Quo_Benef_Details findOne(Integer id) throws Exception;
	
	Integer deleteOne(Integer id) throws Exception;

	List<Quo_Benef_Details> findByQuoDetailId(Integer id) throws Exception;
	
}
