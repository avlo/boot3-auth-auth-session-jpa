package com.prosilion.scdecisionmatrix.config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
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
                .requestMatchers("/users/**")//.hasRole("USER")
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
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public StandardPBEStringEncryptor standardPBEStringEncryptor() {
    StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
    standardPBEStringEncryptor.setAlgorithm("PBEWithMD5AndDES");
    standardPBEStringEncryptor.setPassword("app-encryption-key");
    return standardPBEStringEncryptor;
  }

  @Bean
  WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring().requestMatchers(new AntPathRequestMatcher("/h2-console/**"));
  }

//  @Autowired
//  public void configure(AuthenticationManagerBuilder auth) throws Exception {
//    auth
//        .ldapAuthentication()
//        .userDnPatterns("uid={0},ou=people")
//        .groupSearchBase("ou=groups")
//        .contextSource()
//        .url("ldap://localhost:8389/dc=springframework,dc=org")
//        .and()
//        .passwordCompare()
//        .passwordEncoder(passwordEncoder())
//        .passwordAttribute("userPassword");
//  }
}
