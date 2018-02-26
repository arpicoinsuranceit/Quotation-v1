package org.arpicoinsurance.groupit.main.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import org.arpicoinsurance.groupit.main.dao.LoginDao;
import org.arpicoinsurance.groupit.main.model.Login;
import org.arpicoinsurance.groupit.main.model.Users;
import org.arpicoinsurance.groupit.main.service.ChildService;
import org.arpicoinsurance.groupit.main.service.LoginService;
import org.arpicoinsurance.groupit.main.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;


@RestController
@CrossOrigin(origins = "*")
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	HttpServletResponse response;
	
	@Autowired
	private LoginDao loginDao;
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
    private ApplicationContext appContext;
	
	@Autowired
	private ChildService childService;
	
	
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
		
		try {
			return "201";
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "409";
	}
	
	@RequestMapping(value="/login/{id}",method=RequestMethod.GET)
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
	
	@RequestMapping(value="/pwreset",method=RequestMethod.POST)
	public Integer getPasswordResetDate(@RequestBody String id) {
		try {
			Users users=usersService.getUserByLoginId(Integer.valueOf(id));
			Integer day=getNewPwDayCount(users.getLogin().getLoginId());
			return day;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private Integer getNewPwDayCount(Integer loginId) {
		Integer dayCount = 0;
		Integer count = 0;

		try {
			dayCount = loginDao.findDaysToNextPsw(loginId);

			if (dayCount != null) {
				if (dayCount <= 45) {
					count = 45 - dayCount;
				} else {
					count = -1;
				}
			} else {
				count = -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return count;
	}
	
	
	@RequestMapping(path = "/pdf", method = RequestMethod.GET)
    public ModelAndView report() {
		System.out.println("Called reports.....");
        JasperReportsPdfView view = new JasperReportsPdfView();
        view.setUrl("classpath:report1.jrxml");
        view.setApplicationContext(appContext);

        Map<String, Object> params = new HashMap<>();
        try {
			params.put("datasource", childService.getAll());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return new ModelAndView(view, params);
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
