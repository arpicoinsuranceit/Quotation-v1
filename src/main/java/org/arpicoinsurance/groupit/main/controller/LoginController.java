package org.arpicoinsurance.groupit.main.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import org.arpicoinsurance.groupit.main.dao.LoginDao;
import org.arpicoinsurance.groupit.main.model.Login;
import org.arpicoinsurance.groupit.main.model.Logs;
import org.arpicoinsurance.groupit.main.model.Users;
import org.arpicoinsurance.groupit.main.service.LogService;
import org.arpicoinsurance.groupit.main.service.LoginService;
import org.arpicoinsurance.groupit.main.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	@Autowired
	private LoginDao loginDao;

	@Autowired
	private UsersService usersService;

	@Autowired
	private LogService logService;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ResponseEntity<Object> getAllLogin() {
		
		/*try {
			List<Login> loginList = loginService.getAllLogin();
			return new ResponseEntity<Object>(loginList, HttpStatus.OK);
		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage());
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("getAllLogin : LoginController");

			try {
				logService.saveLog(logs);
			} catch (Exception e1) {
				System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}*/
		
		 return null;
	}

	@RequestMapping(value = "/logOut", method = RequestMethod.GET)
	public ResponseEntity<Object> logout() {

		try {
			return new ResponseEntity<Object>("201", HttpStatus.OK);
		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage());
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("logout : LoginController");
			try {
				logService.saveLog(logs);
			} catch (Exception e1) {
				System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// return "409";
	}

	@RequestMapping(value = "/login/{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> getLogin(@PathVariable Integer id) {
		try {
			Login login = loginService.getLogin(id);
			return new ResponseEntity<Object>(login, HttpStatus.OK);
		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n Parameters : " + id);
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("getLogin : LoginController");
			try {
				logService.saveLog(logs);
			} catch (Exception e1) {
				System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// return null;
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<Object> addLogin(@RequestBody Login login) {
		try {
			if (loginService.saveLogin(login)) {
				return new ResponseEntity<Object>("201", HttpStatus.OK);
			} else {
				return new ResponseEntity<Object>("409", HttpStatus.OK);
			}
		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n Parameters : " + login.toString());
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("addLogin : LoginController");
			try {
				logService.saveLog(logs);
			} catch (Exception e1) {
				System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// return "409";
	}

	@RequestMapping(value = "/pwreset", method = RequestMethod.POST)
	public ResponseEntity<Object> getPasswordResetDate(@RequestBody String id) {
		try {
			Users users = usersService.getUserByLoginId(Integer.valueOf(id));
			Integer day = getNewPwDayCount(users.getLogin().getLoginId());
			return new ResponseEntity<Object>(day, HttpStatus.OK);
		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n Parameters : " + id);
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("getPasswordResetDate : LoginController");
			try {
				logService.saveLog(logs);
			} catch (Exception e1) {
				System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// return null;
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
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n Parameters : " + loginId);
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("getNewPwDayCount : LoginController");
			try {
				logService.saveLog(logs);
			} catch (Exception e1) {
				System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			throw new RuntimeException(e.getMessage());
		}

		return count;
	}

}
