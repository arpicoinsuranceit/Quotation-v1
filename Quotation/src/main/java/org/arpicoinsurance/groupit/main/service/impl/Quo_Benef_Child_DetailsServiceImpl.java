package org.arpicoinsurance.groupit.main.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.arpicoinsurance.groupit.main.dao.Quo_Benef_Child_DetailsDao;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Child_Details;
import org.arpicoinsurance.groupit.main.service.Quo_Benef_Child_DetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Quo_Benef_Child_DetailsServiceImpl implements Quo_Benef_Child_DetailsService{
	
	@Autowired
	private Quo_Benef_Child_DetailsDao childBenefDao;

	@Override
	public boolean saveQuo_Benef_Child_Details(Quo_Benef_Child_Details childbenf) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateQuo_Benef_Child_Details(Quo_Benef_Child_Details childbenf) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteQuo_Benef_Child_Details(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Quo_Benef_Child_Details getQuo_Benef_Child_Details(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Quo_Benef_Child_Details> getAllQuo_Benef_Child_Details() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Quo_Benef_Child_Details> getQuo_Benef_Child_DetailsByQuo_Benf_DetailsId(Integer id) throws Exception {
		List<Quo_Benef_Child_Details> qbcdList=new ArrayList<>();
		childBenefDao.findByQuoBenfDetailsId(id).forEach(qbcdList::add);
		return qbcdList;
	}

}
