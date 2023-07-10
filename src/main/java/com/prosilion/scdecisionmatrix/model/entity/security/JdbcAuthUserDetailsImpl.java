package com.prosilion.scdecisionmatrix.model.entity.security;

import java.io.Serializable;
import java.util.Collection;
import lombok.NonNull;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Authentication & authorization user, directly bound to Spring Security.
 * If you'd like a customizable user, use:
 * @see com.prosilion.scdecisionmatrix.model.entity.AppUser
 *
 * Note: Spring Security using JPA maps this class to "USERS" DB table.
 */
@Scope("session")
public class JdbcAuthUserDetailsImpl implements AuthUserDetails, Serializable {

  final private UserDetails user;
  public JdbcAuthUserDetailsImpl(@NonNull UserDetails user) {
    this.user = user;
  }

  @Override
  public UserDetails getUser() {
    return user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return user.getAuthorities();
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public String toString() {
    return "JdbcAuthUserDetailsImpl{" +
        "user=" + user +
        '}';
  }
}
