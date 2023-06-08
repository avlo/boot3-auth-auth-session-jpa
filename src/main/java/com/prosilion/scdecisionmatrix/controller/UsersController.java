package com.prosilion.scdecisionmatrix.controller;

import com.prosilion.scdecisionmatrix.dto.AppUserDto;
import com.prosilion.scdecisionmatrix.service.AppUserAuthUserService;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsersController {
  private static final Logger LOGGER = LoggerFactory.getLogger(UsersController.class);
  private final AppUserAuthUserService appUserAuthUserService;

  @Autowired
  public UsersController(AppUserAuthUserService appUserAuthUserService) {
    this.appUserAuthUserService = appUserAuthUserService;
  }

  @GetMapping("/users")
  public String users(Model model) {
    List<AppUserDto> users = appUserAuthUserService.getAllAppUsersAsDto();
    LOGGER.info("Fetched users: {}", users);
    model.addAttribute("users", users);
    return "users";
  }

}
