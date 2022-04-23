FROM openjdk:11

VOLUME /tmp

ARG JAR_FILE=target/job-scraper-spring-server-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} job-scraper-spring-server.jar

ENTRYPOINT ["java","-jar","/job-scraper-spring-server.jar"]