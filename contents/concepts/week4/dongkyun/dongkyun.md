# Spring Security

---

## 공통 주제

### 🍀 Spring Security란 무엇인가요? CORS 에러가 무엇인가요?

1. **Spring Security**란 무엇인가요?
    - 스프링 기반 어플리케이션의 보안(인증과 권한, 인가)을 담당하는 스프링 하위 프레임워크

1. **인증**과 **인가**란 무엇일까요?
    1. *인증 (Authentication)* : 해당 사용자가 본인인지 확인하는 절차
    2. *인가 (Authorization)* : 인증된 사용자가 요청한 자원에 접근 가능한 지 결정하는 절차
    3. *접근 주체 (Principal)* : 보호받는 Resource에 접근하는 대상
    4. *비밀번호 (Credential)* : Resource에 접근하는 대상의 비밀번호
    5. *권한* : 인증된 주체가 어플리케이션의 동작을 수행할 수 있도록 허락되어 있는 지 결정

2. **Spring Security의 구조**
    1. Spring Security는 기본적으로 인증 절차를 거친 후, 인가 절차를 진행,            인가 과정에서 해당 리소스에 대한 접근 권한이 있는지를 확인한다

       ![스크린샷 2025-04-29 오전 2.40.08.png](%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202025-04-29%20%EC%98%A4%EC%A0%84%202.40.08.png)


3. Spring Security의 동작 과정
    1.  ***Http Request 수신***
        1. 사용자가 인증 요청

        ```java
        // SecurityFilterChain에서 요청 수신
        @Bean
            public SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
                http
                        /*
                        ...
                        */
                return http.build();
            }
        ```

    2. ***유저 자격을 기반으로 인증토큰 생성***
        1. AuthenticationFilter가 요청을 가로챔, 정보를 통해 UsernamePasswordAuthenticationToken의 인증용 객체를 생성

        ```java
        // 로그인 이후 AccessToken을 발급 ( 인증 토큰 생성 )
        jwtTokenProvider.generateToken(authentication);
        ```

        ```java
        // SecurityConfig에서 JwtFilter를 UsernamePasswordAuthenticationFilter의
        // 앞에 추가해 모든 요청을 JwtFilter가 먼저 처리
        
        .addFilterBefore(new JwtFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        
        ```

    3. ***FIlter를 통해 AuthenticationToken을 AuthenticationManager로 위임***
        1. 생성한 UsernamePasswordToken 객체를 AuthenticationManager의 구현체인 ProviderManager에게 전달

        ```java
        // JWT 기반이므로 로그인 최초 1회만 AuthenticationManager를 통해 사용자 인증
        // 이후 AuthenticationManager를 거치지 않고 바로 JWT로 인증
        ```

    4. ***AuthenticationProvider의 목록으로 인증을 시도***
        1. AutenticationManger는 등록된 AuthenticationProvider들을 조회하며 인증을 요구

        ```java
        // JWT 자체를 파싱하고, 내부 claims를 사용하기 때문에 Provider를 거치지 않음
        ```

    5. ***UserDetailsService의 요구***
        1. 실제 데이터베이스에서 사용자의 인증 정보를 가져오는 UserDetailsService에 사용자 정보를 넘겨줌

        ```java
        // JWT Filter
        // JWT에서 유저 정보를 추출 후 Authentication 객체 생성
        Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
        
        // JWT 내부 subject(email)로 유저 조회
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(claims.getSubject());
        
        ```

    6. ***UserDetails를 이용해 User객체에 대한 정보 탐색***
        1. 넘겨받은 사용자 정보를 통해 데이터베이스에서 찾아낸 사용자 정보인 UserDetails 객체를 생성

        ```java
        // DB 조회 후 UserDetailsImpl 생성
        @Override
            public UserDetailsImpl loadUserByUsername(String email) throws UsernameNotFoundException {
                Member member =  memberRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException(email));
        
                return new UserDetailsImpl(member);
            }
        ```

    7. ***User 객체의 정보들을 UserDetails가 UserDetailsService(LoginService)로 전달***
        1. AuthenticaitonProvider들은 UserDetails를 넘겨받고 사용자 정보를 비교

        ```java
        // JWT 인증이므로 비교하지 않고 UserDetails만으로 Authentication 객체 생성
        ```

    8. ***인증 객체 or AuthenticationException***
        1. 인증이 완료 되면, 권한 등의 사용자 정보를 담은 Authentication 객체를 반환한다.

        ```java
        // 인증 성공 -> Authentication 객체 반환
        return new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
        ```

    9. ***인증 끝***
        1. 다시 최초의 AuthenticationFilter에 Authentication 객체가 반환된다.
    10. ***SecurityContext에 인증 객체를 설정***
        1. Authentication 객체를 Security Context에 저장한다.

            ```java
            // 인증 객체를 SecurityContext에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
            ```
           
            ```java
            // 인증된 상태로 다음 필터로 진행
            filterChain.doFilter(request, response);
            ```
        
    
    →
    최종적으로는 SecurityContextHolder는 세션 영역에 있는 SecurityContext에 Authentication 객체를 저장한다.

    사용자 정보를 저장한다는 것은 스프링 시큐리티가 전통적인 세선-쿠키 기반의 인증 방식을 사용한다는 것을 의미한다.

    →

    But, JWT 기반 인증을 쓸 때는 세션에 인증 정보를 저장하지 않고 매 요청마다 토큰으로 인증하는 구조

    → Stateless 방식으로 변경


### 🍀 Jwt란 무엇이며, 어떤 역할을 하나요?

1. Jwt란 무엇인가요?
    - **JWT (Json Web Token)**는 JSON 포맷을 이용하여 사용자에 대한 속성을 저장하는 Claim 기반의 Web 토큰이다.
    - 이는 필요한 정보를 자체적으로 지니는 Self-Contained 방식으로 정보를 안정성 있게 전달한다.

1. 쿠키, 세션, 토큰 각각의 인증 방식은 무엇이고 각각 어떤 차이가 있을까요?
    1. *🍪 쿠키 ( Cookie )*
        1. 브라우저가 서버로부터 받은 데이터 조각을 클라이언트에 저장하는 방식
        2. 주로 세션 ID 등의 인증 관련 정보를 저장하며, 요청마다 자동으로 서버에 전송한다.
        3. 쿠키만으로 인증할 경우, 인증 정보가 클라이언트에 저장되어 보안상 위험이 존재

           → 실제 인증에서는 세션과 함께 사용되는 경우가 많다.

    2. *ℹ️ 세션 ( Session )*
        1. 사용자가 로그인하면 서버는 사용자 정보를 세션 저장소 ( 메모리, DB 등 )에 저장하고, 세션 ID를 생성해 쿠키로 클라이언트에 전달
        2. 이후 요청마다 세션 ID가 담긴 쿠키를 서버에 전송
        3. 서버는 세션 ID로 세션 저장소에서 사용자 정보를 찾아 인증
        4. 서버가 인증 상태를 관리하므로 보안성이 높다

           → But 서버에 저장공간이 필요, 서버가 여러 대일 경우 세션 동기화가 필요

    3. *🪙 토큰 ( Token )*
        1. 사용자가 로그인하면 서버가 인증 정보를 담은 토큰 ( JWT 등 )을 발급
        2. 클라이언트는 이 토큰을 저장 ( 로컬스토리지 등 )하고, 요청 시 **HTTP 헤더  ( 주로 Authorization )**에 실어 서버에 보낸다.
        3. 서버는 토큰 자체를 검증해 인증을 처리, 별도의 세션 저장소가 필요하지 않음
        4. 서버가 상태 저장 하지 않음 ( Stateless )

           → 확장성과 분산 환경에 유리

           → But, 토큰이 탈취 당하면 만료 전까지 막을 방법이 없고, 토큰이 길어질 수록 네트워크 부담이 커질 가능성

2. Jwt는 어떤 구조로 이루어져 있나요?

   ![스크린샷 2025-04-29 오전 2.40.30.png](%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202025-04-29%20%EC%98%A4%EC%A0%84%202.40.30.png)

    1. Header, Payload, Signature 세 가지로 구성된다.
    2. 각 부분은 Base64로 인코딩되어 표현되며, 각각의 요소는 . 으로 구분한다.
    3. 구성 요소
        1. ***⚽️  Header***
            1. alg 와 typ 로 구성된다.

                ```java
                { 
                   "alg": "HS256",
                   "typ": "JWT"
                 }
                ```

                1. alg → 해싱 알고리즘 ( 서명을 해싱하기 위한 알고리즘 )
                2. typ → 토큰의 타입
        2. ***📦  PayLoad***
            1. 토큰에서 사용할 정보의 조각들인 클레임( Claim )이 담겨있다.
            2. 클레임 ( key-value )
                1. *등록된 클레임 ( Registered Claim )*
                    1. 토큰 정보를 표현하기 위해 표준으로 정해진 종류의 데이터들

                       ![스크린샷 2025-04-29 오전 2.40.41.png](%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202025-04-29%20%EC%98%A4%EC%A0%84%202.40.41.png)

                2. *공개 클레임 ( Public Claim )*
                    1. 사용자 정의 클레임으로, 공개용 정보를 위해 사용한다. 충돌 방지를 위해 URI 포맷을 이용한다.

                    ```java
                    {
                       "https://suddiyo.tistory.com": true
                    }
                    ```

                3. *비공개 클레임 ( Private Claim )*
                    1. 사용자 정의 클레임으로, 서버와 클라이언트 사이에 임의로 지정한 정보를 저장한다.

                    ```java
                    {
                       "access_token": access
                    }
                    ```

        3. ***📝  Signature***
            1. 토큰을 인코딩하거나 유효성 검증을 할 때 사용하는 고유한 암호화 코드
            2. 헤더와 페이로드, 비밀 키를 기반으로 생성되며, 해당 토큰이 변조되지 않았음을 확인하기 위한 매커니즘이다.
            3. *🏭 생성 과정*
                1. 헤더와 페이로드 값을 각각 BASE64로 인코딩
                2. 인코딩한 값을 비밀 키를 이용해 헤더에서 정한 알고리즘으로 해싱
                3. 해싱한 값을 다시 BASE64로 인코딩하여 생성

        - ***🔒 비밀 키***
            - *😐 대칭키*
                - 암호화 복호화 키가 같은 방식
                - 같은 키를 사용 → 속도가 빠름
                - 대표적으로 HMAC 암호화 알고리즘(해시함수와 비밀키 사용)
                - 값에 SHA-256을 적용해서 해싱 후 private key로 암호화
                - private key를 알고 있는 서버만 Signature 유효성 검증이 가능
            - *🫤 비대칭키*
                - 암호화 복호화 키가 다른 방식
                - 다른 키를 사용 →  속도가 느림, But 안전
                - 대표적으로 RSA 암호화 알고리즘(공개키, 개인키 두 개의 키 사용)
                - 값에 SHA-256을 적용해서 해싱 후 private key로 암호화
                - public key는 공개적으로 제공, 어떠한 서버든 이 public key를 통해 JWT 복호화가 가능

3. Jwt의 장점과 단점은 각각 무엇일까요?
    1. *👍🏻 장점*
        1. OAuth를 이용해 카카오, 네이버 같은 외부 로그인을 쉽게 연동 가능
        2. 서버측 부하를 낮춤, 독립적이므로 능률적으로 접근 권한 관리, 분산/클라우드 기반 인프라 스트럭처에 잘 대응
        3. 별도의 인증 저장소가 필요하지 않아서 인증 서버와 DB에 의존하지 않음
    2. *👎🏻 단점*
        1. 서버로부터 받은 토큰이 쿠키 또는 로컬스토리지, 세션 스토리지에 저장

           → **탈취 당할 위험**이 존재

           → Token에 중요한 정보를 넣지 않아야 함

        2. 토큰에 넣는 데이터가 많아질수록 토큰이 길어짐

           → 토큰 데이터를 서버에 전달할 때 네트워크 대역폭 낭비가 심해질 가능성

        3. 한 번 발급된 Token은 수정, 폐기가 불가

           → Access Token, Refresh Token 사용


1. Spring에서 Jwt를 어떻게 활용할 수 있을까요?
    1. AccessToken과 RefreshToken은 각각 무엇일까요?
        1. ***🎫  AccessToken***
            1. 사용자가 로그인에 성공하면 서버가 발급하는 토큰
            2. 사용자가 인증되었음을 증명, 헤더에 담아 서버에 전송
            3. 유효기간이 짧아서 만료되면 더 이상 사용 불가
        2. ***🔄  RefreshToken***
            1. AccessToken이 만료되었을 때, 새로운 AccessToken을 발급 받기 위해 사용하는 토큰
            2. AccessToken보다 유효기간이 길고, 서버나 안전한 장소에 보관
            3. RefreshToken을 서버에 보내면, 서버는 유효한지 확인 후 새로운 AccessToken을 발급
            4. 만료되면 다시 로그인 해야 한다.

           ![스크린샷 2025-04-29 오전 2.41.02.png](%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202025-04-29%20%EC%98%A4%EC%A0%84%202.41.02.png)

            - 로그인 시 AccessToken과 RefreshToken을 발급

           ![스크린샷 2025-04-29 오전 2.41.12.png](%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202025-04-29%20%EC%98%A4%EC%A0%84%202.41.12.png)

            - API 요청을 보낼 때 AccessToken을 Authorization에 실어 서버에 전송

           ![스크린샷 2025-04-29 오전 2.41.22.png](%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202025-04-29%20%EC%98%A4%EC%A0%84%202.41.22.png)

            - 잘못된 AccessToken에 대해서는 401 에러 발생

           ![스크린샷 2025-04-29 오전 2.41.31.png](%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202025-04-29%20%EC%98%A4%EC%A0%84%202.41.31.png)

            - RefreshToken을 통해 재발급

    2. 각 토큰은 클라이언트와 서버에서 어떤 방식으로 관리되어야 할까요?
        1. ***AccessToken***
            1. 클라이언트
                1. *🍪 쿠키 (Cookies)*
                    1. 모든 클라이언트 요청에 자동으로 포함되므로 별도로 포함시켜 주지 않아도 됨
                    2. HttpOnly 플래그를 사용해 JavaScript에서 쿠키에 접근할 수 없게 함으로써 XSS 공격에 대한 보안을 높일 수 있다.
                    3. Secure 플래그를 사용해 HTTPS 연결에서만 쿠키가 전송되게 하여 중간자 공격 ( MITM )으로부터 보호할 수 있다.
                    4. SameSite 쿠키 속성을 통해 CSRF 공격을 방어할 수 있다.

                2. *🗄️ 로컬 스토리지 (Local Storage) [ ⚠️ 위험 ]*
                    1. 클라이언트 측에 데이터를 영구적으로 저장하는 방식

                       → 브라우저를 닫아도 데이터가 유지

                    2. XSS 공격에 취약, 탈취당할 위험

                3. *ℹ️ 세션 스토리지 (Session Storage) [ ⚠️ 위험 ]*
                    1. 브라우저가 닫히면 데이터가 삭제, 같은 탭에서만 유효
                    2. 로컬 스토리지보다는 보안 측에서 안전하지만, 여전히 XSS 공격에 취약

                4. *💿 메모리 (Memory)*
                    1. 클라이언트 서버의 메모리에 저장하는 방식
                    2. 매 요청 시마다 API 호출 시 AccessToken에 접근이 쉬워짐

                       → But, 브라우저는 세션 단위로 관리

                       → 페이지를 이동하면 AcessToken이 소멸

                    3. SPA(Single Page Application)에서 주로 사용
                    4. 페이지를 새로고침 시, 다시 로그인이 필요
                    5. 스크립트가 메모리 공간을 직접적으로 제어할 수 없음

                       → XSS 공격에 쉽게 탈취되지 않음

            2. 서버
                1. 서명만 검증하고, 별도의 저장소에 관리하지 않음
                2. 만료시간을 짧게 설정해 보안 위험을 줄임
        2. ***RefreshToken***
            1. 클라이언트
                1. 보안상 HttpOnly 플래그, Secure 플래그를 사용해 쿠키에 저장
                2. 탈취 당할 경우, 장기 세션이 노출되므로, 노출되지 않게 주의
            2. 서버
                1. 반드시 안전하게 저장

                   → 일반적으로 암호화된 데이터베이스에 저장

                2. 토큰 사용 시마다 유효성 검사, 만료 확인 등으로 보안 강화

### 🍀 CORS란 무엇인가요?

1. **Origin**이 무엇인가요?
    1. Origin은 URL에서 Scheme (Protocol), Host, Port까지를 의미
        1. 각각 통신 방식, 서버 주소, 통신 포트 번호

    ```jsx
    https://github.com/inu-appcenter/server-study-17th
    ```

    1. Origin
        1. Protocol → https
        2. Host → github.com
        3. Port → 443 ( https의 기본 포트 )
    2. Protocol, Host, Port 3가지만 동일하면 동일한 Origin

2. **SOP 정책**이란 무엇인가요?
    1. *Same Origin Policy* → 같은 Origin만 리소스를 공유할 수 있다.
    2. *왜 필요한가?*
        1. Origin이 다른 두 어플리케이션이 자유롭게 소통할 수 있는 환경은 굉장히 위험
            1. CSRF, XSS 공격이 이 허점을 이용
                1. CSRF → 이미 인증되어 있는 세션을 이용한 공격
                    1. 악성 사이트가 대신 요청을 보내도 정상 사용자로 착각
                2. XSS → 사이트 안에 악성 스크립트를 심는 공격
                    1. xss로 심은 스크립트가 다른 origin 서버랑 마음대로 소통 가능
        2. 웹에서 다른 출처에 있는 리소스를 사용하는 것은 굉장히 흔한 일이기 때문에 무작정 제한할 수 없다.

       →  따라서 다른 출처에서도 리소스를 공유할 수 있도록 세운 정책인 **CORS**가 등장


3. **CORS**란 *다른 출처의 자원의 공유에 대한 정책*이다.
    1. 다른 출처의 자원에 대해 SOP를 위반하지만 CORS 정책을 따르면 허용


    2. *기본 동작*
        1. 브라우저에서 HTTP 요청을 서버로 보낼 때, 요청 헤더에 내 출처 (Origin)을 담아 보냄
            
            ex) Origin: http://localhost:3000
            
        2. 서버는 응답할 때, “*Access-Control-Allow-Origin*”이라는 응답 헤더에 허용할 출처를 적어 보냄
            
            ex) Access-Control-Allow-Origin: http://localhost:3000
            
        3. 브라우저는 응답을 받았을 때, 내가 요청한 Origin과 서버가 허락한 Origin이 일치하는지 확인
            1. 일치하면 정상적으로 데이터 사용
            2. 일치하지 않으면 브라우저가 데이터 차단 후 CORS 에러 띄움
                
                
    3. *CORS 작동 시나리오*
        1. *Preflight Request (예비 요청)*
            1. 본 요청을 하기 전 예비 요청을 보내 서버와 잘 통신할 수 있는지 확인
                1. Cross-Origin 요청 자체가 유저 데이터에 영향을 줄 수 있기 때문
            2. *흐름*
                1. 브라우저가 OPTIONS 메서드로 예비 요청을 서버로 보냄
                    1. 요청 헤더
                    - **`Origin`**: 내 출처(예: [http://localhost:3000](http://localhost:3000/))
                    - **`Access-Control-Request-Method`**: 실제로 쓸 HTTP 메서드(예: POST)
                    - **`Access-Control-Request-Headers`**: 실제로 쓸 헤더들       (예: Content-Type 등)
                2. 서버가 예비 요청에 대해 응답
                    1. 응답 헤더
                    - **`Access-Control-Allow-Origin`**: 허용할 출처(Origin)
                    - **`Access-Control-Allow-Methods`**: 허용할 메서드들
                    - **`Access-Control-Allow-Headers`**: 허용할 헤더들
                    - **`Access-Control-Max-Age`**: 이 허락을 브라우저가 얼마동안 기억해도 되는지 (초 단위)
                3. 브라우저가 서버의 응답을 확인
                    1. 서버가 허락한 범위에 요청이 포함되어 있으면 실제 요청을 보냄
                    2. 허락하지 않았으면, **CORS 에러**를 띄우고 요청을 보내지 않음
                4. 실제 요청
                    1. 서버가 다시 CORS 관련 헤더를 잘 보내야 브라우저가 데이터를 정상적으로 받음
                    2. 본 요청의 응답에 CORS 헤더가 없거나 잘못될 경우, 브라우저는 데이터를 차단하고, **CORS 에러**를 띄움
                
        2. *Simple Request (일반적인 요청)*
            1. 예비 요청을 생략하고 바로 서버에 본 요청을 보낸 뒤, 서버의 응답 헤더를 보고 브라우저가 CORS 정책 위반 여부를 검사
            2. ***조건***
                1. **요청의 메소드**는 GET, HEAD, POST 중 하나
                2. **Request Header**에는 아래 속성만 허용                **`Accept`**, **`Accept-Language`**, **`Content-Language`**, **`Content-Type`**, **`DPR`**, **`Downlink`**, **`Save-Data`**, **`Viewport-Width`**, **`Width`**
                3. Content-Type 헤더에는 아래 속성만 허용                                                    `application/x-www-form-urlencoded, multipart/form-data, text/plain`
            3. 대부분의 HTTP 요청은 text/html이나 application/json이기 때문에 해당 조건은 매우 까다롭다. → 대부분은 예비 요청
            
        3. *Credential Request (인증된 요청)*
            1. 클라이언트가 서버에 쿠키, 세션, ID, 토큰 같은 인증 정보를 포함해서 다른 출처로 요청을 보낼 때 사용하는 방식
            2. 요청
                1. 클라이언트는 요청에 인증 정보를 포함
                    
                    ( 쿠키(세션 ID), Authorization 헤더(토큰) 등 )
                    
            3. 응답
                1. 서버는 CORS 관련 헤더를 설정
                - **`Access-Control-Allow-Origin`**: 정확한 출처(origin) 명시
                - **`Access-Control-Allow-Credentials: true`** 추가

4. Spring에서 CORS를 어떤 방식으로 관리해야 할까요?
    1. ***전역 설정***
        1. CorsConfigurationSource을 사용해 전역적으로 CORS 설정

           ![스크린샷 2025-04-29 오전 2.41.44.png](%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202025-04-29%20%EC%98%A4%EC%A0%84%202.41.44.png)

        2. Spring Security 필터 체인에 적용이 가능

           → 인증, 인가가 필요한 요청이나 예비 요청이 Security 필터를 거치므로 Spring Security 레벨에서 CORS가 동작할 필요가 있음

           ![스크린샷 2025-04-29 오전 2.41.54.png](%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202025-04-29%20%EC%98%A4%EC%A0%84%202.41.54.png)

    2. ***특정 컨트롤러 설정***
        1. 특정 컨트롤러나 메서드에서 @CrossOrigin 어노테이션을 사용해 CORS 설정

        ```java
        @RestController
        @RequestMapping("/api")
        public class MyController {
        
            ***@CrossOrigin***(origins = "http://example.com", maxAge = 3600)
            @GetMapping("/data")
            public String getData() {
                return "Hello, CORS!";
            }
        }
        ```


## 🔎 과제

<aside>
✅ **Spring Security를 적용해봅시다.**

1. Spring Security 구성하기
2. AccessToken 발급/검증 로직 구현하기

</aside>