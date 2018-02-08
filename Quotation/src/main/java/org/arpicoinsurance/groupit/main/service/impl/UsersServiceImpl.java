package org.arpicoinsurance.groupit.main.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.arpicoinsurance.groupit.main.dao.UsersDao;
import org.arpicoinsurance.groupit.main.model.Users;
import org.arpicoinsurance.groupit.main.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UsersServiceImpl implements UsersService{
	
	@Autowired
	private UsersDao usersDao;

	@Override
	public boolean saveUser(Users users) throws Exception {
		if(usersDao.save(users)!=null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean updateUser(Users users) throws Exception {
		if(usersDao.save(users)!=null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteUser(Integer id) throws Exception {
		if(usersDao.deleteOne(id)!= null) {
			return true;
		}
		return false;
	}

	@Override
	public Users getUser(Integer id) throws Exception {
		return usersDao.findOne(id);
	}

	@Override
	public List<Users> getAllUsers() throws Exception {
		List<Users> users = new ArrayList<>();
		usersDao.findAll().forEach(users::add);
		return users;
	}

	@Override
	public Users getUserByLoginId(Integer id) throws Exception {
		return usersDao.findByLoginId(id);
	}

}
