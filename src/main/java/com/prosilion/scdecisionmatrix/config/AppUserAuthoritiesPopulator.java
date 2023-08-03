package com.prosilion.scdecisionmatrix.config;

import com.prosilion.scdecisionmatrix.service.security.AuthUserDetailsService;
import java.util.Collection;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AppUserAuthoritiesPopulator implements LdapAuthoritiesPopulator {
	private static final Logger LOGGER = LoggerFactory.getLogger(AppUserAuthoritiesPopulator.class);
	private final AuthUserDetailsService authUserDetailsService;

	@Override
	public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations userData, String username) {
		LOGGER.info("Using custom application user authorities, not LDAP");
		return authUserDetailsService.loadUserByUsername(username).getAuthorities();
	}
}
