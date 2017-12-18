package org.arpicoinsurance.groupit.main.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.arpicoinsurance.groupit.main.dao.UsersDao;
import org.arpicoinsurance.groupit.main.model.HelperLogin;
import org.arpicoinsurance.groupit.main.model.Login;
import org.arpicoinsurance.groupit.main.model.Users;
import org.arpicoinsurance.groupit.main.security.JwtGenerator;
import org.arpicoinsurance.groupit.main.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

@RestController
@CrossOrigin(origins = "*")
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	HttpServletResponse response;
	
	@Autowired
	UsersDao userDao;
	
	/*@RequestMapping(value="/login/addlogin",method=RequestMethod.GET)
	public String navLogin() {
		return "Hello World";
	}*/
	
	@RequestMapping(value="/login/view",method=RequestMethod.GET)
	public List<Login> getAllLogin() {
		try {
			List<Login> loginList=loginService.getAllLogin();
			
			return loginList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/*@RequestMapping(value="/login/{id}",method=RequestMethod.GET)
	public Login getLogin(@PathVariable Integer id) {
		try {
			Login login=loginService.getLogin(id);
			
			return login;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public String addLogin(@RequestBody Login login) {
		try {
			if(loginService.saveLogin(login)) {
				return "201";
			}else {
				return "409";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "409";
	}
	
	@RequestMapping(value="/logincheck",method=RequestMethod.POST)
	public void checkLogin(@RequestBody Login logins) {
		PrintWriter out=null;
		
		try {
			Gson gson=new Gson();
			out=response.getWriter();
			
			Login login=loginService.checkLogin(logins.getUserName(), logins.getPassword());
			Users users=null;
			HelperLogin helperLogin=new HelperLogin();
			
			if(login!=null) {
				users=userDao.findByLoginId(login.getLoginId());
				
				if(users!=null) {
					helperLogin.setAvailability(1);
					helperLogin.setUserCode(users.getUser_Code());
					helperLogin.setUserFullName(users.getUser_Name());
					helperLogin.setUserId(users.getUserId());
					
					out.write(gson.toJson(helperLogin));
				}
				
			}else {
				helperLogin.setAvailability(0);
				out.write(gson.toJson(helperLogin));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(out!=null) {
				out.close();
			}
		}
	}*/

}
