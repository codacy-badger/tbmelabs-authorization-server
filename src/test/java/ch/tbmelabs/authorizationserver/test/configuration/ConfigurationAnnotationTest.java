package ch.tbmelabs.authorizationserver.test.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.reflections.Reflections;
import org.springframework.context.annotation.Configuration;
import ch.tbmelabs.authorizationserver.Application;

public class ConfigurationAnnotationTest {

  private static final Integer EXPECTED_CONFIGURATION_COUNT = 16;

  @Test
  public void packageShouldOnlyContainConfigurations() {
    new Reflections(Application.class.getPackage().getName() + ".configuration")
      .getSubTypesOf(Object.class)
      .forEach(configuration -> assertThat(configuration.getClass().getSimpleName())
        .contains("Configuration"));
  }

  @Test
  public void allConfigurationsShouldBeAnnotated() {
    assertThat(new Reflections(Application.class.getPackage().getName() + ".configuration")
      .getTypesAnnotatedWith(Configuration.class)).hasSize(EXPECTED_CONFIGURATION_COUNT);
  }
}
