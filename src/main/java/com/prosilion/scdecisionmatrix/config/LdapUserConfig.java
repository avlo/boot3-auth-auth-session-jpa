package com.prosilion.scdecisionmatrix.config;

import com.prosilion.scdecisionmatrix.controller.UsersController;
import com.prosilion.scdecisionmatrix.repository.AppUserAuthUserRepository;
import com.prosilion.scdecisionmatrix.service.AppUserService;
import com.prosilion.scdecisionmatrix.service.security.AuthUserServiceImpl;
import com.prosilion.scdecisionmatrix.service.security.ldap.LdapAuthUserDetailServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableLdapRepositories
public class LdapUserConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(LdapUserConfig.class);

	@Bean
	public AuthUserServiceImpl ldapUserAuthUserService(LdapAuthUserDetailServiceImpl ldapAuthUserDetailServiceImpl, AppUserService appUserService, AppUserAuthUserRepository appUserAuthUserRepository) {
		LOGGER.info("Loading LDAP - LdapAuthUserService");
		return new AuthUserServiceImpl(ldapAuthUserDetailServiceImpl, appUserService, appUserAuthUserRepository);
	}

	@Bean
	public LdapTemplate ldapTemplate(ContextSource contextSource) {
		return new LdapTemplate(contextSource);
	}

	@Bean
	public UserDetailsService ldapAuthUserDetailsService(ContextSource contextSource) {
		LOGGER.info("Loading LDAP - LdapAuthUserDetailServiceImpl");
		return new LdapAuthUserDetailServiceImpl(contextSource, ldapTemplate(contextSource));
	}

	@Bean
	public UsersController ldapUsersController(AuthUserServiceImpl authUserServiceImpl) {
		return new UsersController(authUserServiceImpl);
	}
}
