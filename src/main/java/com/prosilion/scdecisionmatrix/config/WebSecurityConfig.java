package com.prosilion.scdecisionmatrix.config;

import com.prosilion.scdecisionmatrix.repository.security.AppUserAuthUserRepository;
import com.prosilion.scdecisionmatrix.service.security.AuthUserDetailServiceImpl;
import com.prosilion.scdecisionmatrix.service.security.AuthUserDetailsService;
import com.prosilion.scdecisionmatrix.service.AppUserAuthUserService;
import com.prosilion.scdecisionmatrix.service.AppUserService;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
  private static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfig.class);

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf().disable()
        .authorizeHttpRequests((authorize) ->
            authorize.requestMatchers("/register/**").permitAll()
                .requestMatchers("/index").permitAll()
                .requestMatchers("/users/**").hasRole("USER")
        ).formLogin(
            form -> form
                .loginPage("/login")
                .loginProcessingUrl("/loginuser")
                .defaultSuccessUrl("/users")
                .permitAll()
        ).logout(
            logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .permitAll()
        );
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AppUserAuthUserService appUserAuthUserService(AuthUserDetailsService authUserDetailsService,
      AppUserService appUserService, AppUserAuthUserRepository appUserAuthUserRepository) {
    return new AppUserAuthUserService(authUserDetailsService, appUserService,
        appUserAuthUserRepository);
  }

  @Bean
  public AuthUserDetailsService authUserDetailsService(DataSource dataSource) {
    AuthUserDetailsService authUserDetailsService = new AuthUserDetailServiceImpl(dataSource, passwordEncoder());
    return authUserDetailsService;
  }

  @Bean
  WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring().requestMatchers(new AntPathRequestMatcher("/h2-console/**"));
  }
}
