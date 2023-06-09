package com.prosilion.scdecisionmatrix.service;

import com.prosilion.scdecisionmatrix.model.entity.AppUser;
import com.prosilion.scdecisionmatrix.model.dto.AppUserDto;
import com.prosilion.scdecisionmatrix.model.entity.AppUserAuthUser;
import com.prosilion.scdecisionmatrix.repository.security.AppUserAuthUserRepository;
import com.prosilion.scdecisionmatrix.model.entity.security.AuthUserDetails;
import com.prosilion.scdecisionmatrix.service.security.AuthUserDetailsService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * Class instantiated as bean via:
 * @see com.prosilion.scdecisionmatrix.config.WebSecurityConfig#appUserAuthUserService(AuthUserDetailsService, AppUserService, AppUserAuthUserRepository)
 *
 */
public class AppUserAuthUserService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AppUserAuthUserService.class);
	private final AppUserAuthUserRepository appUserAuthUserRepository;
	private final AuthUserDetailsService authUserDetailsService;
	private final AppUserService appUserService;

	public AppUserAuthUserService(AuthUserDetailsService authUserDetailsService,
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

	public AppUserAuthUser getAppUserAuthUser(@NonNull AppUser appUser) {
		return appUserAuthUserRepository.findByAppUserId(appUser.getId()).get();
	}

	public AppUserAuthUser getAppUserAuthUser(@NonNull AuthUserDetails authUserDetails) {
		return appUserAuthUserRepository.findByAuthUserName(authUserDetails.getUsername()).get();
	}

	public List<AppUserAuthUser> getAllAppUsersMappedAuthUsers() {
		return appUserAuthUserRepository.findAll();
	}

	/**
	 * Users for view display
	 * @return list of all app users
	 */
	public List<AppUserDto> getAllAppUsersAsDto() {
		return convertAppUserToDto(getAllAppUsersMappedAuthUsers());
	}

	private List<AppUserDto> convertAppUserToDto(List<AppUserAuthUser> users) {
		return users.stream().map((user) -> mapToUserDto(user)).collect(Collectors.toList());
	}

	private AppUserDto mapToUserDto(AppUserAuthUser appUserAuthUser) {
		AppUserDto userDto = new AppUserDto();
		userDto.setUsername(appUserAuthUser.getAuthUserName());
		return userDto;
	}
}
