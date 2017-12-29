package org.arpicoinsurance.groupit.main.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.arpicoinsurance.groupit.main.dao.Quo_Benef_DetailsDao;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Details;
import org.arpicoinsurance.groupit.main.service.Quo_Benef_DetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class Quo_Benef_DetailsServiceImpl implements Quo_Benef_DetailsService{

	@Autowired
	private Quo_Benef_DetailsDao quoBenefDao;
	
	@Override
	public boolean saveQuo_Benef_Details(Quo_Benef_Details qbd) throws Exception {
		if(quoBenefDao.save(qbd)!=null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean updateQuo_Benef_Details(Quo_Benef_Details qbd) throws Exception {
		if(quoBenefDao.save(qbd)!=null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteQuo_Benef_Details(Integer id) throws Exception {
		if(quoBenefDao.deleteOne(id)!=null) {
			return true;
		}
		return false;
	}

	@Override
	public Quo_Benef_Details getQuo_Benef_Details(Integer id) throws Exception {
		return quoBenefDao.findOne(id);
	}

	@Override
	public List<Quo_Benef_Details> getAllQuo_Benef_Details() throws Exception {
		List<Quo_Benef_Details> qbd = new ArrayList<>();
		quoBenefDao.findAll().forEach(qbd::add);
		return qbd;
	}

	@Override
	public List<Quo_Benef_Details> getQuo_Benef_DetailsByQid(Integer id) throws Exception {
		List<Quo_Benef_Details> qbd = new ArrayList<>();
		quoBenefDao.findAll().forEach(qbd::add);
		return qbd;
	}

	
}
