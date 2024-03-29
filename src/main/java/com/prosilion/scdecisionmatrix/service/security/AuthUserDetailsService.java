package com.prosilion.scdecisionmatrix.service.security;

import com.prosilion.scdecisionmatrix.PreExistingUserException;
import com.prosilion.scdecisionmatrix.model.dto.AppUserDto;
import com.prosilion.scdecisionmatrix.model.entity.security.AuthUserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthUserDetailsService extends UserDetailsService {
	boolean userExists(String userName);
	AuthUserDetails createAuthUser(AppUserDto appUserDto) throws PreExistingUserException;
	AuthUserDetails loadUserByUsername(String username);
	AuthUserDetails loadUserByUserDto(AppUserDto appUserDto);
}
