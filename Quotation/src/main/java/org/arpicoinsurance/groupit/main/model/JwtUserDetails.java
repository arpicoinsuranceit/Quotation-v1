package org.arpicoinsurance.groupit.main.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtUserDetails implements UserDetails {

	private Integer loginId;
    private String userName;
    private String password;
	
	public JwtUserDetails() {
	}
	
	
	public JwtUserDetails(String userName,String password,Integer loginId) {
		this.userName=userName;
		this.password=password;
		this.loginId=loginId;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return password;
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
	
	public Integer getLoginId() {
		return loginId;
	}


	public String getUserName() {
		return userName;
	}
	
	public String getUPassword() {
		return password;
	}


}
