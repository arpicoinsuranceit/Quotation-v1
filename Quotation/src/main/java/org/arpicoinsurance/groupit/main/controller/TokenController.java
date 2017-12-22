package org.arpicoinsurance.groupit.main.controller;

import java.util.Date;
import javax.servlet.http.HttpSession;
import org.arpicoinsurance.groupit.main.dao.LoginDao;
import org.arpicoinsurance.groupit.main.dao.UsersDao;
import org.arpicoinsurance.groupit.main.encrypt.EncryptData;
import org.arpicoinsurance.groupit.main.model.HelperLogin;
import org.arpicoinsurance.groupit.main.model.Login;
import org.arpicoinsurance.groupit.main.model.Users;
import org.arpicoinsurance.groupit.main.security.JwtGenerator;
import org.arpicoinsurance.groupit.main.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin(origins = "*")
public class TokenController {
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private UsersDao userDao;
	
	@Autowired
	private LoginDao loginDao;
	
	@Autowired
	private HttpSession session;
	
	private JwtGenerator generator;
	
	TokenController(JwtGenerator generator){
		this.generator=generator;
	}

	@RequestMapping(value="/checks",method=RequestMethod.POST)
	public String generate(@RequestBody Login logins) {
		
		if(logins.getLocks()==0) {//login to system
			return loginToSystem(logins);
		}
		
		if(logins.getLocks()==1) {//check availability for change pw
			return checkPwAndUserName(logins);
		}
		
		return null;
		
	}
	
	private String loginToSystem(Login logins) {
		try {
			Login login=loginService.checkLogin(logins.getUserName(), EncryptData.encrypt(logins.getPassword()));
			Users users=null;
			HelperLogin helperLogin=new HelperLogin();
			
			session.setAttribute("log", "aaa");
			
			if(login!=null) {
				
				Integer dayCount=getNewPwDayCount(login.getLoginId());
				
				if(dayCount<=0) {
					return "Pw Change";
				}else {
				
					users=userDao.findByLoginId(login.getLoginId());
					
					if(users!=null) {
						helperLogin.setUserCode(users.getUser_Code());
						helperLogin.setUserFullName(users.getUser_Name());
						helperLogin.setUserId(users.getUserId());
						loginDao.updateOne(new Date(), login.getLoginId());
						return generator.generate(helperLogin);
					}
				}
				
			}else {
				return "Not Found";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private String checkPwAndUserName(Login logins) {
		try {
			Login login=loginService.checkLogin(logins.getUserName(), EncryptData.encrypt(logins.getPassword()));
			Users users=null;
			HelperLogin helperLogin=new HelperLogin();
			
			if(login!=null) {
				users=userDao.findByLoginId(login.getLoginId());
			
				if(users!=null) {
					helperLogin.setUserCode(users.getUser_Code());
					helperLogin.setUserFullName(users.getUser_Name());
					helperLogin.setUserId(users.getUserId());
				
					return generator.generate(helperLogin);
				}
				
			}else {
				return "Not Found";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private Integer getNewPwDayCount(Integer loginId) {
		Integer dayCount=0;
		Integer count=0;
		try {
			dayCount = loginDao.findDaysToNextPsw(loginId);
			
			if(dayCount!=null) {
				if(dayCount<=45) {
					count=45-dayCount;
				}else {
					count=-1;
				}
			}else {
				count=-1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		return count;
	}
}
