package org.arpicoinsurance.groupit.main.controller;

import java.util.Date;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
import org.arpicoinsurance.groupit.main.dao.LoginDao;
import org.arpicoinsurance.groupit.main.encrypt.EncryptData;
import org.arpicoinsurance.groupit.main.helper.HelperLogin;
import org.arpicoinsurance.groupit.main.model.Login;
import org.arpicoinsurance.groupit.main.model.Logs;
import org.arpicoinsurance.groupit.main.model.Users;
import org.arpicoinsurance.groupit.main.security.JwtGenerator;
import org.arpicoinsurance.groupit.main.service.LogService;
import org.arpicoinsurance.groupit.main.service.LoginService;
import org.arpicoinsurance.groupit.main.service.UsersService;
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
	private UsersService userService;

	@Autowired
	private LoginDao loginDao;
	
	@Autowired
	private LogService logService;

	private JwtGenerator generator;

	TokenController(JwtGenerator generator) {
		this.generator = generator;
	}

	@RequestMapping(value = "/checks", method = RequestMethod.POST)
	public String generate(@RequestBody Login logins) {

		if (logins.getLocks() == 0) {// login to system
			return loginToSystem(logins);
		}

		if (logins.getLocks() == 1) {// check availability for change pw
			return checkPwAndUserName(logins);
		}

		return null;

	}

	private String loginToSystem(Login logins) {
		try {

			Login login = loginService.checkLogin(logins.getUserName(), EncryptData.encrypt(logins.getPassword()));
			Users users = null;
			HelperLogin helperLogin = new HelperLogin();

			if (login != null) {
				if (login.getLocks().equals(0)) {
					Integer dayCount = getNewPwDayCount(login.getLoginId());

					if (dayCount <= 0) {
						return "Pw Change";
					} else {

						users = userService.getUserByLoginId(login.getLoginId());

						if (users != null && users.getUser_Active() == 1) {
							helperLogin.setUserCode(users.getUserCode());
							helperLogin.setUserFullName(users.getUser_Name());
							helperLogin.setUserId(users.getUserId());
							loginDao.updateOne(new Date(), login.getLoginId());

							loginService.updateFailCount(0, users.getLogin().getLoginId());

							return generator.generate(helperLogin);
						}else {
							return "User Inactive";
						}
					}
				} else {
					return "Lock";
				}

			} else {
				Login login2 = loginService.checkLogin(logins.getUserName());
				if (login2 != null) {
					if (login2.getFailCount() == 3) {
						loginService.updateLock(1, login2.getLoginId());
						return "Lock";
					} else {
						loginService.updateFailCount(login2.getFailCount() + 1, login2.getLoginId());
					}
				}

				return "Not Found";
			}

		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n Parameters : " + logins.toString());
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("loginToSystem : TokenController");
			try {
				logService.saveLog(logs);
			} catch (Exception e1) {
				//System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				//System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			throw new RuntimeException(e.getMessage());
		}
	}

	private String checkPwAndUserName(Login logins) {
		try {
			Login login = loginService.checkLogin(logins.getUserName(), EncryptData.encrypt(logins.getPassword()));
			Users users = null;
			HelperLogin helperLogin = new HelperLogin();

			if (login != null) {
				users = userService.getUserByLoginId(login.getLoginId());

				if (users != null) {
					helperLogin.setUserCode(users.getUserCode());
					helperLogin.setUserFullName(users.getUser_Name());
					helperLogin.setUserId(users.getUserId());

					return generator.generate(helperLogin);
				}

			} else {
				return "Not Found";
			}
			
		} catch (Exception e) {
			Logs logs = new Logs();
			logs.setData("Error : " + e.getMessage() + ",\n Parameters : " + logins.toString());
			logs.setDate(new Date());
			logs.setHeading("Error");
			logs.setOperation("checkPwAndUserName : TokenController");
			try {
				logService.saveLog(logs);
			} catch (Exception e1) {
				//System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				//System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			throw new RuntimeException(e.getMessage());
		}

		return null;
	}

	// check password pattern
/*	private boolean isPasswordMatchToPattern(Login login) {
		String regex = "(?=.*\\d)(?=.*[A-Za-z]).{8,}";
		
		//^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\S+$).{8,}$

		Pattern pattern = Pattern.compile(regex);

		Matcher matcher = pattern.matcher(login.getPassword());

		return matcher.matches();
	}*/

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
			logs.setOperation("getNewPwDayCount : TokenController");
			try {
				logService.saveLog(logs);
			} catch (Exception e1) {
				//System.out.println("... Error Message for Operation ...");
				e.printStackTrace();
				//System.out.println("... Error Message for save log ...");
				e1.printStackTrace();
			}
			throw new RuntimeException(e.getMessage());
		}

		return count;
	}

	/*
	 * private void setSession(Logs logs) { ArrayList<Logs> logList =
	 * (ArrayList<Logs>) session.getAttribute("log_list"); if (logList == null) {
	 * logList = new ArrayList<>(); } logList.add(logs);
	 * session.setAttribute("log_list", logList);
	 * 
	 * ArrayList<Logs> logList2 = (ArrayList<Logs>)
	 * session.getAttribute("log_list"); //System.out.println(session.getId());
	 * 
	 * }
	 */
}
