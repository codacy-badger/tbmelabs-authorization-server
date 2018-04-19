package ch.tbmelabs.tv.core.authorizationserver.domain;

import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import ch.tbmelabs.tv.core.authorizationserver.domain.association.userrole.UserRoleAssociation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "user_roles")
@JsonInclude(Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Role extends NicelyDocumentedJDBCResource implements GrantedAuthority {
  @Transient
  private static final long serialVersionUID = 1L;

  @Transient
  public static final String ROLE_PREFIX = "ROLE_";

  @Id
  @GenericGenerator(name = "pk_sequence",
      strategy = NicelyDocumentedJDBCResource.SEQUENCE_GENERATOR_STRATEGY,
      parameters = {@Parameter(name = "sequence_name", value = "user_roles_id_seq"),
          @Parameter(name = "increment_size", value = "1")})
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_sequence")
  @Column(unique = true)
  private Long id;

  @NotEmpty
  @Length(max = 16)
  private String name;

  @JsonManagedReference("role_has_users")
  @LazyCollection(LazyCollectionOption.FALSE)
  @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE},
      mappedBy = "userRoleId")
  private Collection<UserRoleAssociation> usersWithRoles;

  public Role(String authority) {
    this.name = authority;
  }

  @Override
  public String getAuthority() {
    return getName();
  }
}
