FROM maven:3.6-jdk-11 AS build

WORKDIR /app

COPY pom.xml .

COPY src ./src

RUN mvn -f /app/pom.xml clean package


FROM openjdk:11

ARG JAR_FILE=/app/target/job-scraper-spring-server-0.0.1-SNAPSHOT.jar

COPY --from=build ${JAR_FILE} job-scraper-spring-server.jar

ENTRYPOINT ["java","-Dsun.net.http.allowRestrictedHeaders=true","-jar","/job-scraper-spring-server.jar"]