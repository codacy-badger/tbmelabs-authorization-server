package ch.tbmelabs.tv.core.authorizationserver.test.security.logging;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import ch.tbmelabs.tv.core.authorizationserver.domain.AuthenticationLog;
import ch.tbmelabs.tv.core.authorizationserver.domain.AuthenticationLog.AUTHENTICATION_STATE;
import ch.tbmelabs.tv.core.authorizationserver.domain.User;
import ch.tbmelabs.tv.core.authorizationserver.domain.repository.AuthenticationLogCRUDRepository;
import ch.tbmelabs.tv.core.authorizationserver.domain.repository.UserCRUDRepository;
import ch.tbmelabs.tv.core.authorizationserver.service.bruteforce.BruteforceFilterService;
import ch.tbmelabs.tv.core.authorizationserver.test.AbstractOAuth2AuthorizationServerContextAwareTest;
import ch.tbmelabs.tv.core.authorizationserver.test.domain.dto.UserProfileTest;

public class LoginEndpointAuthenticationAttemptLoggerIntTest
    extends AbstractOAuth2AuthorizationServerContextAwareTest {

  private static final String LOGIN_PROCESSING_URL = "/signin";
  private static final String USERNAME_PARAMETER_NAME = "username";
  private static final String PASSWORD_PARAMETER_NAME = "password";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private AuthenticationLogCRUDRepository authenticationLogRepository;

  @Autowired
  private UserCRUDRepository userRepository;

  private User testUser;
  private String password;

  @Before
  public void beforeTestSetUp() {
    authenticationLogRepository.deleteAll();
    BruteforceFilterService.resetFilter();

    User newUser = UserProfileTest.createTestUser();
    newUser.setIsEnabled(true);
    password = newUser.getPassword();

    testUser = userRepository.save(newUser);
  }

  @Test
  public void loginWithInvalidUsernameShouldNotBeRegistered() throws Exception {
    mockMvc
        .perform(post(LOGIN_PROCESSING_URL).param(USERNAME_PARAMETER_NAME, "invalid")
            .param(PASSWORD_PARAMETER_NAME, "invalid"))
        .andDo(print()).andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));

    assertThat(authenticationLogRepository.findAll()).isNullOrEmpty();
  }

  @Test
  public void loginWithValidUsernameAndInvalidPasswordShouldBeRegistered() throws Exception {
    mockMvc
        .perform(post(LOGIN_PROCESSING_URL).param(USERNAME_PARAMETER_NAME, testUser.getUsername())
            .param(PASSWORD_PARAMETER_NAME, "invalid"))
        .andDo(print()).andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));

    List<AuthenticationLog> logs =
        (ArrayList<AuthenticationLog>) authenticationLogRepository.findAll();

    assertThat(logs).hasSize(1).extracting("state")
        .containsExactly(AUTHENTICATION_STATE.NOK.name());
    assertThat(logs).extracting("user").extracting("username")
        .containsExactly(testUser.getUsername());
  }

  @Test
  public void loginWithValidUserShouldBeRegistered() throws Exception {
    String redirectUrl = mockMvc
        .perform(post(LOGIN_PROCESSING_URL).param(USERNAME_PARAMETER_NAME, testUser.getUsername())
            .param(PASSWORD_PARAMETER_NAME, password))
        .andDo(print()).andExpect(status().is(HttpStatus.FOUND.value())).andReturn().getResponse()
        .getRedirectedUrl();

    assertThat(redirectUrl).isEqualTo("/");

    List<AuthenticationLog> logs =
        (ArrayList<AuthenticationLog>) authenticationLogRepository.findAll();

    assertThat(logs).hasSize(1).extracting("state").containsExactly(AUTHENTICATION_STATE.OK.name());
    assertThat(logs).extracting("user").extracting("username")
        .containsExactly(testUser.getUsername());
  }
}
