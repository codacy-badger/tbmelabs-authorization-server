package ch.tbmelabs.authorizationserver.test.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import ch.tbmelabs.authorizationserver.Application;
import ch.tbmelabs.authorizationserver.configuration.TokenStoreConfiguration.RedisTokenStoreConfiguration;
import ch.tbmelabs.serverconstants.spring.SpringApplicationProfileConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextBeforeModesTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({SpringApplicationProfileConstants.TEST, SpringApplicationProfileConstants.NO_MAIL})
@SpringBootTest(classes = {Application.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestExecutionListeners({ServletTestExecutionListener.class,
  DirtiesContextTestExecutionListener.class, DirtiesContextBeforeModesTestExecutionListener.class,
  DependencyInjectionTestExecutionListener.class, WithSecurityContextTestExecutionListener.class})
public class RedisTokenStoreConfigurationIntTest {

  @Autowired
  private RedisTokenStoreConfiguration redisConfiguration;

  @Autowired
  private TokenStore bean;

  @Test
  public void primaryRegisteredBeanShouldEqualConfiguredDataSource() {
    assertThat(redisConfiguration.tokenStore()).isEqualTo(bean);
  }
}
