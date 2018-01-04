package org.arpicoinsurance.groupit.main.security;

import org.arpicoinsurance.groupit.main.helper.HelperLogin;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtGenerator {

	public String generate(HelperLogin login) {
		
		Claims claims=Jwts.claims().
				setSubject(login.getUserCode());
		
		claims.put("userId", login.getUserId());
		claims.put("fullName", login.getUserFullName());
				
		return Jwts.builder().setClaims(claims)
				.signWith(SignatureAlgorithm.HS512, "harindi")
				.compact();
		
	}

}
