package ch.tbmelabs.tv.core.authorizationserver.domain.dto;

import java.util.Collection;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import ch.tbmelabs.tv.core.authorizationserver.domain.Role;
import ch.tbmelabs.tv.core.authorizationserver.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class UserProfile {
  private Date created;

  private Date lastUpdated;

  private Long id;

  private String username;

  private String email;

  private Boolean isEnabled;

  private Boolean isBlocked;

  @JsonIgnoreProperties({ "usersWithRoles" })
  private Collection<Role> roles;

  public UserProfile(User user, Collection<Role> roles) {
    this.created = user.getCreated();
    this.lastUpdated = user.getLastUpdated();
    this.id = user.getId();
    this.username = user.getUsername();
    this.email = user.getEmail();
    this.isEnabled = user.getIsEnabled();
    this.isBlocked = user.getIsBlocked();
    this.roles = roles;
  }
}