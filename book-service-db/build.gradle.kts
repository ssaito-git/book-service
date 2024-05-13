plugins {
    kotlin("jvm")
    `java-library`
    `book-service-detekt`
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)
}
