package org.arpicoinsurance.groupit.main.config;

import java.util.Collections;

import org.arpicoinsurance.groupit.main.security.JwtAuthenticationEntryPoint;
import org.arpicoinsurance.groupit.main.security.JwtAuthenticationFilter;
import org.arpicoinsurance.groupit.main.security.JwtAuthenticationProvider;
import org.arpicoinsurance.groupit.main.security.JwtSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableGlobalMethodSecurity(prePostEnabled=true)
@EnableWebSecurity
@Configurable
public class JwtSecurityConfig extends WebSecurityConfigurerAdapter{

		@Autowired
		private JwtAuthenticationEntryPoint entryPoint;
		
		@Autowired
		private JwtAuthenticationProvider authenticationProvider;

		@Bean
		public AuthenticationManager authenticationManager() {
			return new ProviderManager(Collections.singletonList(authenticationProvider));
		}
		
		@Bean
		public JwtAuthenticationFilter authenticationTokenFilter() {
			JwtAuthenticationFilter filter=new JwtAuthenticationFilter();
			filter.setAuthenticationManager(authenticationManager());
			filter.setAuthenticationSuccessHandler(new JwtSuccessHandler());
			
			return filter;
		}
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable().authorizeRequests().antMatchers("**/logins/**").authenticated().and()
			.exceptionHandling().authenticationEntryPoint(entryPoint).and().sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			
			http.addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
			
			http.headers().cacheControl();
		}
}
