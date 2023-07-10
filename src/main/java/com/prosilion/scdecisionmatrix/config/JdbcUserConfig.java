package com.prosilion.scdecisionmatrix.config;

import com.prosilion.scdecisionmatrix.controller.UsersController;
import com.prosilion.scdecisionmatrix.repository.AppUserAuthUserRepository;
import com.prosilion.scdecisionmatrix.service.AppUserService;
import com.prosilion.scdecisionmatrix.service.security.AuthUserService;
import com.prosilion.scdecisionmatrix.service.security.AuthUserServiceImpl;
import com.prosilion.scdecisionmatrix.service.security.jdbc.JdbcAuthUserDetailServiceImpl;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class JdbcUserConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(JdbcUserConfig.class);

	@Bean
	public AuthUserService jdbcUserAuthUserService(JdbcAuthUserDetailServiceImpl jdbcAuthUserDetailServiceImpl, AppUserService appUserService, AppUserAuthUserRepository appUserAuthUserRepository) {
		LOGGER.info("Loading JDBC - AppUserJdbcUserService");
		return new AuthUserServiceImpl(jdbcAuthUserDetailServiceImpl, appUserService, appUserAuthUserRepository);
	}

	@Bean
	public UserDetailsService jdbcAuthUserDetailsService(DataSource dataSource, PasswordEncoder passwordEncoder) {
		LOGGER.info("Loading JDBC - JdbcAuthUserDetailServiceImpl");
		return new JdbcAuthUserDetailServiceImpl(dataSource, passwordEncoder);
	}

	@Bean
	public UsersController jdbcUsersController(AuthUserServiceImpl authUserServiceImpl) {
		return new UsersController(authUserServiceImpl);
	}
}
