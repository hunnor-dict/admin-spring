package net.hunnor.dict.admin.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "net.hunnor.dict.admin.security")
public class SecurityUsersProperties {

  private List<UserConfig> users = new ArrayList<>();

  /**
   * Returns configured users loaded from properties.
   *
   * @return list of configured users
   */
  public List<UserConfig> getUsers() {
    return new ArrayList<>(users);
  }

  /**
   * Sets configured users loaded from properties.
   *
   * @param users list of configured users
   */
  public void setUsers(List<UserConfig> users) {
    if (users == null) {
      this.users = new ArrayList<>();
      return;
    }
    this.users = new ArrayList<>(users);
  }

  public static class UserConfig {

    private String username;

    private String password;

    private List<String> roles = new ArrayList<>();

    /**
     * Returns the username.
     *
     * @return username value
     */
    public String getUsername() {
      return username;
    }

    /**
     * Sets the username.
     *
     * @param username username value
     */
    public void setUsername(String username) {
      this.username = username;
    }

    /**
     * Returns the configured password.
     *
     * @return password value
     */
    public String getPassword() {
      return password;
    }

    /**
     * Sets the configured password.
     *
     * @param password password value
     */
    public void setPassword(String password) {
      this.password = password;
    }

    /**
     * Returns role names for the user.
     *
     * @return list of role names
     */
    public List<String> getRoles() {
      return new ArrayList<>(roles);
    }

    /**
     * Sets role names for the user.
     *
     * @param roles list of role names
     */
    public void setRoles(List<String> roles) {
      if (roles == null) {
        this.roles = new ArrayList<>();
        return;
      }
      this.roles = new ArrayList<>(roles);
    }
  }
}
