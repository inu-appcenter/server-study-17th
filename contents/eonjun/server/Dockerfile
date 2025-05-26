## 1. 베이스 이미지 설정 - Java 21을 사용하여 실행할 것이므로 적절한 JDK 이미지 선택
#FROM bellsoft/liberica-openjdk-alpine:21
#LABEL name="frozzun"
#
## 2. ARG를 사용해 빌드 결과 JAR 파일의 경로를 지정
#ARG JAR_FILE=build/libs/*.jar

FROM openjdk:21
ARG JAR_FILE=build/libs/*.jar
LABEL name="frozzun99"

# 3. 위에서 정의한 JAR 파일을 컨테이너의 app.jar로 복사
COPY ${JAR_FILE} app.jar

# 4. 컨테이너 시작 시 JAR 파일을 실행하는 명령어
ENTRYPOINT ["java", "-jar", "/app.jar"]
