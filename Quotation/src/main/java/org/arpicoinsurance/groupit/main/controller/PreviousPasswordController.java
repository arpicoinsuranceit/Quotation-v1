package org.arpicoinsurance.groupit.main.controller;

import java.util.List;

import org.arpicoinsurance.groupit.main.model.Login;
import org.arpicoinsurance.groupit.main.model.PreviousPassword;
import org.arpicoinsurance.groupit.main.model.Users;
import org.arpicoinsurance.groupit.main.service.PreviousPasswordService;
import org.arpicoinsurance.groupit.main.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class PreviousPasswordController {
	
	@Autowired
	private PreviousPasswordService previousPasswordService;
	
	@Autowired
	private UsersService usersService;
	
	
	@RequestMapping(value="/password",method=RequestMethod.POST)
	public List<PreviousPassword> getLatestPw(@RequestBody Login login) {
		try {
			Users users=usersService.getUser(login.getLocks());
			List<PreviousPassword> passwordList=previousPasswordService.getLatestPassword(users.getLogin().getLoginId());
			System.out.println(passwordList);
			return passwordList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
