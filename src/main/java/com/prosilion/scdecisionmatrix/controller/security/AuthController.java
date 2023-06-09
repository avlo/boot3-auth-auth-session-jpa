package com.prosilion.scdecisionmatrix.controller.security;

import com.prosilion.scdecisionmatrix.model.dto.AppUserDto;
import com.prosilion.scdecisionmatrix.model.entity.AppUserAuthUser;
import com.prosilion.scdecisionmatrix.service.AppUserAuthUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
	private final AppUserAuthUserService appUserAuthUserService;

	@Autowired
	public AuthController(AppUserAuthUserService appUserAuthUserService) {
		this.appUserAuthUserService = appUserAuthUserService;
	}

	@GetMapping("/index")
	public String home(){
		return "index";
	}

	@GetMapping("/login")
	public String login(){
		return "login";
	}

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		AppUserDto user = new AppUserDto();
		model.addAttribute("user", user);
		return "register";
	}

	@PostMapping("/register/save")
	public String registration(@ModelAttribute("user") AppUserDto appUserDto, BindingResult result, Model model){
		AppUserAuthUser appUserAuthUser = appUserAuthUserService.createUser(appUserDto);
		LOGGER.info("Registered AppUserAuthUser {}", appUserAuthUser.getAuthUserName());

		if(result.hasErrors()){
			model.addAttribute("user", appUserDto);
			return "/register";
		}

		return "redirect:/register?success";
	}
}
