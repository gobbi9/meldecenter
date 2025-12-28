# Stage 1: Extract layers using the Spring Boot jarmode
FROM eclipse-temurin:21-jre-jammy AS builder
WORKDIR application
# This assumes you've run ./gradlew bootJar
COPY build/libs/*.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract

# Stage 2: Final runtime image
FROM eclipse-temurin:21-jre-jammy
WORKDIR application
# Copy layers individually for better caching
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
