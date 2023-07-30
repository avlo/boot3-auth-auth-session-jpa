package com.prosilion.scdecisionmatrix.config;

import com.prosilion.scdecisionmatrix.service.security.ldap.LdapAuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class LdapUserConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(LdapUserConfig.class);

	@Bean
	AuthenticationManager authenticationManager(HttpSecurity httpSecurity, LdapAuthenticationProvider ldapAuthenticationProvider) throws Exception {
		LOGGER.info("Loading LDAP - AuthenticationManager");
		AuthenticationManagerBuilder builder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
		builder.authenticationProvider(ldapAuthenticationProvider);
		return builder.build();
	}
}
