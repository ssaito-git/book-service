plugins {
    kotlin("jvm")
    alias(libs.plugins.spring)
    alias(libs.plugins.springframework.boot)
    alias(libs.plugins.spring.dependency.management)
    `book-service-detekt`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":book-service-core"))
    implementation(project(":book-service-db"))

    // Spring Boot
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.jdbc)
    developmentOnly(libs.spring.boot.devtools)
    testImplementation(libs.spring.boot.starter.test)

    // PostgreSQL JDBC Driver
    runtimeOnly(libs.postgresql)

    // Jackson
    implementation(libs.jackson.module.kotlin)

    // jOOQ
    implementation(libs.jooq)

    // Springdoc-openapi
    implementation(libs.springdoc.openapi.starter.webmvc.api)

    // Flyway
    testImplementation(libs.flyway.core)
    testRuntimeOnly(libs.flyway.database.postgresql)

    // JUnit
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)

    // Testcontainers
    testImplementation(platform(libs.testcontainers.bom))
    testImplementation(libs.testcontainers)
    testImplementation(libs.testcontainers.junit.jupiter)
    testImplementation(libs.testcontainers.postgresql)

    // Database Rider
    testImplementation(libs.rider.core)
    testImplementation(libs.rider.junit5)
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}
