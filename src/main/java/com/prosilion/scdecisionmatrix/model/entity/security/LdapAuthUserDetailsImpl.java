package com.prosilion.scdecisionmatrix.model.entity.security;

import java.io.Serializable;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.InetOrgPerson;

@Scope("session")
public class LdapAuthUserDetailsImpl extends InetOrgPerson implements AuthUserDetails, Serializable {

	private static final Logger LOGGER = LoggerFactory.getLogger(LdapAuthUserDetailsImpl.class);
	final private UserDetails user;
	public LdapAuthUserDetailsImpl(@NonNull UserDetails user) {
		LOGGER.info("Creating LdapAuthUserDetailsImpl object");
		this.user = user;
	}

	@Override
	public UserDetails getUser() {
		LOGGER.info("getting user via LdapAuthUserDetailsImpl");
		return user;
	}

}
