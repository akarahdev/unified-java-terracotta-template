# syntax=docker/dockerfile:1.7

FROM eclipse-temurin:27-jdk
WORKDIR /data

COPY ./ ./
RUN ./gradlew bootJar --no-daemon
RUN mv ./build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
