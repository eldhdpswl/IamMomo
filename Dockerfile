FROM openjdk:11-jdk

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew bootJAR
# JAR_FILE 변수 정의 -> 기본적으로 jar file이 2개이기 때문에 이름을 특정해야함
ARG JAR_FILE=./build/libs/momo-0.0.1-SNAPSHOT.jar

# JAR 파일 메인 디렉토리에 복사
COPY ${JAR_FILE} app.jar

# 시스템 진입점 정의
# ENTRYPOINT ["java","-jar","/app.jar"]
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/app.jar"]
# ENTRYPOINT ["./wait-for-it.sh", "momo_mysql:3307", "--", "java", "-jar", "/app.jar"]