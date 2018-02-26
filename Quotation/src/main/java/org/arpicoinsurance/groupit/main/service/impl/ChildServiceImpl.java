package org.arpicoinsurance.groupit.main.service.impl;

import java.util.List;

import org.arpicoinsurance.groupit.main.dao.ChildDao;
import org.arpicoinsurance.groupit.main.model.Child;
import org.arpicoinsurance.groupit.main.service.ChildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChildServiceImpl implements ChildService{
	
	@Autowired
	private ChildDao childDao;

	@Override
	public List<Child> getAll() throws Exception {
		return (List<Child>) childDao.findAll();
	}

}
