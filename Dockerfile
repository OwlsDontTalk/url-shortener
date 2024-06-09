FROM openjdk:17-jdk-alpine

WORKDIR /app
COPY target/UrlShortener-0.0.1-SNAPSHOT.jar /app/UrlShortener-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "/app/UrlShortener-0.0.1-SNAPSHOT.jar"]
