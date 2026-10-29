# =====================================================================
# Multi-stage Dockerfile - didarova-medina-student-api
# Student: Didarova Medina | ID 37944 | Group IT2-2313
# Build:   docker build -t didarova-medina-student-api:v1.0-didarova-medina-37944 .
# =====================================================================

# ---------- Stage 1: build (official JDK image + Gradle wrapper) ----------
FROM eclipse-temurin:17-jdk AS build
WORKDIR /workspace

# 1) Copy only Gradle files first -> dependencies are cached in a separate layer
COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
RUN chmod +x gradlew && ./gradlew --no-daemon dependencies > /dev/null

# 2) Copy the source code and build the executable jar
#    (tests are executed in the Jenkins "Test" stage, so they are skipped here)
COPY src ./src
RUN ./gradlew --no-daemon clean bootJar -x test

# ---------- Stage 2: runtime (small JRE image, no JDK, no sources) ----------
FROM eclipse-temurin:17-jre AS runtime

ARG APP_VERSION=v1.0-didarova-medina-37944
LABEL org.opencontainers.image.title="didarova-medina-student-api" \
      org.opencontainers.image.authors="Didarova Medina <medidid0509@gmail.com>" \
      org.opencontainers.image.version="${APP_VERSION}" \
      org.opencontainers.image.description="Student CRUD API (Spring Boot + PostgreSQL) - ID 37944, IT2-2313"

# Run as a non-root user
RUN groupadd --system app && useradd --system --gid app --home /app app
WORKDIR /app

COPY --from=build /workspace/build/libs/didarova-medina-student-api.jar app.jar
RUN chown app:app app.jar
USER app

ENV APP_VERSION=${APP_VERSION} \
    SERVER_PORT=8080

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
