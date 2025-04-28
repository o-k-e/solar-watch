FROM maven:3.9.9-amazoncorretto-21-al2023 AS builder
WORKDIR /tmp
COPY . .
RUN mvn clean package

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /tmp/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]