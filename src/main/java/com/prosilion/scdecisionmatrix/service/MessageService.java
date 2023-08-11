package com.prosilion.scdecisionmatrix.service;

import com.prosilion.scdecisionmatrix.service.security.AuthUserDetailsService;
import com.prosilion.scdecisionmatrix.service.security.jdbc.AuthUserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

	private final AuthUserDetailsService authUserDetailsService;

	@Autowired
	public MessageService(AuthUserDetailsService authUserDetailsService) {
		this.authUserDetailsService = authUserDetailsService;
	}

	public String getSubscriptionMessage() {
		return "TEST MESSAGE";
	}

	public void throwMyException() throws NumberFormatException {
		throw new NumberFormatException("no arg");
	}
}
