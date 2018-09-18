package org.arpicoinsurance.groupit.main.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.arpicoinsurance.groupit.main.dao.PreviousPasswordDao;
import org.arpicoinsurance.groupit.main.model.Login;
import org.arpicoinsurance.groupit.main.model.PreviousPassword;
import org.arpicoinsurance.groupit.main.service.LoginService;
import org.arpicoinsurance.groupit.main.service.PreviousPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PreviousPasswordServiceImpl implements PreviousPasswordService{

	@Autowired
	private PreviousPasswordDao passwordDao;
	
	@Autowired
	private LoginService loginService;


	@Override
	public boolean savePassword(PreviousPassword password,Login login) throws Exception {
		if(loginService.updateLogin(login)) {
			if(passwordDao.save(password)!=null) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public boolean updatePassword(PreviousPassword password) throws Exception {
		if(passwordDao.save(password)!=null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean deletePassword(Integer id) throws Exception {
		if(passwordDao.deleteOne(id)!= null) {
			return true;
		}
		return false;
	}

	@Override
	public PreviousPassword getPassword(Integer id) throws Exception {
		return passwordDao.findOne(id);
	}

	@Override
	public List<PreviousPassword> getLatestPassword(Integer id) throws Exception {
		List<PreviousPassword> passwords = new ArrayList<>();
		passwordDao.findRecentPsw(id).forEach(passwords::add);
		return passwords;
	}

}
