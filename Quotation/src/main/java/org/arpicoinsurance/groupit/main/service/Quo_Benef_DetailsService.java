package org.arpicoinsurance.groupit.main.service;

import java.util.List;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Details;

public interface Quo_Benef_DetailsService {
	boolean saveQuo_Benef_Details(Quo_Benef_Details qbd) throws Exception;
	
	boolean updateQuo_Benef_Details(Quo_Benef_Details qbd) throws Exception;
	
	boolean deleteQuo_Benef_Details(Integer id) throws Exception;
	
	Quo_Benef_Details getQuo_Benef_Details(Integer id) throws Exception;
	
	List <Quo_Benef_Details> getAllQuo_Benef_Details() throws Exception;
	
	List <Quo_Benef_Details> getQuo_Benef_DetailsByQid(Integer id) throws Exception;
}
