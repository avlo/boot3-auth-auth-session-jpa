package com.prosilion.scdecisionmatrix.service.security;

import com.prosilion.scdecisionmatrix.model.dto.AppUserDto;
import com.prosilion.scdecisionmatrix.model.entity.AppUser;
import com.prosilion.scdecisionmatrix.model.entity.AppUserAuthUser;
import com.prosilion.scdecisionmatrix.model.entity.security.AuthUserDetails;
import com.prosilion.scdecisionmatrix.repository.AppUserAuthUserRepository;
import com.prosilion.scdecisionmatrix.service.AppUserService;
import java.util.List;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public class AuthUserServiceImpl implements AuthUserService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthUserServiceImpl.class);
	private final AppUserAuthUserRepository appUserAuthUserRepository;
	private final AuthUserDetailsService authUserDetailsService;
	private final AppUserService appUserService;

	public AuthUserServiceImpl(AuthUserDetailsService authUserDetailsService,
			AppUserService appUserService, AppUserAuthUserRepository appUserAuthUserRepository) {
		this.authUserDetailsService = authUserDetailsService;
		this.appUserService = appUserService;
		this.appUserAuthUserRepository = appUserAuthUserRepository;
	}

	@Transactional
	public AppUserAuthUser createUser(@NonNull AppUserDto appUserDto) {
		AuthUserDetails savedAuthUserDetails = authUserDetailsService.loadUserByUserDto(appUserDto);
		AppUser appUser = appUserService.save(new AppUser());
		AppUserAuthUser appUserAuthUser = new AppUserAuthUser(appUser.getId(), savedAuthUserDetails.getUsername());
		return appUserAuthUserRepository.saveAndFlush(appUserAuthUser);
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

	/**
	 * Users for view display
	 * @return list of all app users
	 */
	@Override
	public List<AppUserDto> getAllAppUsersAsDto() {
		return convertAppUserToDto(getAllAppUsersMappedAuthUsers());
	}
}
