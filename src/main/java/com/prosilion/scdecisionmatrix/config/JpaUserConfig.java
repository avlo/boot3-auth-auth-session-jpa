package com.prosilion.scdecisionmatrix.config;

import com.prosilion.scdecisionmatrix.repository.AppUserAuthUserRepository;
import com.prosilion.scdecisionmatrix.service.AppUserAuthUserService;
import com.prosilion.scdecisionmatrix.service.AppUserService;
import com.prosilion.scdecisionmatrix.service.security.AuthUserDetailsService;
import com.prosilion.scdecisionmatrix.service.security.jdbc.AuthUserDetailServiceImpl;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Profile("jpa")
@Configuration
public class JpaUserConfig {
	@Bean
	public AppUserAuthUserService appUserAuthUserService(AuthUserDetailsService authUserDetailsService,
			AppUserService appUserService, AppUserAuthUserRepository appUserAuthUserRepository) {
		return new AppUserAuthUserService(authUserDetailsService, appUserService,
				appUserAuthUserRepository);
	}

	@Bean
	public AuthUserDetailsService authUserDetailsService(DataSource dataSource, PasswordEncoder passwordEncoder) {
		return new AuthUserDetailServiceImpl(dataSource, passwordEncoder);
	}
}
