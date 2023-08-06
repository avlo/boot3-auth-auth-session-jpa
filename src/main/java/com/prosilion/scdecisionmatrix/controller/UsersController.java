package com.prosilion.scdecisionmatrix.controller;

import com.prosilion.scdecisionmatrix.model.dto.AppUserDto;
import com.prosilion.scdecisionmatrix.service.security.AuthUserService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsersController {
  private static final Logger LOGGER = LoggerFactory.getLogger(UsersController.class);
  private final AuthUserService authUserService;

  @Autowired
  public UsersController(AuthUserService authUserService) {
    this.authUserService = authUserService;
  }

  @PreAuthorize("authenticated")
  @GetMapping("/users")
  public String users(Model model) {
    List<AppUserDto> users = authUserService.getAllAppUsersAsDto();
    LOGGER.info("Fetched users: {}", users);
    model.addAttribute("users", users);
    return "users";
  }

}
