package ch.tbmelabs.tv.core.servicediscovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.web.WebApplicationInitializer;

@SpringBootApplication
@EnableAutoConfiguration
public class Application extends SpringBootServletInitializer implements WebApplicationInitializer {
  private static final Class<Application> APPLICATION_SOURCE_CLASS = Application.class;

  public static void main(String[] args) {
    SpringApplication.run(APPLICATION_SOURCE_CLASS, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder applicationBuilder) {
    return applicationBuilder.sources(APPLICATION_SOURCE_CLASS);
  }
}