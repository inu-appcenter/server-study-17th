# Docker, CI/CD와 깃허브 액션

# ✅ 발표 내용

---

### 🐳 Docker란 무엇일까요?

1. 컨테이너 기술은 무엇이고 왜 필요할까요?
    1. **📦  컨테이너란?**
        1. 컨테이너(Container)는 애플리케이션과 그 실행에 필요한 **라이브러리, 설정 파일, 종속성** 등을 하나로 묶어 **격리된 환경에서 실행**할 수 있게 해주는 기술
        2. 마치 가상머신처럼 보이지만, **호스트 OS 위에서 직접 실행**되므로 **가볍고 빠르다.**
    2. 왜 필요?

        | **항목** | **이유** |
        | --- | --- |
        | ❌ 환경 불일치 문제 해결 | 개발자는 Windows, 서버는 Linux → 동작 안 함 → 컨테이너로 동일 환경 제공 |
        | 🚀 배포 자동화 | 코드만 아니라 **환경까지 함께 배포** 가능 |
        | 📁 가벼움 | 가상머신보다 수 MB 단위로 훨씬 가볍고 부팅도 빠름 |
        | 🧪 테스트 용이 | 테스트용 컨테이너를 바로 띄우고 제거 가능 |
        | 🛠 확장성 | 여러 개의 컨테이너로 **마이크로서비스 아키텍처** 구현 가능 |

2. 도커란 무엇일까요?
    1. **🐳  Docker란?**
        1. 도커(Docker)는 컨테이너를 만들고 실행하는 데 사용하는 **대표적인 플랫폼 및 도구 모음**
        2. 복잡한 설치 없이 명령어 몇 줄로 애플리케이션 환경을 만들고 실행

    2. 도커 이미지란 무엇이고 스프링 애플리케이션을 어떻게 이미지화할 수 있을까요?
        1. **📷 도커 이미지 (Docker Image)**
            1. **이미지**는 컨테이너 실행의 **설계도**

               → 실행하면 컨테이너

            2. 이미지에는 애플리케이션 코드, 라이브러리, 설정 등 실행에 필요한 모든 것이 포함

        2. **☕️ 스프링 애플리케이션 이미지화**
            1. **스프링 애플리케이션 빌드 (jar 파일 만들기)**

                ```java
                ./gradlew bootJar
                ```

            2. **Dockerfile 작성**

                ```java
                # 1. Java 실행 환경이 있는 베이스 이미지 선택
                FROM openjdk:17-jdk-slim
                
                # 2. JAR 파일을 컨테이너 내부로 복사
                COPY build/libs/my-spring-app.jar app.jar
                
                # 3. 컨테이너 실행 시 실행할 명령어
                ENTRYPOINT ["java", "-jar", "/app.jar"]
                ```

            3. **도커 이미지 빌드**

                ```java
                docker build -t my-spring-app .
                ```

            4. **컨테이너 실행**

                ```java
                docker run -p 8080:8080 my-spring-app
                ```

    3. 여러 대의 컨테이너를 어떻게 동시에 띄울 수 있을까요?
        1. 방법 1.  **여러 docker run 명령 실행** 
            
            ```java
            docker run -d --name app1 my-app
            docker run -d --name app2 my-app
            ```
            
        2. 방법 2.  **Docker Compose 사용** 
            
            ```java
            // docker-compose.yml
            version: '3'
            services:
              app:
                image: my-spring-app
                ports:
                  - "8080:8080"
              db:
                image: mysql:8
                environment:
                  MYSQL_ROOT_PASSWORD: rootpass
                  MYSQL_DATABASE: mydb
            ```
            
            1. `docker-compose up` 으로 실행 
    4. ex)
        1. ***Dockerfile***
            
            ```java
            // Dockerfile
            
            // Java 17이 설치된 리눅스 환경 위에서 동작하는 이미지를 만들겠다
            FROM openjdk:17
            
            // version=0.1이라는 정보를 추가 (꼭 필요한 건 아님)
            LABEL version=0.1
            
            // 빌드할 때 사용할 변수(인자)를 정의
            // JAR_NAME: 우리가 빌드한 .jar 파일 이름
            // JAR_PATH: 그 파일의 위치 (보통 Gradle 빌드 결과가 build/libs/에 나옴)
            ARG JAR_NAME=Inffy-0.0.1-SNAPSHOT.jar
            
            ARG JAR_PATH=./build/libs/${JAR_NAME}
            
            // 컨테이너 안에 /app 디렉터리를 생성
            RUN mkdir -p /app
            
            // 작업 경로를 /app으로 설정
            WORKDIR /app
            
            // 로컬의 build/libs/Inffy-0.0.1-SNAPSHOT.jar를 → 컨테이너의 /app/app.jar로 복사
            COPY ${JAR_PATH} /app/app.jar
            
            // 컨테이너가 실행되면 이 명령이 수행
            // prod 환경 프로파일로 Spring Boot 앱을 실행
            // prod 환경 프로파일은 운영(production) 환경에서 사용할 설정 집합
            CMD ["java", "-jar", "-Dspring.profiles.active=prod", "/app/app.jar"]
            ```
            
        2. docker-compose.yml
        
        ```java
        // docker-compose.yml
        
        // Docker Compose 파일의 버전 (최신 권장 버전 중 하나)
        version: '3.8'
        
        services:
        	// spring-server라는 이름으로 컨테이너 생성
          spring-app:
            container_name: spring-server
            // 도커허브의 DOCKER_USERNAME 사용자 아래 있는 inffy-server 이미지 사용
            image: ${DOCKER_USERNAME}/inffy-server:latest
            // 호스트 포트 80 → 컨테이너의 8080 (HTTP)
            // 호스트 포트 443 → 컨테이너의 8443 (HTTPS)
            ports:
              - "80:8080"
              - "443:8443"
            // redis 컨테이너가 먼저 실행돼야 spring-app이 실행
            depends_on:
              - redis
            // redis와 같은 네트워크로 묶임 (서로 내부 DNS로 접근 가능)
            networks:
              - app-network
            // Spring Boot 앱 실행 시 환경변수로 Redis 정보 주입
            environment:
              - REDIS_HOST=${REDIS_HOST}
              - REDIS_PASSWORD=${REDIS_PASSWORD}
        
          redis:
        		// 컨테이너 이름은 inffy-redis
            container_name: inffy-redis
            // Redis 공식 이미지 중 7.2 버전 사용
            image: redis:7.2
            // Redis 실행 시 비밀번호를 요구하도록 설정
            command: redis-server --requirepass ${REDIS_PASSWORD}
            // Redis 기본 포트 외부 노출
            ports:
              - "6379:6379"
            // Redis 데이터를 redis-data라는 볼륨에 저장 → 컨테이너가 꺼져도 데이터 유지
            volumes:
              - redis-data:/data
            // 같은 네트워크로 묶여 있음
            networks:
              - app-network
        
        // redis-data라는 이름의 로컬 볼륨을 생성 → Redis 데이터 영속화
        // (컨테이너가 꺼지거나 삭제돼도 데이터가 유지)
        volumes:
          redis-data:
            driver: local
        
        // app-network라는 가상 네트워크 생성 (브리지 방식)
        // 두 컨테이너 간 통신 가능 (e.g., redis:6379으로 접속)
        networks:
          app-network:
            driver: bridge
        ```


### 🛠️ CI/CD란 무엇일까요?

1. CI/CD의 의미
    1. 🔍 CI의 의미
        1. **CI**: *Continuous Integration (지속적인 통합)*
        2. 여러 개발자가 작성한 코드가 병합될 때마다 자동으로 **빌드 및 테스트**하는 프로세스
        3. 목적
            1. ***코드 충돌 방지***
            2. *빠른 **버그 탐지***
            3. *자동화된 **테스트로 품질 확보***

       ex) main 브랜치에 Push 할 경우, 자동으로 테스트가 돌아가고 빌드가 실행

    2. 💨 CD의 두가지 의미
        1. **CD**: Continuous Delivery (지속적인 전달) 또는 Continuous Deployment (지속적인 배포)

        2. *Continuous Delivery (지속적인 전달)*
            1. 빌드된 애플리케이션을 운영 직전 단계까지 자동으로 배포
            2. 실제 **배포는 사람의 수동 승인** 필요
                
                ex) EC2에 올릴 준비가 자동으로 되지만, 실제 배포는 수동 클릭으로 진행

        3. *Continuous Deployment (지속적인 배포)*
            1. **코드가 변경되면 즉시 운영 서버에까지 자동 배포**
            2. 사람의 개입 없이, 코드 Push → 테스트 통과 → 운영 반영 ( 자동화 )
                
                ex) main 브랜치에 Push하면 바로 EC2 서버에 새로운 버전이 배포

    3. **CI/CD**는 **소프트웨어 개발과 배포를 자동화**하는 핵심적인 방법론, **더 빠르고 안정적으로 코드 변경 사항을 통합하고 배포**할 수 있도록 도움
        
        | **구분** | **의미** | **특징** |
        | --- | --- | --- |
        | CI | 지속적 통합 | 코드 병합 시 자동 테스트 & 빌드 |
        | CD (Delivery) | 지속적 전달 | 스테이징까지 자동, 운영 배포는 수동 |
        | CD (Deployment) | 지속적 배포 | 운영까지 완전 자동 배포 |

2. 다양한 CI/CD 툴
    1. 🚗 Jenkins
        1. **Jenkins**는 가장 널리 사용되는 **오픈소스 CI/CD 자동화 서버**
        2. 다양한 플러그인을 통해 거의 모든 개발 환경과 통합이 가능
        3. 특징
            1. Java 기반으로 동작하며 설치가 간편
            2. 수천 개의 플러그인으로 확장 가능 (Git, Docker, Slack, Kubernetes 등)
            3. 다양한 플랫폼에서 실행 가능 (Windows, macOS, Linux)
            4. **Pipeline as Code** 지원 (Jenkinsfile로 CI/CD 정의)
        4. 장점
            1. 강력한 커뮤니티와 문서 지원
            2. 매우 유연한 구성 가능
            3. 큰 프로젝트에도 적합한 확장성
        5. 단점
            1. 설정 및 유지보수가 복잡할 수 있음
            2. UI가 다소 구식이며 사용자 친화적이지 않음

    2. 🪈 GoCD
        1. **GoCD**는 ThoughtWorks에서 개발한 오픈소스 CI/CD 도구
        2. **배포 파이프라인을 시각적으로 설계하고 관리**하는 데 초점을 둔 툴
        3. 특징
            1. **파이프라인 기반 모델**을 기본으로 설계

               ex) [코드 빌드] → [테스트 실행] → [Docker 이미지 생성] → [스테이징 배포] → [운영 배포]

               이 작업 흐름을 **그래픽 UI로 한눈에 볼 수 있다**

            2. **분기(branch) 기반 작업보다는 파이프라인 흐름 관리**에 강점
                1. 어떤 브랜치냐보다는 **작업 흐름(파이프라인) 자체를 어떻게 흘려보낼지**에 집중
            3. 에이전트 기반 분산 빌드 실행 지원
                1. 에이전트란: 빌드나 테스트 작업을 실제로 수행하는 작업 머신
                2. 하나의 서버에서 모든 걸 처리하지 않고, 여러 에이전트를 등록해서 **작업을 분산 실행**
            4. JSON, YAML로 파이프라인 구성 가능
        4. 장점
            1. **시각적인 파이프라인 흐름 관리**가 뛰어남
            2. 복잡한 배포 과정 (multi-stage, multi-env) 설계에 적합
                1. **multi-stage**
                    1. **여러 단계를 거쳐 배포**하는 구조

                       1단계: 개발 환경에 배포
                       2단계: 테스트 환경에 배포
                       3단계: 스테이징 환경에 배포
                       4단계: 운영 환경에 배포

                2. **multi-env**
                    1. 배포 환경이 **하나가 아닌 여러 개인 구조**

                        | **환경** | **목적** |
                        | --- | --- |
                        | Dev | 개발자가 자유롭게 테스트 |
                        | Test | QA 팀이 기능 테스트 |
                        | Staging | 실제 운영과 거의 유사한 환경 |
                        | Production | 실제 사용자 대상 운영 환경 |
            3. Docker, Kubernetes 등과 통합 용이
        5. 단점
            1. Jenkins에 비해 사용자층과 플러그인 수가 적음
            2. GitHub Actions 등 최신 툴에 비해 다소 무거움


| **항목** | **Jenkins** | **GoCD** |
| --- | --- | --- |
| 개발사 | 오픈소스 커뮤니티 | ThoughtWorks |
| 파이프라인 방식 | 자유로운 구성, 코드 기반 | 파이프라인 중심, 시각화 강점 |
| 확장성 | 플러그인 수천 개 | 제한적 (기본 제공 기능 충실) |
| 사용자 인터페이스 | 구식이나 커스터마이즈 가능 | 직관적인 시각화 UI |

### 🐱 Github Actions란 무엇일까요?

1. Github Actions 소개
    1. **GitHub Actions**는 GitHub에서 제공하는 **CI/CD 자동화 도구**
    2. 코드 저장소에 push 등의 이벤트가 발생했을 때, 자동으로 빌드, 테스트, 배포 등의 작업을 수행
    3. **🔁 어떻게 동작하나요?**
        1. 개발자가 main 브랜치에 코드를 Push
        2. .github/workflows/ 폴더 안에 있는 YAML 파일을 기반으로 GitHub Actions가 실행
        3. ex)

           •	코드 빌드

           •	테스트 실행

           •	Docker 이미지 생성 및 업로드

           •	EC2에 자동 배포

    4. *👍🏻 장점*
        1. GitHub에 내장되어 있어 **설정이 간편**
        2. YAML 형식으로 **코드 기반 자동화**
        3. 다양한 **오픈소스 Action들**을 재사용 가능
        4. **Docker, AWS, Firebase 등 다양한 서비스와 통합** 가능

2. Workflow란?
    1. **Workflow란 ?**
        1. GitHub Actions에서 **자동화 작업의 전체 흐름을 정의한 파일**
        2. 이 파일은 보통 .github/workflows/ 디렉토리에 .yml 또는 .yaml 확장자로 작성
        3. ex) CICD.yml, ci.yml

    2. Workflow를 작성하기 위한 문법들

        ```java
        name: CI Example
        
        on: [push]
        
        jobs:
          build:
            runs-on: ubuntu-latest
            steps:
              - uses: actions/checkout@v3
              - run: echo "코드 빌드 시작!"
        ```

        1. workflow
            1. Workflow 파일의 **이름**을 정의
            2. GitHub Actions 탭에서 워크플로우를 식별하는 데 사용
        2. event
            1. 이벤트 트리거
                1. 어떤 **이벤트가 발생할 때** Workflow가 실행될지를 지정
                2. 주요 이벤트 트리거

                    | **이벤트** | **설명** |
                    | --- | --- |
                    | push | 특정 브랜치에 푸시될 때 |
                    | pull_request | PR이 열리거나 병합될 때 |
                    | workflow_dispatch | 수동 실행 (버튼 클릭) |
                    | schedule | 정해진 시간에 주기적으로 실행 (크론 표현식 사용) |
                    
                    ```java
                    on:
                      push:
                        branches: [ "main", "develop" ]
                      pull_request:
                        branches: [ "main" ]
                    ```
                    
        3. jobs
            1. **jobs**란? Workflow 안에서 실행할 **작업 단위 집합**
            2. 여러 개의 Job을 병렬 혹은 순차적으로 실행 
                
                ```java
                jobs:
                  build:
                    runs-on: ubuntu-latest
                    steps:
                      - run: echo "Build 시작"
                ```
                
            3. 가상환경 선택
                1. **runs-on: 가상환경 선택**
                    1. Job이 실행될 **OS 환경**을 선택

                        | **값** | **설명** |
                        | --- | --- |
                        | ubuntu-latest | 최신 Ubuntu 리눅스 (가장 많이 사용) |
                        | windows-latest | 최신 Windows |
                        | macos-latest | 최신 macOS |
                2. **steps: 실제 실행 명령들**
                    1. job 내에서 **순차적으로 실행되는 명령 리스트**
                    2. run 키워드로 쉘 명령을 직접 실행하거나,
                    3. uses 키워드로 재사용 가능한 Action을 실행 
                        
                        ( 재사용 : 누군가 만들어 둔 자동화 기능을 불러와서 쓰는 걸 의미 )
                         
                    ```java
                    steps:
                      - uses: actions/checkout@v3
                      - run: echo "Hello, world!"
                    ```
                    
                    | **Action 이름** | **기능** |
                    | --- | --- |
                    | actions/checkout@v3 | GitHub 저장소 코드를 체크아웃 (clone) |
                    | actions/setup-java@v3 | Java 환경을 자동으로 설치 |
                    | docker/login-action@v2 | Docker Hub 로그인 |
                    | appleboy/scp-action@master | 파일을 SCP로 원격 서버에 전송 |
                    
                    ```java
                    // 코드 체크아웃
                    - uses: actions/checkout@v3
                    ```
                    
                    ```java
                    //Java 17 설치
                    - uses: actions/setup-java@v3
                      with:
                        java-version: '17'
                        distribution: 'adopt'
                    ```
                    
                    ```java
                    // EC2에 파일 보내기
                    - uses: appleboy/scp-action@master
                      with:
                        host: ${{ secrets.EC2_HOST }}
                        username: ubuntu
                        key: ${{ secrets.EC2_KEY }}
                        source: "docker-compose.yml"
                        target: "/home/ubuntu"
                    ```

3. ex) **GitHub Actions CI/CD 워크플로우**

    ```java
    // GitHub Actions CI/CD 워크플로우
    
    name: Inffy Server CI & CD
    
    // main 브랜치에 코드가 푸시될 때마다 워크플로우가 자동 실행
    on:
      push:
        branches: [ "main" ]
    
    // Docker 이미지 만들기
    jobs:
      build:
        name: Build jar and Push Docker Image at Docker hub
        runs-on: ubuntu-latest
    
    		// 코드 체크아웃
    		// 현재 GitHub Repository의 코드를 받아옴
        steps:
          - name: Checkout code
            uses: actions/checkout@v3
    
    			// Java 17 환경으로 설정
          - uses: actions/checkout@v3
          - name: Set up JDK 17
            uses: actions/setup-java@v3
            with:
              java-version: '17'
              distribution: 'adopt'
              
          // Firebase 서비스 계정 키를 secrets에서 받아 파일로 생성
          - name: Create Firebase Service Account File
            run: |
              mkdir -p ./src/main/resources/firebase
              echo '${{ secrets.FIREBASE_SERVICE_ACCOUNT }}' > ./src/main/resources/firebase/firebase_service_key.json
              
          // application-prod.yml 설정
          // yml 설정 파일에 secrets을 삽입해서, 빌드 시 필요한 설정들을 자동 주입
          - name: Set up yml file
            uses: microsoft/variable-substitution@v1
            with:
              files: ./src/main/resources/application-prod.yml
            env:
              spring.datasource.url: ${{ secrets.MYSQL_URL }}
              spring.datasource.username: ${{ secrets.MYSQL_USERNAME }}
              spring.datasource.password: ${{ secrets.MYSQL_PASSWORD }}
              spring.data.redis.host: ${{ secrets.REDIS_HOST }}
              jwt.secret: ${{ secrets.JWT_SECRET }}
              spring.mail.username: ${{ secrets.MAIL_USERNAME }}
              spring.mail.password: ${{ secrets.MAIL_PASSWORD }}
    
    			// Gradle로 빌드
    			// 테스트는 제외하고 프로덕션 설정으로 jar 빌드
    			// 결과물: build/libs/*.jar
          - name: Build with Gradle
            run: |
              chmod +x ./gradlew
              ./gradlew clean build -x test -i --no-daemon -Dspring.profiles.active=prod
    
    			// Docker Hub 로그인
    			// 로그인 인증 정보를 통해 나중에 이미지를 Docker Hub에 푸시할 수 있게 함
          - name: Login to Docker Hub
            uses: docker/login-action@v2
            with:
              username: ${{ secrets.DOCKER_USERNAME }}
              password: ${{ secrets.DOCKER_PASSWORD }}
    
    			// Docker 이미지 빌드
    			// Dockerfile을 기반으로 Docker 이미지 생성
          - name: Build Docker image
            run: docker build -t ${{ secrets.DOCKER_USERNAME }}/inffy-server:latest .
    
    			// Docker 이미지 푸시
    			// 만들어진 이미지를 Docker Hub에 업로드
          - name: Push Docker image to Docker Hub
            run: docker push ${{ secrets.DOCKER_USERNAME }}/inffy-server:latest
    
    	// build 잡이 성공적으로 끝나면 아래가 실행됨
    	
    	//  EC2 서버에 배포
      deploy:
        name: Deploy to remote EC2 Server
        needs: build
        runs-on: ubuntu-latest
    
        steps:
          - name: Checkout code
            uses: actions/checkout@v3
    
    			// EC2로 docker-compose.yml 복사
    			// 로컬에 있는 docker-compose.yml을 EC2 서버의 /home/ubuntu에 복사
          - name: Copy docker compose file to EC2
            uses: appleboy/scp-action@master
            with:
              host: ${{ secrets.AWS_EC2_IP }}
              username: ${{ secrets.AWS_EC2_USERNAME }}
              key: ${{ secrets.AWS_EC2_KEY }}
              port: ${{ secrets.AWS_EC2_PORT }}
              source: "docker-compose.yml"
              target: "/home/ubuntu"
    
    			// EC2 원격 접속 후 배포
          - name: Deploy to EC2
            uses: appleboy/ssh-action@master
            with:
              host: ${{ secrets.AWS_EC2_IP }}
              username: ${{ secrets.AWS_EC2_USERNAME }}
              key: ${{ secrets.AWS_EC2_KEY }}
              port: ${{ secrets.AWS_EC2_PORT }}
              script: |
                cd /home/ubuntu
                docker pull ${{ secrets.DOCKER_USERNAME }}/inffy-server:latest
                
                echo "DOCKER_USERNAME=${{ secrets.DOCKER_USERNAME }}" > .env
                echo "REDIS_PASSWORD=${{ secrets.REDIS_PASSWORD }}" >> .env
                
                if [ "$(docker ps -aq -f name=spring-server)" ]; then
                  docker stop spring-server
                  docker rm spring-server
                fi
                
                if [ "$(docker ps -aq -f name=inffy-redis)" ]; then
                  docker stop inffy-redis
                  docker rm inffy-redis
                fi
    
                docker-compose up -d
    ```


## 🔎 과제

<aside>
✅ **배포를 위한 밑작업 해오기**

0. 개인 레포지토리 하나 생성하고 실습 코드 커밋 & 푸쉬
1. AWS 계정 만들고 사용할 IAM 계정 만들기(MFA 적용, EC2, RDS 접근권한 설정 필수)
2. EC2 생성하고 스왑 메모리 설정해주기([https://diary-developer.tistory.com/32](https://diary-developer.tistory.com/32))
3. RDS 생성하기

</aside>

## 🔎 실습

<aside>
✅ **Swagger 띄우기**

1. DockerFile 작성하기
2. 배포용 application-prod.yml 작성하기
3. GitActions Secret에 주입받을 값 설정하기
4. 워크플로우 생성하고 CICD.yml 작성하기

</aside>