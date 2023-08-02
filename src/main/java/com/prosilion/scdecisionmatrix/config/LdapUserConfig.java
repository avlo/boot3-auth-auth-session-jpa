package com.prosilion.scdecisionmatrix.config;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.naming.directory.SearchControls;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.ldap.authentication.PasswordComparisonAuthenticator;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;

@Configuration
public class LdapUserConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(LdapUserConfig.class);
	private static final String GROUP_BASE = "dc=mfad,dc=mfroot,dc=org";
	private static final String SEARCH_BASE = "dc=mfad,dc=mfroot,dc=org";
	private static final String DIST_VARIABLE = "cn";
	public static final String OBJECT_CLASS = "objectClass";
	public static final String USER = "user";
	public static final String CN = "cn";
	public static final String USER_SEARCH_FILTER = "{0}";
	public static final String GROUP_SEARCH_FILTER = "{1}";

	@Bean
	LdapAuthenticationProvider ldapAuthenticationProvider(LdapAuthenticator authenticator, LdapAuthoritiesPopulator authoritiesPopulator) {
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

	@Bean
	LdapAuthoritiesPopulator ldapAuthoritiesPopulator(BaseLdapPathContextSource baseLdapPathContextSource) {
		Function<Map<String, List<String>>, GrantedAuthority> authorityMapper =
				record -> {
					String role = record.get("ROLE_").get(0);
					return new SimpleGrantedAuthority(role);
				};
		LOGGER.info("Loading LDAP - Authorities (authorization) Provider");
		DefaultLdapAuthoritiesPopulator populator = new DefaultLdapAuthoritiesPopulator(baseLdapPathContextSource, GROUP_BASE);
		populator.setGroupSearchFilter(getAndFilter(GROUP_SEARCH_FILTER));
		populator.setGroupRoleAttribute(CN);
		populator.setIgnorePartialResultException(true);
		populator.setSearchSubtree(true);
		populator.setAuthorityMapper(authorityMapper);
		return populator;
	}

	@Bean
	public StandardPBEStringEncryptor standardPBEStringEncryptor() {
		StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
		standardPBEStringEncryptor.setAlgorithm("PBEWithMD5AndDES");
		standardPBEStringEncryptor.setPassword("app-encryption-key");
		return standardPBEStringEncryptor;
	}

	private static String getAndFilter(String distValue) {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter(OBJECT_CLASS, USER));
		filter.and(new EqualsFilter(DIST_VARIABLE, distValue));
		return filter.toString();
	}

	private static SearchControls getSearchControls() {
		SearchControls controls = new SearchControls();
		controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		String[] attrIDs = { "sn", "givenname", "initials", CN, "uid", "l", "memberOf"};
		controls.setReturningAttributes(attrIDs);
		return controls;
	}

}
