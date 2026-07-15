FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY pom.xml .
COPY broker-api/pom.xml broker-api/pom.xml
COPY broker-reporting/pom.xml broker-reporting/pom.xml

RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -pl broker-reporting -am dependency:go-offline

COPY broker-api broker-api
COPY broker-reporting broker-reporting

RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -pl broker-reporting -am package -DskipTests

FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

WORKDIR /app

COPY --from=build /workspace/broker-reporting/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-jar", "app.jar"]