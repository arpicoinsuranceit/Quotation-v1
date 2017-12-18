package org.arpicoinsurance.groupit.main.security;

import org.arpicoinsurance.groupit.main.model.Login;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtGenerator {

	public String generate(Login login) {
		
		Claims claims=Jwts.claims().
				setSubject(login.getUserName());
		
		claims.put("loginId", login.getLoginId());
		claims.put("password", login.getPassword());
				
		return Jwts.builder().setClaims(claims)
				.signWith(SignatureAlgorithm.HS512, "harindi")
				.compact();
		
	}

}
