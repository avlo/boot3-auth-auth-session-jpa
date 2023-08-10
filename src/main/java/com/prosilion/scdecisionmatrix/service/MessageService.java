package com.prosilion.scdecisionmatrix.service;

import com.prosilion.scdecisionmatrix.service.security.jdbc.AuthUserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

	private final AuthUserDetailServiceImpl authUserDetailService;

	@Autowired
	public MessageService(AuthUserDetailServiceImpl authUserDetailService) {
		this.authUserDetailService = authUserDetailService;
	}

	public String getSubscriptionMessage() {
		return "TEST MESSAGE";
	}

	public void throwMyException() throws NumberFormatException {
		throw new NumberFormatException("no arg");
	}
}
