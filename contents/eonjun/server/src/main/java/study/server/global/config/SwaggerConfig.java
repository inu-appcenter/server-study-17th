package study.server.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
      .components(new Components()
        .addSecuritySchemes("JWT", new SecurityScheme()
          .name("JWT")
          .type(SecurityScheme.Type.HTTP)
          .scheme("bearer")
          .bearerFormat("JWT")))
      .addSecurityItem(new SecurityRequirement().addList("JWT"))
      .info(apiInfo())
      .servers(List.of(
        new Server().url("http://localhost:8080")
      ));
  }

  private Info apiInfo() {
    return new Info()
      .title("API Test")
      .description("Backend Swagger")
      .version("1.0.0");
  }
}

