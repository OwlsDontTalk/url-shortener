# Dockerfile

FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/UrlShortener-0.0.1-SNAPSHOT.jar /app/UrlShortener-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "/app/UrlShortener-0.0.1-SNAPSHOT.jar"]
