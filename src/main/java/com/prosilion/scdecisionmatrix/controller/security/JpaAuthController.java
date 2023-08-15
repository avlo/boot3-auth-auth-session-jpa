package com.prosilion.scdecisionmatrix.controller.security;

import com.prosilion.scdecisionmatrix.PreExistingUserException;
import com.prosilion.scdecisionmatrix.model.dto.AppUserDto;
import com.prosilion.scdecisionmatrix.model.entity.AppUserAuthUser;
import com.prosilion.scdecisionmatrix.service.security.AuthUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Profile({"jpa", "test"})
@Controller
public class JpaAuthController extends AbstractAuthController {
	private static final Logger LOGGER = LoggerFactory.getLogger(JpaAuthController.class);
	@Autowired
	public JpaAuthController(AuthUserService authUserService) {
		super(authUserService);
	}

	@Override
	@PostMapping("/register/save")
	public String registration(@ModelAttribute("user") AppUserDto appUserDto, BindingResult result, Model model) {
		try {
			AppUserAuthUser appUserAuthUser = authUserService.createUser(appUserDto);
			LOGGER.info("Registered AppUserAuthUser [{}]", appUserAuthUser.getAuthUserName());

			if(result.hasErrors()){
				LOGGER.info("Found user [{}] with errors.", appUserAuthUser.getAuthUserName());
				model.addAttribute("user", appUserDto);
				return "/register";
			}

			return "redirect:/register?success";
		} catch (PreExistingUserException e) {
			LOGGER.info("User [{}] already exists.", appUserDto.getUsername());
			model.addAttribute("user", appUserDto);
			return "/login";
		}
	}
}
