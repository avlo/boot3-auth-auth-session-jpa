package com.prosilion.scdecisionmatrix.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;

@Configuration
public class LdapUserConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(LdapUserConfig.class);
	private static final String SEARCH_BASE = "dc=mfad,dc=mfroot,dc=org";
	private static final String DIST_VARIABLE = "cn";
	public static final String OBJECT_CLASS = "objectClass";
	public static final String USER = "user";
	public static final String CN = "cn";
	public static final String USER_SEARCH_FILTER = "{0}";

	@Bean
	LdapAuthenticationProvider ldapAuthenticationProvider(LdapAuthenticator authenticator, AppUserAuthoritiesPopulator authoritiesPopulator) {
		LOGGER.info("Loading LDAP - Authentication Provider");
		return new LdapAuthenticationProvider(authenticator, authoritiesPopulator);
	}

	@Bean
	LdapAuthenticator ldapAuthenticator(BaseLdapPathContextSource baseLdapPathContextSource, FilterBasedLdapUserSearch filterBasedLdapUserSearch, BCryptPasswordEncoder bCryptPasswordEncoder) {
		BindAuthenticator authenticator = new BindAuthenticator(baseLdapPathContextSource);
		authenticator.setUserSearch(filterBasedLdapUserSearch);
		authenticator.afterPropertiesSet();
		return authenticator;
	}

	@Bean
	FilterBasedLdapUserSearch filterBasedLdapUserSearch(BaseLdapPathContextSource baseLdapPathContextSource) {
		return new FilterBasedLdapUserSearch(SEARCH_BASE, getAndFilter(USER_SEARCH_FILTER), baseLdapPathContextSource);
	}

	private static String getAndFilter(String distValue) {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter(OBJECT_CLASS, USER));
		filter.and(new EqualsFilter(DIST_VARIABLE, distValue));
		return filter.toString();
	}
}
