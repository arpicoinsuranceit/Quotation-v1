package org.arpicoinsurance.groupit.main.controller;

import org.arpicoinsurance.groupit.main.model.Login;
import org.arpicoinsurance.groupit.main.security.JwtGenerator;
import org.arpicoinsurance.groupit.main.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {
	
	@Autowired
	private LoginService loginService;
	
	private JwtGenerator generator;
	
	TokenController(JwtGenerator generator){
		this.generator=generator;
	}

	@RequestMapping(value="/checks",method=RequestMethod.POST)
	public String generate(@RequestBody Login logins) {
		System.out.println(logins.getUserName()+"User name///////");
		try {
			Login login=loginService.checkLogin(logins.getUserName(), logins.getPassword());
			
			if(login!=null) {
				System.out.println(login.getUserName());
				return generator.generate(logins);
			}else {
				return "Not Found";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
}
