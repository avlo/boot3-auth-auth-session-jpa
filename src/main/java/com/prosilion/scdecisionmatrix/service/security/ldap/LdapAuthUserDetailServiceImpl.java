package com.prosilion.scdecisionmatrix.service.security.ldap;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import com.prosilion.scdecisionmatrix.model.dto.AppUserDto;
import com.prosilion.scdecisionmatrix.model.entity.security.AuthUserDetails;
import com.prosilion.scdecisionmatrix.model.entity.security.LdapAuthUserDetailsImpl;
import com.prosilion.scdecisionmatrix.service.security.AuthUserDetailsService;
import java.util.List;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.userdetails.LdapUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LdapAuthUserDetailServiceImpl extends LdapUserDetailsManager implements AuthUserDetailsService {
  private static final Logger LOGGER = LoggerFactory.getLogger(LdapAuthUserDetailServiceImpl.class);
  private final LdapTemplate ldapTemplate;
  @Autowired
  public LdapAuthUserDetailServiceImpl(ContextSource contextSource, LdapTemplate ldapTemplate) {
    super(contextSource);
    this.ldapTemplate = ldapTemplate;
    System.out.println("111111111111111111");
    System.out.println("111111111111111111");
    getAllPersonNames();
    System.out.println("111111111111111111");
    System.out.println("111111111111111111");
  }

  public List<String> getAllPersonNames() {
    List<String> users = ldapTemplate.search(
        query().where("objectclass").is("person"),
        new AttributesMapper<String>() {
          public String mapFromAttributes(Attributes attrs)
              throws NamingException {
            return (String) attrs.get("cn").get();
          }
        });
    System.out.println(users);
    return users;
  }

  @Transactional
  @Override
  public void createAuthUser(AppUserDto appUserDto) {
//    UserDetails userDetails = User.withUsername(appUserDto.getUsername()).password(passwordEncoder.encode(
//        appUserDto.getPassword())).roles("USER").build();
//    AuthUserDetails authUserDetails = new JdbcAuthUserDetailsImpl(userDetails);
//    super.createUser(authUserDetails);
  }

  @Override
  public AuthUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    System.out.println("22222222222222222222");
    System.out.println("22222222222222222222");
    return new LdapAuthUserDetailsImpl(super.loadUserByUsername(username));
  }

  @Override
  public AuthUserDetails loadUserByUserDto(AppUserDto appUserDto) {
    try {
      AuthUserDetails authUserDetails = loadUserByUsername(appUserDto.getUsername());
      LOGGER.info("User found");
      return authUserDetails;
    } catch (UsernameNotFoundException u) {
      LOGGER.info("User not found.");
      // TODO: properly handle below
      throw new RuntimeException(u);
    }
  }
}
