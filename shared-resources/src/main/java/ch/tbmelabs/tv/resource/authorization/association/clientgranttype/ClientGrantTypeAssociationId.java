package ch.tbmelabs.tv.resource.authorization.association.clientgranttype;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClientGrantTypeAssociationId implements Serializable {
  private static final long serialVersionUID = 1L;

  private Long clientId;
  private Long clientGrantTypeId;
}