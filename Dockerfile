FROM gradle:6.9.3-jdk11-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:11.0.11-jre-slim

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/momo-0.0.1-SNAPSHOT.jar /app/spring-boot-application.jar

## wait-for-it.sh
COPY wait-for-it.sh ./
RUN chmod +x wait-for-it.sh

# ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar","/app/spring-boot-application.jar"]
