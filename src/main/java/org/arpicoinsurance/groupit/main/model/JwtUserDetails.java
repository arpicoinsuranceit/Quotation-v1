package org.arpicoinsurance.groupit.main.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtUserDetails implements UserDetails {

	private Integer userId;
    private String userName;
    private String fullName;
	
	public JwtUserDetails() {
	}
	
	
	public JwtUserDetails(String userName,String fullName,Integer userId) {
		this.userName=userName;
		this.fullName=fullName;
		this.userId=userId;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}


	@Override
	public String getUsername() {
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	public Integer getUserId() {
		return userId;
	}


	public String getUserName() {
		return userName;
	}
	
	public String getFullName() {
		return fullName;
	}


	@Override
	public String getPassword() {
		
		return null;
	}


}
