package com.prosilion.scdecisionmatrix.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Profile({"ldap"})
@Configuration
@EnableWebSecurity
public class LdapSecurityConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(LdapSecurityConfig.class);
	public static final String OBJECT_CLASS = "objectClass";
	public static final String USER_SEARCH_FILTER = "{0}";
	public static final String DIST_VARIABLE = "cn";
	public static final String USER = "user";
	@Value("${ldap.search.base}")
	private String ldapSearchBase;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		LOGGER.info("Loading LDAP - Endpoint authorization configuration");
		http.authorizeHttpRequests(authorize -> authorize
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
	/**
	 * Bean loaded by Spring Boot Security (6.1.2 as of this writing) specifying both authentication and authorization/authorities beans.
	 * It's worth noting 2nd parameter @see com.prosilion.scdecisionmatrix.config.AppUserAuthoritiesPopulator overrides usual Ldap
	 * authorities mechanism in favor of one supplied by the application itself.
	 * @param authenticator
	 * @param authoritiesPopulator
	 * @return
	 */
	@Bean
	AuthenticationProvider ldapAuthenticationProvider(LdapAuthenticator authenticator, AppUserAuthoritiesPopulator authoritiesPopulator) {
		LOGGER.info("Loading LDAP - Authentication Provider (LdapAuthenticationProvider)");
		return new LdapAuthenticationProvider(authenticator, authoritiesPopulator);
	}

	@Bean
	LdapAuthenticator ldapAuthenticator(BaseLdapPathContextSource baseLdapPathContextSource, FilterBasedLdapUserSearch filterBasedLdapUserSearch) {
		LOGGER.info("Loading LDAP - Authenticator (BindAuthenticator extends AbstractLdapAuthenticator implements LdapAuthenticator)");
		BindAuthenticator authenticator = new BindAuthenticator(baseLdapPathContextSource);
		authenticator.setUserSearch(filterBasedLdapUserSearch);
		authenticator.afterPropertiesSet();
		return authenticator;
	}

	@Bean
	FilterBasedLdapUserSearch filterBasedLdapUserSearch(BaseLdapPathContextSource baseLdapPathContextSource) {
		LOGGER.info("Loading LDAP - User Search (FilterBasedLdapUserSearch implements LdapUserSearch)");
		return new FilterBasedLdapUserSearch(ldapSearchBase, getAndFilter(USER_SEARCH_FILTER), baseLdapPathContextSource);
	}

//	@Bean
//	AbstractAuthController authController(AuthUserService authUserService, AuthUserDetailsService authUserDetailsService) {
//		return new LdapAuthController(authUserService, authUserDetailsService);
//	}

	private static String getAndFilter(String distValue) {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter(OBJECT_CLASS, USER));
		filter.and(new EqualsFilter(DIST_VARIABLE, distValue));
		return filter.toString();
	}
}
