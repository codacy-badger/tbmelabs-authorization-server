package ch.tbmelabs.authorizationserver.domain.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ch.tbmelabs.authorizationserver.domain.Client;

@Repository
public interface ClientCRUDRepository extends CrudRepository<Client, Long> {

  Page<Client> findAll(Pageable pageable);

  Optional<Client> findByClientId(String clientId);
}
