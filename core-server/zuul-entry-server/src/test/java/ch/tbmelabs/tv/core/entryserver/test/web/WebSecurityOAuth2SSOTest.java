package ch.tbmelabs.tv.core.entryserver.test.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.servlet.MockMvc;

import ch.tbmelabs.tv.core.entryserver.test.AbstractZuulApplicationContextAwareJunitTest;

public class WebSecurityOAuth2SSOTest extends AbstractZuulApplicationContextAwareJunitTest {
  private static final String FORWARD_HEADER_NAME = "location";
  private static final String ZUUL_AUTHENTICATION_ENTRY_POINT_URI = "http://localhost/login";
  private static final String OAUTH2_AUTHENTICATION_ENTRY_POINT_URI = "http://localhost/oauth/authorize";

  @Autowired
  private MockMvc mockMvc;

  @Value("${security.oauth2.client.clientId}")
  private String clientId;

  @Test
  public void requestToRootURLShouldForwardToLoginEndpoint() throws Exception {
    String forwardUrl = mockMvc.perform(get("/")).andExpect(status().is3xxRedirection()).andReturn().getResponse()
        .getHeader(FORWARD_HEADER_NAME);

    assertThat(forwardUrl).startsWith(ZUUL_AUTHENTICATION_ENTRY_POINT_URI)
        .withFailMessage("Check if the security configuration was intentionally changed!");
  }

  @Test
  public void requestToLoginEndpointShouldForwardToOAuth2AuthorizationEndpoint() throws Exception {
    String forwardUrl = mockMvc.perform(get("/login")).andExpect(status().is3xxRedirection()).andReturn().getResponse()
        .getHeader(FORWARD_HEADER_NAME);

    assertThat(forwardUrl).startsWith(OAUTH2_AUTHENTICATION_ENTRY_POINT_URI).contains("client_id=" + clientId)
        .contains("redirect_uri=" + ZUUL_AUTHENTICATION_ENTRY_POINT_URI).contains("response_type=code")
        .contains("state=").withFailMessage("Check if the security configuration was intentionally changed!");
  }
}