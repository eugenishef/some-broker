FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml .
COPY broker-api/pom.xml broker-api/pom.xml
COPY broker-reporting/pom.xml broker-reporting/pom.xml
COPY broker-parser/pom.xml broker-parser/pom.xml

RUN mvn -B dependency:go-offline -pl broker-parser -am

COPY broker-api/src broker-api/src
COPY broker-reporting/src broker-reporting/src
COPY broker-parser/src broker-parser/src

RUN mvn -B -pl broker-parser -am clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=build /workspace/broker-parser/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]