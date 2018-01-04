package org.arpicoinsurance.groupit.main.security;

import org.arpicoinsurance.groupit.main.helper.HelperLogin;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtValidator {
	
	private String secret="harindi";

	public HelperLogin validate(String token) {
		HelperLogin jwtLogin=null;
		try {
		
		Claims claims=Jwts.parser().setSigningKey(secret).parseClaimsJws(token)
				.getBody();
		
		jwtLogin=new HelperLogin();
		jwtLogin.setUserCode(claims.getSubject());
		jwtLogin.setUserId(Integer.parseInt((String) claims.get("loginId")));
		jwtLogin.setUserFullName((String) claims.get("password"));
		}catch (Exception e) {
			System.out.println(e);
		}
		
		return jwtLogin;
		
	}
	
	

}
