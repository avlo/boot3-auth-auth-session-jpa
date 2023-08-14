package com.prosilion.scdecisionmatrix.config;

import com.prosilion.scdecisionmatrix.service.security.AuthUserDetailsService;
import com.prosilion.scdecisionmatrix.service.security.jdbc.AuthUserDetailServiceImpl;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Profile({"!ldap"})
@Configuration
@EnableWebSecurity
public class JpaSecurityConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(JpaSecurityConfig.class);
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		LOGGER.info("Loading JPA - Endpoint authorization configuration");
		http.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/register/**").permitAll()
				.requestMatchers("/index").permitAll()
				.requestMatchers("/users/**").hasRole("USER")
				.anyRequest().authenticated() // anyRequest() defines a rule chain for any request which did not match the previous rules
		).formLogin(form -> form
				.loginPage("/login")
				.loginProcessingUrl("/loginuser")
				.defaultSuccessUrl("/users")
				.permitAll()
		).logout(logout -> logout
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.permitAll()
		);
		return http.build();
	}

	@Bean
	public AuthUserDetailsService authUserDetailsService(DataSource dataSource, PasswordEncoder passwordEncoder) {
		LOGGER.info("Loading JPA - AuthUserDetailsService");
		return new AuthUserDetailServiceImpl(dataSource, passwordEncoder);
	}
}
