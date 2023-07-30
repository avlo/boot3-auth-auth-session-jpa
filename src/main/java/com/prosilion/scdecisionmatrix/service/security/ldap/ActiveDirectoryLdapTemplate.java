package com.prosilion.scdecisionmatrix.service.security.ldap;

import javax.naming.directory.SearchControls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.security.ldap.SpringSecurityLdapTemplate;
import org.springframework.stereotype.Component;

@Component
public class ActiveDirectoryLdapTemplate extends SpringSecurityLdapTemplate {
	private static final Logger LOGGER = LoggerFactory.getLogger(ActiveDirectoryLdapTemplate.class);
	private static final String LDAP_BASE_FOR_DN_USER = "DC=mfad,DC=mfroot,DC=org";
	public static final String OBJECT_CLASS = "objectClass";
	public static final String USER = "user";
	public static final String CN = "cn";

	@Autowired
	public ActiveDirectoryLdapTemplate(LdapContextSource ldapContextSource) {
		super(ldapContextSource);
		LOGGER.info("Using AD LdapTemplate Ignore Partial Results Exception");
		LOGGER.info("User DN: " + ldapContextSource.getUserDn());
		super.setIgnorePartialResultException(true);
	}

	protected boolean authenticate(final String lanId, String password) {
		LOGGER.info("Authenticating via LDAP");
		try{
			boolean result = super.authenticate(LDAP_BASE_FOR_DN_USER, getAndFilter(lanId).toString(), password);
			LOGGER.info(result ? "LDAP Authentication SUCCESS" : "LDAP Authentication FAILED");
			return result;
		} catch(Exception e){
			LOGGER.info("LDAP Authentication exception thrown");
			LOGGER.info("LDAP was not configured properly.", e);
			return false;
		}
	}

	private static AndFilter getAndFilter(String lanId) {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter(OBJECT_CLASS, USER));
		filter.and(new EqualsFilter(CN, lanId));
		return filter;
	}

	private static SearchControls getSearchControls() {
		SearchControls controls = new SearchControls();
		controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		String[] attrIDs = { "sn", "givenname", "initials", CN, "uid", "l", "memberOf"};
		controls.setReturningAttributes(attrIDs);
		return controls;
	}
}
