# 🧱 Controller & Service Layer

---

## 공통 주제

### 🍀 Controller Layer는 무엇인가요?

1. Controller Layer는 어떤 역할을 하며, 어떤 구조로 이루어져 있나요?
    1. Controller는 Model과 View 사이에서 상호작용을 조정하고 제어하는 역할
    2. 사용자의 요청을 Model에 전달하고 Model의 결과를 반환해 View를 업데이트
    3. Spring MVC 구조에서 Controller, 즉 클라이언트 요청을 처리하고 Model과 View를 연결하는 구조를 가짐
   
   <br>
   
2. ResponseEntity는 무엇이며, 어떻게 활용할 수 있나요?
    1. Spring MVC에서 HTTP 요청에 대한 응답을 제어하는 데 사용하는 클래스
    2. Body, Status Code, Header 를 담을 수 있어 세밀한 응답 관리

       ![스크린샷 2025-04-09 오전 3.12.01.png](%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202025-04-09%20%EC%98%A4%EC%A0%84%203.12.01.png)

    3. status에 200 OK, body에 서비스 로직의 함수를 호출한 반환 값을 담음
    4. 요청을 받아서 이에 대한 서비스 로직만 호출해 결과를 반환하는 모습

### 🍀 Service Layer는 무엇인가요?

1. Service Layer는 어떤 역할을 하며, 어떤 구조로 이루어져 있나요?
    1. 비즈니스 로직을 처리하는 중간 계층으로 Controller와 Repository 사이에서 역할을 수행
    2. 데이터 가공 및 검증 등의 로직을 담당
    3. Controller가 비즈니스 로직을 직접 다루지 않게 하여 역할 분리

       ![스크린샷 2025-04-09 오전 3.12.40.png](%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202025-04-09%20%EC%98%A4%EC%A0%84%203.12.40.png)

    4. Controller의 요청을 받아 Repository를 호출해 요청에 대한 결과를 반환

### 🍀 Spring에서 요청을 어떤 방식으로 처리하나요?

1. 요청과 응답이란 무엇인가요?
    1. 요청과 응답에는 어떤 값이 담기나요?
        1. 요청 : 클라이언트 → 서버로 보내는 데이터
            1. 사용자의 행동이 서버로 전달 되는 것을 의미
            2. 담기는 값
                1. HTTP Method ( GET, POST, PUT, DELETE )
                2. URL
                3. 헤더 ( 인증 정보, Content-Type 등 )
                4. Body ( JSON 등 )
                5. 쿼리 파라미터
        2. 응답 : 서버 → 클라이언트로 보내는 데이터
            1. 요청에 대한 결과, 즉 성공, 실패, 데이터 반환, 에러 메세지 등
            2. 담기는 값
                1. 응답 코드 ( 200, 404, 400 )
                2. 응답 형식, 쿠키 등
                3. Body ( JSON, HTML, 이미지 등 )
        
        <br>

2. Spring에서 요청을 어떻게 처리하나요?
    1. Spring에서는 들어오는 HTTP 요청을 받아서, 이를 Controller → Service → Repository 순으로 처리하고 다시 HTTP 응답으로 돌려보낸다.
    2. **스프링에서 객체의 직렬화와 역직렬화는 어떻게 이뤄질까요?**
        1. 직렬화 : Java 객체 → JSON 변환
        2. 역직렬화 : JSON → Java 객체 변환
        3. **Jackson**
            1. Spring Boot에서는 Jackson 라이브러리를 사용해 자동으로 직렬화, 역직렬화
            2. 동작 구조
                1. 직렬화
                    1. @ResponseBody / ResponseEntity<>를 리턴할 경우
                    2. Spring은 HttpMessageConverter를 찾음
                    3. MappingJackson2HttpMessageConverter가 직렬화 수행
                2. 역직렬화
                    1. @RequestBody가 붙은 파라미터 발견
                    2. Spring은 HttpMessageConverter를 찾음
                    3. MappingJackson2HttpMessageConverter가 직렬화 수행
            3. 필요에 따라 커스터마이징도 가능
                1. Json과 Java의 이름 매핑을 커스텀

                    ```json
                    @JsonProperty("member_name") 
                    private String name; 
                    ```

                2. 원하는 패턴으로 지정

                    ```json
                    @JsonFormat(pattern = "yyyy-MM-dd")
                    private LocalDate date;
                    ```

    3. **ObjectMapper의 작동방식**
        1. ObjectMapper란 Java 객체와 JSON을 상호 변환해주는 엔진
        2. Flow
            1. 직렬화
                1. Java 객체 내부의 필드들을 reflection으로 검사
                2. getter 메서드를 통해 값을 읽음
                3. JSON 키-값으로 변환
                4. JSON 문자열 생성
            2. 역직렬화
                1. JSON 문자열을 파싱해서 key-value map으로 만듦
                2. 대상 클래스의 생성자와 setter를 통해 값 주입
                3. 최종적으로 객체 생성
        3. Spring Boot는 ObjectMapper를 빈으로 자동 등록
        4. Jackson의 핵심 클래스 중 하나
    
    <br>
    
    4. @RequestBody과 @ModelAttribute는 어떤 차이가 있으며 언제 사용해야 할까요?
        1. 두 어노테이션 모두 Controller에서 요청 데이터를 바인딩 할 때 사용
        2. 바인딩 → 요청 데이터를 자바 객체 필드에 자동으로 채워주는 것
        3. @RequestBody
            1. JSON, XML 등 Request Body로 요청
            2. HTTP Body에 담아서 사용
            3. API 요청 처리 시 주로 사용
            4. Jackson으로 역직렬화
        4. @ModelAttribute
            1. 쿼리 파라미터, Request Parameter로 요청
            2. URL 쿼리, form-data에 담아서 사용
            3. GET 요청 처리 시 주로 사용
            4. Spring의 바인딩 시스템으로 파라미터 매핑
           
    <br>
           
    5. @PathVariable와 @RequestParam은 어떤 차이가 있으며 언제 사용해야 할까요?
        1. @PathVariable
            1. URL 경로에 값을 전달
            2. 리소스 ID를 전달할 때 사용

                ```html
                GET /game/info/1
                ```

               ![스크린샷 2025-04-09 오전 3.12.01.png](%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202025-04-09%20%EC%98%A4%EC%A0%84%203.12.01.png)
        
            <br>
           
        2. @RequestParam
            1. 쿼리 파라미터에 값을 전달
            2. 검색, 필터, 옵션을 전달할 때 사용

                ```html
                GET /game/search?keyword=rust
                ```

               ![스크린샷 2025-04-09 오전 3.13.37.png](%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202025-04-09%20%EC%98%A4%EC%A0%84%203.13.37.png)

### 🍀 데이터 전달 객체란 무엇인가요? 그리고 Transaction은 무엇인가요?

1. Spring에서 데이터 전달 객체로 무엇이 있나요?
    1. 데이터 전달 객체를 왜 사용하나요?
        1. 계층 간 책임 분리 → Entity가 변해도 API 응답 형식은 DTO로 유지
        2. DTO로 필요한 정보만 전달
        3. Entity는 DB 구조에 맞춤, DTO는 클라이언트 요청에 맞춤
       
    <br>
   
    2. DAO, DTO, VO는 각각 무엇이며, 어떤 차이점이 있나요?
        1. DAO ( Data Access Object )
            1. DB 접근의 목적
            2. DB 접근 계층에 사용
            3. 가변
        2. DTO ( Data Transfer Object )
            1. 데이터 전달의 목적
            2. 계층 간 통신에 사용
            3. 가변
        3. VO ( Value Object )
            1. 값 표현의 목적
            2. 주로 읽기 전용 / 응담
            3. 불변
           
<br>
        
2. **Transaction은 무엇인가요?**
    1. Transaction이란 하나의 작업 단위를 말한다.
        1. 여러 개의 작업 ( 쿼리 등 )을 하나의 논리적 묶음으로 처리해서 모두 성공, 혹은 모두 실패를 하도록 보장한다.
       
    <br>
   
    2. ACID 원칙은 무엇인가요?
        1. A : 원자성 → 모든 작업이 전부 성공하거나 실패해야 함
        2. C : 일관성 → 트랜잭션 전후의 데이터 상태가 항상 유효한 상태여야 함
        3. I : 고립성 → 동시에 여러 트랜잭션이 실행되어도 서로 간섭이 없어야 함
        4. D : 지속성 → 트랜잭션이 완료되면 그 결과는 영구히 저장되어야 함
       
    <br>
   
    3. Spring에서는 Transaction을 어떤 방식으로 관리하나요?(**@Transcational**)
        1. @Transactional 어노테이션을 통해 관리
        2. Spring AOP를 이용하여
            1. 메서드 실행 전 → 트랜잭션 시작
            2. 메서드 실행 후 → 커밋 ( 성공 ) / 롤백 ( 실패 )
           
    <br>
           
    4. 프록시를 거치지 않을 경우 트랜잭션이 적용되지 않음
    5. DB와 연동되는 로직이어야 의미가 있음
    6. DB 변경 작업 수행에 트랜잭션을 붙임

       ![스크린샷 2025-04-09 오전 3.14.07.png](%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202025-04-09%20%EC%98%A4%EC%A0%84%203.14.07.png)


## 🔎 과제

<aside>
✅ ERD를 작성할 때 선택한 서비스의 대표 API를 최소 5개 이상 작성해봅시다. 

1. Controller, Service, Repository 계층을 각각 분리하여 작성합니다.
2. Testing Tool을 활용해 테스팅한 결과를 발표 자료에 첨부해주세요.

</aside>

- GET → 게임 정보 불러오기

    ![스크린샷 2025-04-09 오전 3.14.39.png](%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202025-04-09%20%EC%98%A4%EC%A0%84%203.14.39.png)

<br>

- POST → 장바구니 추가

    ![스크린샷 2025-04-09 오전 3.14.58.png](%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202025-04-09%20%EC%98%A4%EC%A0%84%203.14.58.png)

<br>

- PUT → 마이페이지 수정

    ![스크린샷 2025-04-09 오전 3.15.21.png](%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202025-04-09%20%EC%98%A4%EC%A0%84%203.15.21.png)

<br>

- PATCH → 이메일 변경

    ![스크린샷 2025-04-09 오전 3.15.40.png](%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202025-04-09%20%EC%98%A4%EC%A0%84%203.15.40.png)

<br>

- DELETER → 게임 환불

    ![스크린샷 2025-04-09 오전 3.15.59.png](%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202025-04-09%20%EC%98%A4%EC%A0%84%203.15.59.png)