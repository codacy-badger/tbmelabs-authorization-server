package ch.tbmelabs.tv.core.authorizationserver.domain;

import ch.tbmelabs.tv.core.authorizationserver.domain.association.clientauthority.ClientAuthorityAssociation;
import ch.tbmelabs.tv.core.authorizationserver.domain.association.clientgranttype.ClientGrantTypeAssociation;
import ch.tbmelabs.tv.core.authorizationserver.domain.association.clientscope.ClientScopeAssociation;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "clients")
public class Client extends AuditingEntity {

  @Transient
  private static final long serialVersionUID = 1L;

  @Transient
  public static final String REDIRECT_URI_SPLITTERATOR = ";";

  @Id
  @GenericGenerator(name = "pk_sequence",
      strategy = AuditingEntity.SEQUENCE_GENERATOR_STRATEGY,
      parameters = {@Parameter(name = "sequence_name", value = "clients_id_seq"),
          @Parameter(name = "increment_size", value = "1")})
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_sequence")
  @Column(unique = true)
  private Long id;

  @NotEmpty
  @Length(max = 36, min = 36)
  @Column(name = "client_id", columnDefinition = "bpchar(36)", unique = true)
  private String clientId;

  @Length(max = 36, min = 36)
  @Column(columnDefinition = "bpchar(36)")
  private String secret;

  @NotNull
  @Column(name = "secret_required")
  private Boolean isSecretRequired = true;

  @NotNull
  @Column(name = "auto_approve")
  private Boolean isAutoApprove = false;

  @NotNull
  @Column(name = "access_token_validity")
  private Integer accessTokenValiditySeconds;

  @NotNull
  @Column(name = "refresh_token_validity")
  private Integer refreshTokenValiditySeconds;

  @Length(max = 256)
  private String redirectUri;

  @JsonManagedReference("client_has_grant_types")
  @LazyCollection(LazyCollectionOption.FALSE)
  @OneToMany(mappedBy = "client")
  private Set<ClientGrantTypeAssociation> grantTypes;

  @JsonManagedReference("client_has_authorities")
  @LazyCollection(LazyCollectionOption.FALSE)
  @OneToMany(mappedBy = "client")
  private Set<ClientAuthorityAssociation> grantedAuthorities;

  @JsonManagedReference("client_has_scopes")
  @LazyCollection(LazyCollectionOption.FALSE)
  @OneToMany(mappedBy = "client")
  private Set<ClientScopeAssociation> scopes;

  @Override
  public boolean equals(Object object) {
    if (object == null || !(object instanceof Client)) {
      return false;
    }

    Client other = (Client) object;
    return Objects.equals(this.getId(), other.getId())
        && Objects.equals(this.getClientId(), other.getClientId())
        && Objects.equals(this.getSecret(), other.getSecret())
        && Objects.equals(this.getIsAutoApprove(), other.getIsAutoApprove())
        && Objects.equals(this.getIsSecretRequired(), other.getIsSecretRequired())
        && Objects.equals(this.getAccessTokenValiditySeconds(),
        other.getAccessTokenValiditySeconds())
        && Objects.equals(this.getRefreshTokenValiditySeconds(),
        other.getRefreshTokenValiditySeconds())
        && Objects.equals(this.getRedirectUri(), other.getRedirectUri());
  }

  @Override
  public int hashCode() {
    if (this.getId() == null) {
      return super.hashCode();
    }

    // @formatter:off
    return new HashCodeBuilder()
        .append(this.getId())
        .build();
    // @formatter:on
  }
}
