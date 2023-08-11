package com.prosilion.scdecisionmatrix.config;

import com.prosilion.scdecisionmatrix.repository.AppUserAuthUserRepository;
import com.prosilion.scdecisionmatrix.service.AppUserAuthUserService;
import com.prosilion.scdecisionmatrix.service.AppUserService;
import com.prosilion.scdecisionmatrix.service.security.AuthUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("jpa")
@Configuration
public class JpaUserConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(JpaUserConfig.class);
	@Bean
	public AppUserAuthUserService appUserAuthUserService(AuthUserDetailsService authUserDetailsService, AppUserService appUserService, AppUserAuthUserRepository appUserAuthUserRepository) {
		LOGGER.info("Loading JPA - AppUserAuthUserService (**candidate for refactor into AuthUserService/AuthUserServiceImpl**");
		return new AppUserAuthUserService(authUserDetailsService, appUserService,
				appUserAuthUserRepository);
	}
}
