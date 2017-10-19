package ch.tbmelabs.tv.resource.authentication.logging;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ch.tbmelabs.tv.resource.authentication.NicelyDocumentedJDBCResource;
import ch.tbmelabs.tv.resource.authorization.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "authentication_log")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenticationLog extends NicelyDocumentedJDBCResource {
  @Transient
  private static final long serialVersionUID = 1L;

  public enum AUTHENTICATION_STATE {
    OK, NOK
  };

  @Id
  @GenericGenerator(name = "pk_sequence", strategy = NicelyDocumentedJDBCResource.SEQUENCE_GENERATOR_STRATEGY, parameters = {
      @Parameter(name = "sequence_name", value = "authentication_log_id_seq"),
      @Parameter(name = "increment_size", value = "1") })
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_sequence")
  @Column(unique = true)
  private Long id;

  @NotEmpty
  @Length(max = 3, min = 3)
  @Column(columnDefinition = "bpchar(3)")
  private AUTHENTICATION_STATE state;

  @NotEmpty
  @Length(max = 45)
  private String ip;

  @Length(max = 256)
  private String message;

  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
  @PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  public AuthenticationLog(AUTHENTICATION_STATE authenticationState, String ip, String message, User user) {
    this.state = authenticationState;
    this.ip = ip;
    this.message = message;
    this.user = user;
  }
}