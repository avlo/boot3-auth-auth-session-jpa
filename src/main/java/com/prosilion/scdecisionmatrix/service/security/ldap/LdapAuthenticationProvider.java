package com.prosilion.scdecisionmatrix.service.security.ldap;

import com.google.common.base.Strings;
import com.prosilion.scdecisionmatrix.model.dto.AppUserDto;
import com.prosilion.scdecisionmatrix.model.entity.security.AuthUserDetails;
import javax.naming.directory.SearchControls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class LdapAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(LdapAuthenticationProvider.class);
	private static final String LDAP_BASE_FOR_DN_USER = "DC=mfad,DC=mfroot,DC=org";
	public static final String OBJECT_CLASS = "objectClass";
	public static final String USER = "user";
	public static final String CN = "cn";

	private final ADLdapTemplate adLdapTemplate;
	private final LdapAuthUserDetailServiceImpl authUserDetailsService;

	@Autowired
	public LdapAuthenticationProvider(ADLdapTemplate adLdapTemplate, LdapAuthUserDetailServiceImpl authUserDetailsService) {
		this.adLdapTemplate = adLdapTemplate;
		this.authUserDetailsService = authUserDetailsService;
	}

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
	}

	@Override
	protected AuthUserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authToken) throws AuthenticationException {
		LOGGER.info("LDAP AuthenticationProvider retrieving user...");
		final String password = Strings.emptyToNull((String)authToken.getCredentials());
		if(username == null || password == null){
			throw new BadCredentialsException("The username and password supplied are not valid.");
		}

		if(ldapAuthenticate(username, password)) {
			AuthUserDetails authUserDetails = authUserDetailsService.loadUserByUsername(username);
			if (authUserDetails.getUser() != null) {
				return authUserDetails;
			}
			AppUserDto appUserDto = new AppUserDto(111L, username, password);
			authUserDetailsService.createAuthUser(appUserDto);

		}
		throw new BadCredentialsException("The user does not exist in LDAP.");
	}

	private boolean ldapAuthenticate(final String lanId, String password) {
		LOGGER.info("Authenticating via LDAP");
		try{
			boolean result = adLdapTemplate.authenticate(LDAP_BASE_FOR_DN_USER, getAndFilter(lanId).toString(), password);
			LOGGER.info(result ? "LDAP Authentication SUCCESS" : "LDAP Authentication FAILED");
			return result;
		} catch(Exception e){
			LOGGER.info("LDAP Authentication exception thrown");
			LOGGER.info("LDAP was not configured properly.", e);
			return false;
		}
	}

	private static AndFilter getAndFilter(String lanId) {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter(OBJECT_CLASS, USER));
		filter.and(new EqualsFilter(CN, lanId));
		return filter;
	}

	private static SearchControls getSearchControls() {
		SearchControls controls = new SearchControls();
		controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		String[] attrIDs = { "sn", "givenname", "initials", CN, "uid", "l", "memberOf"};
		controls.setReturningAttributes(attrIDs);
		return controls;
	}
}
