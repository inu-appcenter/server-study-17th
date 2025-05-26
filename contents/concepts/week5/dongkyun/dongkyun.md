# Validation & Exception Handling

---

## 공통 주제

### **🍀 유효성 검사는 무엇이고 Spring에서 어떻게 적용할 수 있나요?**

1. ⚠️ 유효성 검사란?
    1. 유효성 검사의 의미와 이유는 무엇인가요?
        1. **유효성 검사(Validation)**는 사용자가 입력한 데이터가 ***정해진 형식, 조건, 규칙*에 맞는지 확인하는 과정**
    2. 유효성 검사를 해야 하는 이유는 무엇일까요?
        1. 유효하지 않은 입력을 방지함으로써, 오류를 줄이고, 보안 문제를 예방하며, 프로그램의 안정성과 신뢰성을 높이기 위해서 필요하다.

2. Spring에서의 유효성 검사는 어떻게 할까요?
    1. 어디에서 유효성 검사를 실시해야 할까요?
        1. *🖥️   Controller Layer*
            1. 사용자가 보낸 데이터( ex : RequestBody)를 컨트롤러 진입 시점에 검사
            2. @Valid / @Validated와 함께 사용
        2. *⌨️   Service Layer*
            1. 입력은 잘 통과 했지만 비즈니스 조건에 맞지 않는 경우
            2. ex) 중복 체크, 허용되지 않은 범위
        3. *💾   Repository Layer*
            1. DB에서 제약 조건에 위배될 경우
            2. Column(nullable = false) 등

    2. Bean Validation은 무엇이며 왜 등장하게 됐나요?
        1. **Bean Validation**이란 Java에서 표준으로 제공하는 유효성 검사 프레임워크
        2. 등장 배경
            1. 기존에는 개발자들이 각자의 방식으로 유효성 검사를 함
                1. 중복 코드 발생
                2. 재사용성 낮음
                3. 비표준적 방식으로 유지보수가 어려움
            2. Java에서 공통된 유효성 검사 표준 ( JSR 380 )을 정의
            3. Spring에서 이를 기반으로 쉽게 사용할 수 있도록 통합

    3. @Valid와 @Validated 의 차이는 무엇인가요?
        1. *@Valid*
            - 자바 표준 스펙 기술
            - 주로 객체나 메서드 파라미터의 유효성 검사를 위해 사용
            - 컨트롤러 단계에서만 사용 가능
            - 검증이 필요한 객체를 파라미터로 사용하는 메서드에 @Valid를 붙여 사용

            ```java
                // 회원가입
                @PostMapping("/signup")
                public ResponseEntity<SignupResponseDto> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
                    return ResponseEntity.status(HttpStatus.OK).body(memberService.signup(signupRequestDto));
                }
            ```

            - ***💨 동작 원리***
                1. 클라이언트의 요청을 받은 Dispatcher Servlet은 적절한 컨트롤러에 요청을 전달
                2. @RequestBody가 붙은 파라미터는 JSON → 객체 변환 과정에서 RequestResponseBodyMethodProcessor의 resolveArgument() 메서드에서 유효성 검사 수행
                3. 이때 LocalValidatorFactoryBean을 Validator로 이용해 Validation 수행
                4. @ModelAttribute 사용 시 ModelAttributeMethodProcessor에서 동일한 방식으로 유효성 검사
                5. 검사 실패 시 MethodArgumentNotValidException 예외 발생

        2. *@Validated*
            - 자바 표준 기술이 아닌 스프링에서 제공하는 기술
            - 컨트롤러가 아닌 다른 계층에서도 사용 가능
                - 클래스에 @Validated를 붙이고 검증이 필요한 파라미터에 @Valid를 붙여줌

            ```java
            @Service
            @Validated
            public class MemberService {
            
                @Transactional
                public SignupResponseDto signup(@Valid SignupRequestDto signupRequestDto) {
            				...
                }
            ```

            - ***💨 동작 원리***
                1. AOP를 기반으로 메서드의 요청을 가로채 유효성 검증을 진행
                2. 스프링 부트가 ValidationAutoConfiguration을 통해MethodValidationPostProcessor 클래스를 등록
                3. 이 MethodValidationPostProcessor가 내부적으로 AOP Advice인 MethodValidationInterceptor를 등록
                4. MethodValidationInterceptor가 Point Cut으로 메서드 요청을 가로채 유효성 검사를 진행
                5. 검사 실패 시 ConstraintViolationException 예외 발생

    4. @Valid가 동작되는 시점은 어디일까요?
        1. 컨트롤러 메서드가 호출되기 직전 동작
        2. ArgumentResolver (resolveArgument() 내부)에서 동작

    5. 유효성 검사를 위한 어노테이션은 어떤 것들이 있을까요? (@Email, @NotNull 등)
        1. *@NotNull* → null 값이 아닌지 검증
        2. *@NotEmpty →* null, 빈 문자열("")이 아닌지 검증, 공백(" ")은 통과
        3. *@NotBlank* → null, 빈 문자열(""), 공백(" ")이 아닌지 검증
        4. *@Min* → 최솟값 검증
        5. *@Max* → 최댓값 검증
        6. *@Pattern* → 패턴 검증
        7. *@Email →* 이메일 형식인지 검증

### **🍀 에러와 예외의 차이는 무엇일까요? Spring에서의 예외처리는 어떻게 진행할까요?**

1. 예외 처리의 개념
    1. 에러와 예외의 차이는 무엇인가요?
        1. *❌ **에러***
            - 시스템적인 심각한 문제
            - 복구 불가능 → 처리하지 않음
        2. *⭕️ **예외***
            - 개발자가 처리할 수 있는 문제
            - 복구 가능 → try - catch 등으로 처리

    2. 예외 처리의 방법 (예외 복구, 예외 처리 회피, 예외 전환)
        1. ***예외 복구***
            1. 예외 상황을 파악하고 문제를 해결해 정상 상태로 돌려 놓는 방법
            2. ex ) try - catch로 예외를 처리하고, 가능한 동작을 이어감

            ```java
            try {
                int result = 10 / 0;
            } catch (ArithmeticException e) {
                System.out.println("0으로 나눌 수 없습니다.");
            }
            ```

        2. ***예외 처리 회피***
            1. 해당 메서드에서 직접 처리하지 않고, 호출한 쪽으로 예외를 전달하는 방법

            ```java
            public void add() throws SQLException {
                try {
                    // ...
                } catch(SQLException e) {
                    e.printStackTrace(); // 🔹 예외 내용을 로그로 출력
                    throw e;              // 🔹 예외를 다시 던짐 → 예외 회피
                }
            }
            ```

        3. ***예외 전환***
            1. 예외를 더 의미있는 예외로 바꾸어 던짐
            2. 계층화된 예외 처리, Spring에서도 자주 사용

            ```java
            try {
                userRepository.save(user);
            } catch (DataIntegrityViolationException e) {
                throw new CustomException(ErrorCode.DUPLICATED_EMAIL); // 예외 전환
            }
            ```

    3. 자바의 예외 클래스는? (Checked Exception / Unchecked Exception)
        1. *✅  **Checked Exception***
            1. 컴파일 시점에 반드시 예외 처리를 해야 함
            2. try - catch / throws
            3. 대표적으로 입출력, 네트워크, DB 등 외부와의 상호작용에서 발생
            4. Exception 클래스를 직접 상속한 클래스들
                1. ex) `IOException, SQLException, FileNotFoundException`

        2. *☑️  **Unchecked Exception***
            1. 컴파일러가 예외 처리를 강제하지 않음
            2. 프로그램 실행 중 발생
            3. 대부분 개발자 실수 또는 로직 오류
            4. RuntimeException을 상속한 클래스들
                1. ex) `NullPointerException, IllegalArgumentException, IndexOutOfBoundsException`

2. Spring에서의 예외 처리
    1. @ControllerAdvice, @ExceptionHandler 은 무엇이며 또한 이들을 활용한 예외처리 방식은 무엇인가요?
        - ***@ControllerAdvice***
            - 모든 컨트롤러의 예외를 한 곳에서 처리할 수 있게 해주는 어노테이션
            - 예외 처리, 바인딩 설정, 모델 객체 추가 등을 전역적으로 적용 가능

            ```java
            @ControllerAdvice
            public class GlobalExceptionHandler {
                // ...
            }
            ```

        - ***@ExceptionHandler***
            - 특정 예외가 발생했을 때 호출될 메서드를 지정하는 어노테이션
            - @Controller, @ControllerAdvice 클래스 안에서 사용 가능
            - 다양한 예외 유형에 맞춰 여러 개 정의 가능

            ```java
                @ExceptionHandler(CustomException.class)
                protected ResponseEntity<ErrorResponseEntity> handleCustomException(CustomException ex) {
                    return ErrorResponseEntity.toResponseEntity(ex.getErrorCode());
                }
            ```

    2. ControllerAdvice와 RestControllerAdvice의 차이가 무엇인가요?
        1. ***📢  ControllerAdvice***
            1. 예외 처리, 바인딩 등 컨트롤러 전역 설정
            2. 스프링 MVC의 전통적인 컨트롤러 방식과 호환
            3. 뷰 혹은 JSON을 반환
                1. 이때 JSON을 반환하려면 @ResponseBody를 메서드에 추가해야 함 
                
                ```java
                @ControllerAdvice
                public class GlobalExceptionHandler {
                
                    @ExceptionHandler(CustomException.class)
                    @ResponseBody // 반드시 붙여야 JSON 응답됨
                    public ErrorResponse handleCustom(CustomException e) {
                        return new ErrorResponse("에러", e.getMessage());
                    }
                }
                ```
            
        2. ***🔊  RestControllerAdvice***
            1. @ControllerAdvice + @ResponseBody 조합
            2. 항상 JSON 등 객체를 HTTP Body로 반환
                1. 자동으로 HTTP Body ( JSON )으로 변환되므로 @ResponseBody 별도 지정 필요 없음
            3. 주로 REST API 프로젝트에서 사용 
            
            ```java
            @RestControllerAdvice
            public class GlobalExceptionHandler {
            
                @ExceptionHandler(CustomException.class)
                public ErrorResponse handleCustom(CustomException e) {
                    return new ErrorResponse("에러", e.getMessage());
                }
            }
            ```
        
    3. ControllerAdvice 내 우선 순위와 ExceptionHandler 내 우선 순위
        1. ***@ControllerAdvice 내 우선순위***
            1. 우선 순위 기준
                1. *@Order*
                    
                    → 숫자가 작을 수록 우선 순위 높음
                    
                2. *Ordered 인터페이스 구현*
                    
                    → getOrder()의 반환 값이 작을 수록 우선 순위 높음
                    
                3. *등록 순서*
                    
                    → 명시적 우선 순위가 없다면 클래스 스캔 순서에 따라 다름
            
            ```java
            @RestControllerAdvice
            @Order(1)
            public class HighPriorityAdvice {
                // 가장 먼저 적용됨
            }
            
            @RestControllerAdvice
            @Order(2)
            public class LowPriorityAdvice {
                // 나중에 적용됨
            }
            ```
            
        2. ***@ExceptionHanlder 내 우선 순위***
            1. 가장 가까운 컨트롤러 내부의 @ExceptionHanlder가 먼저 적용
                1. 예외가 발생한 클래스에 @ExceptionHanlder가 있으면 그 메서드가 먼저 사용됨
                2. 컨트롤러가 없으면 그 때 @ControllerAdvice로 이동
            2. @ControllerAdvice끼리의 우선 순위는 위의 @Order 기준 적용
            3. **예외 타입의 구체성**
                1. 여러 @ExceptionHanlder가 있을 때, 더 구체적인 예외 타입을 처리하는 메서드가 우선 
                
                ```java
                @ExceptionHandler(RuntimeException.class) // ⬅ 일반적
                public ResponseEntity<String> handleRuntime() { ... }
                
                @ExceptionHandler(NullPointerException.class) // ⬅ 더 구체적
                public ResponseEntity<String> handleNPE() { ... }
                ```


## 🔎 이번 주 과제

<aside>
✅ 전 주에 작성한 API에 유효성 검사 적용하기 + 예외 처리 적용하기

[커스텀 예외의 4가지 Best Practices [링크 참고]](https://parkadd.tistory.com/69)

</aside>