package com.prosilion.scdecisionmatrix.service.security.jdbc;

import com.prosilion.scdecisionmatrix.model.dto.AppUserDto;
import com.prosilion.scdecisionmatrix.model.entity.security.AuthUserDetails;
import com.prosilion.scdecisionmatrix.model.entity.security.JdbcAuthUserDetailsImpl;
import com.prosilion.scdecisionmatrix.service.security.AuthUserDetailsService;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthUserDetailServiceImpl extends JdbcUserDetailsManager implements AuthUserDetailsService {
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthUserDetailServiceImpl.class);
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public AuthUserDetailServiceImpl(DataSource dataSource, BCryptPasswordEncoder bCryptPasswordEncoder) {
    super(dataSource);
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Transactional
  @Override
  public void createAuthUser(AppUserDto appUserDto) {
    UserDetails userDetails = User.withUsername(appUserDto.getUsername()).password(bCryptPasswordEncoder.encode(
        appUserDto.getPassword())).roles("USER").build();
    AuthUserDetails authUserDetails = new JdbcAuthUserDetailsImpl(userDetails);
    super.createUser(authUserDetails);
  }

  @Override
  public AuthUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return new JdbcAuthUserDetailsImpl(super.loadUserByUsername(username));
  }

  @Override
  public AuthUserDetails loadUserByUserDto(AppUserDto appUserDto) {
    try {
      AuthUserDetails authUserDetails = loadUserByUsername(appUserDto.getUsername());
      LOGGER.info("User found");
      return authUserDetails;
    } catch (UsernameNotFoundException u) {
      LOGGER.info("User not found, try to create new user");
      createAuthUser(appUserDto);
      AuthUserDetails authUserDetails= loadUserByUsername(appUserDto.getUsername());
      LOGGER.info("Created new user");
      return authUserDetails;
    }
  }
}
