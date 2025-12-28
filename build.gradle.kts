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

val mockitoAgent by configurations.creating

dependencies {
    // Spring Boot Starters
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.jdbc)
    implementation(libs.spring.boot.starter.data.r2dbc)
    implementation(libs.spring.boot.starter.webflux)
    implementation(libs.spring.boot.starter.aop)
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.boot.testcontainers)
    developmentOnly(libs.spring.boot.docker.compose)

    // Spring / Reactor
    implementation(libs.spring.jdbc)
    implementation(libs.reactor.kotlin.extensions)
    testImplementation(libs.reactor.test)

    // Kotlin
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.reactor)
    implementation(libs.kotlinx.coroutines.reactive)
    implementation(libs.kotlin.logging)
    testImplementation(libs.kotlin.test.junit5)
    testImplementation(libs.kotlinx.coroutines.test)

    // Database
    implementation(libs.flyway.core)
    implementation(libs.flyway.database.postgresql)
    runtimeOnly(libs.postgresql)
    runtimeOnly(libs.r2dbc.postgresql)

    // OpenAPI, Observability & Logging
    implementation(libs.springdoc.openapi)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.logbook.spring.boot.starter)
    implementation(libs.logbook.netty)
    implementation(libs.logbook.logstash)
    implementation(libs.micrometer.tracing.bridge.otel)
    implementation(platform(libs.micrometer.bom))
    implementation(libs.opentelemetry.exporter.otlp)

    // Netty
    testRuntimeOnly(libs.netty.resolver.dns.native.macos)

    // Test Support
    testImplementation(libs.testcontainers.junit.jupiter)
    testImplementation(libs.testcontainers.postgresql)
    testImplementation(libs.testcontainers.r2dbc)
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.extensions.spring)
    testImplementation(libs.mockk)
    testRuntimeOnly(libs.junit.platform.launcher)
    mockitoAgent(libs.byte.buddy.agent)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    jvmArgs("-javaagent:${mockitoAgent.asPath}")
    testLogging {
        showStandardStreams = true
        exceptionFormat =
            org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
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
