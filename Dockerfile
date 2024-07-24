# 빌드 단계
FROM gradle:7.6-jdk17 AS build

# /app 디렉토리를 작업 디렉토리로 설정합니다.
WORKDIR /app

# 현재 디렉토리(.)의 모든 파일을 컨테이너의 현재 작업 디렉토리(/app)로 복사합니다
COPY . .

# 테스트 케이스를 실행하지 않고 빌드합니다.
RUN gradle build --no-daemon -x test

# 실행 단계
FROM openjdk:17-jdk-slim

# /app 디렉토리를 작업 디렉토리로 설정합니다.
WORKDIR /app

ARG JAR_FILE=build/libs/curly-backend-0.0.1-SNAPSHOT.jar

# 빌드 스테이지에서 생성된 JAR 파일을 현재 스테이지로 복사합니다. --from=build는 이전 빌드 스테이지의 이름입니다.
COPY --from=build /app/${JAR_FILE} app.jar

# secret 파일을 컨테이너의 /app/config/ 폴더로 복사
COPY src/resources/application-secret.properties /app/config/application-secret.properties

# 컨테이너가 9191 포트를 외부에 노출하도록 설정합니다.
EXPOSE 9191

# 컨테이너가 시작될 때 실행할 명령을 정의합니다. java -jar app.jar 명령을 실행하여 JAR 파일을 실행합니다.
ENTRYPOINT ["java", "-Xms3g", "-Xmx3g", "-Dspring.profiles.active=prod,secret", "-jar", "app.jar", "--spring.config.additional-location=file:/app/config/application-secret.properties","--seeder=15000"]