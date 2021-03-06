package ch.tbmelabs.authorizationserver.service.domain.impl;

import org.springframework.stereotype.Service;
import ch.tbmelabs.authorizationserver.domain.GrantType;
import ch.tbmelabs.authorizationserver.domain.repository.GrantTypeCRUDRepository;
import ch.tbmelabs.authorizationserver.service.domain.GrantTypeService;

@Service
public class GrantTypeServiceImpl implements GrantTypeService {

  private GrantTypeCRUDRepository grantTypeRepository;

  public GrantTypeServiceImpl(GrantTypeCRUDRepository grantTypeRepository) {
    this.grantTypeRepository = grantTypeRepository;
  }

  public GrantType findByName(String name) {
    return grantTypeRepository.findByName(name).orElseThrow(() -> new IllegalArgumentException(
      "The default " + GrantType.class + "'" + name + "' does not exist!"));
  }
}
