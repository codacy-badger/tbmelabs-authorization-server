package ch.tbmelabs.authorizationserver.web.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

@Component
public class LoggingExceptionTranslator extends DefaultWebResponseExceptionTranslator {

  private static final Logger LOGGER = LoggerFactory.getLogger(LoggingExceptionTranslator.class);

  @Override
  public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
    LOGGER.warn("Translating exception {}: {}", e.getClass(), e.getLocalizedMessage());

    ResponseEntity<OAuth2Exception> responseEntity = super.translate(e);

    HttpHeaders headers = new HttpHeaders();
    headers.setAll(responseEntity.getHeaders().toSingleValueMap());

    OAuth2Exception exceptionBody = responseEntity.getBody();

    return new ResponseEntity<>(exceptionBody, headers, responseEntity.getStatusCode());
  }
}
