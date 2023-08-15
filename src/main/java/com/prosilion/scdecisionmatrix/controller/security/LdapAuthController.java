package com.prosilion.scdecisionmatrix.controller.security;

import com.prosilion.scdecisionmatrix.model.dto.AppUserDto;
import com.prosilion.scdecisionmatrix.service.security.AuthUserDetailsService;
import com.prosilion.scdecisionmatrix.service.security.AuthUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Profile({"ldap"})
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
		LOGGER.info("LdapAuthController - successful LDAP auth");
		return "redirect:/users";
	}
}
