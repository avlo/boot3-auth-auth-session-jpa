package com.prosilion.scdecisionmatrix.config;

import com.prosilion.scdecisionmatrix.controller.UsersController;
import com.prosilion.scdecisionmatrix.repository.AppUserAuthUserRepository;
import com.prosilion.scdecisionmatrix.service.AppUserService;
import com.prosilion.scdecisionmatrix.service.security.AuthUserServiceImpl;
import com.prosilion.scdecisionmatrix.service.security.ldap.LdapAuthUserDetailServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.ldap.SpringSecurityLdapTemplate;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;

@Configuration
public class LdapUserConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(LdapUserConfig.class);

	@Autowired
	private BaseLdapPathContextSource contextSource;

	@Bean
	public AuthUserServiceImpl ldapUserAuthUserService(LdapAuthUserDetailServiceImpl ldapAuthUserDetailServiceImpl, AppUserService appUserService, AppUserAuthUserRepository appUserAuthUserRepository) {
		LOGGER.info("Loading LDAP - LdapAuthUserService");
		return new AuthUserServiceImpl(ldapAuthUserDetailServiceImpl, appUserService, appUserAuthUserRepository);
	}

	@Bean
	public SpringSecurityLdapTemplate ldapTemplate() {
		return new SpringSecurityLdapTemplate(contextSource);
	}

	@Bean
	public UserDetailsService ldapAuthUserDetailsService() {
		LOGGER.info("Loading LDAP - LdapAuthUserDetailServiceImpl");
		return new LdapAuthUserDetailServiceImpl(contextSource, ldapTemplate());
	}

//	@Bean
//	LdapAuthoritiesPopulator authorities() {
//		String groupSearchBase = "ou=groups";
//		DefaultLdapAuthoritiesPopulator authorities = new DefaultLdapAuthoritiesPopulator(contextSource, groupSearchBase);
//		authorities.setGroupSearchFilter("(uid={0})");
//		return authorities;
//	}

//	@Bean
//	LdapAuthenticationProvider ldapAuthenticationProvider() {
//		return new LdapAuthenticationProvider(authenticator());
//	}

	@Bean
	BindAuthenticator authenticator() {
		FilterBasedLdapUserSearch search = new FilterBasedLdapUserSearch("ou=people", "(uid={0})", contextSource);
		BindAuthenticator authenticator = new BindAuthenticator(contextSource);
		authenticator.setUserSearch(search);
		return authenticator;
	}

	@Bean
	public UsersController ldapUsersController(AuthUserServiceImpl authUserServiceImpl) {
		return new UsersController(authUserServiceImpl);
	}

//	@Bean
//	AuthenticationManager authenticationManager(LdapAuthoritiesPopulator authorities) {
//		System.out.println("5555555555555555555555");
//		System.out.println("5555555555555555555555");
//		LdapBindAuthenticationManagerFactory factory = new LdapBindAuthenticationManagerFactory(contextSource);
//		factory.setUserSearchBase("ou=people");
//		factory.setUserSearchFilter("(uid={0})");
//		AuthenticationManager a = factory.createAuthenticationManager();
//		System.out.println("5555555555555555555555");
//		System.out.println("5555555555555555555555");
//		return a;
//	}
}
