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
}

kotlin {
    jvmToolchain(21)
}
