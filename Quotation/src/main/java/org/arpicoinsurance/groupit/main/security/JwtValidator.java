package org.arpicoinsurance.groupit.main.security;

import org.arpicoinsurance.groupit.main.model.Login;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtValidator {
	
	private String secret="harindi";

	public Login validate(String token) {
		Login jwtLogin=null;
		try {
		
		Claims claims=Jwts.parser().setSigningKey(secret).parseClaimsJws(token)
				.getBody();
		
		jwtLogin=new Login();
		jwtLogin.setUserName(claims.getSubject());
		jwtLogin.setLoginId(Integer.parseInt((String) claims.get("loginId")));
		jwtLogin.setPassword((String) claims.get("password"));
		}catch (Exception e) {
			System.out.println(e);
		}
		
		return jwtLogin;
		
	}
	
	

}
