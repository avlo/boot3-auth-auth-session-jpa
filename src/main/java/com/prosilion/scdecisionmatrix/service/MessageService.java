package com.prosilion.scdecisionmatrix.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MessageService {

	public String getSubscriptionMessage() {
		return "TEST MESSAGE";
	}

	public void throwMyException() throws NumberFormatException {
		throw new NumberFormatException("no arg");
	}
}
