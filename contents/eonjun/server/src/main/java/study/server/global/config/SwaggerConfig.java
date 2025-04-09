package study.server.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author : frozzun
 * @filename :SwaggerConfig.java
 * @since 10/12/24
 */
@Configuration
public class SwaggerConfig {
  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
      .components(new Components())
      .info(apiInfo());
  }

  private Info apiInfo() {
    return new Info()
      .title("API Test") // API의 제목
      .description("AppCenter Swagger") // API에 대한 설명
      .version("1.0.0"); // API의 버전
  }
}

