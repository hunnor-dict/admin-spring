package net.hunnor.dict.admin.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

@Configuration
@EnableConfigurationProperties(SecurityUsersProperties.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private SecurityUsersProperties securityUsersProperties;

  @Autowired
  private SecurityProperties securityProperties;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    super.configure(http);
    http.csrf().disable();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    List<UserDetails> userDetails = new ArrayList<>();

    if (StringUtils.hasText(securityProperties.getUser().getName())) {
      String[] roles = securityProperties.getUser().getRoles().toArray(new String[0]);
      if (roles.length == 0) {
        roles = new String[] {"USER"};
      }
      userDetails.add(
          toUser(
              securityProperties.getUser().getName(),
              securityProperties.getUser().getPassword(),
              roles,
              passwordEncoder));
    }

    for (SecurityUsersProperties.UserConfig configuredUser : securityUsersProperties.getUsers()) {
      if (!StringUtils.hasText(configuredUser.getUsername())
          || !StringUtils.hasText(configuredUser.getPassword())) {
        continue;
      }
      List<String> roles = configuredUser.getRoles();
      String[] rolesArray;
      if (roles.isEmpty()) {
        rolesArray = new String[] {"USER"};
      } else {
        rolesArray = roles.toArray(new String[0]);
      }
      userDetails.add(
          toUser(
              configuredUser.getUsername(),
              configuredUser.getPassword(),
              rolesArray,
              passwordEncoder));
    }

    if (!userDetails.isEmpty()) {
      auth.inMemoryAuthentication().withUser(userDetails.get(0));
      for (int index = 1; index < userDetails.size(); index++) {
        auth.inMemoryAuthentication().withUser(userDetails.get(index));
      }
    }
  }

  private UserDetails toUser(
      String username,
      String password,
      String[] roles,
      PasswordEncoder passwordEncoder) {
    String storedPassword = password;
    if (!password.startsWith("{")) {
      storedPassword = passwordEncoder.encode(password);
    }
    UserBuilder builder = User.withUsername(username).password(storedPassword);
    builder.roles(roles);
    return builder.build();
  }

}
