package com.prosilion.scdecisionmatrix.config;

import com.prosilion.scdecisionmatrix.PreExistingUserException;
import com.prosilion.scdecisionmatrix.model.dto.AppUserDto;
import com.prosilion.scdecisionmatrix.service.security.AuthUserService;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.stereotype.Component;

/**
 * Application authorization/authorities bean serving in place of Ldap authorization/authorities
 * bean in favor of application specific authorization/authorities (user roles).
 */
@Component
public class AppUserAuthoritiesPopulator implements LdapAuthoritiesPopulator {
	private static final Logger LOGGER = LoggerFactory.getLogger(AppUserAuthoritiesPopulator.class);
	private final AuthUserService authUserService;

	@Autowired
	public AppUserAuthoritiesPopulator(AuthUserService authUserService) {
		this.authUserService = authUserService;
	}

	@Override
	public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations userData, String username) {
		LOGGER.info("LDAP authentication successful");
		LOGGER.info("Creating local user & user authorities (NOT using LDAP authorities)");
		AppUserDto appUserDto = new AppUserDto();
		appUserDto.setUsername(username);
		appUserDto.setPassword("NO-OP");
		try {
			authUserService.createUser(appUserDto);
		} catch (PreExistingUserException e) {
			LOGGER.info(e.getMessage());
		}
		return authUserService.getGrantedAuthorities(username);
	}
}