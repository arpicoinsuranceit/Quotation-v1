package org.arpicoinsurance.groupit.main.listeners;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;


public class SessionListener implements HttpSessionListener{

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		System.out.println("Create");
		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		System.out.println("DEstroy");
		
	}

}
