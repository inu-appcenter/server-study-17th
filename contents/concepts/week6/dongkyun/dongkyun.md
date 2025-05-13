# Logging & Swagger

---

## 공통 주제

### 🍀 Logging은 무엇이며 Spring에서 어떻게 적용할 수 있을까요?

- **📁 Logging**이란?
    - 시스템이 실행되는 동안 발생하는 이벤트, 오류, 경고, 정보 등을 **기록** 하는 것
    - 코드의 흐름을 추적하고 문제가 발생했을 때 원인을 파악하는 데 도움
    - 운영 환경에서는 장애 대응, 보안 감사, 성능 개선의 근거 자료로 사용

1. ♟️ Logging 전략은 무엇이고 왜 세워야 할까요?
    1. **Logging 전략**이란?
        1. 로그를 ***어디서, 어떻게, 얼마나, 어떤 방식으로 남길지***에 대한 정책
        2. 시스템 전체에서 일관된 로그를 남기기 위한 가이드라인

    2. *필요한 이유*
        1. **문제 진단**이 쉽고 빠르게 이뤄짐
        2. **불필요한 로그 남기기 방지** → 저장 비용, 보안 이슈 감소
        3. **보안 로그 강화** → 인증, 권한, 민감 정보 처리 이력 추적
        4. **모니터링/알림 시스템 연계** 가능
        5. **분산 시스템/마이크로서비스**에서는 구조화된 로그와 추적 ID 필수

    3. *전략 예시*
        1. 어떤 레벨(Level)의 로그를 어떤 환경에서 출력할지 결정
           (ex: 개발: DEBUG, 운영: INFO 이상)
        2. 예외 처리 시 반드시 로그 남기기
        3. 요청 시작/종료 로그, 응답 시간 측정
        4. etc . . .

2. Log Level은 무엇이고 어떻게 적용해야 할까요?
    1. **⭐️ Log Level**이란?
        1. 로그의 **중요도**에 따라 구분하는 수준.
        2. *주요 레벨*

            | **Level** | **설명** |
            | --- | --- |
            | **TRACE** | 가장 상세한 정보. 흐름을 아주 세밀히 추적. |
            | **DEBUG** | 디버깅 목적의 정보. 개발 시 주로 사용. |
            | **INFO** | 일반적인 정보성 메시지. 시스템 상태 파악. |
            | **WARN** | 경고 상황이지만 시스템은 정상 작동. 주의 필요. |
            | **ERROR** | 예외나 시스템 오류 등 문제 발생 상황. |
            | **FATAL** | 심각한 오류로 애플리케이션 종료 가능성. (Log4j에 있음) |
3. Spring에서 Logging을 적용하는 방법에는 어떤 것이 있나요?
    1. *의존성 추가 ( build.gradle )*

        ```java
        implementation 'org.springframework.boot:spring-boot-starter-logging'
        ```

    2. *Spring Boot는 기본 포함*
        1. spring-boot-starter 또는 spring-boot-starter-web를 포함시 위 의존성이 자동으로 포함
    3. *구성 요소*
        1. SLF4J (Logging facade)
        2. Logback (기본 로깅 구현체)
    4. *설정 변경 ( application.yml )*

        ```java
        logging:
          level:
            root: INFO                    # 전체 기본 로그 레벨은 INFO
            com.example.demo: DEBUG       # 우리 프로젝트 패키지는 DEBUG
            org.springframework.web: WARN # Spring Web은 경고 이상만
            org.hibernate.SQL: DEBUG      # Hibernate 쿼리 출력
            org.hibernate.type.descriptor.sql.BasicBinder: TRACE # 바인딩 파라미터 출력
        ```

        ```java
        // Hibernate에서 실행되는 SQL 쿼리와 바인딩 파라미터를 로그로 확인
        logging:
          level:
            org.hibernate.SQL: DEBUG
            org.hibernate.type.descriptor.sql.BasicBinder: TRACE
        ```

        ```java
        log.info("ℹ️ INFO - 정상 흐름의 정보");
        log.debug("🐞 DEBUG - 디버깅 정보");
        log.warn("⚠️ WARN - 주의가 필요한 상황");
        log.error("❌ ERROR - 오류 발생!");
        
        // 실행 결과
        ℹ️ INFO - 정상 흐름의 정보
        🐞 DEBUG - 디버깅 정보
        ⚠️ WARN - 주의가 필요한 상황
        ❌ ERROR - 오류 발생!
        ```

    ```java
    @Slf4j
    // ...
    public class MemberService {
    		
    		// ...
    
        @Transactional
        public SignupResponseDto signup(SignupRequestDto signupRequestDto) {
    		   // ...
    
            Member member = new Member(signupRequestDto, passwordEncoder.encode(signupRequestDto.getPassword()));
            log.info("Member {} created", member);
    
    			// ...
        }
    }
    ```


### 🍀 Swagger는 무엇이며 어떻게 활용할 수 있을까요?

1. Swagger는 무엇일까요?
    1. **📘 Swagger**란?
        1. REST API의 명세를 자동으로 문서화하고 UI로 시각화할 수 있는 오픈소스 프레임워크
    2. *주요 기능*
        1. REST API 문서 자동 생성
        2. Web UI에서 API 테스트 가능
        3. API 명세를 팀 간 공유 가능 (백엔드 ↔ 프론트엔드 협업 용이)
        4. OpenAPI 3.0 스펙 지원
            1. OpenAPI는 REST API를 기술하기 위한 **표준 문서 포맷**
                1. 예: API의 경로, 요청 파라미터, 응답 코드, 인증 방식 등을 명확하게 정의한 문서

2. Spring에 Swagger를 어떻게 적용할 수 있을까요?(with Authorization)
    1. Swagger Config
        1. 의존성 추가 ( build.gradle )

            ```java
            implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0'
            ```

        2. SwaggerConfig

            ```java
            @Configuration
            public class SwaggerConfig {
            
                    // 🔹 API 기본 정보
                    Info info = new Info()
                            .title("Inffy Server")
                            .description("인천대학교 소모임 주선 서비스 'Inffy' 서버")
                            .version("1.0.0");
            
                    // 🔹 서버 정보
                    Server server = new Server()
                            .url("https://inffy-server.site")
                            .description("배포 서버");
            
                    // 🔹 보안 스키마 (JWT Bearer Token)
                    SecurityScheme bearerAuth = new SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                            .in(SecurityScheme.In.HEADER)
                            .name("Authorization");
            
                    // 🔹 보안 요구 사항 (전역 적용)
                    ***// bearerAuth를 모든 API 요청에 기본적으로 적용***
                    SecurityRequirement securityRequirement = new SecurityRequirement()
                            .addList("bearerAuth");
            
            				// 🔹 OpenAPI 객체 구성
                            // 🔹 OpenAPI 객체 구성
                    return new OpenAPI()
                            .info(info)
                            .servers(List.of(server))
                            .components(new Components().addSecuritySchemes("bearerAuth", bearerAuth))
                            .addSecurityItem(securityRequirement);
                    }
            }
            ```

            1. 여담으로 import 할 때

               ![스크린샷 2025-05-13 오후 2.06.44.png](%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202025-05-13%20%EC%98%A4%ED%9B%84%202.06.44.png)

                1. annotations가 아닌 models로 해야한다…
                2. annotations는 **컨트롤러 메서드나 클래스에 어노테이션으로 붙이는 경우** 사용

                    ```java
                    import io.swagger.v3.oas.annotations.security.SecurityRequirement;
                    
                    @SecurityRequirement(name = "bearerAuth")  // ← 여기에 사용됨
                    public class MemberController {
                        ...
                    }
                    ```

    2. Controller Layer

        | @Tag | API 그룹 이름 설정 |
        | --- | --- |
        | @Operation | API 설명 요약 + 상세 설명 |
        | @ApiResponse | HTTP 응답에 대한 설명 및 예시 |
        1. API에 대한 설명
            
            ```java
            @Tag(name = "Member", description = "멤버 관련 API")
            public class MemberController{
            		
            		// ...
            
                @Operation(summary = "멤버 조회", description = "멤버의 정보를 조회"
                    responses = {
                        @ApiResponse(
                            responseCode = "200",
                            description = "정상 응답",
                            content = @Content(
                                schema = @Schema(implementation = MemberResponseDto.class),
                                examples = @ExampleObject(value = "{ ... JSON 예시 ... }")
            		            )
                        )
                    }
                )
                @GetMapping
                public ResponseEntity<ResponseDto<MemberResponseDto>> getMember(@AuthenticationPrincipal Member member) {
                    return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(mypageService.getMember(member), "View Member Information"));
                }
            }
            ```
            
        2. 그런데 컨트롤러가 굉장히 더러워짐 ;;
            
            → Swagger 설정에 대한 부분만 따로 파일을 만들어 컨트롤러는 깔끔하게 만드는 방법도 존재
            
            - swagger 문서화만 정리해놓은 인터페이스를 생성

                ```java
                @Tag(name = "Cart", description = "장바구니 관련 API")
                public interface CartApiSpecification {
                
                    @Operation(
                            summary = "장바구니 추가",
                            description = "게임 ID를 받아 해당 게임을 현재 로그인한 사용자의 장바구니에 추가합니다.",
                            responses = {
                                    // 201 Created 응답일 때
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "장바구니에 성공적으로 추가됨",
                                            // 응답 본문은 JSON 형식이고, 구조는 CartResponse DTO를 따름
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = CartResponse.class)
                                            )
                                    ),
                                    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
                            }
                    )
                    @PostMapping
                    ResponseEntity<CartResponse> addCart(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long gameId);
                }
                
                ```

            - 컨트롤러에서 이를 상속 받는다

                ```java
                // ...
                public class CartController implements CartApiSpecification {
                    // ...
                		@PostMapping("/{gameId}")
                    public ResponseEntity<CartResponse> addCart(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long gameId) {
                        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addCart(userDetails, gameId));
                    }
                }
                ```

    3. DTO 
        1. @Schema → DTO 필드에 대한 설명 및 예시
            
            ```java
            public class UserRequest {
                @Schema(description = "사용자 이름", example = "홍길동")
                private String name;
            
                // getter, setter
            }
            
            public class UserResponse {
                @Schema(description = "사용자 ID", example = "1")
                private Long id;
            
                @Schema(description = "사용자 이름", example = "홍길동")
                private String name;
            
                // 생성자, getter, setter
            }
            ```


## 🔎 과제

<aside>
✅ 실습 프로젝트에 Logging과 Swagger 적용하기

→ 추가로 Swagger를 깔끔하게 작성하기 위한 방법을 생각해봅시다!
→ 스웨거에 jwt 적용하기
→ API 별 로깅 적용하기

</aside>