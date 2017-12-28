package org.arpicoinsurance.groupit.main.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.arpicoinsurance.groupit.main.dao.LoginDao;
import org.arpicoinsurance.groupit.main.model.Login;
import org.arpicoinsurance.groupit.main.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoginServiceImpl implements LoginService{
	
	@Autowired
	private LoginDao loginDao;

	@Override
	public boolean saveLogin(Login login) throws Exception {
		if(loginDao.save(login)!=null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean updateLogin(Login login) throws Exception {
		if(loginDao.save(login)!=null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteLogin(Integer id) throws Exception {
		if(loginDao.deleteOne(id)!= null) {
			return true;
		}
		return false;
	}

	@Override
	public Login getLogin(Integer id) throws Exception {
		return loginDao.findOne(id);
	}

	@Override
	public List<Login> getAllLogin() throws Exception {
		List<Login> logins = new ArrayList<>();
		loginDao.findAll().forEach(logins::add);
		return logins;
	}

	@Override
	public Login checkLogin(String user_name, String password) throws Exception {
		return loginDao.findByUserNameAndPsw(user_name, password);
		
	}

}
