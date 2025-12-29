group = "coding.challenge"
version = "1.0.0-SNAPSHOT"
description = "Coding Challenge mit Spring Boot 3 und PostgreSQL"

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}


dependencies {
    // Spring Boot Reactive Web
    implementation(libs.spring.boot.starter.webflux)

    // Run docker-compose when starting the application locally
    developmentOnly(libs.spring.boot.docker.compose)

    // Reactor
    implementation(libs.reactor.kotlin.extensions)
    testImplementation(libs.reactor.test)

    // Kotlin
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.reactor)
    implementation(libs.kotlinx.coroutines.reactive)
    implementation(libs.kotlin.logging)
    implementation(libs.jackson.module.kotlin)

    // Database
    implementation(libs.spring.boot.starter.jdbc)
    implementation(libs.spring.boot.starter.data.r2dbc)
    implementation(libs.flyway.core)
    implementation(libs.spring.jdbc)
    implementation(libs.flyway.database.postgresql)
    runtimeOnly(libs.postgresql)
    runtimeOnly(libs.r2dbc.postgresql)

    // OpenAPI, Observability & Logging
    implementation(libs.springdoc.openapi)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.logbook.spring.boot.starter)
    implementation(libs.logbook.netty)
    implementation(libs.logbook.logstash)
    implementation(libs.micrometer.tracing.bridge.otel)
    implementation(platform(libs.micrometer.bom))
    implementation(libs.opentelemetry.exporter.otlp)
    implementation(libs.spring.boot.starter.aop)

    // MacOS DNS fix
    testRuntimeOnly("io.netty:netty-resolver-dns-native-macos:${libs.versions.netty.get()}:osx-aarch_64")

    // FTP Client
    implementation(libs.spring.integration.ftp)

    // Test Support
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.testcontainers.junit.jupiter)
    testImplementation(libs.spring.boot.testcontainers)
    testImplementation(libs.testcontainers.postgresql)
    testImplementation(libs.testcontainers.r2dbc)
    testRuntimeOnly(libs.junit.platform.launcher)
    // Kotlin testing
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.extensions.spring)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlin.test.junit5)
    testImplementation(libs.kotlinx.coroutines.test)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes(
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version
        )
    }
}

tasks.register<Exec>("composeUp") {
    group = "docker"
    description = "Runs ./gradlew bootJar && docker-compose up --build -d"

    dependsOn(tasks.bootJar)
    commandLine("sh", "-c", "docker compose up --build -d")
}
