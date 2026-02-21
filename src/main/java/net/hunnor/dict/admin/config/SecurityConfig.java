package net.hunnor.dict.admin.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.StringUtils;

@Configuration
@EnableConfigurationProperties(SecurityUsersProperties.class)
public class SecurityConfig {

  private final List<ConfiguredUser> configuredUsers;

  private final SecurityProperties securityProperties;

  private final String rememberMeKey;

  private final int rememberMeValiditySeconds;

  private final boolean rememberMeSecureCookie;

  /**
   * Creates security configuration with injected user sources.
   *
   * @param securityUsersProperties custom configured users
   * @param securityProperties Spring Boot default security user properties
   */
  public SecurityConfig(
      SecurityUsersProperties securityUsersProperties,
      SecurityProperties securityProperties,
      @Value("${net.hunnor.dict.admin.security.remember-me.key:admin-spring-remember-me-key}")
          String rememberMeKey,
      @Value("${net.hunnor.dict.admin.security.remember-me.validity-seconds:2592000}")
          int rememberMeValiditySeconds,
      @Value("${net.hunnor.dict.admin.security.remember-me.secure-cookie:false}")
          boolean rememberMeSecureCookie) {
    this.configuredUsers = copyConfiguredUsers(securityUsersProperties.getUsers());
    this.securityProperties = securityProperties;
    this.rememberMeKey = rememberMeKey;
    this.rememberMeValiditySeconds = rememberMeValiditySeconds;
    this.rememberMeSecureCookie = rememberMeSecureCookie;
  }

  /**
   * Builds the HTTP security filter chain.
   *
   * @param http HTTP security builder
   * @return configured security filter chain
   * @throws Exception if the security chain cannot be built
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests((requests) -> requests.anyRequest().authenticated())
        .formLogin(Customizer.withDefaults())
      .rememberMe(
        (rememberMe) ->
          rememberMe
            .key(rememberMeKey)
            .alwaysRemember(true)
            .rememberMeCookieName("ADMIN_REMEMBER_ME")
            .useSecureCookie(rememberMeSecureCookie)
            .tokenValiditySeconds(rememberMeValiditySeconds))
        .httpBasic(Customizer.withDefaults())
      .logout(
        (logout) -> logout.deleteCookies("JSESSIONID", "ADMIN_REMEMBER_ME"))
        .csrf((csrf) -> csrf.disable());
    return http.build();
  }

  /**
   * Builds an in-memory user details service from configured users.
   *
   * @return in-memory user details service
   */
  @Bean
  public UserDetailsService userDetailsService() {
    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    List<UserDetails> userDetails = new ArrayList<>();

    if (StringUtils.hasText(securityProperties.getUser().getName())
        && StringUtils.hasText(securityProperties.getUser().getPassword())) {
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

    for (ConfiguredUser configuredUser : configuredUsers) {
      if (!StringUtils.hasText(configuredUser.username)
          || !StringUtils.hasText(configuredUser.password)) {
        continue;
      }
      List<String> roles = configuredUser.roles;
      String[] rolesArray;
      if (roles.isEmpty()) {
        rolesArray = new String[] {"USER"};
      } else {
        rolesArray = roles.toArray(new String[0]);
      }
      userDetails.add(
          toUser(
              configuredUser.username,
              configuredUser.password,
              rolesArray,
              passwordEncoder));
    }

    if (userDetails.isEmpty()) {
      throw new IllegalStateException("No valid security users configured");
    }

    return new InMemoryUserDetailsManager(userDetails);
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

  private List<ConfiguredUser> copyConfiguredUsers(List<SecurityUsersProperties.UserConfig> users) {
    List<ConfiguredUser> copiedUsers = new ArrayList<>();
    for (SecurityUsersProperties.UserConfig user : users) {
      copiedUsers.add(
          new ConfiguredUser(user.getUsername(), user.getPassword(), List.copyOf(user.getRoles())));
    }
    return List.copyOf(copiedUsers);
  }

  private static class ConfiguredUser {

    private final String username;

    private final String password;

    private final List<String> roles;

    ConfiguredUser(String username, String password, List<String> roles) {
      this.username = username;
      this.password = password;
      this.roles = roles;
    }
  }

}
