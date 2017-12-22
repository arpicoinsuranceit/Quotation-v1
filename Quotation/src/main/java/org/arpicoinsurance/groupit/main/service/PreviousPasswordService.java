package org.arpicoinsurance.groupit.main.service;


import java.util.List;

import org.arpicoinsurance.groupit.main.model.Login;
import org.arpicoinsurance.groupit.main.model.PreviousPassword;


public interface PreviousPasswordService {

	boolean savePassword(PreviousPassword password,Login login) throws Exception;
	
	boolean updatePassword(PreviousPassword password) throws Exception;
	
	boolean deletePassword(Integer id) throws Exception;
	
	PreviousPassword getPassword(Integer id) throws Exception;
	
	List<PreviousPassword> getLatestPassword(Integer id)throws Exception;
	
}
