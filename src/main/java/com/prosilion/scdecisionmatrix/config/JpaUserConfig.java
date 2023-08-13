package com.prosilion.scdecisionmatrix.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Profile({"jpa", "test"})
@Configuration
public class JpaUserConfig {
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/register/**").permitAll()
				.requestMatchers("/index").permitAll()
				.requestMatchers("/users/**").hasRole("USER")
				.anyRequest().authenticated()
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
}
