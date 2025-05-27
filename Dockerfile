# 빌드 스테이지
FROM amazoncorretto:17 AS builder

# 작업 디렉토리 설정
WORKDIR /app

# Gradle Wrapper 파일 먼저 복사
COPY gradle ./gradle
COPY gradlew ./gradlew

# Gradle 캐시를 위한 의존성 파일 복사
COPY build.gradle settings.gradle ./

# 의존성 다운로드
RUN ./gradlew dependencies

# 소스 코드 복사 및 빌드
COPY src ./src
RUN ./gradlew build -x test


# 런타임 스테이지
FROM amazoncorretto:17-alpine3.21

# 작업 디렉토리 설정
WORKDIR /app

# 프로젝트 정보를 ENV로 설정
ENV PROJECT_NAME=discodeit \
    PROJECT_VERSION=1.2-M8 \
    JVM_OPTS=""

# 빌드 스테이지에서 jar 파일만 복사
COPY --from=builder /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar ./

# 80 포트 노출
EXPOSE 80

# jar 파일 실행
ENTRYPOINT ["sh", "-c", "java ${JVM_OPTS} -jar ${PROJECT_NAME}-${PROJECT_VERSION}.jar"]

# --- Build Stage ---
FROM amazoncorretto:17 AS builder

WORKDIR /app

# Gradle 관련 파일 먼저 복사하여 의존성 캐시 활용
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle settings.gradle ./
COPY src/ src/


# 실행 권한 부여
RUN chmod +x gradlew

# 의존성 캐싱
RUN ./gradlew build --no-daemon -x test

# 최종 빌드
RUN ./gradlew clean build --no-daemon -x test


# --- Run Stage ---
FROM amazoncorretto:17-alpine

WORKDIR /app

ENV PROJECT_VERSION=1.2-M8
ENV PROJECT_NAME=discodeit
ENV SPRING_PROFILE=prod
ENV JVM_OPTS=""

COPY --from=builder /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar

EXPOSE 8081

#ENTRYPOINT ["java", "-jar", "app.jar"]
CMD ["sh", "-c", "java $JVM_OPTS -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE -jar /app/app.jar"]
