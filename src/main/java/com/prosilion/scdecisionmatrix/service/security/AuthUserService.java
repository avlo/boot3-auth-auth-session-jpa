package com.prosilion.scdecisionmatrix.service.security;

import com.prosilion.scdecisionmatrix.model.dto.AppUserDto;
import com.prosilion.scdecisionmatrix.model.entity.AppUser;
import com.prosilion.scdecisionmatrix.model.entity.AppUserAuthUser;
import com.prosilion.scdecisionmatrix.model.entity.security.AuthUserDetails;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;

public interface AuthUserService {

  AppUserAuthUser createUser(@NonNull AppUserDto appUserDto);

  AppUserAuthUser getAppUserAuthUser(@NonNull AppUser appUser);

  AppUserAuthUser getAppUserAuthUser(@NonNull AuthUserDetails authUserDetails);

  List<AppUserDto> getAllAppUsersAsDto();

  List<AppUserAuthUser> getAllAppUsersMappedAuthUsers();

  default List<AppUserDto> convertAppUserToDto(List<AppUserAuthUser> users) {
    return users.stream().map((user) -> mapToUserDto(user)).collect(Collectors.toList());
  }

  default AppUserDto mapToUserDto(AppUserAuthUser appUserAuthUser) {
    AppUserDto userDto = new AppUserDto();
    userDto.setUsername(appUserAuthUser.getAuthUserName());
    return userDto;
  }
}
