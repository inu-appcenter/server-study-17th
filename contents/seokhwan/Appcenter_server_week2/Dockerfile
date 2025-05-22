FROM bellsoft/liberica-openjdk-alpine:21

WORKDIR /app

VOLUME /tmp

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]