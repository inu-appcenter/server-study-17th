# OAuth 2.0

### 🍀 Spring OAuth 2.0

1. OAuth 2.0은 무엇인가요?
    1. **OAuth 2.0란?**
        - 인터넷 서비스 간에 제한된 접근 권한을 안전하게 위임하기 위한 인증·인가(Authorization) 프레임워크
        - 사용자가 자신의 로그인 정보를 다른 서비스에 직접 제공하지 않고도, 제3자 애플리케이션이 사용자를 대신해 특정 자원(ex: 프로필, 사진, 이메일)에 접근할 수 있도록 허용하는 표준 프로토콜

    2. OAuth 2.0는 왜 필요한가요?
        - ***비밀번호 공유 문제 해결***

          예전에는 다른 서비스가 내 계정 정보(아이디, 비밀번호)를 요구하는 경우가 많았는데, 이는 보안상 매우 위험. OAuth 2.0은 비밀번호를 직접 제공하지 않고도 인증과 권한 부여를 가능하게 함

        - ***안전한 권한 위임***

          사용자가 특정 권한만 제3자에게 제한적으로 줄 수 있어, 불필요한 권한 남용을 막을 수 있음

        - ***사용자 경험 개선***

          하나의 계정(예: 구글, 페이스북)으로 여러 서비스를 간편하게 이용 가능

        - ***분리된 역할로 보안 강화***

          인증과 권한 부여 서버를 분리하여 관리할 수 있어, 보안 사고 발생 시 피해 범위를 줄임

    3. OAuth 2.0을 구성하는 Resource Owner, Client, Authorization Server, Resource Server은 무엇이고 각각 어떤 역할을 하나요?

        | **구성요소** | **설명** | **역할** |
        | --- | --- | --- |
        | **Resource Owner** | 자원의 소유자 = 사용자 (User) | 자신의 자원에 대한 접근 권한을 갖고, 클라이언트가 자원에 접근하는 것을 승인 |
        | **Client** | 자원에 접근하려는 애플리케이션 또는 서비스 | Resource Owner의 허가를 받아 Resource Server의 자원에 접근하는 주체 |
        | **Authorization Server** | 인증과 권한 부여를 담당하는 서버 | Resource Owner를 인증하고, 클라이언트에게 접근 권한을 나타내는 토큰(Access Token)을 발급 |
        | **Resource Server** | 실제 보호된 자원(API 등)을 제공하는 서버 | Access Token을 검증하여 권한이 확인된 클라이언트에만 자원 접근을 허용 |
        
        ### Ex)
        
        - **Resource Owner**: 사용자
        - **Client**: 설치한 SNS 연동 앱, 예를 들어 “배달의 민족”
        - **Authorization Server**: 네이버, 카카오, 구글 등 로그인 서버
        - **Resource Server**: 네이버 / 카카오 / 구글 등의 사용자 정보 API 서버

2. Spring Security와 함께 적용된 OAuth 2.0의 내부 동작 원리
    1. Spring Security는 OAuth 2.0을 어떻게 적용하고 처리하나요?

       ![스크린샷 2025-05-28 오전 3.14.32.png](%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202025-05-28%20%EC%98%A4%EC%A0%84%203.14.32.png)

        1. **🔁 전체 동작 흐름**
            1. **로그인 요청 및 리디렉션**
                - 사용자가 프론트엔드에서 소셜 로그인 버튼을 클릭하면, 프론트엔드는 백엔드(Spring Boot 서버)에 소셜 로그인 페이지 요청을 보냅니다.
                - 백엔드는 Spring Security의 **`OAuth2AuthorizationRequestRedirectFilter`**를 통해 해당 요청을 가로채고, OAuth 2.0 Provider(예: 카카오, 네이버, 구글) 인증 서버로 리디렉션할 URL을 생성하여 프론트엔드에 전달합니다.
                - 프론트엔드는 이 URL로 사용자를 리디렉션하여, 사용자는 소셜 서비스의 로그인 페이지로 이동합니다.
            2. **인증 및 인가 코드 발급**
                - 사용자가 소셜 로그인 페이지에서 인증(로그인 및 동의)을 완료하면, 소셜 서비스(Authorization Server)는 인가 코드(Authorization Code)와 함께 백엔드 서버의 리디렉션 URL로 사용자를 다시 보냅니다.
                - 이때, Spring Security의 **`OAuth2LoginAuthenticationFilter`**가 **`/login/oauth2/code/{provider}`** 패턴의 요청을 가로채어 인가 코드를 처리합니다.
            3. **인가 코드로 Access Token 교환**
                - 백엔드는 받은 인가 코드로 소셜 서비스의 토큰 엔드포인트에 Access Token을 요청합니다.
                - 소셜 서비스는 Access Token(및 필요시 Refresh Token)을 백엔드에 반환합니다.
            4. **사용자 정보 요청 및 저장**
                - 백엔드는 발급받은 Access Token을 이용해 소셜 서비스의 사용자 정보 API에 요청을 보냅니다.
                - 사용자 정보를 받아오면, 이를 자체 DB에 저장하거나 세션/토큰에 포함해 관리합니다.
            5. **최종 응답 및 세션/토큰 발급**
                - 백엔드는 필요한 경우 자체적으로 JWT 등 추가 토큰을 발급하거나, 인증된 사용자 정보를 프론트엔드에 전달합니다.
                - 프론트엔드는 이 정보를 로컬 스토리지 등에 저장하고, 이후 인증된 상태로 서비스를 이용할 수 있습니다.

        2. **🔐 Spring Security 내부 처리**
            - **자동화된 필터 체인**: Spring Security는 OAuth 2.0 인증 과정을 여러 필터(**`OAuth2AuthorizationRequestRedirectFilter`**, **`OAuth2LoginAuthenticationFilter`** 등)로 나누어 자동으로 처리합니다.
            - **설정 기반 동작**: **`application.yml`**에 각 소셜 Provider의 클라이언트 ID, 시크릿, 리디렉션 URL 등을 설정하면, Spring Security가 이를 바탕으로 인증 과정을 자동화
            - **엔드포인트 표준화**:
                - 로그인 요청: **`/oauth2/authorization/{provider}`**
                - 인가 코드 콜백: **`/login/oauth2/code/{provider}`**
            - **토큰 및 사용자 정보 관리**: Access Token과 사용자 정보는 내부적으로 관리되며, 필요시 커스텀 UserService, SuccessHandler 등을 통해 추가 처리(회원가입, DB 저장, 추가 정보 입력 등)가 가능
            - **비즈니스 로직 분리**: 인증/인가
           
## 🔎 과제

<aside>
✅ **실습 프로젝트에 OAuth 2.0 적용하기**

→ 현재 진행 중인 프로젝트에 OAuth 2.0을 적용해보기
→ 로컬에서 서버 실행 후 [http://localhost:8080/login/oauth2/code/](http://localhost:8080/login/oauth2/code/github){oauth 종류} 로 접속해서 테스트 해보기
→ Google, Kakao, Naver, Github 등 원하는 서비스로 적용해보기

</aside>

Access Token을 검증하여 권한이 확인된 클라이언트에만 자원 접근을 허용