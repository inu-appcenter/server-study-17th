# 🍀 웹/앱 서비스에서 클라이언트와 서버는 어떻게 데이터를 주고 받을까요?

### 1) REST API = REST + API


    REST(Representation State Trasfer) : 자원을 이름으로 구분하여, 해당 자원의 상태를 주고받는 모든 것
    HTTP URI(Uniform Resource Idetifier)를 통해 자원을 명시하고,
    HTTP Method(POST, GET, PUT, DELETE, PATCH 등)를 통해 해당 자원에 대한 CRUD Operation을 적용
    API(Application Programing Interface) : 클라이언트와 웹 리소스 사이의 네트워크 통신을 위한 게이트웨이
    다른 소프트웨어 시스템과 통신하기 위해 따라야하는 규칙


### 2) WebSocket

    HTTP 요청을 통해 WebSocket 연결을 설정(HandShake)

    연결이 유지되며 클라이언트와 서버가 자유롭게 데이터를 주고 받음



### 3) gRPC

    HTTP/2 기반 -> REST API보다 빠른 전송 속도
    
    Protocol Buffers(ProtoBuf) 사용 -> JSON보다 효율적인 데이터 직렬화 가능

    양방향 스트리밍 지원


### 4) GraphQL

    클라이언트가 원하는 데이터만 선택적으로 요청하는 쿼리 언어 기반 API

    하나의 요청에서 여러 데이터 조합 가능

    유연한 데이터 요청

    필요 데이터만 선택적으로 가져올 수 있음


# 🍀 Spring은 무엇이고 어떻게 구성되어 있을까요?

## 스프링이란?
    자바 엔터프라이즈 개발을 편하게 해주는 오픈소스 경량급 애플리케이션 프레임워크

## 스프링의 구성 요소

### Spring Core
    IoC (제어의 역전)과 DI (의존성 주입)를 제공하는 핵심 모듈 (BeanFactory 포함)
### Spring Context
    Core 모듈을 기반으로, 메시지 처리, 애플리케이션 생명주기 관리 등의 기능 제공
### Spring DAO
    JDBC를 쉽게 사용하도록 DAO 계층을 추상화하여 지원
### Spring ORM
    Hibernate, JPA 같은 ORM 프레임워크와의 통합 지원
### Spring AOP
    공통 관심사(로깅, 보안)를 분리하는 AOP 기능 제공
### Spring Web
    웹 애플리케이션 개발 지원 (Multipart 요청, 웹 통합 기능)
### Spring MVC
    웹 애플리케이션을 위한 MVC 패턴 제공 (Controller, View, Model 관리)

## - Spring과 SpringBoot의 주요한 차이점은 무엇일까요?

### Dependency
### Configuration
### 편리한 배포

## - Spring Framework의 주요 특징은 무엇일까요?

### IOC
### DI
### AOP
### Transaction Management
### 다양한 데이터 접근 기술 지원

## - IoC, DI, AOP는 각각 무엇인가요?

### IoC
    Inversion of Control : 제어의 역전
    메소드나 객체의 호출작업을 개발자가 결정하는 것이 아니라, 외부에서 결정되는 것.
### Di
    Dependency Injection : 의존성 주입
    제어의 역행이 일어날 때 스프링이 내부에 있는 객체들간의 관계를 관리할 때 사용하는 기법
### AOP
    Aspect Oriented Programming : 관점 지향 프로그래밍
    어떤 로직을 기준으로 핵심적인 관점, 부가적인 관점으로 나누어서 보고
    그 관점을 기준으로 각각 모듈화하겠다는 것이다.

# 🍀 Servlet Container와 Spring Container는 무엇인가요? 그리고 어떻게 동작하나요?
    Spring이 동작하려면 Servlet Container 안에서 Spring Container가 동작하는 구조
### Servlet Container
    웹 애플리케이션을 실행할 수 있는 환경을 제공하는 컨테이너
#### 역할
    클라이언트(브라우저)에서 HTTP 요청을 받음
    서블릿(Servlet) 객체를 생성, 실행, 제거하면서 요청을 처리
    멀티스레드를 지원하여 동시 요청을 처리
    필터(Filter)와 리스너(Listener) 같은 기능 제공
    보안 및 세션 관리
#### 동작 방식
    클라이언트에서 HTTP 요청을 받음
    Servlet Container가 요청을 받아서 해상 URL에 맞는 서블릿을 찾음
    서블릿이 없으면 생성하고, service 메서드를 호출하여 요청을 처리
    처리 결과를 HTTP 응답으로 클라이언트에게 반환
### Spring Container
    Spring Framework에서 객체(Bean)를 관리하고, 의존성을 주입하는 컨테이너
#### 역할
    객체(Bean)를 생성하고 관리
    객체 간의 의존성을 자동으로 주입
    트랜잭션 관리, AOP, 이벤트 처리 등의 기능 제공
    Spring MVC 환경에서는 DispatcherServlet과 함께 동작
#### 동작 방식
    Spring Boot가 실행되면 Spring Container가 초기화됨
    설정 파일(@Configuration, application.properties 등)을 읽어서 Bean 객체를 생성
    생성된 Bean들을 의존성 주입(DI)하여 관리
    필요할 때 Controller, Service, Repository 같은 컴포넌트에 Bean을 자동으로 주입
## - MVC 패턴이 무엇인가요? 또 이 패턴은 어떻게 동작하나요?
    애플리케이션의 역할을 3가지로 분리하여 유지보수성과 확장성을 높이는 디자인 패턴
### Model  
    데이터, 비즈니스 로직을 처리하는 부분(DB와 연결)
### View
    사용자에게 보여지는 UI
### Controller
    사용자의 요청을 받고, Model과 View를 연결하는 역할
## - Spring MVC 패턴은 무엇인가요?
    MVC패턴을 기반으로 동작하는 Spring의 웹 프레임워크
## - Servlet Container는 무엇인가요?
    웹 애플리케이션을 실행할 수 있는 환경을 제공하는 컨테이너
## - 프론트 컨트롤러 패턴은 무엇이고, DispatcherServlet은 뭘까요?
    모든 요청을 하나의 컨트롤러가 받아서 처리하는 방식.
    모든 요청을 하나의 중앙 컨트롤러가 받아서 적절한 컨트롤러로 분배.
    Spring MVC 에서는 DispatcherServlet이 Front Controller 역할을 수행

    DispatcherServlet : 요청을 받고, 적절한 컨트롤러를 호출한 후, 응답을 반환
## - Spring Container은 Bean을 어떻게 관리하나요?
    Bean 등록 -> Bean 의존성 주입(DI) -> 
    Bean의 lifecycle 관리

    객체 생성 -> 초기화 메서드 실행 -> 의존성 주입 -> 사용 -> 소멸 메서드 실행