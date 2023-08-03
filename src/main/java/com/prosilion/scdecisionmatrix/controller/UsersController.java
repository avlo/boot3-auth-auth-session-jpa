package com.prosilion.scdecisionmatrix.controller;

import com.prosilion.scdecisionmatrix.model.dto.AppUserDto;
import com.prosilion.scdecisionmatrix.service.security.AuthUserService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Bean definition for this class in LdapUserConfig and JdbcUserConfig
 */
public class UsersController {
  private static final Logger LOGGER = LoggerFactory.getLogger(UsersController.class);
  private final AuthUserService authUserService;

  public UsersController(AuthUserService authUserService) {
    this.authUserService = authUserService;
  }

  @GetMapping("/users")
  public String users(Model model) {
    List<AppUserDto> users = authUserService.getAllAppUsersAsDto();
    LOGGER.info("Fetched users: {}", users);
    model.addAttribute("users", users);
    return "users";
  }

}
