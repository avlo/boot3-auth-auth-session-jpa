package com.prosilion.scdecisionmatrix.config;

import com.prosilion.scdecisionmatrix.controller.UsersController;
import com.prosilion.scdecisionmatrix.repository.AppUserAuthUserRepository;
import com.prosilion.scdecisionmatrix.service.AppUserService;
import com.prosilion.scdecisionmatrix.service.security.AuthUserService;
import com.prosilion.scdecisionmatrix.service.security.AuthUserServiceImpl;
import com.prosilion.scdecisionmatrix.service.security.jdbc.AuthUserDetailServiceImpl;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class JdbcUserConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(JdbcUserConfig.class);

	@Bean
	public AuthUserService jdbcUserAuthUserService(
			AuthUserDetailServiceImpl authUserDetailServiceImpl, AppUserService appUserService, AppUserAuthUserRepository appUserAuthUserRepository) {
		LOGGER.info("Loading JDBC - AppUserJdbcUserService");
		return new AuthUserServiceImpl(authUserDetailServiceImpl, appUserService, appUserAuthUserRepository);
	}

	@Bean
	public UserDetailsService jdbcAuthUserDetailsService(DataSource dataSource, BCryptPasswordEncoder bCryptPasswordEncoder) {
		LOGGER.info("Loading JDBC - AuthUserDetailServiceImpl");
		return new AuthUserDetailServiceImpl(dataSource, bCryptPasswordEncoder);
	}

	@Bean
	public UsersController authUserController(AuthUserService authUserService) {
		return new UsersController(authUserService);
	}
}
