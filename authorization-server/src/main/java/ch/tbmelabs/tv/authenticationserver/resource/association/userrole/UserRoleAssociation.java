package ch.tbmelabs.tv.authenticationserver.resource.association.userrole;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.internal.NotNull;

import ch.tbmelabs.tv.authenticationserver.resource.NicelyDocumentedJDBCResource;
import ch.tbmelabs.tv.authenticationserver.resource.user.Role;
import ch.tbmelabs.tv.authenticationserver.resource.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_has_roles")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@IdClass(UserRoleAssociationId.class)
public class UserRoleAssociation extends NicelyDocumentedJDBCResource {
  @Transient
  private static final long serialVersionUID = 1L;

  @Id
  @NotNull
  @Column(name = "user_id")
  private Long userId;

  @Id
  @NotNull
  @Column(name = "user_role_id")
  private Long userRoleId;

  @JoinColumn(insertable = false, updatable = false)
  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
  @PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @JoinColumn(insertable = false, updatable = false)
  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
  @PrimaryKeyJoinColumn(name = "user_role_id", referencedColumnName = "id")
  private Role userRole;
}