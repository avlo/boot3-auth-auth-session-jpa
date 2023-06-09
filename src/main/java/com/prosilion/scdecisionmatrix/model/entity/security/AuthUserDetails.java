package com.prosilion.scdecisionmatrix.model.entity.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface AuthUserDetails extends UserDetails {
  UserDetails getUser();
}
