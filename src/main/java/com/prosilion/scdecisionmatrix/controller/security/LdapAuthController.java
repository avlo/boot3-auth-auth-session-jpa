package com.prosilion.scdecisionmatrix.controller.security;

import com.prosilion.scdecisionmatrix.PreExistingUserException;
import com.prosilion.scdecisionmatrix.model.dto.AppUserDto;
import com.prosilion.scdecisionmatrix.model.entity.AppUserAuthUser;
import com.prosilion.scdecisionmatrix.service.security.AuthUserDetailsService;
import com.prosilion.scdecisionmatrix.service.security.AuthUserService;
import java.text.MessageFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Profile({"ldap", "test"})
@Controller
public class LdapAuthController extends AbstractAuthController {
	private static final Logger LOGGER = LoggerFactory.getLogger(LdapAuthController.class);
	private AuthUserDetailsService authUserDetailsService;
	@Autowired
	public LdapAuthController(AuthUserService authUserService, AuthUserDetailsService authUserDetailsService) {
		super(authUserService);
		this.authUserDetailsService = authUserDetailsService;
	}

	@Override
	@PostMapping("/register/save")
	public String registration(@ModelAttribute("user") AppUserDto appUserDto, BindingResult result, Model model) {
		LOGGER.info("LdapAuthController - register user [{}]", appUserDto.getUsername());
		if (!authUserDetailsService.userExists(appUserDto.getUsername())) {
			LOGGER.info("LdapAuthController - user [{}] not found in LDAP", appUserDto.getUsername());
			throw new UsernameNotFoundException(MessageFormat.format("User [{}] does not exist in LDAP", appUserDto.getUsername()));
		}

		LOGGER.info("LdapAuthController - LDAP user [{}] found", appUserDto.getUsername());
		try {
			LOGGER.info("LdapAuthController - create local user [{}]", appUserDto.getUsername());
			AppUserAuthUser appUserAuthUser = authUserService.createUser(appUserDto);
			LOGGER.info("LdapAuthController - created local user [{}]", appUserAuthUser.getAuthUserName());

			if (result.hasErrors()) {
				LOGGER.info("LdapAuthController - Found user [{}] with errors.", appUserAuthUser.getAuthUserName());
				model.addAttribute("user", appUserDto);
				return "/register";
			}

			LOGGER.info("LdapAuthController - successful LDAP auth, send user to login page");
			return "redirect:/register?success";
		} catch (PreExistingUserException e) {
			LOGGER.info("LdapAuthController - User [{}] already exists, re-routing to login page.", appUserDto.getUsername());
			model.addAttribute("user", appUserDto);
			return "/login";
		}
	}
}
