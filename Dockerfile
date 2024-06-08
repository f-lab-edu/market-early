# 빌드 단계
# gradle:7.3.3-jdk17 이미지를 기반으로 새로운 빌드 스테이지를 정의합니다. 
# AS build는 이 스테이지에 build라는 이름을 붙여서 나중에 참조할 수 있게 합니다.
FROM gradle:7.6-jdk17 AS build

# /app 디렉토리를 작업 디렉토리로 설정합니다.
WORKDIR /app

# 현재 디렉토리(.)의 모든 파일을 컨테이너의 현재 작업 디렉토리(/app)로 복사합니다
COPY . .

# gradle build 명령을 실행하여 애플리케이션을 빌드합니다. --no-daemon 옵션은 Gradle 데몬을 사용하지 않고 빌드를 수행합니다.
RUN gradle build --no-daemon

# 실행 단계 
# openjdk:17-jdk-slim 이미지를 기반으로 새로운 실행 스테이지를 정의합니다.
# openjdk:17 이미지는 여러가지 종속성들을 포함하고있어서 개발이나 테스트환경에 적합하고
# 프로덕션 환경에서는 이미지의 크기와 성능이 중요한데 openjdk:17-jdk-slim 이미지는 경량화되어있고
# 불필요한 패키지가 포함되지 않았기 때문에 더 나은 성능을 제공합니다. 배포 속도를 향상시키고
# 컨테이너 시작 시간을 단축시키며 전체 시스템 리소스 사용량을 줄이는데 도움이 됩니다.
FROM openjdk:17-jdk-slim

# /app 디렉토리를 작업 디렉토리로 설정합니다.
WORKDIR /app

ARG JAR_FILE=build/libs/curly-backend-0.0.1-SNAPSHOT.jar

# 빌드 스테이지에서 생성된 JAR 파일을 현재 스테이지로 복사합니다. --from=build는 이전 빌드 스테이지의 이름입니다.

COPY ${JAR_FILE}  app.jar

# 컨테이너가 8080 포트를 외부에 노출하도록 설정합니다.
EXPOSE 9191

# 컨테이너가 시작될 때 실행할 명령을 정의합니다. java -jar app.jar 명령을 실행하여 JAR 파일을 실행합니다.
ENTRYPOINT ["java", "-Dspring.profiles.active=prod,secret", "-jar", "app.jar"]