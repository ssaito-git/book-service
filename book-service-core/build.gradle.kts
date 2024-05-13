plugins {
    kotlin("jvm")
    `java-library`
    `book-service-detekt`
}

repositories {
    mavenCentral()
}

dependencies {
}

kotlin {
    jvmToolchain(21)
}
