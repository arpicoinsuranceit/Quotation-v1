package org.arpicoinsurance.groupit.main.controller;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.arpicoinsurance.groupit.main.encrypt.EncryptData;
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
	public String changePassword(@RequestBody Login login) {
		try {
			if(isPasswordMatchToPattern(login)) {
				Users users=usersService.getUser(login.getLocks());
				List<PreviousPassword> passwordList=previousPasswordService.
						getLatestPassword(users.getLogin().getLoginId()); // Get the previous password list using loginId
				
				if(!(EncryptData.encrypt(login.getPassword()).equals(users.getLogin().getPassword()))) {
					if(!isPasswordUsedBefore(login,passwordList)) {
						Login login2=new Login();
						login2.setLoginId(users.getLogin().getLoginId());
						login2.setPassword(EncryptData.encrypt(login.getPassword()));
						login2.setModifydate(new Date());
						login2.setModifyBy(login.getUserName());
						login2.setCreateBy(users.getLogin().getCreateBy());
						login2.setCreatedate(users.getLogin().getCreatedate());
						login2.setFailCount(users.getLogin().getFailCount());
						login2.setInactiveDate(users.getLogin().getInactiveDate());
						login2.setLastLog(users.getLogin().getLastLog());
						login2.setLocks(users.getLogin().getLocks());
						login2.setUserName(users.getLogin().getUserName());
						
						PreviousPassword password=new PreviousPassword();
						password.setPassword(users.getLogin().getPassword());
						password.setDisableDate(new Date());
						password.setDisabledBy(login.getUserName());
						password.setLogin(users.getLogin());
						
						if(previousPasswordService.savePassword(password,login2)) {
							return "Success";
						}else {
							return "fail";
						}
						
					}else {
						return "Used";
					}
				}else {
					return "Current Pw";
				}
				
			}else {
				return "Pw Not Match";
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	//check password pattern
	private boolean isPasswordMatchToPattern(Login login) {
		String regex="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
		
		Pattern pattern=Pattern.compile(regex);
		
		Matcher matcher=pattern.matcher(login.getPassword());
		
		return matcher.matches();
	}

	//check new password is include peviously used password
	private boolean isPasswordUsedBefore(Login login, List<PreviousPassword> passwordList) {
		
		if(!passwordList.isEmpty()) {
			if(passwordList.size()<=5) {
				for(int i=0;i<passwordList.size();i++) {
					PreviousPassword password=passwordList.get(i);
					if(password.getPassword().equals(login.getPassword())) {
						return true;
					}
				}
			}
			
			if(passwordList.size()>5) {
				for(int i=0;i<5;i++) {
					PreviousPassword password=passwordList.get(i);
					if(password.getPassword().equals(login.getPassword())) {
						return true;
					}
				}
			}
		}else {
			return false;
		}
		
		return false;
	}
	
}
