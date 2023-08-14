package com.prosilion.scdecisionmatrix.service.security;

import com.prosilion.scdecisionmatrix.model.dto.AppUserDto;
import com.prosilion.scdecisionmatrix.model.entity.AppUser;
import com.prosilion.scdecisionmatrix.model.entity.AppUserAuthUser;
import com.prosilion.scdecisionmatrix.model.entity.security.AuthUserDetails;
import com.prosilion.scdecisionmatrix.repository.AppUserAuthUserRepository;
import com.prosilion.scdecisionmatrix.service.AppUserService;
import java.util.Collection;
import java.util.List;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthUserServiceImpl implements AuthUserService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthUserServiceImpl.class);
	private final AppUserAuthUserRepository appUserAuthUserRepository;
	private final AuthUserDetailsService authUserDetailsService;
	private final AppUserService appUserService;

	@Autowired
	public AuthUserServiceImpl(AuthUserDetailsService authUserDetailsService,
			AppUserService appUserService, AppUserAuthUserRepository appUserAuthUserRepository) {
		this.authUserDetailsService = authUserDetailsService;
		this.appUserService = appUserService;
		this.appUserAuthUserRepository = appUserAuthUserRepository;
	}

	@Override
	public boolean userExists(String userName) {
		return authUserDetailsService.userExists(userName);
	}

	@Transactional
	@Override
	public AppUserAuthUser createUser(@NonNull AppUserDto appUserDto) {
		AuthUserDetails savedAuthUserDetails = getAppUserAuthUser(appUserDto);
		AppUser appUser = appUserService.save(new AppUser());
		AppUserAuthUser appUserAuthUser = new AppUserAuthUser(appUser.getId(), savedAuthUserDetails.getUsername());
		return appUserAuthUserRepository.saveAndFlush(appUserAuthUser);
	}

	@Override
	public AuthUserDetails getAppUserAuthUser(@NonNull AppUserDto appUserDto) {
		return authUserDetailsService.loadUserByUserDto(appUserDto);
	}

	@Override
	public AppUserAuthUser getAppUserAuthUser(@NonNull AppUser appUser) {
		return appUserAuthUserRepository.findByAppUserId(appUser.getId()).get();
	}

	@Override
	public AppUserAuthUser getAppUserAuthUser(@NonNull AuthUserDetails authUserDetails) {
		return appUserAuthUserRepository.findByAuthUserName(authUserDetails.getUsername()).get();
	}

	@Override
	public List<AppUserAuthUser> getAllAppUsersMappedAuthUsers() {
		return appUserAuthUserRepository.findAll();
	}

	@Override
	public Collection<? extends GrantedAuthority> getGrantedAuthorities(@NonNull String username) {
		return authUserDetailsService.loadUserByUsername(username).getAuthorities();
	}
	/**
	 * Users for view display
	 * @return list of all app users
	 */
	@Override
	public List<AppUserDto> getAllAppUsersAsDto() {
		return convertAppUserToDto(getAllAppUsersMappedAuthUsers());
	}
}
