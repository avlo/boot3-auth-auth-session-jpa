package com.prosilion.scdecisionmatrix.service.security.ldap;

import com.prosilion.scdecisionmatrix.model.dto.AppUserDto;
import com.prosilion.scdecisionmatrix.model.entity.security.AuthUserDetails;
import com.prosilion.scdecisionmatrix.model.entity.security.JdbcAuthUserDetailsImpl;
import com.prosilion.scdecisionmatrix.model.entity.security.LdapAuthUserDetailsImpl;
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
public class LdapAuthUserDetailServiceImpl extends JdbcUserDetailsManager implements AuthUserDetailsService {
  private static final Logger LOGGER = LoggerFactory.getLogger(LdapAuthUserDetailServiceImpl.class);

  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public LdapAuthUserDetailServiceImpl(DataSource dataSource, BCryptPasswordEncoder bCryptPasswordEncoder) {
    super(dataSource);
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }
  @Transactional
  @Override
  public void createAuthUser(AppUserDto appUserDto) {
    LOGGER.info("Creating LDAP AuthUser, method contents currently not implemented");
    UserDetails userDetails = User.withUsername(appUserDto.getUsername()).password(bCryptPasswordEncoder.encode(
        appUserDto.getPassword())).roles("USER").build();
    AuthUserDetails authUserDetails = new JdbcAuthUserDetailsImpl(userDetails);
    super.createUser(authUserDetails);
  }

  @Override
  public AuthUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    LOGGER.info("Loading LDAP user by username via LdapAuthenticationProvider");
    System.out.println("55555555555555555555");
    System.out.println("55555555555555555555");
    return new LdapAuthUserDetailsImpl(super.loadUserByUsername(username));
  }

  @Override
  public AuthUserDetails loadUserByUserDto(AppUserDto appUserDto) {
    LOGGER.info("Loading LDAP user by UserDto");
    try {
      AuthUserDetails authUserDetails = loadUserByUsername(appUserDto.getUsername());
      LOGGER.info("User found");
      return authUserDetails;
    } catch (UsernameNotFoundException u) {
      LOGGER.info("User not found.");
      // TODO: properly handle below
      throw new RuntimeException(u);
    }
  }
}
