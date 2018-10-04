package org.arpicoinsurance.groupit.main.service.impl;

import java.util.List;
import javax.transaction.Transactional;
import org.arpicoinsurance.groupit.main.dao.custom.QuotationCustomDao;
import org.arpicoinsurance.groupit.main.helper.QuotationSearch;
import org.arpicoinsurance.groupit.main.model.Users;
import org.arpicoinsurance.groupit.main.security.JwtDecoder;
import org.arpicoinsurance.groupit.main.service.BranchUnderwriteService;
import org.arpicoinsurance.groupit.main.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BranchUnderwriteServiceImpl implements BranchUnderwriteService{
	
	@Autowired
	private QuotationCustomDao quotationCustomDao;
	
	@Autowired
	private UsersService usersService;

	@Override
	public List<QuotationSearch> getQuotationToUnderwrite(String token) throws Exception {
		String userCode=new JwtDecoder().generate(token);
		Users user=usersService.getUserByUserCode(userCode);
		System.out.println(user.getBranch().getBranchId() + " user.getBranch().getBranchId() /////////////");
		return quotationCustomDao.getQuotationToUnderwrite("PROP", user.getBranch().getBranchId());
	}
	
	

}
