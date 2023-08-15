package com.prosilion.scdecisionmatrix.controller.security;

import com.prosilion.scdecisionmatrix.model.dto.AppUserDto;
import com.prosilion.scdecisionmatrix.service.security.AuthUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

public abstract class AbstractAuthController {
  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAuthController.class);
  protected final AuthUserService authUserService;

  public AbstractAuthController(AuthUserService authUserService) {
    this.authUserService = authUserService;
  }

  @GetMapping("/index")
  public String home() {
    return "index";
  }

  @GetMapping("/login")
  public String login() {
    return "login";
  }

  @GetMapping("/register")
  public String showRegistrationForm(Model model) {
    AppUserDto user = new AppUserDto();
    model.addAttribute("user", user);
    return "register";
  }

  @PostMapping("/register/save")
  public abstract String registration(@ModelAttribute("user") AppUserDto appUserDto, BindingResult result, Model model);
}
