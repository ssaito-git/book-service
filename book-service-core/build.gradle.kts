plugins {
    kotlin("jvm")
    `java-library`
    `book-service-detekt`
}

repositories {
    mavenCentral()
}

dependencies {
    // kotlin-result
    api(libs.kotlin.result)

    // JUnit
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
}

kotlin {
    jvmToolchain(21)
}
