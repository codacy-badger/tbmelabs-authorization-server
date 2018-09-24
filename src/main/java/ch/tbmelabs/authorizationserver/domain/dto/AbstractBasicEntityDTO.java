package ch.tbmelabs.authorizationserver.domain.dto;

import java.util.Date;
import javax.persistence.MappedSuperclass;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@MappedSuperclass
@NoArgsConstructor
public abstract class AbstractBasicEntityDTO {

  private Long id;
  private Date created;
  private Date lastUpdated;
}
