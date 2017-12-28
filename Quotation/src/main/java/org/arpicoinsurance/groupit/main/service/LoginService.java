package org.arpicoinsurance.groupit.main.service;

import java.util.List;

import org.arpicoinsurance.groupit.main.model.Login;


public interface LoginService {

	boolean saveLogin(Login login) throws Exception;
	
	boolean updateLogin(Login login) throws Exception;
	
	boolean deleteLogin(Integer id) throws Exception;
	
	Login getLogin(Integer id) throws Exception;
	
	List <Login> getAllLogin() throws Exception;
	
	Login checkLogin(String user_name,String password)throws Exception;
	
	Login checkLogin(String user_name)throws Exception;
	
	Integer updateLock(Integer lock,Integer id)throws Exception;
	
	Integer updateFailCount(Integer failCount,Integer id)throws Exception;
	
}
