package com.prosilion.scdecisionmatrix.service.security.ldap;

import com.prosilion.scdecisionmatrix.model.dto.AppUserDto;
import com.prosilion.scdecisionmatrix.model.entity.security.AuthUserDetails;
import com.prosilion.scdecisionmatrix.service.security.jdbc.AuthUserDetailServiceImpl;
import java.util.Objects;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class LdapAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
	// TODO: try ActiveDirectoryLdapAuthenticationProvider
	private static final Logger LOGGER = LoggerFactory.getLogger(LdapAuthenticationProvider.class);
	private final ActiveDirectoryLdapTemplate activeDirectoryLdapTemplate;
	private final AuthUserDetailServiceImpl authUserDetailsService;

	@Autowired
	public LdapAuthenticationProvider(ActiveDirectoryLdapTemplate activeDirectoryLdapTemplate, AuthUserDetailServiceImpl authUserDetailsService) {
		this.activeDirectoryLdapTemplate = activeDirectoryLdapTemplate;
		this.authUserDetailsService = authUserDetailsService;
	}

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
	}

	@Override
	protected AuthUserDetails retrieveUser(@NonNull String username, @NonNull UsernamePasswordAuthenticationToken authToken) throws AuthenticationException {
		LOGGER.info("LDAP AuthenticationProvider retrieving user...");
		if (activeDirectoryLdapTemplate.authenticate(username, authToken.getCredentials().toString())) {
			LOGGER.info("Should only reach here if user not in LDAP");
			throw new BadCredentialsException("User does not exist in LDAP.");
		}

		LOGGER.info("Checking AuthUserDetails from application DB...");
		AuthUserDetails authUserDetails = authUserDetailsService.loadUserByUsername(username);
		if (!Objects.isNull(authUserDetails.getUser())) {
			LOGGER.info("Application user found, returning AuthUserDetails");
			return authUserDetails;
		}

		// below should trigger redirect to user registration page
		LOGGER.info("Application User not found, creating new AuthUser");
		AppUserDto appUserDto = new AppUserDto(111L, username, authToken.getCredentials().toString());
		authUserDetailsService.createAuthUser(appUserDto);
		LOGGER.info("New AuthUser successfully created");
		return authUserDetailsService.loadUserByUsername(username);
	}
}
