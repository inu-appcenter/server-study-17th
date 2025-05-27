# Validation & Exception Handling

---

## 공통 주제

### 🍀 유효성 검사는 무엇이고 Spring에서 어떻게 적용할 수 있나요?

1. 유효성 검사란?
    1. 유효성 검사의 의미와 이유는 무엇인가요?
        
        유효성 검사(Validation)이란, 어떤 데이터(주로 사용자 또는 외부의 request)가 개발자가 정의한 규칙이나 조건을 만족하는지(유효한지) 확인하는 과정이다. 예를 들어, 이메일 입력란에 올바른 이메일 형식이 들어왔는지, 나이 입력란에 음수가 들어오지는 않았는지를 검사할 필요가 있다.
        
    2. 유효성 검사를 해야 하는 이유는 무엇일까요?
        
        데이터 무결성 보장 / 보안 강화 / 사용자 경험 개선
        
        - 데이터 무결성 : 정확하고 일관성이 있는 데이터, 즉 ‘결함이 없는’ 데이터만이 DB에 들어가야 한다는 원칙이다.
            - 잘못된 값이 DB에 저장되는 것을 방지하여 오류를 막는다.
        - 보안 강화: 악의적인 스크립트나 SQL문 삽입을 방지하여 XSS, SQL 인젝션 공격으로부터 보호한다.
            
            ![image.png](image.png)
            
        - 사용자 경험 개선: 잘못된 입력이 감지되면 즉시 사용자에게 피드백을 공유하여 빠른 수정을 돕는다.
            
            ![image.png](image%201.png)
            
        
2. Spring에서의 유효성 검사는 어떻게 할까요?
    1. 어디에서 유효성 검사를 실시해야 할까요?
        - 서비스 레이어
            - 컨트롤러에 데이터를 전달받아서 비즈니스 로직을 수행한다.
            - → 도메인 로직 진입 전, 필요한 비즈니스 규칙을 코드 내에서 직접 검증한다.
        - 컨트롤러 레이어
            - 입력받은 데이터을 서비스로 전달하고, 서비스가 반환한 데이터를 다시 클라이언트에 전달한다.
            - → 받은 데이터가 예상한 형식에 부합하는지, 필수 데이터가 존재하는지 확인해야 한다.
            - 스프링 MVC는 클라이언트 요청을 컨트롤러 메서드의 파라미터로 바인딩할 때 `@Valid`나 `@Validated` 어노테이션이 붙은 객체에 대해 자동으로 Bean Validation을 수행한다.
        - 컨트롤러에서 필수 입력값, 문자열 길이, 형식 등 검증 → 서비스에서 중복 가입, 권한 등 체크
        
        - 바인딩(Binding)은 HTTP 요청 값을 컨트롤러 메서드의 파라미터나 DTO 필드에 자동으로 매핑하는 과정을 의미한다.
            - 클라이언트가 `{ "loginId": "user1", "password": "abc123" }` 형태의 JSON을 보내면
            - → `@RequestBody`가 JSON을 `PersonLoginRequestDto` 객체로 변환
            - → Jackson 라이브러리가 JSON 속성 이름과 DTO 필드를 매칭하여 `loginId` 필드에 "user1" 값을, `password` 필드에 "abc123" 값을 할당한다.
        
        ```java
        @Getter
        public class PersonLoginRequestDto {
        
            @NotBlank(message = "ID를 입력해주세요.")
            private String loginId;
        
            @NotBlank(message = "비밀번호를 입력해주세요.")
            private String password;
        }
        ```
        
        ```java
            @PostMapping("/login")
            public ResponseEntity<PersonLoginResponseDto> login(@Valid @RequestBody PersonLoginRequestDto loginRequest) {
                return ResponseEntity.ok(personService.login(loginRequest));
            }
        ```
        
    2. Bean Validation은 무엇이며 왜 등장하게 됐나요?
        - 검증 기능을 매번 구현할 경우 컨트롤러의 크기가 커져 불편하고, 단일 책임 원칙에 맞지 않으며 일관성도 떨어질 수 있다.
        - → 특정 필드를 검증하기 위한 null, 길이, 크기, 형식 로직들을 모든 프로젝트에 적용 가능하도록 표준화한 것이 Bean Validation이다.
            - @NotNull, @Size, @Email 등 어노테이션으로 제약 조건을 선언하여 검증 규칙이 명확히 드러난다.
            
            ```java
            public class UserDto {
                @NotBlank(message = "이름은 필수입니다.")
                private String name;
            
                @Email(message = "유효한 이메일 주소를 입력하세요.")
                private String email;
            
                @Min(value = 18, message = "만 18세 이상만 가입 가능합니다.")
                private int age;
            }
            ```
            
    3. @Valid와 @Validated 의 차이는 무엇인가요?
        
        
        |  | @Valid | @Validation |
        | --- | --- | --- |
        |  | 자바 표준 검증 어노테이션 | 스프링 프레임워크 제공 어노테이션 |
        | 적용 계층 | 컨트롤러 | 컨트롤러+다른 계층 |
        | 검증 실패시 발생 에러 | MethodArgumentNotValidException | ConstraintViolationException |
        | 특징 |  | @Valid 기능 + 제약 조건이 적용될 검증 그룹 지정 가능 |
    4. @Valid가 동작되는 시점은 어디일까요?
        
        ![image.png](image%202.png)
        
        ![image.png](image%203.png)
        
        - 모든 요청은 Dispatcher Servlet을 통해 컨트롤러로 전달된다. 이 과정에서 컨트롤러의 메서드 객체를 생성하는 Argument Resolver가 동작하고, 이 단계에서 `@RequestBody` 또는 `@ModelAttribute`로 바인딩 완료된 직후에 @Valid 또한 Argument Resolver에 의해 처리된다.
        - 즉, 핸들러 어댑터 처리 과정 중에 동작하기 때문에 컨트롤러(핸들러)에서만 동작한다.
        - @Validated는 컨트롤러 외의 계층에서도 유효성 검사를 할 수 있도록 스프링에서 지원하는 어노테이션이다.
            
            ```java
            @Service
            @Validated
            public class UserService {
            
            	public void addUser(@Valid AddUserRequest addUserRequest) {
            		...
            	}
            }
            ```
            
    5. 유효성 검사를 위한 어노테이션은 어떤 것들이 있을까요? (@Email, @NotNull 등)
        - Null/Blank 체크
            - `@NotNull`: null을 허용하지 않는다
            - `@NotEmpty`: null, 빈 문자열을 허용하지 않지만 공백은 허용한다.
            - `@NotBlank`: null, 빈 문자열, 공백을 허용하지 않는다.
        - 크기/길이 제한
            - `@Size(min=, max=)`: 크기 또는 길이를 제한한다.
        - 숫자 범위
            - `@Min`, `@Max`: 정수의 최소, 최대값을 보장한다.
            - `@DecimalMin`, `@DecimalMax`: 소수점을 포함한 숫자의 최대 최소 값을 제한한다.
        - 형식 검증
            - `@Email`: 이메일 형식을 검증한다.
        - 날짜/시간 검증
            - `@Past` ,  `@Future` , `@PastOrPresent`, `@FutureOrPresent` : 날짜가 과거/미래인지 검증한다.
        - 기타
            - `@Positive`, `@PositiveOrZero`, `@Negative`: 부호를 검증한다.

### 🍀 에러와 예외의 차이는 무엇일까요? Spring에서의 예외처리는 어떻게 진행할까요?

1. 예외 처리의 개념
    1. 에러와 예외의 차이는 무엇인가요?
        - 에러(Error): JVM이나 시스템 레벨에서 발생하여 시스템이 종료되어야 할 수준의 심각한 문제. 개발자가 미리 예측하여 방지할 수 없다
            - OutOfMemoryError, StackOverflowError
        - 예외(Exception): 개발자가 구현한 로직에서 발생한 실수, 또는 사용자의 영향에 의해 발생하는 문제. 미리 예측하여 방지할 수 있기 때문에 상황에 맞는 예외처리가 필요하다.
    2. 예외 처리의 방법  (예외 복구, 예외 처리 회피, 예외 전환)
        - 예외 복구
            - 예외 상황을 파악하고, 문제를 해결하여 정상 상태로 돌려놓는 것이다. 정상 상태의 다른 작업 흐름으로 유도하는 것도 예외 복구에 해당한다.
            - try-catch → 예외가 발생해도 어플리케이션은 정상적으로 동작한다.
        - 예외 처리 회피
            - 예외 발생 시 현재 메서드에서 직접 처리하는 대신 throws를 통해 호출자에게 위임한다.
        - 예외 전환
            - 예외 처리 회피처럼 메서드 밖으로 예외를 던지지만, 적절한 예외로 전환해서 넘기는 방식이다. → 발생한 예외에 대해 또다른 예외로 변경하여 던지는 것.
            - 호출된 메서드에서 발생한 예외를 호출한 메서드가 이해하기 쉬운 예외로 변환하여 전달
    3. 자바의 예외 클래스는? (Checked Exception / Unchecked Exception)
        - Checked Exception
            - 컴파일러가 예외처리를 강제(확인)하는 예외이다.
            - 사용자의 동작에 의해 발생될 수 있는 예외로, 존재하지 않는 파일의 입력이나 클래스 이름을 잘못 입력한 경우가 이에 해당된다.(`FileNotFoundException, ClassNotFoundException`)
        - Unchecked Exception
            - 컴파일러가 예외처리를 강제하지 않는 예외이다.
            - 개발자의 실수에 의해 발생될 수 있는 예외로, null인 참조변수의 멤버호출하는 `NullPointException`이 이에 해당된다.
        
        ⇒ 예외 처리를 컴파일 시점에서 강제하는지의 차이(Checked Exception에 대한 예외 처리가 되어 있지 않는 경우에는 컴파일 에러가 발생한다)
        
2. Spring에서의 예외 처리
    1. @ControllerAdvice, @ExceptionHandler 은 무엇이며 또한 이들을 활용한 예외처리 방식은 무엇인가요?
        - @ControllerAdvice
            - 모든 컨트롤러에서 발생할 예외를 정의한다. → 한 곳에서 관리 및 처리할 수 있도록 하는 어노테이션
        - @ExceptionHandler
            - 발생하는 예외마다 처리할 메소드를 정의한다.
            
            ```java
            @ControllerAdvice
            public class ExampleControllerAdvice {
            
                @ExceptionHandler(NullPointerException.class)
                public String handleNullPointerException(NullPointerException ex) {
                    return "error";
                }
            }
            ```
            
    2. ControllerAdvice와 RestControllerAdvice의 차이가 무엇인가요?
        - 예외 발생 시 뷰 이름을 반환한다 → @ControllerAdvice
        - JSON 형태로 예외 정보를 반환한다 → @RestControllerAdvice
        
        ```java
        
        ```
        
    3. ControllerAdvice 내 우선 순위와 ExceptionHandler 내 우선 순위
        - ControllerAdvice
            - 여러 예외 처리 클래스를 정의할 때 `@Order` 어노테이션을 사용하여 적용 순서를 지정한다. 순서 값이 낮을수록 먼저 적용된다.
            - →  `@Order(1)`이 `@Order(2)`보다 먼저 적용된다. 지정되지 않은 경우 우선순위가 낮아진다.
            
            ```java
            @Order(1)
            @ControllerAdvice
            public class GlobalControllerExceptionHandler {
                // ...
            }
            ```
            
        - ExceptionHandler
            - 예외 발생 시 일치하는 예외 처리 메서드를 찾고, 찾지 못한 경우 상위 예외 처리 메서드를 순차적으로 찾는다.
            
            ```java
            @ExceptionHandler(NullPointerException.class)
            public String handleNPE(NullPointerException ex) { ... }
            ```
            
            - 발생한 예외와 메서드에 지정된 예외 타입이 일치하는 @ExceptionHandler가 가장 먼저 선택된다.
            
            ```java
            @ExceptionHandler(RuntimeException.class)
            public String handleRuntime(RuntimeException ex) { ... }
            ```
            
            - 정확한 핸들러가 없으면, 상속 계층을 따라 더 일반적인 예외 타입(`RuntimeException.class` 또는 `Exception.class`)을 처리하는 메서드를 찾는다.