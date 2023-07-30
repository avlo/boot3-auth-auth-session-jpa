package com.prosilion.scdecisionmatrix.service.security.ldap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.ldap.SpringSecurityLdapTemplate;
import org.springframework.stereotype.Component;

@Component
public class ADLdapTemplate extends SpringSecurityLdapTemplate {
	private static final Logger LOGGER = LoggerFactory.getLogger(ADLdapTemplate.class);

	@Autowired
	public ADLdapTemplate(LdapContextSource ldapContextSource) {
		super(ldapContextSource);
		LOGGER.info("Using AD LdapTemplate Ignore Partial Results Exception");
		LOGGER.info("User DN: " + ldapContextSource.getUserDn());
		super.setIgnorePartialResultException(true);
	}
}
