package org.arpicoinsurance.groupit.main.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.arpicoinsurance.groupit.main.model.Login;
import org.arpicoinsurance.groupit.main.model.Logs;
import org.arpicoinsurance.groupit.main.service.LogService;
import org.arpicoinsurance.groupit.main.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin(origins = "*")
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	HttpServletResponse response;
	
	//@Autowired
	//private HttpSession session;
	
	//@Autowired
	//private LogService logService;
	
	
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public List<Login> getAllLogin() {
		try {
			List<Login> loginList=loginService.getAllLogin();
			
			return loginList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@RequestMapping(value="/logOut",method=RequestMethod.GET)
	public String logout() {
		System.out.println("Called Logout");
		try {
			//saveData();
			//session.invalidate();
			return "201";
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "409";
	}
	
	@RequestMapping(value="/login/{id}",method=RequestMethod.GET)
	public Login getLogin(@PathVariable Integer id) {
		System.out.println("Called get id");
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
	
	/*public void saveData() {
		ArrayList<Logs> logList=(ArrayList<Logs>) session.getAttribute("log_list");
		
		System.out.println(session.getId());
		if(logList!=null) {
			for (Logs logs : logList) {
				try {
					logService.saveLog(logs);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else {
			System.out.println("Null ");
		}
	}*/
	

}
