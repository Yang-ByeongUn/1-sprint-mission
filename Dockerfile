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

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
