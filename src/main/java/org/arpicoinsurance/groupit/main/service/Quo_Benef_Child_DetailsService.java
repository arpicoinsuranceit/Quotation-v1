package org.arpicoinsurance.groupit.main.service;

import java.util.List;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Child_Details;


public interface Quo_Benef_Child_DetailsService {

	boolean saveQuo_Benef_Child_Details(Quo_Benef_Child_Details childbenf) throws Exception;
	
	boolean updateQuo_Benef_Child_Details(Quo_Benef_Child_Details childbenf) throws Exception;
	
	boolean deleteQuo_Benef_Child_Details(Integer id) throws Exception;
	
	Quo_Benef_Child_Details getQuo_Benef_Child_Details(Integer id) throws Exception;
	
	List <Quo_Benef_Child_Details> getAllQuo_Benef_Child_Details() throws Exception;
	
	List <Quo_Benef_Child_Details> getQuo_Benef_Child_DetailsByQuo_Benf_DetailsId(Integer id) throws Exception;
	
}
