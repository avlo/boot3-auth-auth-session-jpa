package com.prosilion.scdecisionmatrix.config;

import com.prosilion.scdecisionmatrix.controller.UsersController;
import com.prosilion.scdecisionmatrix.repository.AppUserAuthUserRepository;
import com.prosilion.scdecisionmatrix.service.AppUserService;
import com.prosilion.scdecisionmatrix.service.security.AuthUserServiceImpl;
import com.prosilion.scdecisionmatrix.service.security.ldap.LdapAuthUserDetailServiceImpl;
import com.prosilion.scdecisionmatrix.service.security.ldap.LdapAuthenticationProvider;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class LdapUserConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(LdapUserConfig.class);

	@Bean
	public AuthUserServiceImpl ldapUserAuthUserService(LdapAuthUserDetailServiceImpl ldapAuthUserDetailServiceImpl, AppUserService appUserService, AppUserAuthUserRepository appUserAuthUserRepository) {
		LOGGER.info("Loading LDAP - LdapAuthUserService");
		return new AuthUserServiceImpl(ldapAuthUserDetailServiceImpl, appUserService, appUserAuthUserRepository);
	}

	@Bean
	public UserDetailsService ldapAuthUserDetailsService(DataSource dataSource, BCryptPasswordEncoder bCryptPasswordEncoder) {
		LOGGER.info("Loading LDAP - LdapAuthUserDetailServiceImpl");
		return new LdapAuthUserDetailServiceImpl(dataSource, bCryptPasswordEncoder);
	}

	@Bean
	AuthenticationManager authenticationManager(HttpSecurity httpSecurity, LdapAuthenticationProvider ldapAuthenticationProvider) throws Exception {
		LOGGER.info("Loading LDAP - AuthenticationManager");
		AuthenticationManagerBuilder builder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
		builder.authenticationProvider(ldapAuthenticationProvider);
		return builder.build();
	}

	@Bean
	public UsersController ldapUsersController(AuthUserServiceImpl authUserServiceImpl) {
		LOGGER.info("Loading LDAP - UsersController");
		return new UsersController(authUserServiceImpl);
	}
}
