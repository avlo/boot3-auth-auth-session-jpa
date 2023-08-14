package com.prosilion.scdecisionmatrix.service.security.jdbc;

import com.prosilion.scdecisionmatrix.PreExistingUserException;
import com.prosilion.scdecisionmatrix.model.dto.AppUserDto;
import com.prosilion.scdecisionmatrix.model.entity.security.AuthUserDetails;
import com.prosilion.scdecisionmatrix.model.entity.security.AuthUserDetailsImpl;
import com.prosilion.scdecisionmatrix.service.security.AuthUserDetailsService;
import java.text.MessageFormat;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthUserDetailServiceImpl extends JdbcUserDetailsManager implements AuthUserDetailsService {
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthUserDetailServiceImpl.class);
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public AuthUserDetailServiceImpl(DataSource dataSource, PasswordEncoder passwordEncoder) {
    super(dataSource);
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public boolean userExists(String username) {
    return super.userExists(username);
  }

  @Transactional
  @Override
  public AuthUserDetails createAuthUser(AppUserDto appUserDto) throws PreExistingUserException {
    if (userExists(appUserDto.getUsername())) {
      throw new PreExistingUserException(MessageFormat.format("User [{}] already exists", appUserDto.getUsername()));
    }

    UserDetails userDetails = User.withUsername(appUserDto.getUsername()).password(passwordEncoder.encode(
        appUserDto.getPassword())).roles("USER").build();
    super.createUser(new AuthUserDetailsImpl(userDetails));
    return loadUserByUsername(appUserDto.getUsername());
  }

  @Override
  public AuthUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return new AuthUserDetailsImpl(super.loadUserByUsername(username));
  }

  @Override
  public AuthUserDetails loadUserByUserDto(AppUserDto appUserDto) throws UsernameNotFoundException {
    AuthUserDetails authUserDetails = loadUserByUsername(appUserDto.getUsername());
    LOGGER.info("User found");
    return authUserDetails;
  }
}
