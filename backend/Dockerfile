FROM maven:3.9.9-amazoncorretto-21-al2023
WORKDIR /tmp
COPY . .
RUN mvn clean package
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "target/solar-watch-0.0.1-SNAPSHOT.jar"]

