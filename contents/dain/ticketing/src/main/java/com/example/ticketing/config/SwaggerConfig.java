package com.example.ticketing.config;


import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${swagger.server-url}")
    private String serverUrl;
    private static final String securitySchemeName = "bearer"; //스키마 이름 정의

    @Bean
    public OpenAPI openAPI() {
        Server server = new Server();
        server.url(serverUrl);

        //JWT 인증 방식 정의
        SecurityScheme securityScheme = new SecurityScheme()
                .name(securitySchemeName) //스키마 이름
                .type(SecurityScheme.Type.HTTP) //HTTP 인증 방식
                .scheme("bearer") //bearer 사용
                .bearerFormat("JWT") //JWT 형식 명시
                .in(SecurityScheme.In.HEADER); //토큰을 HTTP 헤더에 포함시킴

        //전체에 JWT 보안 적용
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(securitySchemeName);

        return new OpenAPI()
                .addServersItem(server)
                .components(new Components()
                .addSecuritySchemes(securitySchemeName, securityScheme)) //등록
                .addSecurityItem(securityRequirement)//적용
                .info(apiInfo());
    }

    //기본 정보
    private Info apiInfo() {
        return new Info()
                .title("Server Study")
                .description("API test")
                .version("1.0.0");
    }
}
