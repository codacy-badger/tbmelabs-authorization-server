package ch.tbmelabs.tv.core.entryserver.test.application;

import org.junit.Test;
import ch.tbmelabs.tv.core.entryserver.Application;
import ch.tbmelabs.tv.core.entryserver.test.AbstractZuulServerContextAwareTest;

public class ApplicationIntTest extends AbstractZuulServerContextAwareTest {

  @Test
  public void applicationContextLoads() {
  }

  @Test
  public void publicStaticVoidMainShouldStartSpringApplication() {
    Application.main(new String[]{"--spring.profiles.active=test"});
  }
}
