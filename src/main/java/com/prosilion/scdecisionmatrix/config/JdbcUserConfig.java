package com.prosilion.scdecisionmatrix.config;

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
	public UserDetailsService userDetailsService(DataSource dataSource, BCryptPasswordEncoder bCryptPasswordEncoder) {
		LOGGER.info("Loading JDBC - UserDetailService");
		return new AuthUserDetailServiceImpl(dataSource, bCryptPasswordEncoder);
	}
}
