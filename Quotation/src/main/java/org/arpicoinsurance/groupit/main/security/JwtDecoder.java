package org.arpicoinsurance.groupit.main.security;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;

public class JwtDecoder {
	public String generate(String token) {
		
		Jwt jwtToken =JwtHelper.decode(token);
		String claims = jwtToken.getClaims();
		System.out.println(claims);
		return null;
		
	}
}
