package com.prosilion.scdecisionmatrix.service.security;

import com.prosilion.scdecisionmatrix.model.dto.AppUserDto;
import com.prosilion.scdecisionmatrix.model.entity.security.AuthUserDetails;
import com.prosilion.scdecisionmatrix.model.entity.security.AuthUserDetailsImpl;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.transaction.annotation.Transactional;

public class AuthUserDetailServiceImpl extends JdbcUserDetailsManager implements AuthUserDetailsService {
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthUserDetailServiceImpl.class);
  private final PasswordEncoder passwordEncoder;

  public AuthUserDetailServiceImpl(DataSource dataSource, PasswordEncoder passwordEncoder) {
    super(dataSource);
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  @Override
  public void createUser(AppUserDto appUserDto) {
    UserDetails userDetails = User.withUsername(appUserDto.getUsername()).password(passwordEncoder.encode(
        appUserDto.getPassword())).roles("USER").build();
    AuthUserDetails authUserDetails = new AuthUserDetailsImpl(userDetails);
    super.createUser(authUserDetails);
  }

  @Override
  public AuthUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return new AuthUserDetailsImpl(super.loadUserByUsername(username));
  }

  @Override
  public AuthUserDetails loadUserByUserDto(AppUserDto appUserDto) {
    try {
      AuthUserDetails authUserDetails = loadUserByUsername(appUserDto.getUsername());
      LOGGER.info("User found");
      return authUserDetails;
    } catch (UsernameNotFoundException u) {
      LOGGER.info("User not found, try to create new user");
      createUser(appUserDto);
      AuthUserDetails authUserDetails= loadUserByUsername(appUserDto.getUsername());
      LOGGER.info("Created new user");
      return authUserDetails;
    }
  }
}
