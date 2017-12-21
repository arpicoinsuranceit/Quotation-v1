package org.arpicoinsurance.groupit.main.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.arpicoinsurance.groupit.main.dao.UsersDao;
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
	UsersDao userDao;
	
	private JwtGenerator generator;
	
	TokenController(JwtGenerator generator){
		this.generator=generator;
	}

	@RequestMapping(value="/checks",method=RequestMethod.POST)
	public String generate(@RequestBody Login logins) {
		
		try {
			Login login=loginService.checkLogin(logins.getUserName(), logins.getPassword());
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
}
