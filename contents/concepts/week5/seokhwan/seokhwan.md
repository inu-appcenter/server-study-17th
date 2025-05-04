# Validation & Exception Handling

추가 일시: 2025년 5월 4일 오전 1:30
강의: Appcenter_Server

# ⚠️ Validation & Exception Handling

---

## 🍀 유효성 검사는 무엇이고 Spring에서 어떻게 적용할 수 있나요?

---

### ✅ 유효성 검사란?

![image.png](image.png)

유효성 검사(Validation)는 사용자로부터 입력받은 데이터가 애플리케이션에서 요구하는 형식과 조건을 충족하는지 확인하는 과정입니다.

이는 데이터가 시스템에서 정의한 규칙과 제약 조건을 만족하는지 검증하여, 잘못된 데이터가 처리되는 것을 방지합니다.

❓**유효성 검사를 해야 하는 이유**

1. 데이터 무결성 보장
    - 잘못된 형식의 데이터가 데이터베이스에 저장되는 것을 방지
    - 비즈니스 규칙에 맞는 데이터만 시스템에서 처리되도록 보장
2. 보안 강화
    - SQL 인젝션, XSS 등의 보안 공격 방지
    - 악의적인 입력으로부터 시스템 보호
3. 사용자 경험 개선
    - 즉각적인 피드백 제공으로 사용자의 입력 오류 수정 지원
    - 명확한 오류 메시지로 올바른 입력 방법 안내
4. 시스템 안정성 확보
    - 예상치 못한 입력으로 인한 시스템 오류나 충돌 방지
    - 애플리케이션의 안정적인 동작 보장
5. 비용 절감
    - 잘못된 데이터로 인한 후속 처리 비용 감소
    - 데이터 정제 작업 최소화

### ✅ Spring에서의 유효성 검사는 어떻게 할까요?

Spring에서는 두 가지 방법으로 유효성 검사를 할 수 있습니다.

**@Valid 와 @Validated 비교**

![image.png](image%201.png)

`@Valid` 는 오직 컨트롤러 메소드의 유효성만 검사가 가능하다는 점과

`@Validated` 는 다른 계층에서도 유효성 검사가 가능하다는 부분에서도 차이가 있습니다.

```java
// @Valid - 그룹 검증 불가
@PostMapping("/users")
public ResponseEntity<User> createUser(@Valid @RequestBody UserDto userDto) {
    return ResponseEntity.ok(userService.createUser(userDto));
}

// @Validated - 그룹 검증 가능
@PostMapping("/users")
public ResponseEntity<User> createUser(
    @Validated(CreateGroup.class) @RequestBody UserDto userDto) {
    return ResponseEntity.ok(userService.createUser(userDto));
}
```

**@Valid 란?**

`@Valid` 는 Java Bean Validation API에서 제공하는 어노테이션으로 객체의 유효성검사를 트리거 하는 역할을 합니다.

<aside>
🔄

**동작과정**

1. Spring MVC가 요청을 받으면 @Valid가 붙은 파라미터를 발견
2. 해당 객체의 모든 필드에 선언된 제약 조건을 검사
3. 유효성 검사 실패 시 MethodArgumentNotValidException 발생
4. @Valid가 필드에 사용된 경우, 해당 필드의 중첩 객체도 검증

</aside>

**유효성검사 실제 사용 예시**

```java
// 의존성 추가 (build.gradle)
implementation 'org.springframework.boot:spring-boot-starter-validation'
```

```java
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest request) {
        // 유효성 검사 통과 후 실행
        OrderResponse response = orderService.createOrder(request);
        return ResponseEntity.ok(response);
    }
}

// DTO with nested validation
public class OrderRequest {
    @NotNull(message = "주문 유형은 필수입니다")
    private OrderType orderType;
    
    @Valid  // 중첩 객체 검증
    @NotNull(message = "고객 정보는 필수입니다")
    private CustomerInfo customer;
    
    @Valid  // 컬렉션 내 각 요소 검증
    @NotEmpty(message = "주문 항목은 최소 1개 이상이어야 합니다")
    private List<OrderItem> items;
    
    @Future(message = "배송일은 현재 시간 이후여야 합니다")
    private LocalDateTime deliveryDate;
}

// 전역 예외 처리
@RestControllerAdvice
public class ValidationExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        
        ValidationErrorResponse errorResponse = new ValidationErrorResponse();
        fieldErrors.forEach(error -> 
            errorResponse.addError(error.getField(), error.getDefaultMessage())
        );
        
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
```

- **유효성 검사 어노테이션**
    
    `@Null`
    null만 허용한다.
    
    `@NotNull`
    빈 문자열(""), 공백(" ")은 허용하되, Null은 허용하지 않음
    
    `@NotEmpty`
    공백(" ")은 허용하되, Null과 빈 문자열("")은 허용하지 않음
    
    `@NotBlank`
    null, 빈 문자열(""), 공백(" ") 모두 허용하지 않는다.
    
    `@Email`
    이메일 형식을 검사한다. 단, 빈 문자열("")의 경우엔 통과 시킨다. ( @Pattern을 통한 정규식 검사를 더 많이 사용
    
    `@Pattern(regexp = )`
    정규식 검사할 때 사용한다.
    
    `@Size(min=, max=)`
    길이를 제한할 때 사용한다.
    
    `@Max(value = )`
    value 이하의 값만 허용한다.
    
    `@Min(value = )`
    value 이상의 값만 허용한다.
    
    `@Positive`
    값을 양수로 제한한다.
    
    `@PositiveOrZero`
    값을 양수와 0만 가능하도록 제한한다.
    
    `@Negative`
    값을 음수로 제한한다.
    
    `@NegativeOrZero`
    값을 음수와 0만 가능하도록 제한한다.
    
    `@Future`
    Now 보다 미래의 날짜, 시간이어야 한다.
    
    `@FutureOrPresent`
    Now 거나 미래의 날짜, 시간이어야 한다.
    
    `@Past`
    Now 보다 과거의 날짜, 시간이어야 한다.
    
    `@PastFutureOrPresent`
    Now 거나 과거의 날짜, 시간이어야 한다.
    

**유효성 검사시 유의사항**

- **성능 고려사항**
    - 복잡한 객체 구조에서는 검증 비용이 증가할 수 있음
    - 필요한 경우 검증 그룹을 사용하여 선택적 검증 수행
- **중첩 검증**
    - @Valid를 사용하지 않으면 중첩 객체는 검증되지 않음
    - 컬렉션 요소들도 검증하려면 반드시 @Valid 필요
- **예외 처리**
    - 유효성 검사 실패 시 자동으로 예외가 발생
    - 적절한 예외 처리기를 구현하여 사용자 친화적인 응답 제공

**❓ 중첩 객체가 검증되지 않는다는 것은 어떤 의미인가요?**

부모 객체의 유효성 검사만 수행되고, 포함된 자식 객체의 유효성 검사는 수행되지 않는다는 의미입니다.

## 🍀 에러와 예외의 차이는 무엇일까요? Spring에서의 예외처리는 어떻게 진행할까요?

---

![image.png](image%202.png)

### ✅ 예외 처리의 개념

**에러와 예외의 차이**

**에러(Error)**

- 시스템 레벨에서 발생하는 심각한 문제
- 개발자가 미리 예측하거나 처리할 수 없는 상황
- 애플리케이션의 정상적인 실행을 방해하는 치명적 문제
- JVM의 메모리 부족, 스택 오버플로우 등
- java.lang.Error 클래스를 상속
- 예: OutOfMemoryError, StackOverflowError, VirtualMachineError

**예외(Exception)**

- 애플리케이션 레벨에서 발생하는 문제
- 개발자가 예측하고 적절히 처리할 수 있는 상황
- 정상적인 프로그램 흐름을 방해하지만 복구 가능
- java.lang.Exception 클래스를 상속
- 예: NullPointerException, IllegalArgumentException, IOException

![image.png](image%203.png)

**예외 처리 방법**

예외는 예외 복구, 예외 처리 회피,  예외 전환으로 나누어 처리 할 수 있습니다.

1. 예외 복구 (Exception Recovery)
    - 예외 상황을 파악하고 문제를 해결하여 정상 상태로 돌아가는 방법
    - 재시도, 대체 값 사용, 기본값 반환 등

```java
public class AuthService {
    public LoginResult login(String username, String password) {
        try {
            return authManager.authenticate(username, password);
        } catch (DatabaseException e) {
            // DB 실패 시 캐시에서 인증
            return authenticateFromCache(username, password);
        } catch (InvalidPasswordException e) {
            // 실패 횟수 증가 후 계정 잠금
            int failCount = incrementFailCount(username);
            if (failCount >= 5) {
                lockAccount(username);
            }
            return LoginResult.failed("비밀번호 오류");
        }
    }
}
```

1. **예외 처리 회피 (Exception Avoidance)**
    - 예외를 자신이 처리하지 않고 호출한 쪽으로 던짐
    - throws 키워드 사용

```java
public class TokenService {
    // 암호화 예외를 호출자에게 전파
    public String createToken(User user) throws CryptoException {
        return jwtEncoder.encode(user.getId(), user.getRoles());
    }
}

public class AuthController {
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            User user = authService.authenticate(request);
            String token = tokenService.createToken(user);  // 여기서 처리
            return ResponseEntity.ok(token);
        } catch (CryptoException e) {
            return ResponseEntity.status(500).body("토큰 생성 실패");
        }
    }
}
```

1. 예외 전환 (Exception Translation)
    - 발생한 예외를 더 적절한 예외로 변환하여 던짐
    - 저수준 예외를 고수준 예외로 변환
    - check 예외를 uncheck 예외로 변환

```java
public class UserRepository {
    public User findByEmail(String email) {
        try {
            return jdbcTemplate.queryForObject(
                "SELECT * FROM users WHERE email = ?",
                userMapper,
                email
            );
        } catch (EmptyResultDataAccessException e) {
            // DB 예외를 도메인 예외로 전환
            throw new UserNotFoundException("사용자 없음: " + email);
        } catch (DataAccessException e) {
            // 일반 DB 예외를 인증 예외로 전환
            throw new AuthenticationException("인증 시스템 오류", e);
        }
    }
}
```

**JAVA의 예외 처피 클래스는?**

Checked Exception 

- 컴파일 시점에 체크되는 예외
- 반드시 try-catch로 처리하거나 throws로 선언해야 함
- Exception 클래스를 상속하지만 RuntimeException은 상속하지 않음
- 복구 가능한 예외 상황에 사용

대표 예외

IOException, SQLException

Unchecked Exception

- 런타임 시점에 발생하는 예외
- 처리가 강제되지 않음 (선택적)
- RuntimeException 클래스를 상속
- 프로그래밍 오류나 예상치 못한 상황에 사용

대표 예외

NullPointerException, IllegalArgmentException, IndexOutOfBoundException, SystemException

### ✅ Spring에서의 예외 처리

**`@ControllerAdvice`, `@ExceptionHandler` 은 무엇이며 또한 이들을 활용한 예외처리 방식은 무엇인가요?**

두 어노테이션은 Spring에서 전역적인 예외 처리를 구현하기 위한 어노테이션입니다.

ExceptionHandler는 코드를 작성한 컨트롤러에서만 발생하는 예외를 처리합니다.

```java
// 이 컨트롤러 내에서만 동작
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ex.getMessage());
    }
```

코드를 작성하다보면 같은 예외에 대해 여러 컨트롤러에서 동일한 예외 처리를 하고 싶다면 컨트롤러마다 동일한 메서드를 작성해주어야 하는 불편한 상황이 발생하게 됩니다.

이러한 코드의 중복을 해결하기 위해 ControllerAdvice를 사용할 수 있습니다.

ControllerAdvice는 모든 컨트롤러에서 발생하는 예외를 한 곳에서 처리합니다.

즉, 전역 예외 처리기(Global Exception Handler)역할을 합니다. 

```java
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponseDto> handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
            .status(errorCode.getStatus())  // HTTP 상태 코드 설정
            .body(ErrorResponseDto.fromErrorCode(errorCode));
    }
    
    // 다른 예외들도 처리
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ErrorResponseDto> handleIllegalArgument(IllegalArgumentException e) {
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
            .code("BAD_REQUEST")
            .message(e.getMessage())
            .build();
        
        return ResponseEntity.badRequest().body(errorResponse);
    }
    
    // 모든 예외 처리 (fallback)
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponseDto> handleAllExceptions(Exception e) {
        log.error("Unexpected error occurred", e);
        
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
            .code("INTERNAL_SERVER_ERROR")
            .message("서버 오류가 발생했습니다")
            .build();
        
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
```

**ControllerAdvice와 RestControllerAdvice의 차이가 무엇인가요?**

RestControllerAdvice의 인터페이스를 확인해보면

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ControllerAdvice
@ResponseBody
public @interface RestControllerAdvice{...}
```

ControllerAdvice

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ControllerAdvice {...}
```

`@ResponseBody` 어노테이션이 붙어있는 것을 확인 할 수 있고, 응답을 Json으로 처리한다는 것을 알 수 있습니다.

`@RestControllerAdvice` = `@ControllerAdvice` + `@ResponseBody`  라고 생각하면 편하다.

~~정통적인 웹 애플리케이션이라면 `@ControllerAdvice` REST API를 개발한다면 `@RestControllerAdvice` 사용하라는데… 그냥 RestController 사용하면 될 것 같다.~~

**ControllerAdvice 내 우선 순위와 ExceptionHandler 내 우선 순위**

‘Spring에서 우선순위는 항상 구체적인것이 우선순위를 가진다.’ 라는 개념은 머릿속에 넣고 이해하면 편하다.

`@ControllerAdvice`와 `@ExceptionHandler` 의 우선순위를 결정하는 몇가지 요인이 있는데

첫번째로는 `@Order` 어노테이션이다.

```java
@ControllerAdvice
@Order(1)  // 낮은 숫자가 높은 우선순위
public class HighPriorityExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(500).body("High Priority Handler");
    }
}

@ControllerAdvice
@Order(2)
public class LowPriorityExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(500).body("Low Priority Handler");
    }
}
```

우선순위를 개발자가 직접 설정하는 방법이다.

낮은 숫자의 Order어노테이션을 가진 class를 사용하게 된다.

ExceptionHandler에서는 설명했듯이 구체적인 예외가 우선순위를 가지게된다.

```java
@ControllerAdvice
public class ExceptionPriorityHandler {
    
    // 우선순위 1: 가장 구체적인 예외
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointer(NullPointerException ex) {
        return ResponseEntity.status(500).body("NullPointer Handler");
    }
    
    // 우선순위 2: 중간 레벨 예외
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntime(RuntimeException ex) {
        return ResponseEntity.status(500).body("Runtime Handler");
    }
    
    // 우선순위 3: 가장 일반적인 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(500).body("General Handler");
    }
}
```

이 둘을 복합적으로 사용하는 상황을 가정하면 더 구체적이라 할 수 있는 `@ExceptionHandler` 가 우선순위를 가지게 된다.