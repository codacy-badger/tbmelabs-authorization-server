package ch.tbmelabs.tv.core.authorizationserver.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ch.tbmelabs.tv.core.authorizationserver.domain.Scope;
import ch.tbmelabs.tv.core.authorizationserver.domain.repository.ScopeCRUDRepository;
import ch.tbmelabs.tv.shared.constants.security.UserAuthority;

@RestController
@RequestMapping({"${spring.data.rest.base-path}/scopes"})
@PreAuthorize("hasAuthority('" + UserAuthority.SERVER_ADMIN + "')")
public class ScopeController {

  @Autowired
  private ScopeCRUDRepository scopeRepository;

  @GetMapping
  public Page<Scope> getAllScopes(Pageable pageable) {
    return scopeRepository.findAll(pageable);
  }
}
