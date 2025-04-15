# Build Stage
FROM amazoncorretto:17 AS builder
WORKDIR /app

#먼저 gradle 파일만 복사하여 의존성 레이어 캐싱
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
COPY src ./src

ENTRYPOINT ["gradle","build","-t","temp","."]
