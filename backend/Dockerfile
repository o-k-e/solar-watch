## Use a lightweight OpenJDK image (Java 23 since it's stable now)
#FROM openjdk:17-jdk-slim
#
## Set working directory inside the container
#WORKDIR /app
#
## Copy the new JAR file from the target folder into the container
#COPY target/solar-watch-0.0.1-SNAPSHOT.jar /app/app.jar
#
## Expose port 8080 (Spring Boot default)
#EXPOSE 8080
#
## Run the application
#ENTRYPOINT ["java", "-jar", "/app/app.jar"]


FROM eclipse-temurin:21-jre-alpine
WORKDIR /tmp
COPY target/solar-watch-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

#docker image: docker build -t codecool/solarwatch-api .
#docker run -p 8080:8080 codecool/solarwatch-api

