package org.arpicoinsurance.groupit.main.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.arpicoinsurance.groupit.main.model.JwtAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter{

	public JwtAuthenticationFilter() {
		super("/logins/**");
	}


	@Override
	public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws AuthenticationException, IOException, ServletException {
		
		String header=httpServletRequest.getHeader("Authorization");
		
		if(header== null || !header.startsWith("Token")) {
			throw new RuntimeException("JWT Token is missing..");
		}
		
		String authToken=header.substring(6);
		
		JwtAuthenticationToken authenticationToken=new JwtAuthenticationToken(authToken);
		
		return getAuthenticationManager().authenticate(authenticationToken);
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		super.successfulAuthentication(request, response, chain, authResult);
		chain.doFilter(request, response);
	}

	

}
