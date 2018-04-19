package ch.tbmelabs.tv.core.authorizationserver.test.service.signup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.MockitoAnnotations.initMocks;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import ch.tbmelabs.tv.core.authorizationserver.domain.User;
import ch.tbmelabs.tv.core.authorizationserver.domain.repository.RoleCRUDRepository;
import ch.tbmelabs.tv.core.authorizationserver.domain.repository.UserCRUDRepository;
import ch.tbmelabs.tv.core.authorizationserver.service.signup.UserSignupService;

public class UserSignupServiceValidationTest {
  @Mock
  private UserCRUDRepository userRepository;

  @Mock
  private RoleCRUDRepository roleRepository;

  @Spy
  @InjectMocks
  private UserSignupService fixture;

  private static final User existingUser = new User();

  @BeforeClass
  public static void beforeClassSetUp() {
    existingUser.setUsername(RandomStringUtils.random(11));
    existingUser.setEmail(RandomStringUtils.random(11));
  }

  @Before
  public void beforeTestSetUp() {
    initMocks(this);

    doAnswer(new Answer<Optional<User>>() {
      @Override
      public Optional<User> answer(InvocationOnMock invocation) throws Throwable {
        if (((String) invocation.getArgument(0)).equals(existingUser.getUsername())) {
          return Optional.of(existingUser);
        }

        return Optional.ofNullable(null);
      }
    }).when(userRepository).findOneByUsernameIgnoreCase(ArgumentMatchers.anyString());

    doAnswer(new Answer<Optional<User>>() {
      @Override
      public Optional<User> answer(InvocationOnMock invocation) throws Throwable {
        if (((String) invocation.getArgument(0)).equals(existingUser.getEmail())) {
          return Optional.of(existingUser);
        }

        return Optional.ofNullable(null);
      }
    }).when(userRepository).findOneByEmailIgnoreCase(ArgumentMatchers.anyString());

    doCallRealMethod().when(fixture).isUsernameUnique(ArgumentMatchers.any(User.class));
    doCallRealMethod().when(fixture).doesUsernameMatchFormat(ArgumentMatchers.any(User.class));
    doCallRealMethod().when(fixture).isEmailAddressUnique(ArgumentMatchers.any(User.class));
    doCallRealMethod().when(fixture).isEmailAddress(ArgumentMatchers.any(User.class));
    doCallRealMethod().when(fixture).doesPasswordMatchFormat(ArgumentMatchers.any(User.class));
    doCallRealMethod().when(fixture).doPasswordsMatch(ArgumentMatchers.any(User.class));
  }

  @Test
  public void userSignupServiceShouldInvalidateExistingUsername() {
    final User unexistingUser = new User();
    unexistingUser.setUsername(RandomStringUtils.random(11));

    assertThat(fixture.isUsernameUnique(existingUser)).isFalse();
    assertThat(fixture.isUsernameUnique(unexistingUser)).isTrue();
  }

  @Test
  public void userSignupServiceShouldInvalidateNotMatchingUsername() {
    final User invalidUser = new User();
    invalidUser.setUsername(RandomStringUtils.randomAscii(10) + "$");

    final User validUser = new User();
    validUser.setUsername(RandomStringUtils.randomAlphabetic(11));

    assertThat(fixture.doesUsernameMatchFormat(invalidUser)).isFalse();
    assertThat(fixture.doesUsernameMatchFormat(validUser)).isTrue();
  }

  @Test
  public void userSignupServiceShouldInvalidateExistingEmail() {
    User unexistingUser = new User();
    unexistingUser.setEmail(RandomStringUtils.random(11));

    assertThat(fixture.isEmailAddressUnique(existingUser)).isFalse();
    assertThat(fixture.isEmailAddressUnique(unexistingUser)).isTrue();
  }

  @Test
  public void userSignupServiceShouldInvalidateNotMatchingEmail() {
    User invalidUser = new User();
    invalidUser.setEmail(RandomStringUtils.random(11));

    User validUser = new User();
    validUser.setEmail("valid.email@tbme.tv");

    assertThat(fixture.isEmailAddress(invalidUser)).isFalse();
    assertThat(fixture.isEmailAddress(validUser)).isTrue();
  }

  @Test
  public void userSignupServiceShouldInvalidateNotMatchingPassword() {
    User invalidUser = new User();
    invalidUser.setPassword(RandomStringUtils.random(11));

    User validUser = new User();
    validUser.setPassword("V@l1dP@$$w0rd");

    assertThat(fixture.doesPasswordMatchFormat(invalidUser)).isFalse();
    assertThat(fixture.doesPasswordMatchFormat(validUser)).isTrue();
  }

  @Test
  public void userSignupServiceShouldInvalidatePasswordAndConfirmationIfTheyDontMatch() {
    User invalidUser = new User();
    invalidUser.setPassword("APassword$99");
    invalidUser.setConfirmation("NotQuiteAPassword$99");

    User validUser = new User();
    validUser.setPassword("V@l1dP@$$w0rd");
    validUser.setConfirmation(validUser.getPassword());

    assertThat(fixture.doPasswordsMatch(invalidUser)).isFalse();
    assertThat(fixture.doPasswordsMatch(validUser)).isTrue();
  }
}
