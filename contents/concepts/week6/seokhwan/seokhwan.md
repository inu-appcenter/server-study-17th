# Logging & Swagger

추가 일시: 2025년 5월 6일 오후 9:12
강의: Appcenter_Server

# 📁 Logging & Swagger

---

## 🍀 Logging은 무엇이며 Spring에서 어떻게 적용할 수 있을까요?

---

![image.png](image.png)

### ✅ Logging 전략은 무엇이고 왜 세워야 할까요?

Logging 전략은 어플리케이션, 시스템 또는 네트워크에서 생성되는  로그 데이터를 수집, 저장, 분석, 관리하기 위한 체계적인 접근 방식입니다. 로그를 기록할뿐 아니라, 필요에 맞게 로그 데이터를 활용하는 전반적인 계획을 포함합니다.

- 개발 측면

**시스템 모니터링 및 문제 해결**

로그는 시스템 상태와 동작을 실시간으로 모니터링하고 오류가 발생했을 때 빠르게 진단하고 해결하는 데 필수적이다. 효과적인 로깅 전략으로 문제 해결 시간을 단축시킬 수 있다.

**성능 최적화**

로그 테이터는 시스템 성능의 병목 현상을 식별하고 최적화할 수 있도록 합니다. 이를 통해 리소스 사용을 개선할 수 있습니다.

**일관성 및 효율성**

표준화된 로깅 전략을 통해 개발팀 전체에 일관된 접근 방식을 제공하여 코드의 질을 향상시킬 수 있습니다.

- 산업 측면

**비즈니스 관련**

로그 데이터는 사용자의 행동, 트렌드 및 패턴에 대한 중요한 정보를 담고 있어 비즈니스 의사 결정에 활용될 수 있습니다.

**보안 및 규정**

많은 산업에서 데이터 보안과 개인정보 보호를 위한 특정 로깅 요구사항을 의무화하고 있다. 체계적인 로깅 전략은 이러한 규정을 준수하고 보안 위협을 감지하는 데 도움이 됩니다.

### ✅ Log Level은 무엇이고 어떻게 적용해야 할까요?

Log Level은 Log 메시지의 중요도를 나타낸다.

각 Log Level은 특정 유셩의 정보나 이벤트에 대한 가시성을 제공하고, 어플리케이션의 상태를 이해하고 문제를 진단하는데 도움을 준다.

로그의 종류는 크게 6가지로 나뉜다.

1️⃣ **TRACE**

가장 상세한 수준의 정보를 제공합니다.

주로 개발 단계에서 사용됩니다.

2️⃣ **DEBUG**

디버깅 목적으로 유용한 상세 정보를 제공합니다.

3️⃣ **INFO**

어플리케이션의 정상적인 작동을 확인하는 일반적인 정보성 메시지입니다.

4️⃣ **WARNING**

잠재적인 문제나 경고 상황을 나타냅니다.

현상태는 오류가 아니지만 주의가 필요한 단계입니다.

5️⃣ **ERROR**

어플리케이션이 특정 작업을 수행하지 못하는 심각한 문제, 또는 오류 상황을 나타냅니다.

6️⃣ **CRITICAL 또는 FATAL**

어플리케이션이 계속 실행될 수 없는 심각한 오류 상황을 나타냅니다.

일반적으로 오류복구가 불가능하거나 매우 어려운 상황입니다.

📝 **ALL < DEBUG < INFO < WARN < ERROR < FATAL < OFF**

로그레벨은 로깅 시스템 설정을 통해 지정하며, 설정된 로그 레벨 이상의 중요도를 가진 로그 메시지만 기록됩니다.

 ****🔔 **로그 메시지 설계 원칙**

- 각 로그 레벨에 적합한 정보만 포함시킨다.
- 추적가능한 식별자(트랜잭션 ID, 요청 ID 등)를 포함시킨다.
- 민감한 정보는 로깅하지 않는다.
- 문제 진단에 필요한 충분한 컨텍스트를 제공한다.

### ✅ Spring에서 Logging을 적용하는 방법에는 어떤 것이 있나요?

Spring Boot는 기본적으로 SLF4J(Simple Logging Facade for Java)를 추상화 레이어로 사용하고, Logback을 구현체로 사용한다.

SLF4J의 추상 인터페이스에 사용할 수 있는 구현체로는 log4j, logback, log4j2 등이 있다.

`@Slf4j` 어노테이션은 Lombok의 로깅 어노테이션으로  컴파일 시점에 아래와 같은 코드로 변환된다.

```java

private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserController.class);

```

Ai에게 로깅을 구현시켜보았습니다.

```java
@Slf4j
@Service
public class OrderService {

    public Order processOrder(Order order) {
        log.info("주문 처리 시작: 주문번호={}, 고객ID={}", order.getOrderId(), order.getCustomerId());
        
        // MDC를 사용한 로깅 컨텍스트 추가
        MDC.put("orderId", order.getOrderId());
        
        try {
            // 비즈니스 로직
            log.debug("재고 확인 중...");
            // ...
            
            log.debug("결제 처리 중...");
            // ...
            
            log.info("주문 처리 완료");
            return processedOrder;
        } catch (InsufficientStockException e) {
            log.warn("재고 부족으로 주문 처리 실패: {}", e.getMessage());
            throw e;
        } catch (PaymentFailedException e) {
            log.error("결제 처리 실패: {}", e.getMessage(), e);
            throw e;
        } finally {
            MDC.remove("orderId");
        }
    }
}
```

❓ MDC를 사용하는데 이는 무엇인가요?

MDC는 Mapped Diagnostic Context의 약자로, 이는 각 요청이나 트랜잭션에 대한 컨텍스트 정보를 로그에 포함시킬 수 있도록 합니다.

```java
String requestId = UUID.randomUUID().toString();
MDC.put("requestId", requestId);
```

```java
if (auth != null && auth.isAuthenticated()) {
                MDC.put("username", auth.getName());
            }
```

직접 찍어본 로그

```java
2025-05-11T18:02:21.533+09:00  INFO 14408 --- [nio-8082-exec-8] c.b.a.service.UserService                : 회원가입 시작 - 아이디: qwer0123
Hibernate: select u1_0.id,u1_0.location,u1_0.login_id,u1_0.manner,u1_0.nickname,u1_0.password,u1_0.profile_url,u1_0.role,u1_0.username from user_table u1_0 where u1_0.login_id=?
Hibernate: insert into user_table (location,login_id,manner,nickname,password,profile_url,role,username) values (?,?,?,?,?,?,?,?)
2025-05-11T18:02:22.564+09:00  INFO 14408 --- [nio-8082-exec-8] c.b.a.service.UserService                : 회원가입 완료 - 아이디: qwer0123, 사용자명: seokhwan
2025-05-11T18:02:48.756+09:00  INFO 14408 --- [nio-8082-exec-9] c.b.a.service.UserService                : 회원가입 시작 - 아이디: qwer1234
Hibernate: select u1_0.id,u1_0.location,u1_0.login_id,u1_0.manner,u1_0.nickname,u1_0.password,u1_0.profile_url,u1_0.role,u1_0.username from user_table u1_0 where u1_0.login_id=?
2025-05-11T18:02:48.785+09:00  WARN 14408 --- [nio-8082-exec-9] c.b.a.service.UserService                : 회원가입 실패 - 이미 존재하는 아이디: qwer1234
```

## 🍀 Swagger는 무엇이며 어떻게 활용할 수 있을까요?

---

### ✅ Swagger는 무엇일까요?

![image.png](image%201.png)

Swagger는 RESTful API를 문서화하고 시각화하는 도구입니다.

**주요 기능**

- API 문서 자동화: 코드에서 API 명세를 자동으로 생성하여 항상 최신 상태의 문서를 유지합니다.
- 시각적 UI 제공: Swagger UI를 통해 API를 시각적으로 테스트 할 수 있는 인터페이스를 제공합니다.
- API 설계 도구: API를 먼저 설계한 후 코드를 생성할 수 있는 도구를 제공합니다.
- 코드 생성: API명세를 기반으로 클라이언트 코드를 자동 생성합니다.

**사용시 주의 사항**

Swagger UI는 API의 엔드포인트, 파라미터, 요청, 응답 본문들을 모두 노출합니다.

때문에 이를 악용할 수 없도록 보안에 신경써야 합니다.

### ✅ Spring에 Swagger를 어떻게 적용할 수 있을까요?(with Authorization)

**Swagger Config**

```java
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        String securitySchemeName = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(securitySchemeName);
				
				//Security 스키마 설정
        Components components = new Components()
                .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                );

        return new OpenAPI()
                .info(apiInfo())
                .addSecurityItem(securityRequirement)
                .components(components);
    }
    
		//API 기본 정보 설정
    private Info apiInfo() {
        return new Info()
                .title("App Center Server Study API")
                .description("Practice API")
                .version("1.0.0");
    }
}
```

**Controller Layer**

```java
@Operation(
        summary = "사용자 조회", 
        description = "ID로 사용자 정보를 조회합니다", 
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "사용자 없음"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
        }
    )
```

`@Operation` 어노테이션으로 각 엔드포인트에 대해 설정할 수 있습니다.

또한 `@ApiResponse` 어노테이션으로 response상태에 따른 응답을 설정할 수 있습니다.

**DTO**

```java
@NotBlank(message = "아이디를 입력하세요")
@Schema(description = "로그인 아이디", example = "qwre1234")
private String loginId;

@NotBlank(message = "이름을 입력하세요")
@Schema(description = "이름", example = "seokhwan")
private String username;

@NotBlank(message = "비밀번호를 입력하세요")
@Schema(description = "비밀번호", example = "!qwer1234")
private String password;
```

`@Schema` 어노테이션을 통해 각 스키마에 대한 정보를 문서화 할 수 있다.