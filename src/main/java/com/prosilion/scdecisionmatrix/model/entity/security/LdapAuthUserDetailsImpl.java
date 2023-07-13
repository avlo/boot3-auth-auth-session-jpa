package com.prosilion.scdecisionmatrix.model.entity.security;

import java.io.Serializable;
import lombok.NonNull;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.InetOrgPerson;

@Scope("session")
public class LdapAuthUserDetailsImpl extends InetOrgPerson implements AuthUserDetails, Serializable {

	final private UserDetails user;
	public LdapAuthUserDetailsImpl(@NonNull UserDetails user) {
		this.user = user;
	}

	@Override
	public UserDetails getUser() {
		return user;
	}

}
